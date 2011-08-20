
#include "Defines.h"
#include "ConfigLoader.h"
//#include "TerminalManager.h"
//#include "DataLoader.h"
//#include "MainTask.h"

#include "Packet.h"

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
////////////////////////////////////////////////////////////////////
CCmdShowDataAccess* CCmdShowDataAccess::clone() const
{
	return new CCmdShowDataAccess(*this);
}

ACEX_Command_Tag CCmdShowDataAccess::tag() const
{
	return "showdata";
}

int CCmdShowDataAccess::execute(std::ostream& os)
{
    if(this->args_.count() > 0)
    {
        if(this->args_[0] == "col" || this->args_[0] == "column")
		{
            theApp.ShowData(false, os);
		}
        else if(this->args_[0] == "pac" || this->args_[0] == "packet")
		{
            theApp.ShowPacket(CLIENTTYPE_TERMINAL, os);
		}
		else if(this->args_[0] == "id" || this->args_[0] == "stationid")
		{
			if(this->args_.count() > 1)
			{
				theApp.ShowStationID(os, this->args_[1]);
			}
			else
			{
				this->help_verbose(os);
			}
		}
		else if(this->args_[0] == "inf" || this->args_[0] == "infectant")
		{
			if(this->args_.count() > 1)
			{
				theApp.ShowInfectantID(os, this->args_[1]);
			}
			else
			{
				this->help_verbose(os);
			}
		}
		else if(this->args_[0] == "state")
		{
			theApp.ShowStateData(os);
		}
        else
            this->help_verbose(os);
    }
    else
    {
        theApp.ShowData(true, os);
    }
	return 1;
}

void CCmdShowDataAccess::help(std::ostream& os) const
{
	os << "    Show data info.";
}

void CCmdShowDataAccess::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag() << "[,col(umn)/(station)id,absoluteno/inf(ectant),stdid]";
	os << "\n    Example:\n\t" << tag() << std::endl;
}

////////////////////////////////////////////////////////////////////
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
	if(this->args_.count() > 0)
	{
        if(this->args_[0] == "pac" || this->args_[0] == "packet")
		{
            theApp.ShowPacket(CLIENTTYPE_TERMINAL, os);
		}
		else
		{
			this->help_verbose(os);
		}
	}
	else
	{
		theApp.ShowTerminal(CLIENTTYPE_TERMINAL, os);
	}
	return 1;
}

void CCmdShowTerminal::help(std::ostream& os) const
{
	os << "    Show terminal info.";
}

void CCmdShowTerminal::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag() << "[,pac(ket)]";
	os << "\n    Example:\n\t" << tag() << std::endl;
}
////////////////////////////////////////////////////////////////////
CCmdShowController* CCmdShowController::clone() const
{
	return new CCmdShowController(*this);
}

ACEX_Command_Tag CCmdShowController::tag() const
{
	return "showcontroller";
}

int CCmdShowController::execute(std::ostream& os)
{
	if(this->args_.count() > 0)
	{
        if(this->args_[0] == "pac" || this->args_[0] == "packet")
		{
            theApp.ShowPacket(CLIENTTYPE_CONTROLLER, os);
		}
		else
		{
			this->help_verbose(os);
		}
	}
	else
	{
		theApp.ShowTerminal(CLIENTTYPE_CONTROLLER, os);
	}
	return 1;
}

void CCmdShowController::help(std::ostream& os) const
{
	os << "    Show terminal info.";
}

void CCmdShowController::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag() << "[,pac(ket)]";
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
	os << "test111" << std::endl;

	Packet packet;

	std::string str = "ST=32;CN=2051;QN=20040516010101001;PW=123456;MN=88888880000001;PNO=1;PNUM=1;CP=&&DataTime=20040516021000;B01-Cou=200;101-Cou=2.5,101-Min=1.1,101-Avg=1.1,101-Max=1.1;102-Cou=2.5,102-Min=2.1,102-Avg=2.1,102-Max=2.1&&";
    //std::string str = ST=31;CN=2011;PW=123456;MN=88888888888888;CP=&&DataTime=20110806122650;02-Rtd=111.0,02-ZsRtd=226.8,02-Flag=N;03-Rtd=0.0,03-ZsRtd=0.0,03-Flag=N;01-Rtd=0.0,01-ZsRtd=0.0,01-Flag=N;S01-Rtd=6.0,S01-Flag=N;S03-Rtd=100.0,S03-Flag=N;S08-Rtd=2.0,S08-Flag=N;S02-Rtd=8.0,S02-Flag=N;B02-Rtd=297217.2,B02-Flag=N;S05-Rtd=0.0,S05-Flag=N&&C5C1;

	packet.Analyse(str);
	os << packet << std::endl;
	//packet.Show(os);


	return 1;
}

void CCmdTest::help(std::ostream& os) const
{
	os << "    Run a test for ... a method or object, and so on...";
}

void CCmdTest::help_verbose(std::ostream& os) const
{
	help(os);
	os << "\n    Usage:\n\t" << tag() ;
	os << "\n    Example:\n\t" << tag() << std::endl;
}
/*
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
*/
