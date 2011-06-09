
#include "ConfigLoader.h"
//#include "TerminalManager.h"
//#include "DataLoader.h"
//#include "MainTask.h"

#include "DataCollectServerApp.h"

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
	theConfig.Show(os);
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

//////////////////////////////////////////////////////////////////////////
CCmdShowTerminal* CCmdShowTerminal::clone() const
{
	return new CCmdShowTerminal(*this);
}

ACEX_Command_Tag CCmdShowTerminal::tag() const
{
	return "showterminal";
}

int CCmdShowTerminal::execute(std::ostream& os)
{
    if(args_.count() > 0 && args_[0] == "cache")
        theTerminalManager.ShowCache(os);
    else
        theTerminalManager.Show(os);
	return 1;
}

void CCmdShowTerminal::help(std::ostream& os) const
{
	os << "    Show terminal info.";
}

void CCmdShowTerminal::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag() << "[,cache]";
	os << "\n    Example:\n\t" << tag() << std::endl;
}

//////////////////////////////////////////////////////////////////////////
CCmdShowDataLoader* CCmdShowDataLoader::clone() const
{
	return new CCmdShowDataLoader(*this);
}

ACEX_Command_Tag CCmdShowDataLoader::tag() const
{
	return "showdata";
}

int CCmdShowDataLoader::execute(std::ostream& os)
{
    theDataLoader.Show(os);
	return 1;
}

void CCmdShowDataLoader::help(std::ostream& os) const
{
	os << "    Show database info.";
}

void CCmdShowDataLoader::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag();
	os << "\n    Example:\n\t" << tag() << std::endl;
}

//////////////////////////////////////////////////////////////////////////
CCmdTest* CCmdTest::clone() const
{
	return new CCmdTest(*this);
}

ACEX_Command_Tag CCmdTest::tag() const
{
	return "test";
}

int CCmdTest::execute(std::ostream& os)
{   
//STATION_ID = 1
//STATION_TYPE = 2
//INFECTANT_ID = 32
//INFECTANT_CHNAME = COD(434F44)
//INFECTANT_ENAME = P-COD
//INFECTANT_UNIT = mg/L
//M_VALUE = 152.1
//M_ALERM = 0
//M_TIME = 02-MAR-11


    DataLoader::data_t data;
    data.m_strType = "2";
    data.m_strInfectantID = "32";
    data.m_strInfectantChName = "COD";
    data.m_strInfectantEnName = "P-COD";
    data.m_strInfectantUnit = "mg/L";
    data.m_strValue = "152.1";
    data.m_strAlerm = "0";
    data.m_strDate = "02-MAR-11";

    DataLoader::TDataVector vct;
    vct.push_back(data);

    DataLoader::TTerminalDataMap datamap;
    datamap.insert(std::make_pair("1", vct));

    if(theTerminalManager.DataPost(theMainTask, "0", datamap) != 0)
    {
        os << "\ntest failed." << std::endl;
    }

	return 1;
}

void CCmdTest::help(std::ostream& os) const
{
	os << "    Run a test...";
}

void CCmdTest::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag();
	os << "\n    Example:\n\t" << tag() << std::endl;
}
