/*
 * CoreMsgTask.h
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#ifndef __COREMSGTASK_H__
#define __COREMSGTASK_H__

#include "acex/Task.h"

#include "DataAccess.h"
#include "CollectServerTask.h"
#include "CommandServerTask.h"

class Packet;
class CommandPacket;
class ConfigLoader;

class CoreMsgTask: public ACEX_Message_Task
{
protected:
	struct ClientData_t
	{
		std::string ip;
		unsigned int port;
		time_t update;

		size_t count;
	};

	typedef std::map<int, ClientData_t> TClientMap;

public:
	CoreMsgTask();
	virtual ~CoreMsgTask();

	int Init(const ConfigLoader& config);

    void ShowData(bool stat, std::ostream& os) const;
    void ShowPacket(std::ostream& os) const;
	void ShowStationID(std::ostream& os, const std::string& ano) const;
	void ShowInfectantID(std::ostream& os, const std::string& nid) const;
	void ShowClient(std::ostream& os) const;
protected:
	void Final();
	int UpdateClientCount(int clientid);
protected:
	virtual int handle_msg(const ACEX_Message& msg);
    int OnTimerMsgProc(const ACEX_Message& msg);
    int OnAppTaskMsgProc(const ACEX_Message& msg);
    int OnCollectServerMsgProc(const ACEX_Message& msg);
    int OnCommandServerMsgProc(const ACEX_Message& msg);

private:
    int OnCollectPacket(const Packet& packet);
	int OnCollectConnect(int clientid, const std::string& ip, unsigned int port);
	int OnCollectDisconnect(int clientid);

    int OnCommandPacket(const CommandPacket& packet);
	int OnCommandConnect(int clientid, const std::string& ip, unsigned int port);
	int OnCommandDisconnect(int clientid);
private:
    std::auto_ptr<DataAccess> _objDataAccess;
    std::auto_ptr<CollectServerTask> _taskCollectServer;
    std::auto_ptr<CommandServerTask> _taskCommandServer;
private:
	TClientMap _mapClient;
    TClientMap _mapCommand;
};

#endif /* __COREMSGTASK_H__ */
