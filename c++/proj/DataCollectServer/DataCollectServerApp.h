/*
 * DataCollectServerApp.h
 *
 *  Created on: Jun 9, 2011
 *      Author: jie
 */

#ifndef DATACOLLECTSERVERAPP_H_
#define DATACOLLECTSERVERAPP_H_

#include <acef/App.h>

class DataCollectServerApp: public ACEF_App {
public:
	DataCollectServerApp();
	virtual ~DataCollectServerApp();
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
	int Shutdown();
private:
    const std::string TimeToString(const time_t& tTime) const;
private:
	time_t _tStartupTime;
	COptions _stOptions;

};

extern DataCollectServerApp theApp;

#endif /* DATACOLLECTSERVERAPP_H_ */
