#ifndef __DTUCONNECTORAPP_H__
#define __DTUCONNECTORAPP_H__

#include <acef/App.h>

#include "Options.h"
#include "ConfigLoader.h"
#include "CoreMsgTask.h"

class DTUConnectorApp: public ACEF_App {
public:
	DTUConnectorApp();
	virtual ~DTUConnectorApp();
protected:
	virtual int init(int argc, char* argv[]);
	virtual int fini();

	virtual int regist_command();
	virtual int handle_signal(int signum, siginfo_t* siginfo, ucontext_t* ucontext);

	virtual int handle_msg(const ACEX_Message& msg);
protected:
    virtual int regist_signal();
private:
	int InitConfig();
	int StartTasks();
	void StopTasks();
	int OnAppTaskMsgProc(const ACEX_Message& msg);
public:
	time_t StartupTime() const { return _tStartupTime; }
	void ShowVersion(std::ostream& os) const;
	void ShowOptions(std::ostream& os) const;
	void ShowConfig(std::ostream& os) const;
 //   void ShowData(bool stat, std::ostream& os) const;
 //   void ShowPacket(std::ostream& os) const;
	//void ShowStationID(std::ostream& os, const std::string& ano) const;
	//void ShowInfectantID(std::ostream& os, const std::string& nid) const;
	//void ShowTerminal(std::ostream& os) const;

	int Shutdown();
private:
    const std::string TimeToString(const time_t& tTime) const;
private:
	time_t _tStartupTime;
	COptions _stOptions;
	ConfigLoader _stConfigLoader;
	CoreMsgTask _taskCore;

};

extern DTUConnectorApp theApp;

#endif /* DATACOLLECTSERVERAPP_H_ */
