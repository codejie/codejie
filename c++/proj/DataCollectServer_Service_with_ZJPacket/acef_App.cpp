#ifndef ACEF_APP_C
#define ACEF_APP_C

#include "acex/ACEX.h"
#include "acex/Exception.h"
#include "acex/Command_Parser.h"
#include "acef_Commands.h"
#include "acef_App.h"

#if !defined (ACE_LACKS_PRAGMA_ONCE)
#  pragma once
#endif /* ACE_LACKS_PRAGMA_ONCE */

ACEF_App* pACEF_App = 0;

ACEF_App::ACEF_App()
: command_parser_(ACEX_COMMAND_PARSER),
  command_executor_(ACEX_COMMAND_EXECUTOR),
  mt_log_file_stream_(ACEX_MT_LOG_FILE_STREAM),
  output_proxy_(ACEX_OUTPUT_PROXY),
  std_in_task_(ACEX_STD_IN_TASK),
  telnet_server_task_(ACEX_TELNET_SERVER_TASK),
  timer_task_(ACEX_TIMER_TASK)
{
	ACE_TRACE("ACEF_App::ACEF_App()");

	this->regist_app();
}

ACEF_App::~ACEF_App()
{
	ACE_TRACE("ACEF_App::~ACEF_App()");
}

void ACEF_App::run(int argc, char* argv[])
{
	ACE_TRACE("ACEF_App::run()");

	this->regist_signal();

	this->regist_command();

	this->init(argc, argv);

	this->svc();

	this->fini();
}

int ACEF_App::init(int argc, char* argv[])
{
	ACE_TRACE("ACEF_App::init()");

	return 0;
}

int ACEF_App::fini()
{
	ACE_TRACE("ACEF_App::fini()");

	return 0;
}

int ACEF_App::init_as_daemon()
{
	ACE_TRACE("ACEF_App::init_as_daemon()");

	if (ACE_OS::fork() > 0)
	{
		ACE_OS::sleep(1);
		exit(0);
	}

	ACE_OS::setsid();
/*
	if (ACE_OS::fork() > 0)
		exit(0);

	ACE_OS::umask(0);
*/
	return 0;
}

int ACEF_App::regist_command()
{
	ACE_TRACE("ACEF_App::regist_command()");

	if (this->command_parser()->regist_command(ACEF_Output_Level_Command()) != 0)
		throw ACEX_Runtime_Exception("Command output_level regist failed", __FILE__, __LINE__);
	if (this->command_parser()->regist_command(ACEF_Output_Proxy_Command()) != 0)
		throw ACEX_Runtime_Exception("Command output_proxy regist failed", __FILE__, __LINE__);
	if (this->command_parser()->regist_command(ACEF_Output_Verbose_Command()) != 0)
		throw ACEX_Runtime_Exception("Command output_verbose regist failed", __FILE__, __LINE__);

	return 0;
}

int ACEF_App::regist_signal()
{
	ACE_TRACE("ACEF_App::regist_signal()");

	ACE_Sig_Guard sig_guard;

//	if (this->regist_sig(SIGHUP) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGHUP regist failed", __FILE__, __LINE__);
	if (this->regist_sig(SIGINT) != 0)
		throw ACEX_Runtime_Exception("Signal SIGINT regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGQUIT) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGQUIT regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGILL) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGILL regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGTRAP) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGTRAP regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGIOT) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGIOT regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGABRT) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGABRT regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGEMT) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGEMT regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGFPE) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGFPE regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGKILL) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGKILL regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGBUS) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGBUS regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGSEGV) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGSEGV regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGSYS) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGSYS regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGPIPE) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGPIPE regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGALRM) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGALRM regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGTERM) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGTERM regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGUSR1) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGUSR1 regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGUSR2) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGUSR2 regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGCLD) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGCLD regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGCHLD) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGCHLD regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGPWR) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGPWR regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGWINCH) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGINT regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGURG) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGURG regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGPOLL) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGINT regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGIO) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGIO regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGSTOP) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGSTOP regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGTSTP) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGTSTP regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGCONT) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGCONT regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGTTIN) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGTTIN regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGTTOU) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGTTOU regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGVTALRM) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGVTALRM regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGPROF) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGPROF regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGXCPU) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGXCPU regist failed", __FILE__, __LINE__);
//	if (this->regist_sig(SIGXFSZ) != 0)
//		throw ACEX_Runtime_Exception("Signal SIGXFSZ regist failed", __FILE__, __LINE__);

	return 0;
}

int ACEF_App::handle_signal(int signum, siginfo_t*, ucontext_t*)
{
	ACE_TRACE("ACEF_App::handle_signal()");

	ACEX_DEBUG((LM_DEBUG, "Received signal [%S]\n", signum));

	if (signum == SIGINT)
	{
		ACEX_LOG((LM_EMERGENCY, "Received signal [%S]. Program will exit.\n", signum));
		exit(1);
	}

	return 0;
}


ACEX_Command_Parser* ACEF_App::command_parser() const
{
	return this->command_parser_;
}


ACEX_Command_Executor* ACEF_App::command_executor() const
{
	return this->command_executor_;
}


ACEX_MT_Log_File_Stream* ACEF_App::mt_log_file_stream() const
{
	return this->mt_log_file_stream_;
}


ACEX_Output_Proxy* ACEF_App::output_proxy() const
{
	return this->output_proxy_;
}


ACEX_Std_In_Task* ACEF_App::std_in_task() const
{
	return this->std_in_task_;
}


ACEX_Telnet_Server_Task* ACEF_App::telnet_server_task() const
{
	return this->telnet_server_task_;
}


ACEX_Timer_Task* ACEF_App::timer_task() const
{
	return this->timer_task_;
}


void ACEF_App::regist_app() const
{
	ACE_TRACE("ACEF_App::regist_app()");

	::pACEF_App = ACE_const_cast(ACEF_App*, this);
}


int ACEF_App::regist_sig(int signum)
{
	ACE_TRACE("ACEF_App::regist_sig()");

	return this->sig_handler_.register_handler(signum, this);
}


int ACEF_App::remove_sig(int signum)
{
	ACE_TRACE("ACEF_App::remove_sig()");

	return this->sig_handler_.remove_handler(signum);
}

#endif /* ACEF_APP_C */
