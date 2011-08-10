/*
 * CoreMsgTask.cpp
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"

#include "Defines.h"
#include "ConfigLoader.h"
#include "Packet.h"
#include "ConnectionServer.h"
#include "DataAccess.h"
#include "Toolkit.h"
#include "CoreMsgTask.h"

CoreMsgTask::CoreMsgTask()
: _iReqInterval(0)
, _iPacketTimeout(0)
, _ptrConnServer(NULL)
, _ptrDataAccess(NULL)
{
}

CoreMsgTask::~CoreMsgTask()
{
	Final();
}

int CoreMsgTask::Init(const ConfigLoader& config)
{
	_ptrDataAccess.reset(new DataAccess());
	if(_ptrDataAccess.get() == NULL)
		return -1;

	if(_ptrDataAccess->Init(config.m_strDBServer, config.m_strDBDatabase, config.m_strDBUser, config.m_strDBPasswd) != 0)
		return -1;

	_ptrConnServer.reset(new ConnectionServer(this));
	if(_ptrConnServer.get() == NULL)
		return -1;

	if(_ptrConnServer->Open(config.m_strConnectionAddr.c_str()) != 0)
		return -1;

	_iReqInterval = config.m_iScanInterval;
	_iPacketTimeout = 30;

	return 0;
}

void CoreMsgTask::Final()
{
	if(_ptrConnServer.get() != NULL)
	{
		_ptrConnServer->Final();
	}

	if(_ptrDataAccess.get() != NULL)
		_ptrDataAccess->Final();

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
	switch(packet._type)
	{
	case Packet::PACKET_FLAG_HELLO:
		OnServerHelloPacket(clientid, packet);
		break;
	case Packet::PACKET_FLAG_DATARESP:
		OnServerDataRespPacket(clientid, packet);
		break;
	default:
		ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnServerPacket>Unknwon packet type - " << packet._type << std::endl);	
	}
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
	data.data = 0;
	data.hello = 0;
	data.timer = RegTimer(clientid, FPARAM_SOCKET_TIMEOUT, _iPacketTimeout);

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

///
int CoreMsgTask::OnServerHelloPacket(int clientid, const Packet& packet)
{
	TDTUMap::iterator it = _mapDTU.find(clientid);
	if(it == _mapDTU.end())
		return -1;

	const HelloPacket& hello = reinterpret_cast<const HelloPacket&>(packet);

	UnregTimer(it->second.timer);

	if(it->second.status == DS_WAITTEL)
	{
		it->second.tel = hello._tele;
		if(_ptrDataAccess->SearchStation(it->second.tel, it->second.stationid) != 0)
		{
			ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnServerHelloPacket>Find station id failed - tel : " << it->second.tel << std::endl);
			return -1;
		}
		it->second.status = DS_DATAREQ;

		it->second.timer = RegTimer(clientid, FPARAM_PACKET, _iReqInterval);
	}

	++ it->second.hello;
	it->second.update = ACE_OS::time(NULL);

	return 0;
}

int CoreMsgTask::OnServerDataRespPacket(int clientid, const Packet &packet)
{
	TDTUMap::iterator it = _mapDTU.find(clientid);
	if(it == _mapDTU.end())
		return -1;

	const DataRespPacket& resp = reinterpret_cast<const DataRespPacket&>(packet);

	UnregTimer(it->second.timer);

	if(it->second.status == DS_WAITTEL)
	{
		ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnServerDataRespPacket>Client does not connect yet - clientid : " << clientid << std::endl);
		return -1;
	}

	if(_ptrDataAccess->OnData(it->second.stationid, Toolkit::Buffer2Float(resp._value)) != 0)
	{
		ACEX_LOG_OS(LM_WARNING, "<>Clienet insert data failed - clientid : " << clientid << "\nPacket : " << packet << std::endl);
		return -1;
	}
	
	++ it->second.data;
	it->second.update = ACE_OS::time(NULL);
	it->second.status = DS_DATAREQ;
	it->second.timer = RegTimer(clientid, FPARAM_PACKET, _iReqInterval);

	return 0;
}

///
int CoreMsgTask::OnTimerSocketTimeout(int clientid)
{
	ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnTimerSocketTimeout>Client wait hello data timeout - clientid : " << clientid << std::endl);
	//shutdown client
	return 0;
}

int CoreMsgTask::OnTimerPacket(int clientid)
{
	TDTUMap::iterator it = _mapDTU.find(clientid);
	if(it == _mapDTU.end())
		return -1;

	if(SendDataReqPacket(clientid) != 0)
	{
		//shutdown client
		return -1;
	}

	return 0;
}

int CoreMsgTask::OnTimerPacketTimeout(int clientid)
{
	ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnTimerPacketTimeout>Client recv data timeout - clientid : " << clientid << std::endl);
	//shutdown client
	return 0;
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