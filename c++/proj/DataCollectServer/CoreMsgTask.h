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
#include "DataLoader.h"
#include "CollectServerTask.h"
#include "ControllerServerTask.h"

class Packet;
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

    struct StateData_t
    {
        int clientid;
        int type;
        const Packet* packet;
        int timer;
    };
    typedef std::map<unsigned int, StateData_t> TStateDataMap;//state + data
    typedef std::map<const std::string, unsigned int> TStateIndexMap;//qn + count
public:
	CoreMsgTask();
	virtual ~CoreMsgTask();

	int Init(const ConfigLoader& config);

    void ShowData(bool stat, std::ostream& os) const;
    void ShowPacket(int clienttype, std::ostream& os) const;
	void ShowStationID(std::ostream& os, const std::string& ano) const;
	void ShowInfectantID(std::ostream& os, const std::string& nid) const;
	void ShowClient(int clienttype, std::ostream& os) const;
	void ShowStateData(std::ostream& os) const;
protected:
	void Final();
	int UpdateClientCount(int collect, int clientid);//collect == 1 is client, else is controller
protected:
	virtual int handle_msg(const ACEX_Message& msg);
    int OnTimerMsgProc(const ACEX_Message& msg);
    int OnAppTaskMsgProc(const ACEX_Message& msg);
    int OnCollectServerMsgProc(const ACEX_Message& msg);
    int OnControllerServerMsgProc(const ACEX_Message& msg);
    int OnDataLoaderMsgProc(const ACEX_Message& msg);
private:
    int OnCollectPacket(const Packet& packet);
	int OnCollectConnect(int clientid, const std::string& ip, unsigned int port);
	int OnCollectDisconnect(int clientid);

    int OnControllerPacket(int clientid, const Packet& packet);
	int OnControllerConnect(int clientid, const std::string& ip, unsigned int port);
	int OnControllerDisconnect(int clientid);
private:
    int InsertStateData(int clientid, int type, const Packet* packet);
    int RemoveStateData(unsigned int state);
    int RemoveStateData(const std::string& qn);
private:
    std::auto_ptr<DataAccess> _objDataAccess;
    std::auto_ptr<DataLoader> _objDataLoader;
    std::auto_ptr<CollectServerTask> _taskCollectServer;
    std::auto_ptr<ControllerServerTask> _taskControllerServer;
private:
    bool _crcCheck;
	TClientMap _mapClient;
    TClientMap _mapController;
private:
    unsigned int _iStateDataCount;
    TStateDataMap _mapStateData;
    TStateIndexMap _mapStateIndex;
};

#endif /* __COREMSGTASK_H__ */
