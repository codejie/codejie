
#include <fstream>

#include "ace/Get_Opt.h"
#include "ace/OS_NS_errno.h"
#include "ace/Reactor.h"

#include "Defines.h"
#include "Service.h"

ServiceKeeper::ServiceKeeper()
{
    reactor (ACE_Reactor::instance ());
}

ServiceKeeper::~ServiceKeeper() 
{ 
}

void ServiceKeeper::handle_control(DWORD control_code)
{
    if (control_code == SERVICE_CONTROL_SHUTDOWN || control_code == SERVICE_CONTROL_STOP)
    {
        report_status(SERVICE_STOP_PENDING);
        ACE_DEBUG((LM_INFO, ACE_TEXT("ServiceKeeper control stop requested\n")));
        stop_ = 1;
        reactor()->notify(this, ACE_Event_Handler::EXCEPT_MASK);
    }
    else
    {
        ACE_NT_Service::handle_control(control_code);
    }
}
int ServiceKeeper::handle_exception(ACE_HANDLE)
{
    return 0;
}

int ServiceKeeper::handle_timeout(const ACE_Time_Value &tv, const void *)
{
    return 0;
}

int ServiceKeeper::svc()
{
    ACE_DEBUG((LM_DEBUG, ACE_TEXT ("ServiceKeeper::svc\n")));
    if(report_status (SERVICE_RUNNING) == 0)
        reactor ()->owner (ACE_Thread::self ());

    this->stop_ = 0;

    while(!this->stop_)
    {
        reactor()->handle_events ();
    }

    // Cleanly terminate connections, terminate threads.
    ACE_DEBUG((LM_DEBUG, ACE_TEXT ("Shutting down\n")));

    return 0;
}

//

static BOOL __stdcall ConsoleHandler(DWORD ctrlType)
{
  ACE_UNUSED_ARG(ctrlType);
  KEEPER::instance()->handle_control(SERVICE_CONTROL_STOP);
  return TRUE;
}

ACE_NT_SERVICE_DEFINE (DataCollectServer, ServiceKeeper, ACE_TEXT(APP_DESC));

//

ServiceStarter::ServiceStarter()
: opt_install (0),
opt_remove (0),
opt_start (0),
opt_kill (0),
opt_type (0),
opt_debug (0),
opt_startup (0)
{
    ACE::init ();
}

ServiceStarter::~ServiceStarter()
{
    ACE::fini ();
}

void ServiceStarter::print_usage_and_die()
{
    ACE_DEBUG ((LM_INFO,
              "Usage: %s"
              " -in -r -s -k -tn -d\n"
              "  -i: Install this program as an NT service, with specified startup\n"
              "  -r: Remove this program from the Service Manager\n"
              "  -s: Start the service\n"
              "  -k: Kill the service\n"
              "  -t: Set startup for an existing service\n"
              "  -d: Debug; run as a regular application\n",
              APP_TITLE,
              0));
    ACE_OS::exit(1);
}

void ServiceStarter::parse_args(int argc, char* argv[])
{
    ACE_Get_Opt get_opt (argc, argv, ACE_TEXT ("i:rskt:d"));
    int c;

    while ((c = get_opt ()) != -1)
    switch (c)
    {
    case 'i':
        opt_install = 1;
        opt_startup = ACE_OS::atoi (get_opt.opt_arg ());
        if (opt_startup <= 0)
            print_usage_and_die ();
        break;
    case 'r':
        opt_remove = 1;
        break;
    case 's':
        opt_start = 1;
        break;
    case 'k':
        opt_kill = 1;
        break;
    case 't':
        opt_type = 1;
        opt_startup = ACE_OS::atoi (get_opt.opt_arg ());
        if (opt_startup <= 0)
        print_usage_and_die ();
        break;
    case 'd':
        opt_debug = 1;
        break;
    default:
        if (ACE_OS::strcmp (get_opt.argv ()[get_opt.opt_ind () - 1], ACE_TEXT ("-i")) == 0)
        {
            opt_install = 1;
            opt_startup = SERVICE_AUTO_START;
        }
        else
        {
            print_usage_and_die ();
        }
        break;
    }
}

int ServiceStarter::run(int argc, char* argv[])
{
    KEEPER::instance()->name(ACE_TEXT(APP_TITLE), ACE_TEXT (APP_DESC));

    parse_args(argc, argv);

    if(opt_install && !opt_remove)
    {
        if(-1 == KEEPER::instance()->insert(opt_startup))
        {
            ACE_ERROR ((LM_ERROR, ACE_TEXT ("%p\n"), ACE_TEXT ("insert")));
            return -1;
        }
        return 0;
    }

    if(opt_remove && !opt_install)
    {
        if(-1 == KEEPER::instance()->remove ())
        {
            ACE_ERROR ((LM_ERROR, ACE_TEXT ("%p\n"), ACE_TEXT ("remove")));
            return -1;
        }
        return 0;
    }

    if(opt_start && opt_kill)
    {
        print_usage_and_die ();
    }

    if(opt_start)
    {
        if (-1 == KEEPER::instance()->start_svc ())
        {
            ACE_ERROR ((LM_ERROR, ACE_TEXT ("%p\n"), ACE_TEXT ("start")));
            return -1;
        }
        return 0;
    }

    if(opt_kill)
    {
        if(-1 == KEEPER::instance()->stop_svc ())
        {
            ACE_ERROR ((LM_ERROR, ACE_TEXT ("%p\n"), ACE_TEXT ("stop")));
            return -1;
        }
        return 0;
    }

    if(opt_type)
    {
        if(-1 == KEEPER::instance()->startup (opt_startup))
        {
            ACE_ERROR ((LM_ERROR, ACE_TEXT ("%p\n"), ACE_TEXT ("set startup")));
            return -1;
        }
        return 0;
    }

    if (opt_debug)
    {
        SetConsoleCtrlHandler(&ConsoleHandler, 1);
        KEEPER::instance()->svc ();
    }
    else
    {
        ofstream *output_file = new ofstream("service_jie.log", ios::out);
        if(output_file && output_file->rdstate() == ios::goodbit)
        {
            ACE_LOG_MSG->msg_ostream(output_file, 1);
        }
        ACE_LOG_MSG->open(argv[0], ACE_Log_Msg::STDERR | ACE_Log_Msg::OSTREAM, 0);
        ACE_DEBUG ((LM_DEBUG, ACE_TEXT ("%T (%t): Starting service.\n")));

        ACE_NT_SERVICE_RUN(DataCollectServer, KEEPER::instance(), ret);
        if (ret == 0)
        {
            ACE_ERROR ((LM_ERROR, ACE_TEXT ("%p\n"), ACE_TEXT ("Couldn't start service")));
        }
        else
        {
            ACE_DEBUG ((LM_DEBUG, ACE_TEXT ("%T (%t): Service stopped.\n")));
        }
    }

    return 0;
}

int main(int argc, char* argv[])
{
    ServiceStarter starter;
    return starter.run(argc, argv);
}

