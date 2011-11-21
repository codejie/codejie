#include "acex/ACEX.h"
#include "acex/Exception.h"

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
    ACE_INET_Addr a(addr.c_str());
    if(open(a) != 0)
	    return -1;
    return -1;
}

int DistributeClient::Send(const std::string &stream)
{
    if(send(stream.c_str(), stream.size()) != ACEX_NB_Tcp_Client_Task::SEND_SUCCESS)
        return -1;
    return 0;
}

int DistributeClient::handle_connecting()
{
    _objManager->OnConnecting(_iID);
    return 0;
}

int DistributeClient::handle_connect(ACEX_TcpStream &server)
{
    _bConnected = true;
    _objManager->OnConnected(_iID, server);
    return 0;
}

int DistributeClient::handle_close(ACEX_TcpStream& server)
{
    _bConnected = false;
    _objManager->OnDistconnect(_iID, server);
    return 0;
}



//
DistributeServerManager::DistributeServerManager(ACEX_Message_Task *msgtask)
: _taskMsg(msgtask)
{
}

DistributeServerManager::~DistributeServerManager()
{
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
        try
        {
            client->activate();
        }
        catch (const ACEX_Runtime_Exception& e)
        {
            ACEX_LOG_OS(LM_ERROR, "<DistributeServerManager::Init>DestributeClient activate failed." << std::endl);
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

void DistributeServerManager::Final()
{
	for(TClientMap::iterator it = _mapClient.begin(); it != _mapClient.end(); ++ it)
	{
        it->second->deactivate();
		delete it->second, it->second = NULL;
	}
	_mapClient.clear();
}

int DistributeServerManager::Send(int id, const std::string& stream)
{
	TClientMap::const_iterator it = _mapClient.find(id);
	if(it == _mapClient.end())
    {
                ACEX_LOG_OS(LM_WARNING, "<DistributeServerManager::Send>Cannot find Distribute client connection of " << id << std::endl);
		return -1;
    }
    if(!it->second->IsConnected())
    {
        ACEX_LOG_OS(LM_WARNING, "<DistributeServerManager::Send>Distribute client " << id << " is not connected." << std::endl);
        return -1;
    }
	return it->second->Send(stream);
}

void DistributeServerManager::OnConnecting(int id)
{
    ACEX_LOG_OS(LM_INFO, "<DistributeServerManager::OnConnecting>client " << id  << " is connecting." << std::endl);
}

void DistributeServerManager::OnConnected(int id, ACEX_TcpStream &server)
{
    ACE_INET_Addr addr;
    server.get_remote_addr(addr);
    ACEX_LOG_OS(LM_INFO, "<DistributeServerManager::OnConnecting>client " << id << " is connected - " << addr.get_host_addr() << ":" << addr.get_port_number() << std::endl);
}

void DistributeServerManager::OnDistconnect(int id, ACEX_TcpStream &server)
{
    ACE_INET_Addr addr;
    server.get_remote_addr(addr);
    ACEX_LOG_OS(LM_INFO, "<DistributeServerManager::OnConnecting>client " << id << " is disconnected - " << addr.get_host_addr() << ":" << addr.get_port_number() << std::endl);
}

int DistributeServerManager::OnStream(const std::string &stream, const DataAccess::TStationDistributeIDVector& vct)
{
    for(DataAccess::TStationDistributeIDVector::const_iterator it = vct.begin(); it != vct.end(); ++ it)
    {
        ACEX_LOG_OS(LM_DEBUG, "<DistributeServerManager::OnStream>Distribute stream to server id:" << *it << "\nStream:" << stream << std::endl);
        if(Send(*it, stream) != 0)
        {
            ACEX_LOG_OS(LM_WARNING, "<DistributeServerManager::OnStream>Send stream failed." << std::endl);
        }
    }
    return 0;
}
