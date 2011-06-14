/*
 * DataCollectServerApp.cpp
 *
 *  Created on: Jun 9, 2011
 *      Author: jie
 */

#include <iomanip>

#include "ace/OS_NS_time.h"

#include "acex/ACEX.h"
#include "acex/Exception.h"

#include "Defines.h"
#include "Commands.h"
#include "ConfigLoader.h"
//#include "DataLoader.h"
//#include "TerminalManager.h"

#include "DataCollectServerApp.h"

DataCollectServerApp theApp;

#include "DataCollectServerApp.h"


DataCollectServerApp::DataCollectServerApp()
: ACEF_App()
, _tStartupTime(ACE_OS::time(NULL))
{
}

DataCollectServerApp::~DataCollectServerApp()
{
}


int DataCollectServerApp::init(int argc, char* argv[])
{
	ACEF_App::init(argc, argv);

	try
	{
		_stOptions.Scan(argc, argv);
	}
	catch(ACEX_Exception &e)
	{
		std::cout << e.what() << std::endl;
		_stOptions.ShowUsage(std::cout);
		exit(0);
	}
	if(_stOptions.HelpOpt())
	{
		_stOptions.ShowUsage(std::cout);
		exit(0);
	}

	if(_stOptions.VersionOpt())
	{
		ShowVersion(std::cout);
		exit(0);
	}

	if(_stOptions.DaemonOpt())
	{
		ACEX_LOG_OS(LM_STARTUP, "Run as daemon init..." << std::endl);
		init_as_daemon();
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);
	}
	output_proxy()->proxy_flags(_stOptions.ProxyOpt());
	output_proxy()->priority_mask(_stOptions.LevelOpt());
	output_proxy()->verbose(_stOptions.VerboseOpt());

	command_executor()->command_prompt(SYS_PROMPT);
	if(InitConfig() != 0)
        return -1;
	if(StartTasks() != 0)
        return -1;

	if(!_stOptions.DaemonOpt() && !_stOptions.PopStart())
	{
		ACEX_LOG_OS(LM_STARTUP, "Standard input task start..." << std::endl);
		std_in_task()->activate();
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);
	}

	if(_stOptions.PopStart())
	{
		char c = EOF;
#if defined(_WIN32)
		std::cout << c << std::flush;
#else
		write(STDOUT_FILENO, &c, sizeof(c));
#endif
	}
	ACEX_LOG_OS(LM_STARTUP, std::endl);
	return 0;
}

int DataCollectServerApp::fini()
{
	StopTasks();
	ACEF_App::fini();
#if defined(_WIN32)
	exit(0);
#endif
	return 0;
}

int DataCollectServerApp::regist_signal()
{
	if (this->regist_sig(SIGINT) != 0)
		throw ACEX_Runtime_Exception("Signal SIGINT regist failed", __FILE__, __LINE__);
    return 0;
}

int DataCollectServerApp::regist_command()
{
	ACEF_App::regist_command();

	if(command_parser()->regist_command(CCmdShowConfigure()) != 0)
		throw ACEX_Runtime_Exception("Register '" + CCmdShowConfigure().tag() + "' command failed.", __FILE__, __LINE__);
	if(command_parser()->regist_command(CCmdShutdown()) != 0)
		throw ACEX_Runtime_Exception("Register '" + CCmdShutdown().tag() + "' command failed.", __FILE__, __LINE__);
	if(command_parser()->regist_command(CCmdShowOptions()) != 0)
		throw ACEX_Runtime_Exception("Register '" + CCmdShowOptions().tag() + "' command failed.", __FILE__, __LINE__);
	if(command_parser()->regist_command(CCmdShowVersion()) != 0)
		throw ACEX_Runtime_Exception("Register '" + CCmdShowVersion().tag() + "' command failed.", __FILE__, __LINE__);
	if(command_parser()->regist_command(CCmdTest()) != 0)
		throw ACEX_Runtime_Exception("Register '" + CCmdTest().tag() + "' command failed.", __FILE__, __LINE__);
/*

	if(command_parser()->regist_command(CCmdShowTerminal()) != 0)
		throw ACEX_Runtime_Exception("Register '" + CCmdShowTerminal().tag() + "' command failed.", __FILE__, __LINE__);
	if(command_parser()->regist_command(CCmdShowDataLoader()) != 0)
		throw ACEX_Runtime_Exception("Register '" + CCmdShowDataLoader().tag() + "' command failed.", __FILE__, __LINE__);
	if(command_parser()->regist_command(CCmdTest()) != 0)
		throw ACEX_Runtime_Exception("Register '" + CCmdTest().tag() + "' command failed.", __FILE__, __LINE__);
*/
	return 0;
}


int DataCollectServerApp::handle_signal(int signum, siginfo_t* siginfo, ucontext_t* ucontext)
{
	ACEX_LOG_OS(LM_WARNING, "<<DataCollectServerApp::handle_signal>>Recv signal '" << signum << "',");
	if(signum != 2)//SIGPIPE
	{
		ACEX_LOG_OS(LM_WARNING, " skip." << std::endl);
		return 0;
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, " ready to shutdown application." << std::endl);
	}
	Shutdown();
	return 0;
}

int DataCollectServerApp::handle_msg(const ACEX_Message& msg)
{
	if(msg.msg_id() == TASK_APP)
	{
		return OnAppTaskMsgProc(msg);
	}
	else
	{
		throw ACEX_Runtime_Exception("Unknown msgid.", __FILE__, __LINE__);
	}
}

int DataCollectServerApp::InitConfig()
{
	ACEX_LOG_OS(LM_STARTUP, "Configuration init.." << std::endl);
/*
	if(theConfig.Load(_stOptions.ConfigureFile(), theDataLoader, theTerminalManager) != 0)
    {
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[FAIL]" << std::endl);
        return -1;
    }
*/
    ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);
	return 0;
}


int DataCollectServerApp::StartTasks()
{
/*
	try
	{
		//ACEX_LOG_OS(LM_STARTUP, "Configuration init.." << std::endl);
		//_stConfigure.Open(_stOptions.ConfigureFile());
		//ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);

		ACEX_LOG_OS(LM_STARTUP, "Log file init.." << std::endl);
		if(mt_log_file_stream()->open(theConfig.GetLogFile(), theConfig.GetLogSize()) != 0)
			throw ACEX_Runtime_Exception("Open LOG file '" + theConfig.GetLogFile() + "' failed.", __FILE__, __LINE__);
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);

        //Init DataLoader
		ACEX_LOG_OS(LM_STARTUP, "DataLoader init.." << std::endl);
        if(theDataLoader.Init() != 0)
			throw ACEX_Runtime_Exception("DataLoader Init failed.", __FILE__, __LINE__);
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);

        //Init TerminalManager
		ACEX_LOG_OS(LM_STARTUP, "TerminalManager init.." << std::endl);
        if(theTerminalManager.Init() != 0)
			throw ACEX_Runtime_Exception("TerminalManager Init failed.", __FILE__, __LINE__);
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);

        //Core
		ACEX_LOG_OS(LM_STARTUP, "Main message task start..." << std::endl);
        if(theMainTask.Init(theConfig.GetServiceID(), theConfig.GetServiceAddr(), theConfig.GetDataLoadPeriod()) != 0)
        {
            throw ACEX_Runtime_Exception("Main message task init failed.", __FILE__, __LINE__);
        }
        theMainTask.activate();
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);

		ACEX_LOG_OS(LM_STARTUP, "Timer task start..." << std::endl);
		timer_task()->activate();
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);

		ACEX_LOG_OS(LM_STARTUP, "Telnet task start.." << std::endl);
		if(telnet_server_task()->open(ACE_INET_Addr(theConfig.GetMMLAddr().c_str())) != 0)
			throw ACEX_Runtime_Exception("Open Telnet task address '" + theConfig.GetMMLAddr() + "' failed.", __FILE__, __LINE__);
		telnet_server_task()->activate();
		ACEX_LOG_OS(LM_STARTUP, std::setw(40) << "[OK]" << std::endl);
	}
	catch (const ACEX_Runtime_Exception& e)
	{
		ACEX_LOG_OS(LM_EMERGENCY, "Startup exception: " << e <<". Program will exit.\n");
		Shutdown();
        return -1;
	}
*/
	return 0;
}

void DataCollectServerApp::StopTasks()
{
/*
	if(!_stOptions.DaemonOpt())
	{
		ACEX_LOG_OS(LM_SHUTDOWN, "\nStandard input task shutdown.." << std::endl);
		std_in_task()->deactivate();
		ACEX_LOG_OS(LM_SHUTDOWN, std::setw(40) << "[OK]" << std::endl);
	}

	ACEX_LOG_OS(LM_SHUTDOWN, "Telnet task shutdown.." << std::endl);
	telnet_server_task()->deactivate();
	ACEX_LOG_OS(LM_SHUTDOWN, std::setw(40) << "[OK]" << std::endl);

	ACEX_LOG_OS(LM_SHUTDOWN, "Timer task shutdown.." << std::endl);
	timer_task()->deactivate();
	ACEX_LOG_OS(LM_SHUTDOWN, std::setw(40) << "[OK]" << std::endl);

    //Core
	ACEX_LOG_OS(LM_SHUTDOWN, "Main message task shutdown.." << std::endl);
    theMainTask.deactivate();
	ACEX_LOG_OS(LM_SHUTDOWN, std::setw(40) << "[OK]" << std::endl);

    //DataLoader
	ACEX_LOG_OS(LM_SHUTDOWN, "Release DataLoader.." << std::endl);
    theDataLoader.Final();
	ACEX_LOG_OS(LM_SHUTDOWN, std::setw(40) << "[OK]" << std::endl);

    //TerminalManager
	ACEX_LOG_OS(LM_SHUTDOWN, "Release TerminalManager.." << std::endl);
    theTerminalManager.Final();
	ACEX_LOG_OS(LM_SHUTDOWN, std::setw(40) << "[OK]" << std::endl);
	//	wait();

*/
}

void DataCollectServerApp::ShowVersion(std::ostream& os) const
{
	os << "\n--------------------------------------------------";
	os << "\nCopyright (c) 2011 - All Rights Reserved by Jie." << std::endl;
	os << APP_TITLE << " Version " << APP_VERSION << std::endl;
	os << "Updated " << __DATE__ << " " << __TIME__ << std::endl;
	os << "\n--------------------------------------------------";
	os << "\nStartup Time : " << TimeToString(_tStartupTime) << std::endl;
}

void DataCollectServerApp::ShowOptions(std::ostream& os) const
{
	_stOptions.Show(os);
}

int DataCollectServerApp::Shutdown()
{
	ACEX_Message msg(TASK_APP, FPARAM_SHUTDOWN, 0);

    //theMainTask.put_msg(msg);

	this->put_msg(msg);

	return 0;
}

int DataCollectServerApp::OnAppTaskMsgProc(const ACEX_Message& msg)
{
	if(msg.fparam() == FPARAM_SHUTDOWN)
	{
		ACEX_LOG_OS(LM_INFO, "<<DataCollectServerApp::OnAppTaskMsgProc>>Recv 'shutdown' msg." << std::endl);
		return -1;
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, "<<DataCollectServerApp::OnAppTaskMsgProc>>Unknwon fparam - " << msg.fparam() << std::endl);
	}
	return 0;
}

const std::string DataCollectServerApp::TimeToString(const time_t& tTime) const
{
	char acTime[19 + 1];
	acTime[19] = '\0';
	struct tm tmTime;
	ACE_OS::strftime(acTime, sizeof(acTime), "%Y-%m-%d %H:%M:%S", ACE_OS::localtime_r(&tTime, &tmTime));
	return std::string(acTime);
}
