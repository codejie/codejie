#include "acex/ACEX.h"

#include "DistributeServerManager.h"

DistributeClient::DistributeClient(DistributeServerManager* manager, int id)
: _objManager(manager)
, _iID(id)
, _bConnected(false)
{
}

DistributeClient::~DistributeClient()
{
}

int DistributeClient::Open(const std::string &addr)
{
	return -1;
}

//
DistributeServerManager::DistributeServerManager(ACEX_Message_Task *msgtask)
: _taskMsg(msgtask)
{
}

DistributeServerManager::~DistributeServerManager()
{
	Release();
}

int DistributeServerManager::Init(const ConfigLoader::TDistributeServerDataVector &data)
{
	for(ConfigLoader::TDistributeServerDataVector::const_iterator it = data.begin(); it != data.end(); ++ it)
	{
		DistributeClient *client = new DistributeClient(this, it->first);
		if(client == NULL)
		{
			ACEX_LOG_OS(LM_ERROR, "<DistributeServerManager::Init>allocate DistributeClient obj failed." << std::endl);
			return -1;
		}
		if(client->Open(it->second) != 0)
		{
			ACEX_LOG_OS(LM_ERROR, "<DistributeServerManager::Init>DistributeClient open failed." << std::endl);
			return -1;
		}
		if(!_mapClient.insert(std::make_pair(it->first,client)).second)
		{
			ACEX_LOG_OS(LM_ERROR, "<DistributeServerManager::Init>Insert DistributeClient data failed." << std::endl);
			return -1;
		}
	}
	return 0;
}

void DistributeServerManager::Release()
{
	for(TClientMap::iterator it = _mapClient.begin(); it != _mapClient.end(); ++ it)
	{
		delete it->second, it->second = NULL;
	}
	_mapClient.clear();
}

int DistributeServerManager::Send(int id, const std::string& stream)
{
	TClientMap::const_iterator it = _mapClient.find(id);
	if(it == _mapClient.end())
		return -1;
	return it->second->Send(stream);
}

