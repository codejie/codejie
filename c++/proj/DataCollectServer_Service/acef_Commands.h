//=============================================================================
/**
 *  @file    Commands.h
 *
 *  version 1.0  2003/05/29
 *
 *  @author Feng Wenyong (wyfeng@utstar.com)
 *
 *  @class ACEF_Output_Level_Command
 *         ACEF_Output_Proxy_Command
 *         ACEF_Output_Verbose_Command
 *
 *  This file define the default command will be registed by framework classes .
 */
//=============================================================================

#ifndef ACEF_COMMANDS_H
#define ACEF_COMMANDS_H

#include <iostream>
#include "ace/ACE.h"
#include "ace/Log_Msg.h"
#include "acex/Command.h"

#if !defined (ACE_LACKS_PRAGMA_ONCE)
#  pragma once
#endif /* ACE_LACKS_PRAGMA_ONCE */

/**
 * @class ACEF_Output_Level_Command
 *
 * @brief Set the output level.
 * 1    - SHUTDOWN
 * 2    - TRACE
 * 4    - DEBUG
 * 8    - INFO
 * 16   - NOTICE
 * 32   - WARNING
 * 64   - STARTUP
 * 128  - ERROR
 * 256  - CRITICAL
 * 512  - ALERT
 * 1024 - EMERGENCY
 */
class ACE_Export ACEF_Output_Level_Command: public ACEX_Command
{
public:
	virtual ACEF_Output_Level_Command* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

/**
 * @class ACEF_Output_Proxy_Command
 *
 * @brief Set the output proxy target.
 * 1 - STD_OUT
 * 2 - LOG_FILE
 * 4 - TELNET_CLIENT
 */
class ACE_Export ACEF_Output_Proxy_Command: public ACEX_Command
{
public:
	virtual ACEF_Output_Proxy_Command* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

/**
 * @class ACEF_Output_Verbose_Command
 *
 * @brief Set the output verbose style or not.
 * 0 - not verbose
 * 1 - verbose
 */
class ACE_Export ACEF_Output_Verbose_Command: public ACEX_Command
{
public:
	virtual ACEF_Output_Verbose_Command* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

#endif /* ACEF_COMMANDS_H */
