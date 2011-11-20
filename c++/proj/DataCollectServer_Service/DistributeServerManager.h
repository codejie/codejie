#ifndef __DISTRIBUTESERVERMANAGER_H__
#define __DISTRIBUTESERVERMANAGER_H__

#include <map>
#include <string>

#include "acex/Task.h"

#include "ConfigLoader.h"

class DistributeServerManager;

class DistributeClient
{
public:
	DistributeClient(DistributeServerManager* manager, int id);
	virtual ~DistributeClient();

	int Open(const std::string& addr);
	int Send(const std::string& stream);
public:
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

	int Send(int id, const std::string& stream);
public:
	void OnConnecting(int id);
	void OnConnected(int id);
	void OnDistconnect(int id);
protected:
	void Release();
private:
	TClientMap _mapClient;
private:
	ACEX_Message_Task* _taskMsg;
};


#endif
