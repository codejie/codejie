ACE_INLINE
ACEX_Command_Parser* ACESF_Service::command_parser() const
{
	return this->command_parser_;
}

ACE_INLINE
ACEX_Command_Executor* ACESF_Service::command_executor() const
{
	return this->command_executor_;
}

ACE_INLINE
ACEX_MT_Log_File_Stream* ACESF_Service::mt_log_file_stream() const
{
	return this->mt_log_file_stream_;
}

ACE_INLINE
ACEX_Output_Proxy* ACESF_Service::output_proxy() const
{
	return this->output_proxy_;
}


ACE_INLINE
ACEX_Telnet_Server_Task* ACESF_Service::telnet_server_task() const
{
	return this->telnet_server_task_;
}

ACE_INLINE
ACEX_Timer_Task* ACESF_Service::timer_task() const
{
	return this->timer_task_;
}

ACE_INLINE
void ACESF_Service::regist_app() const
{
	ACE_TRACE("ACESF_Service::regist_app()");

	::pACESF_Service = ACE_const_cast(ACESF_Service*, this);
}

