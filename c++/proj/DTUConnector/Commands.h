#ifndef __COMMANDS_H__
#define __COMMANDS_H__

#include "acex/Command.h"

class CCmdShowConfigure : public ACEX_Command
{
public:
	virtual CCmdShowConfigure* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

class CCmdShutdown : public ACEX_Command
{
public:
	virtual CCmdShutdown* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

class CCmdShowOptions : public ACEX_Command
{
public:
	virtual CCmdShowOptions* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

class CCmdShowVersion : public ACEX_Command
{
public:
	virtual CCmdShowVersion* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

#endif
