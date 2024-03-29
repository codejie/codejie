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

class CCmdShowDataAccess : public ACEX_Command
{
public:
	virtual CCmdShowDataAccess* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

class CCmdShowTerminal : public ACEX_Command
{
public:
	virtual CCmdShowTerminal* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

class CCmdShowController : public ACEX_Command
{
public:
	virtual CCmdShowController* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

class CCmdTest : public ACEX_Command
{
public:
	virtual CCmdTest* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

/*
class CCmdShowDataLoader : public ACEX_Command
{
public:
	virtual CCmdShowDataLoader* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

class CCmdTest : public ACEX_Command
{
public:
	virtual CCmdTest* clone() const;

	virtual ACEX_Command_Tag tag() const;
	virtual int execute(std::ostream& os);
	virtual void help(std::ostream& os) const;
	virtual void help_verbose(std::ostream& os) const;
};

*/
#endif
