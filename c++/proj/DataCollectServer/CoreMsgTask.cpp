/*
 * CoreMsgTask.cpp
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#include "ace/OS_NS_time.h"
#include "acex/ACEX.h"

#include "Defines.h"
#include "Packet.h"
#include "CommandPacket.h"
#include "ConfigLoader.h"
#include "PacketProcessor.h"
#include "CollectServerTask.h"
#include "CoreMsgTask.h"

CoreMsgTask::CoreMsgTask()
: _objDataAccess(NULL)
, _taskCollectServer(NULL)
, _taskCommandServer(NULL)
{
}

CoreMsgTask::~CoreMsgTask()
{
}

int CoreMsgTask::Init(const ConfigLoader& config)
{
    _objDataAccess.reset(new DataAccess());
    if(_objDataAccess->Init(config.m_strDBServer, config.m_strDBUser, config.m_strDBPasswd) != 0)
    {
        ACEX_LOG_OS(LM_ERROR, "<CoreMsgTask::Init>DataAccesss init failed - " << config.m_strDBUser << ":" << config.m_strDBPasswd << "@" << config.m_strDBServer << std::endl);
        return -1;
    }

	_taskCollectServer.reset(new CollectServerTask(this));
	if(_taskCollectServer->Open(config.m_strCollectAddr) != 0)
	{
		ACEX_LOG_OS(LM_ERROR, "<CoreMsgTask::Init>Collect Server open failed - addr : " << config.m_strCollectAddr << std::endl);
		return -1;
	}

	_taskCommandServer.reset(new CommandServerTask(this));
	if(_taskCommandServer->Open(config.m_strCommandAddr) != 0)
	{
		ACEX_LOG_OS(LM_ERROR, "<CoreMsgTask::Init>Command Server open failed - addr : " << config.m_strCommandAddr << std::endl);
		return -1;
	}

	return 0;
}

void CoreMsgTask::Final()
{
	if(_taskCommandServer.get() != NULL)
	{
		_taskCommandServer->Final();
		_taskCommandServer.reset(NULL);
	}

	if(_taskCollectServer.get() != NULL)
	{
		_taskCollectServer->Final();
		_taskCollectServer.reset(NULL);
	}

    if(_objDataAccess.get() != NULL)
    {
        _objDataAccess->Final();
        _objDataAccess.reset(NULL);
    }
}

int CoreMsgTask::handle_msg(const ACEX_Message& msg)
{
    if(msg.msg_id() == TASK_COLLECT_SERVER)
    {
        return OnCollectServerMsgProc(msg);
    }
    else if(msg.msg_id() == TASK_COMMAND_SERVER)
    {
        return OnCommandServerMsgProc(msg);
    }
    else if(msg.msg_id() == TASK_TIMER)
    {
        return OnTimerMsgProc(msg);
    }
    else if(msg.msg_id() == TASK_APP)
    {
        return OnAppTaskMsgProc(msg);
    }
    else
    {
        ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::handle_msg>Unknown msgid : " << msg.msg_id() << std::endl);
    }
    return 0;
}

int CoreMsgTask::OnAppTaskMsgProc(const ACEX_Message &msg)
{
	if(msg.fparam() == FPARAM_SHUTDOWN)
	{
		ACEX_LOG_OS(LM_INFO, "<CoreMsgTask::OnAppTaskMsgProc>Recv 'shutdown' msg." << std::endl);
        Final();
		return -1;
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnAppTaskMsgProc>Unknwon fparam - " << msg.fparam() << std::endl);
	}
	return 0;
}

int CoreMsgTask::OnTimerMsgProc(const ACEX_Message& msg)
{
	return 0;
}

int CoreMsgTask::OnCollectServerMsgProc(const ACEX_Message& msg)
{
	if(msg.fparam() == FPARAM_PACKET)
	{
		size_t size = (size_t)(msg.sparam() & 0x0000FFFF);
		const char* buf = (const char*)msg.data();

		Packet packet;
		if(PacketProcessor::Analyse(std::string(buf, size), packet) == 0)
		{
			UpdateClientCount(msg.sparam() >> 16);
			OnCollectPacket(packet);
		}
		else
		{
			ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnCollectServerMsgProc>Packet Process failed - stream : " << std::string(buf, size) << std::endl);
		}

		delete [] buf;
	}
	else if(msg.fparam() == FPARAM_SOCKET_CONNECT)
	{
		ACE_INET_Addr* addr =  reinterpret_cast<ACE_INET_Addr*>(msg.data());
		OnCollectConnect(msg.sparam(), addr->get_host_addr(), addr->get_port_number());
		delete addr;
	}
	else if(msg.fparam() == FPARAM_SOCKET_DISCONNECT)
	{
		OnCollectDisconnect(msg.sparam());
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnCollectServerMsgProc>Unknwon fparam - " << msg.fparam() << std::endl);
	}

	return 0;
}

int CoreMsgTask::OnCollectPacket(const Packet& packet)
{
	ACEX_LOG_OS(LM_DEBUG, "<CoreMsgTask::OnCollectPacket>Get Collect Packet - " << packet << std::endl);

    _objDataAccess->OnData(packet);

	return 0;
}

int CoreMsgTask::OnCollectConnect(int clientid, const std::string& ip, unsigned int port)
{
	ClientData_t data;
	data.ip = ip;
	data.port = port;
	data.update = ACE_OS::time(NULL);
	data.count = 0;

	_mapClient.insert(std::make_pair(clientid, data));

	return 0;
}

int CoreMsgTask::OnCollectDisconnect(int clientid)
{
	_mapClient.erase(clientid);

	return 0;
}

int CoreMsgTask::UpdateClientCount(int clientid)
{
	TClientMap::iterator it = _mapClient.find(clientid);
	if(it == _mapClient.end())
		return -1;
	++ it->second.count;

	return 0;
}

void CoreMsgTask::ShowData(bool stat, std::ostream& os) const
{
    if(stat == true)
        _objDataAccess->Show(os);
    else
        _objDataAccess->ShowColumn(os);
}

void CoreMsgTask::ShowPacket(std::ostream &os) const
{
    _taskCollectServer->Show(os);
}

void CoreMsgTask::ShowStationID(std::ostream& os, const std::string& ano) const
{
    _objDataAccess->ShowStationID(os, ano);
}
void CoreMsgTask::ShowInfectantID(std::ostream& os, const std::string& nid) const
{
    _objDataAccess->ShowInfectantID(os, nid);
}

void CoreMsgTask::ShowClient(std::ostream &os) const
{
    os << "--- \nTerminal Client ---";
	for(TClientMap::const_iterator it = _mapClient.begin(); it != _mapClient.end(); ++ it)
	{
		os << "\n Terminal : [" << it->first << "] - " << it->second.count << "\n\t " << it->second.ip << ":" << it->second.port << " - " << it->second.update; 
	}
    os << "--- \nCommand Client ---";

	for(TClientMap::const_iterator it = _mapCommand.begin(); it != _mapCommand.end(); ++ it)
	{
		os << "\n Terminal : [" << it->first << "] - " << it->second.count << "\n\t " << it->second.ip << ":" << it->second.port << " - " << it->second.update; 
	}
	os << std::endl;
}

//

int CoreMsgTask::OnCommandServerMsgProc(const ACEX_Message& msg)
{
	if(msg.fparam() == FPARAM_PACKET)
	{
        std::auto_ptr<CommandPacket> packet(reinterpret_cast<CommandPacket*>(msg.data()));
        OnCommandPacket(*packet);
	}
	else if(msg.fparam() == FPARAM_SOCKET_CONNECT)
	{
		ACE_INET_Addr* addr =  reinterpret_cast<ACE_INET_Addr*>(msg.data());
		OnCommandConnect(msg.sparam(), addr->get_host_addr(), addr->get_port_number());
		delete addr;
	}
	else if(msg.fparam() == FPARAM_SOCKET_DISCONNECT)
	{
		OnCommandDisconnect(msg.sparam());
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnCommandServerMsgProc>Unknwon fparam - " << msg.fparam() << std::endl);
	}

	return 0;
}

int CoreMsgTask::OnCommandPacket(const CommandPacket& packet)
{
	ACEX_LOG_OS(LM_DEBUG, "<CoreMsgTask::OnCommandPacket>Get Command Packet - " << packet << std::endl);

//
	return 0;
}

int CoreMsgTask::OnCommandConnect(int clientid, const std::string& ip, unsigned int port)
{
	ClientData_t data;
	data.ip = ip;
	data.port = port;
	data.update = ACE_OS::time(NULL);
	data.count = 0;

	_mapCommand.insert(std::make_pair(clientid, data));

	return 0;
}

int CoreMsgTask::OnCommandDisconnect(int clientid)
{
	_mapCommand.erase(clientid);

	return 0;
}
