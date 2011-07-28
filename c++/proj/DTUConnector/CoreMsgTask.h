
#ifndef __COREMSGTASK_H__
#define __COREMSGTASK_H__

#include <map>
#include <string>
#include <iostream>

#include "acex/Task.h"

class ConfigLoader;
class Packet;

class CoreMsgTask: public ACEX_Message_Task
{
protected:
	enum DTUStatus { DS_UNKNOWN = -1, DS_WAITTEL = 0, DS_QUERY, DS_GET };

	struct DTUData
	{
		std::string ip;
		unsigned short port;
		std::string tel;
		std::string stationid;

		DTUStatus status;
		int timer;
		time_t update;
		size_t count;
	};
	typedef std::map<int, DTUData> TDTUMap;//clientid + data;
public:
	CoreMsgTask();
	virtual ~CoreMsgTask();

	int Init(const ConfigLoader& config);
protected:
	void Final();

protected:
	virtual int handle_msg(const ACEX_Message& msg);
    int OnTimerMsgProc(const ACEX_Message& msg);
    int OnAppTaskMsgProc(const ACEX_Message& msg);
	int OnServerMsgProc(const ACEX_Message& msg);
private:
	int OnServerPacket(int clientid, const Packet& packet);
	int OnServerSocketConnect(int clientid, const std::string& ip, unsigned int port);
	int OnServerSocketDisconnect(int clientid);

	int RegTimer(int clientid, int type, unsigned int timeout);
	void UnregTimer(int timerid);
private:
	TDTUMap _mapDTU;
};

#endif /* __COREMSGTASK_H__ */
