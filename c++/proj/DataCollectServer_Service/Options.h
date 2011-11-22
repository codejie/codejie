#ifndef __OPTIONS_H__
#define __OPTIONS_H__

#include <string>

class COptions
{
public:
	COptions();
	virtual ~COptions() {}

	int Scan(int argc, char *argv[]);

	bool PopStart() const { return _bPopStart; }
	bool DaemonOpt() const { return _bDaemon; }
	bool VerboseOpt() const { return _bVerbose; }
	bool VersionOpt() const { return _bVersion; }
	bool HelpOpt() const { return _bHelp; }
	unsigned int ProxyOpt() const { return _uiProxy; }
	unsigned int LevelOpt() const { return _uiLevel; }
    bool TestOpt() const { return _bTest; }

	std::string ConfigureFile() const { return _strConfigFile; }

	std::string AppTitle() const { return _strAppTitle; }

	void ShowUsage(std::ostream& os) const;
	void Show(std::ostream& os) const;
private:
	bool _bPopStart;
	bool _bDaemon;
	bool _bVerbose;
	bool _bVersion;
	bool _bHelp;
	unsigned int _uiProxy;
	unsigned int _uiLevel;
    bool _bTest;

	std::string _strConfigFile;

	std::string _strAppTitle;
};


#endif
