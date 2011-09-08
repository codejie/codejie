//=============================================================================
/**
 *  @file    App.h
 *
 *  version 1.0  2003/05/29
 *
 *  @author Feng Wenyong (wyfeng@utstar.com)
 *
 *  @class ACEF_App
 *
 *  This file define the basic app class.
 */
//=============================================================================

#ifndef ACEF_APP_H
#define ACEF_APP_H

#include "ace/Signal.h"
#include "acex/Command_Parser.h"
#include "acex/Command_Executor.h"
#include "acex/MT_Log_File_Stream.h"
#include "acex/Output_Proxy.h"
#include "acex/Task.h"
#include "acex/Std_In_Task.h"
#include "acex/Telnet_Server.h"
#include "acex/Timer.h"

#if !defined (ACE_LACKS_PRAGMA_ONCE)
#  pragma once
#endif /* ACE_LACKS_PRAGMA_ONCE */

/**
 * @class ACEF_App
 *
 * @brief The abstract basic app class.
 *
 * This class define the app interface in framework .
 * User must inherit from this class to define concrete app class.
 */
class ACE_Export ACEF_App: public ACEX_Message_Task
{
public:
	/// Default constructor.
	ACEF_App();

	/// Destructor.
	virtual ~ACEF_App() = 0;

	/// The main thread entry point.
	virtual void run(int argc, char* argv[]);

	/// Return the default command parser.
	ACEX_Command_Parser* command_parser() const;

	/// Return the default command executor.
	ACEX_Command_Executor* command_executor() const;

	/// Return the default log file stream.
	ACEX_MT_Log_File_Stream* mt_log_file_stream() const;

	/// Return the default ouput proxy.
	ACEX_Output_Proxy* output_proxy() const;

	/// Return the default standard input task.
	ACEX_Std_In_Task* std_in_task() const;

	/// Return the default telnet server task.
	ACEX_Telnet_Server_Task* telnet_server_task() const;

	/// Return the default timer task.
	ACEX_Timer_Task* timer_task() const;

protected:
	/// Init before app run.
	/// User must override this method.
	virtual int init(int argc, char* argv[]);

	/// Fini after app run.
	/// User must override this method.
	virtual int fini();

	/// Regist default commands.
	/// User can override this method to regist other commands.
	virtual int regist_command();

	/// Regist default signals.
	/// User can override this method to regist other signals.
	virtual int regist_signal();

	/// The deafult signal handle.
	/// User can override this method to handle signals himself.
	virtual int handle_signal(int signum, siginfo_t*, ucontext_t*);

	/// Run as daemon process.
	int init_as_daemon();

	/// Regist a signal.
	/// If success return 0.
	int regist_sig(int signum);

	/// Remove a signal.
	/// If success return 0.
	int remove_sig(int signum);

private:
	/// Copy constructor. Disabled.
	ACEF_App(const ACEF_App&);

	/// Assignment operator. Disabled.
	const ACEF_App& operator=(const ACEF_App&);

	/// Regist the global app ptr.
	void regist_app() const;

	/// Signal handler.
	ACE_Sig_Handler sig_handler_;

	/// Default command parser.
	ACEX_Command_Parser* command_parser_;

	/// Default command executor.
	ACEX_Command_Executor* command_executor_;

	/// Default log file stream.
	ACEX_MT_Log_File_Stream* mt_log_file_stream_;

	/// Default output proxy.
	ACEX_Output_Proxy* output_proxy_;

	/// Default standard input task.
	ACEX_Std_In_Task* std_in_task_;

	/// Default telnet server task.
	ACEX_Telnet_Server_Task* telnet_server_task_;

	/// Default timer task.
	ACEX_Timer_Task* timer_task_;
};

extern ACEF_App* pACEF_App;

#endif /* ACEF_APP_H */
