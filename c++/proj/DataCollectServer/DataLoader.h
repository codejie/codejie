#ifndef __DATALOADER_H__
#define __DATALOADER_H__

#include <iostream>
#include <string>

#include "acex/Task.h"

class DataAccess;

class DataLoader
{
public:
	enum TimerType { TT_REALTIME = 0, TT_PERIOD, TT_TIMEOUT };
protected:
	struct ClientData 
	{

	};
public:
	DataLoader(ACEX_Message_Task* msgtask, DataAccess* access);
	virtual ~DataLoader();

	int Init(int runtime, int period);
	void Final();

	int OnControllerConnected(int clientid);
	int OnControllerDisconnect(int clientid);

	int OnTimer(TimerType type, int clientid);
protected:
	int RegTime(TimerType type, int clientid);
private:
    ACEX_Message_Task* _taskMsg;
};

#endif
