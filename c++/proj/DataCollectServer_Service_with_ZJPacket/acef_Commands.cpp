#ifndef ACEF_COMMANDS_C
#define ACEF_COMMANDS_C

#include "acex/Output_Proxy.h"
#include "acef_Commands.h"

ACEF_Output_Level_Command* ACEF_Output_Level_Command::clone() const
{
	ACE_TRACE("ACEF_Output_Level_Command::clone()");

	ACEF_Output_Level_Command* pclone = 0;
	ACE_NEW_RETURN(pclone, ACEF_Output_Level_Command(*this), 0);

	return pclone;
}

ACEX_Command_Tag ACEF_Output_Level_Command::tag() const
{
	ACE_TRACE("ACEF_Output_Level_Command::tag()");

	return "_level";
}

int ACEF_Output_Level_Command::execute(std::ostream& os)
{
	ACE_TRACE("ACEF_Output_Level_Command::execute()");

	if (this->args_.count() == 0)
	{
		os << "level = " << ACEX_OUTPUT_PROXY->priority_mask() << std::endl;
		return 1;
	}

	ACEX_OUTPUT_PROXY->priority_mask(ACE_OS::atoi(this->args_[0].c_str()));

	return 1;
}

void ACEF_Output_Level_Command::help(std::ostream& os) const
{
	ACE_TRACE("ACEF_Output_Level_Command::help()");

	os << "    Set or get the output level.";
}

void ACEF_Output_Level_Command::help_verbose(std::ostream& os) const
{
	ACE_TRACE("ACEF_Output_Level_Command::help_verbose()");

	os << "    Description: Set or get the output level." << std::endl;
	os << "    Usage:       level[,level]" << std::endl;
	os << "                 level - Output level." << std::endl;
	os << "                     1    - SHUTDOWN" << std::endl;
	os << "                     2    - TRACE" << std::endl;
	os << "                     4    - DEBUG" << std::endl;
	os << "                     8    - INFO" << std::endl;
	os << "                     16   - NOTICE" << std::endl;
	os << "                     32   - WARNING" << std::endl;
	os << "                     64   - STARTUP" << std::endl;
	os << "                     128  - ERROR" << std::endl;
	os << "                     256  - CRITICAL" << std::endl;
	os << "                     512  - ALERT" << std::endl;
	os << "                     1024 - EMERGENCY" << std::endl;
	os << "    Examples:    (1) _level" << std::endl;
	os << "                 (2) _level,8" << std::endl;
	os << "                 (3) _level,12" << std::endl;
}

//-------------------------------------------------
ACEF_Output_Proxy_Command* ACEF_Output_Proxy_Command::clone() const
{
	ACE_TRACE("ACEF_Output_Proxy_Command::clone()");

	ACEF_Output_Proxy_Command* pclone = 0;
	ACE_NEW_RETURN(pclone, ACEF_Output_Proxy_Command(*this), 0);

	return pclone;
}

ACEX_Command_Tag ACEF_Output_Proxy_Command::tag() const
{
	ACE_TRACE("ACEF_Output_Proxy_Command::tag()");

	return "_proxy";
}

int ACEF_Output_Proxy_Command::execute(std::ostream& os)
{
	ACE_TRACE("ACEF_Output_Proxy_Command::execute()");

	if (this->args_.count() == 0)
	{
		os << "proxy = " << ACEX_OUTPUT_PROXY->proxy_flags() << std::endl;
		return 1;
	}

	ACEX_OUTPUT_PROXY->proxy_flags(ACE_OS::atoi(this->args_[0].c_str()));

	return 1;
}

void ACEF_Output_Proxy_Command::help(std::ostream& os) const
{
	ACE_TRACE("ACEF_Output_Proxy_Command::help()");

	os << "    Set or get the output proxy.";
}

void ACEF_Output_Proxy_Command::help_verbose(std::ostream& os) const
{
	ACE_TRACE("ACEF_Output_Proxy_Command::help_verbose()");

	os << "    Description: Set or get the output proxy." << std::endl;
	os << "    Usage:       proxy[,proxy]" << std::endl;
	os << "                 proxy - Output proxy." << std::endl;
	os << "                     1    - STD_OUT" << std::endl;
	os << "                     2    - LOG_FILE" << std::endl;
	os << "                     4    - TELNET_CLIENT" << std::endl;
	os << "    Examples:    (1) _proxy" << std::endl;
	os << "                 (2) _proxy,2" << std::endl;
	os << "                 (3) _proxy,3" << std::endl;
}

//-------------------------------------------------
ACEF_Output_Verbose_Command* ACEF_Output_Verbose_Command::clone() const
{
	ACE_TRACE("ACEF_Output_Verbose_Command::clone()");

	ACEF_Output_Verbose_Command* pclone = 0;
	ACE_NEW_RETURN(pclone, ACEF_Output_Verbose_Command(*this), 0);

	return pclone;
}

ACEX_Command_Tag ACEF_Output_Verbose_Command::tag() const
{
	ACE_TRACE("ACEF_Output_Verbose_Command::tag()");

	return "_verbose";
}

int ACEF_Output_Verbose_Command::execute(std::ostream& os)
{
	ACE_TRACE("ACEF_Output_Verbose_Command::execute()");

	if (this->args_.count() == 0)
	{
		os << "verbose = " << ACEX_OUTPUT_PROXY->verbose() << std::endl;
		return 1;
	}

	ACEX_OUTPUT_PROXY->verbose(ACE_OS::atoi(this->args_[0].c_str()));

	return 1;
}

void ACEF_Output_Verbose_Command::help(std::ostream& os) const
{
	ACE_TRACE("ACEF_Output_Verbose_Command::help()");

	os << "    Set or get the output verbose.";
}

void ACEF_Output_Verbose_Command::help_verbose(std::ostream& os) const
{
	ACE_TRACE("ACEF_Output_Verbose_Command::help_verbose()");

	os << "    Description: Set or get the output verbose." << std::endl;
	os << "    Usage:       verbose[,verbose]" << std::endl;
	os << "                 verbose - Output verbose." << std::endl;
	os << "                     0    - Not Verbose" << std::endl;
	os << "                     1    - Verbose" << std::endl;
	os << "    Examples:    (1) _verbose" << std::endl;
	os << "                 (2) _verbose,0" << std::endl;
	os << "                 (3) _verbose,1" << std::endl;
}

#endif /* ACEF_COMMANDS_C */
