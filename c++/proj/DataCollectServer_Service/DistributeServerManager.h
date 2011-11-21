#ifndef __DISTRIBUTESERVERMANAGER_H__
#define __DISTRIBUTESERVERMANAGER_H__

#include <map>
#include <string>

#include "acex/Task.h"
#include "acex/NB_Tcp_Client.h"

#include "ConfigLoader.h"
#include "DataAccess.h"

class DistributeServerManager;

class DistributeClient : public ACEX_NB_Tcp_Client_Task
{
public:
	DistributeClient(DistributeServerManager* manager, int id);
	virtual ~DistributeClient();

	int Open(const std::string& addr);
	int Send(const std::string& stream);

    bool IsConnected() const { return _bConnected; }
protected:
	virtual int handle_connecting();
	virtual int handle_connect(ACEX_TcpStream& server);
	virtual int handle_close(ACEX_TcpStream& server);

protected:
	int _iID;
	bool _bConnected;
private:
	DistributeServerManager* _objManager;
};

///

class DistributeServerManager
{
protected:
	typedef std::map<int, DistributeClient*> TClientMap;
public:
	DistributeServerManager(ACEX_Message_Task* msgtask);
	virtual ~DistributeServerManager();

	int Init(const ConfigLoader::TDistributeServerDataVector& data);
	void Final();

    int OnStream(const std::string& stream, const DataAccess::TStationDistributeIDVector& vct);
public:
	void OnConnecting(int id);
	void OnConnected(int id, ACEX_TcpStream& server);
	void OnDistconnect(int id, ACEX_TcpStream& server);
protected:
    int Send(int id, const std::string& stream);
private:
	TClientMap _mapClient;
private:
	ACEX_Message_Task* _taskMsg;
};


#endif
