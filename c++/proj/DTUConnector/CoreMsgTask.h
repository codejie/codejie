
#ifndef __COREMSGTASK_H__
#define __COREMSGTASK_H__

#include <map>
#include <string>
#include <iostream>
#include <memory>

#include "acex/Task.h"

class ConfigLoader;
class Packet;
class ConnectionServer;

class CoreMsgTask: public ACEX_Message_Task
{
protected:
	enum DTUStatus { DS_UNKNOWN = -1, DS_WAITTEL = 0, DS_DATAREQ, DS_WAITRSP };

	struct DTUData
	{
		std::string ip;
		unsigned short port;
		std::string tel;
		std::string stationid;

		DTUStatus status;
		int timer;
		time_t update;
		size_t data;
		size_t hello;
	};
	typedef std::map<int, DTUData> TDTUMap;//clientid + data;
public:
	CoreMsgTask();
	virtual ~CoreMsgTask();

	int Init(const ConfigLoader& config);

	void Show(std::ostream& os) const;
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

	int OnTimerPacket(int clientid);
	int OnTimerPacketTimeout(int clientid);
	int OnTimerSocketTimeout(int clientid);

	int OnServerHelloPacket(int clientid, const Packet& packet);
	int OnServerDataRespPacket(int clientid, const Packet& packet);

	int RegTimer(int clientid, int type, unsigned int timeout);
	void UnregTimer(int timerid);

	int SendDataReqPacket(int clientid);
private:
	int _iReqInterval;
	int _iPacketTimeout;

	std::auto_ptr<ConnectionServer> _ptrConnServer;
private:
	TDTUMap _mapDTU;
};

#endif /* __COREMSGTASK_H__ */
