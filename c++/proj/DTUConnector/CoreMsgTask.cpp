/*
 * CoreMsgTask.cpp
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"

#include "Defines.h"
#include "ConfigLoader.h"
#include "CoreMsgTask.h"

CoreMsgTask::CoreMsgTask()
{
}

CoreMsgTask::~CoreMsgTask()
{
	Final();
}

int CoreMsgTask::Init(const ConfigLoader& config)
{

	return 0;
}

void CoreMsgTask::Final()
{
	this->clear_timer();
}

int CoreMsgTask::handle_msg(const ACEX_Message& msg)
{
	if(msg.msg_id() == TASK_SERVER)
	{
		return OnServerMsgProc(msg);
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
	if(msg.fparam() == FPARAM_PACKET)
	{
		OnTimerPacket(msg.sparam());
	}
	else if(msg.fparam() == FPARAM_PACKET_TIMEOUT)
	{
		OnTimerPacketTimeout(msg.sparam());
	}
	else if(msg.fparam() == FPARAM_SOCKET_TIMEOUT)
	{
		OnTimerSocketTimeout(msg.sparam());
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnTimerMsgProc>Unknwon fparam - " << msg.fparam() << std::endl);
	}

	return 0;
}

int CoreMsgTask::OnServerMsgProc(const ACEX_Message &msg)
{
	if(msg.fparam() == FPARAM_PACKET)
	{
		std::auto_ptr<const Packet> packet(reinterpret_cast<const Packet*>(msg.data()));
		OnServerPacket(msg.sparam(), *packet);
	}
	else if(msg.fparam() == FPARAM_SOCKET_CONNECT)
	{
		std::auto_ptr<const ACE_INET_Addr> addr(reinterpret_cast<const ACE_INET_Addr*>(msg.data()));
		return OnServerSocketConnect(msg.sparam(), addr->get_host_name(), addr->get_port_number());
	}
	else if(msg.fparam() == FPARAM_SOCKET_DISCONNECT)
	{
		return OnServerSocketDisconnect(msg.sparam());
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnServerMsgProc>Unknwon fparam - " << msg.fparam() << std::endl);
	}
	return 0;
}

int CoreMsgTask::OnServerPacket(int clientid, const Packet &packet)
{
	return -1;
}

int CoreMsgTask::OnServerSocketConnect(int clientid, const std::string &ip, unsigned int port)
{
	TDTUMap::const_iterator it = _mapDTU.find(clientid);
	if(it != _mapDTU.end())
	{
		ACEX_LOG_OS(LM_WARNING, "<>Socket " << clientid << " always connects." << std::endl);
		return -1;
	}

	DTUData data;
	data.ip = ip;
	data.port = port;
	data.status = DS_WAITTEL;
	data.update = ACE_OS::time(NULL);
	data.count = 0;
	data.timer = RegTimer(clientid, FPARAM_SOCKET_TIMEOUT, 30);

	return _mapDTU.insert(std::make_pair(clientid, data)).second ? 0 : -1;
}

int CoreMsgTask::OnServerSocketDisconnect(int clientid)
{
	TDTUMap::iterator it = _mapDTU.find(clientid);
	if(it != _mapDTU.end())
	{
		UnregTimer(it->second.timer);
		_mapDTU.erase(it);
	}
	return 0;
}

int CoreMsgTask::OnServerPacket(int clientid, const Packet& packet)
{
}

//
int CoreMsgTask::RegTimer(int clientid, int type, unsigned int timeout)
{
	ACEX_Message msg(TASK_TIMER, FPARAM_SOCKET_TIMEOUT, 0);
	return this->regist_timer(msg, ACE_Time_Value(timeout));
}

void CoreMsgTask::UnregTimer(int timerid)
{
	if(timerid != -1)
		this->remove_timer(timerid);
}