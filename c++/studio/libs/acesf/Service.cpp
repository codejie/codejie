
#include "ace/Get_Opt.h"

#include "acex/ACEX.h"
#include "acex/Exception.h"
#include "acex/Command_Parser.h"
#include "acef/Commands.h"

#include "acesf/Service.h"

#if !defined (ACE_LACKS_PRAGMA_ONCE)
#  pragma once
#endif /* ACE_LACKS_PRAGMA_ONCE */

#if !defined (__ACE_INLINE__)
#  include "acesf/Service.i"
#endif /* __ACE_INLINE__ */


ACESF_Service* pACESF_Service = 0;

ACESF_Service::ACESF_Service(const std::string& name, const std::string& desc)
: command_parser_(ACEX_COMMAND_PARSER)
, command_executor_(ACEX_COMMAND_EXECUTOR)
, mt_log_file_stream_(ACEX_MT_LOG_FILE_STREAM)
, output_proxy_(ACEX_OUTPUT_PROXY)
, telnet_server_task_(ACEX_TELNET_SERVER_TASK)
, timer_task_(ACEX_TIMER_TASK)
{
}

ACESF_Service::~ACESF_Service()
{
}

int ACESF_Service::init(int argc, char *argv[])
{
    if(service_init(argc, argv) != 0)
        return -1;

    return 0;
}

int ACESF_Service::fini()
{

    return service_fini();
}

int ACESF_Service::regist_command()
{
	if (this->command_parser()->regist_command(ACEF_Output_Level_Command()) != 0)
		throw ACEX_Runtime_Exception("Command output_level regist failed", __FILE__, __LINE__);
	if (this->command_parser()->regist_command(ACEF_Output_Proxy_Command()) != 0)
		throw ACEX_Runtime_Exception("Command output_proxy regist failed", __FILE__, __LINE__);
	if (this->command_parser()->regist_command(ACEF_Output_Verbose_Command()) != 0)
		throw ACEX_Runtime_Exception("Command output_verbose regist failed", __FILE__, __LINE__);

	return 0;
}

int ACESF_Service::service_getopt(int argc, char* argv[])
{
    ACE_Get_Opt get_opt (argc, argv, "i:rskt:d");
    int c;

    while ((c = get_opt ()) != -1)
    switch (c)
    {
    case 'i':
        service_opt_.install_ = 1;
        service_opt_.startup_ = ACE_OS::atoi(get_opt.opt_arg());
        if(service_opt_.startup_ <=0)
            return -1;
        break;
    case 'r':
        service_opt_.remove_ = 1;
        break;
    case 's':
        service_opt_.start_ = 1;
        break;
    case 'k':
        service_opt_.kill_ = 1;
        break;
    case 't':
        service_opt_.type_ = 1;
        if(service_opt_.startup_ <=0)
            return -1;
        break;
    case 'd':
        service_opt_.debug_ = 1;
        break;
    default:
      // -i can also be given without a value - if so, it defaults
      // to defined value.
        if (ACE_OS::strcmp (get_opt.argv ()[get_opt.opt_ind () - 1], ACE_TEXT ("-i")) == 0)
        {
            service_opt_.install_ = 1;
            service_opt_.startup_ = SERVICE_AUTO_START;
        }
        else
        {
            return -1;
        }
        break;
    }
    return 0;
}

int ACESF_Service::service_init(int argc, char* argv[])
{
    this->name(ACE_TEXT(service_name_.c_str(), service_desc_.c_str());

    if(service_getopt(argc, argv) != 0)
    {
        ShowUsage(std::cout);
        return -1;
    }



    return 0;

}