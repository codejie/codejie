
#include "ConfigLoader.h"

#include "DTUConnectorApp.h"

#include "Commands.h"

CCmdShowConfigure* CCmdShowConfigure::clone() const
{
	return new CCmdShowConfigure(*this);
}

ACEX_Command_Tag CCmdShowConfigure::tag() const
{
	return "showconf";
}

int CCmdShowConfigure::execute(std::ostream& os)
{
	theApp.ShowConfig(os);
	return 1;
}

void CCmdShowConfigure::help(std::ostream& os) const
{
	os << "    Display system private/public configurations.";
}

void CCmdShowConfigure::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag() << "[,[sys]/pub/pri/sys]";
	os << "\n    Example:\n\t" << tag() << "\n\t" << tag() << ",pri" << std::endl;
}

//////////////////////////////////////////////////////////////////////////
CCmdShutdown* CCmdShutdown::clone() const
{
	return new CCmdShutdown(*this);
}

ACEX_Command_Tag CCmdShutdown::tag() const
{
	return "shutdown";
}

int CCmdShutdown::execute(std::ostream& os)
{
	theApp.Shutdown();
	return 0;
}

void CCmdShutdown::help(std::ostream& os) const
{
	os << "    Shutdown system.";
}

void CCmdShutdown::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag();
	os << "\n    Example:\n\t" << tag() << std::endl;
}

//////////////////////////////////////////////////////////////////////////
CCmdShowOptions* CCmdShowOptions::clone() const
{
	return new CCmdShowOptions(*this);
}

ACEX_Command_Tag CCmdShowOptions::tag() const
{
	return "showopt";
}

int CCmdShowOptions::execute(std::ostream& os)
{
	theApp.ShowOptions(os);
	return 1;
}

void CCmdShowOptions::help(std::ostream& os) const
{
	os << "    Show current options setting.";
}

void CCmdShowOptions::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag();
	os << "\n    Example:\n\t" << tag() << std::endl;
}

//////////////////////////////////////////////////////////////////////////
CCmdShowVersion* CCmdShowVersion::clone() const
{
	return new CCmdShowVersion(*this);
}

ACEX_Command_Tag CCmdShowVersion::tag() const
{
	return "showver";
}

int CCmdShowVersion::execute(std::ostream& os)
{
	theApp.ShowVersion(os);
	return 1;
}

void CCmdShowVersion::help(std::ostream& os) const
{
	os << "    Show system version info.";
}

void CCmdShowVersion::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag();
	os << "\n    Example:\n\t" << tag() << std::endl;
}

