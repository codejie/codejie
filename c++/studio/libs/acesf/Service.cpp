
#include "ace/Get_Opt.h"

#include "acex/ACEX.h"
#include "acex/Exception.h"

#include "acesf/Service.h"

#if !defined (ACE_LACKS_PRAGMA_ONCE)
#  pragma once
#endif /* ACE_LACKS_PRAGMA_ONCE */

#if !defined (__ACE_INLINE__)
#  include "acesf/Service.i"
#endif /* __ACE_INLINE__ */


ACESF_Service* pACESF_Service = 0;

ACESF_Service::ACESF_Service()
: app_(NULL)
, stop_(false)
{
	::pACESF_Service = ACE_const_cast(ACESF_Service*, this);
}

ACESF_Service::ACESF_Service(const std::string& name, const std::string& desc)
: name_(name)
, desc_(desc)
, app_(NULL)
, stop_(false)
{
	::pACESF_Service = ACE_const_cast(ACESF_Service*, this);
}

ACESF_Service::~ACESF_Service()
{
}

int ACESF_Service::getopt(int argc, char* argv[])
{
    ACE_Get_Opt get_opt (argc, argv, "i:rskt:d");
    int c;

    while ((c = get_opt ()) != -1)
    switch (c)
    {
    case 'i':
        opt_.install_ = 1;
        opt_.startup_ = ACE_OS::atoi(get_opt.opt_arg());
        if(opt_.startup_ <=0)
            return -1;
        break;
    case 'r':
        opt_.remove_ = 1;
        break;
    case 's':
        opt_.start_ = 1;
        break;
    case 'k':
        opt_.kill_ = 1;
        break;
    case 't':
        opt_.type_ = 1;
        if(opt_.startup_ <=0)
            return -1;
        break;
    case 'd':
        opt_.debug_ = 1;
        break;
    default:
      // -i can also be given without a value - if so, it defaults
      // to defined value.
        if (ACE_OS::strcmp (get_opt.argv ()[get_opt.opt_ind () - 1], ACE_TEXT ("-i")) == 0)
        {
            opt_.install_ = 1;
            opt_.startup_ = SERVICE_AUTO_START;
        }
        else
        {
            return -1;
        }
        break;
    }
    return 0;
}

int ACESF_Service::init(int argc, char* argv[])
{
    this->name(ACE_TEXT(name_.c_str()), ACE_TEXT(desc_.c_str()));

    if(getopt(argc, argv) != 0)
    {
        show_usage(std::cout);
        return -1;
    }
	return 0;
}

int ACESF_Service::fini()
{
	return 0;
}

void ACESF_Service::handle_control(DWORD control_code)
{
	if (control_code == SERVICE_CONTROL_SHUTDOWN
		|| control_code == SERVICE_CONTROL_STOP)
	{
		ACE_NT_Service::report_status(SERVICE_STOP_PENDING);
		if(app_ != NULL)
		{
			ACEX_Message msg(0, 0, 0);
			app_->put_msg(msg);
			stop_ = true;
		}
	}
	else
	{
		ACE_NT_Service::handle_control (control_code);
	}
}

int ACESF_Service::handle_exception(ACE_HANDLE handle)
{
	return 0;
}

int ACESF_Service::handle_timeout(const ACE_Time_Value &current_time, const void *act)
{
	return 0;
}

int ACESF_Service::svc()
{
	return -1;
}

int ACESF_Service::run(int argc, char* argv[])
{
	pACESF_Service->regist_name();
	pACESF_Service->regist_app();

	this->init(argc, argv);

	if(opt_.install_ == 1 && opt_.remove_ != 1)
	{
		if(this->insert(opt_.startup_) == -1)
			return -1;
		return 0;
	}

	if(opt_.remove_ == 1 && opt_.install_ != 1)
	{
		if(this->remove() == -1)
			return -1;
		return 0;
	}

	if(opt_.start_ == 1)
	{
		if(this->start_svc() == -1)
			return -1;
		return 0;
	}

	if(opt_.kill_ == 1)
	{
		if(this->stop_svc() == -1)
			return -1;
		return 0;
	}

	if(opt_.type_ == 1)
	{
		if(this->startup(opt_.startup_) == -1)
			return -1;
		return 0;
	}

	//opt_.debug_
	
	if(this->app_ != NULL)
	{
		try
		{
			this->app_ ->run(argc, argv);

			return 0;
		}
		catch(ACEX_Exception& e)
		{
			ACEX_LOG_OS(LM_EMERGENCY, "Exception: " << e <<". Program will exit.\n");
		}
		catch(std::exception& e)
		{
			ACEX_LOG_OS(LM_EMERGENCY, "Exception: " << e.what() <<". Program will exit.\n");
		}
		catch(...)
		{
			ACEX_LOG_OS(LM_EMERGENCY, "Exception: Unknow exception. Program will exit.\n");
		}

		return -1;		
	}

	this->regist_svc();

	return 0;
}

/*

void ACESF_Service::regist_app()
{
	app_ = app;
}
void ACESF_Service::regist_name()
{
ACE_NT_SERVICE_DEFINE (Beeper,
                       Service,
                       ACE_TEXT ("Annoying Beeper Service"));
}

void ACESF_Service::regist_svc()
{
      ACE_NT_SERVICE_RUN (Beeper,
                          SERVICE::instance (),
                          ret);
}

*/