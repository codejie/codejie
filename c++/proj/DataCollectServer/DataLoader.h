#ifndef __DATALOADER_H__
#define __DATALOADER_H__

#include <iostream>
#include <string>

#include "acex/Task.h"

class DataAccess;

class DataLoader
{
public:
	enum TimerType { TT_REALTIME = 0, TT_PERIOD };
protected:
	struct ClientData 
	{
        int rtTimer;
        int pdTimer;
        size_t rtCount;
        size_t pdCount;
        size_t rtData;
        size_t pdData;
	};

    typedef std::map<int, ClientData> TClientDataMap;
public:
	DataLoader(ACEX_Message_Task* msgtask, DataAccess* access);
	virtual ~DataLoader();

	int Init(int realtime, int period);
	void Final();

    int OnControllerConnected(int clientid);
	int OnControllerDisconnect(int clientid);

	int OnTimer(int clientid, TimerType type);

    int OnPacket(int clientid, const Packet& packet);
    int OnPacketTimeout(int clientid, const Packet& packet);
protected:
	int RegTimer(TimerType type, int clientid);
    void UnregTimer(int timer);

    int PutMsg(int clientid, int type, const Packet* packet);
private:
    ACEX_Message_Task* _taskMsg;
    DataAccess* _dataAccess;
private:
    int _realtime;
    int _period;
    TClientDataMap _mapClientData;
};

#endif
