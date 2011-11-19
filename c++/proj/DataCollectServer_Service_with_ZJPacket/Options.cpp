
#include "ace/Get_Opt.h"
#include "acex/Output_Proxy.h"
#include "acex/Exception.h"

#include "Defines.h"

#include "Options.h"

COptions::COptions()
{
	_bPopStart = false;
	_bDaemon = false;
	_bVerbose = false;
	_bVersion = false;
	_bHelp = false;

	_uiProxy = ACEX_Output_Proxy::STD_OUT;
	_uiLevel = LM_SHUTDOWN
		| LM_TRACE
		| LM_DEBUG
		| LM_INFO
		| LM_NOTICE
		| LM_WARNING
		| LM_STARTUP
		| LM_ERROR
		| LM_CRITICAL
		| LM_ALERT
		| LM_EMERGENCY;

	_strConfigFile = std::string(APP_TITLE) + ".ini";

	_strAppTitle = APP_TITLE;
}

int COptions::Scan(int argc, char *argv[])
{
	ACE_Get_Opt stGetOpt(argc, argv, "oOvVhHdDxXp:P:l:L:f:F:");
	char ch;
	while((ch = stGetOpt()) != EOF)
	{
		switch(ch)
		{
		case 'O':
		case 'o':
			_bPopStart = true;
			break;
		case 'V':
		case 'v':
			_bVersion = true;
			break;
		case 'h':
		case 'H':
			_bHelp = true;
			break;
		//case 'd':
		//case 'D':
		//	_bDaemon = true;
		//	_uiProxy = ACEX_Output_Proxy::LOG_FILE;
		//	break;
		case 'x':
		case 'X':
			_bVerbose = true;
			break;
		case 'p':
		case 'P':
			_uiProxy = atoi(stGetOpt.opt_arg());
			break;
		case 'l':
		case 'L':
			_uiLevel = atoi(stGetOpt.opt_arg());
			break;
		case 'f':
		case 'F':
			_strConfigFile = stGetOpt.opt_arg();
			break;
		default:
			{
				throw ACEX_Runtime_Exception("Unknown system option.", __FILE__, __LINE__);
			}
		}
	}
//	_strAppTitle = argv[0];
	return 0;
}

void COptions::ShowUsage(std::ostream& os) const
{
	os << "Usage: " << _strAppTitle << "[-f<config file>] [-h] [-v] [-d] [-p<output_proxy>] [-l<output_level>] [-x] [-o]" << std::endl;
    os << "f -- Set config file." << std::endl;
	os << "h -- Show help information." << std::endl;
	os << "v -- Show version information." << std::endl;
	os << "d -- Run as daemon." << std::endl;
	os << "p -- Set the output proxy." << std::endl;
	os << "     1    - STD_OUT" << std::endl;
	os << "     2    - LOG_FILE" << std::endl;
	os << "     4    - TELNET_CLIENT" << std::endl;
	os << "l -- Set the output level." << std::endl;
	os << "     1    - SHUTDOWN" << std::endl;
	os << "     2    - TRACE" << std::endl;
	os << "     4    - DEBUG" << std::endl;
	os << "     8    - INFO" << std::endl;
	os << "     16   - NOTICE" << std::endl;
	os << "     32   - WARNING" << std::endl;
	os << "     64   - STARTUP" << std::endl;
	os << "     128  - ERROR" << std::endl;
	os << "     256  - CRITICAL" << std::endl;
	os << "     512  - ALERT" << std::endl;
	os << "     1024 - EMERGENCY" << std::endl;
	os << "x -- Set the output verbose." << std::endl;
	os << "o -- Run with Pop." << std::endl;
}

void COptions::Show(std::ostream& os) const
{
	os << "\n-------------- Options --------------\n";
	os << "\n    PopStart = " << (_bPopStart ? "TRUE" : "FALSE");
	os << "\n    Daemon = " << (_bDaemon ? "TRUE" : "FALSE");
	os << "\n    Verbose = " << (_bVerbose ? "TRUE" : "FALSE");
	os << "\n    Proxy = " << _uiProxy;
	os << "\n    Level = " << _uiLevel;
	os << "\n    AppTitle = " << _strAppTitle;
	os << "\n    Config = " << _strConfigFile;
	os << "\n-------------------------------------" << std::endl;
}
