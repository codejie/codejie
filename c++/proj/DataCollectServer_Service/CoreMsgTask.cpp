/*
 * CoreMsgTask.cpp
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#include "ace/OS_NS_time.h"
#include "acex/ACEX.h"

#include "Defines.h"
#include "ConfigLoader.h"
#include "PacketProcessor.h"
#include "CollectServerTask.h"
#include "CoreMsgTask.h"

CoreMsgTask::CoreMsgTask()
: _objDataAccess(NULL)
, _objDataLoader(NULL)
, _taskCollectServer(NULL)
, _taskControllerServer(NULL)
, _iStateDataCount(0)
{
}

CoreMsgTask::~CoreMsgTask()
{
    this->clear_timer();
}

int CoreMsgTask::Init(const ConfigLoader& config)
{
    _objDataAccess.reset(new DataAccess());
    if(_objDataAccess->Init(config.m_strDBServer, config.m_strDBUser, config.m_strDBPasswd) != 0)
    {
        ACEX_LOG_OS(LM_ERROR, "<CoreMsgTask::Init>DataAccesss init failed - " << config.m_strDBUser << ":" << config.m_strDBPasswd << "@" << config.m_strDBServer << std::endl);
        return -1;
    }

    _objDataLoader.reset(new DataLoader(this, _objDataAccess.get()));
    if(_objDataLoader->Init(config.m_iRealtimeInterval, config.m_iPeriodInterval) != 0)
    {
        ACEX_LOG_OS(LM_ERROR, "<CoreMsgTask::Init>DataLoader init failed." << std::endl);
        return -1;
    }

	_taskCollectServer.reset(new CollectServerTask(this));
	if(_taskCollectServer->Open(config.m_strCollectAddr) != 0)
	{
		ACEX_LOG_OS(LM_ERROR, "<CoreMsgTask::Init>Collect Server open failed - addr : " << config.m_strCollectAddr << std::endl);
		return -1;
	}

	_taskControllerServer.reset(new ControllerServerTask(this));
	if(_taskControllerServer->Open(config.m_strControllerAddr) != 0)
	{
		ACEX_LOG_OS(LM_ERROR, "<CoreMsgTask::Init>Controller Server open failed - addr : " << config.m_strControllerAddr << std::endl);
		return -1;
	}

    _crcCheck = config.m_bCheckCRC;

	return 0;
}

void CoreMsgTask::Final()
{
	if(_taskControllerServer.get() != NULL)
	{
		_taskControllerServer->Final();
		_taskControllerServer.reset(NULL);
	}

	if(_taskCollectServer.get() != NULL)
	{
		_taskCollectServer->Final();
		_taskCollectServer.reset(NULL);
	}

    if(_objDataLoader.get() != NULL)
    {
        _objDataLoader->Final();
        _objDataLoader.reset(NULL);
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
    else if(msg.msg_id() == TASK_CONTROLLER_SERVER)
    {
        return OnControllerServerMsgProc(msg);
    }
    else if(msg.msg_id() == OBJ_DATALOADER)
    {
        return OnDataLoaderMsgProc(msg);
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
    if(msg.fparam() == FPARAM_STATEDATA)
    {
        RemoveStateData(msg.sparam());
    }

	return 0;
}

int CoreMsgTask::OnCollectServerMsgProc(const ACEX_Message& msg)
{
	if(msg.fparam() == FPARAM_PACKET)
	{
		size_t size = (size_t)(msg.sparam() & 0x0000FFFF);
		const char* buf = (const char*)msg.data();
		const std::string stream = std::string(buf, size);

		Packet packet;
		if(PacketProcessor::Analyse(stream, packet, _crcCheck) == 0)
		{
			UpdateClientCount(CLIENTTYPE_TERMINAL, msg.sparam() >> 16);
            OnCollectPacket((msg.sparam() >> 16), packet, stream);
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

int CoreMsgTask::OnCollectPacket(int clientid, const Packet& packet, const std::string& stream)
{
	ACEX_LOG_OS(LM_DEBUG, "<CoreMsgTask::OnCollectPacket>Get Collect Packet - " << packet << std::endl);
//Distribute
	DataAccess::TStationDistributeIDVector vct;
	if(_objDataAccess->SearchStationDistributeData(packet.MN, vct) == 0)
	{
		_objDistributeServerManager->OnStream(stream, vct);
	}

//Store
    _objDataAccess->OnPacket(packet);

	return 0;
}

int CoreMsgTask::OnCollectConnect(int clientid, const std::string& ip, unsigned int port)
{
	ClientData_t data;
	data.ip = ip;
	data.port = port;
	data.update = ACE_OS::time(NULL);
	data.count = 0;

	_mapClient.insert(std::make_pair(std::make_pair(CLIENTTYPE_TERMINAL, clientid), data));

	return 0;
}

int CoreMsgTask::OnCollectDisconnect(int clientid)
{
	_mapClient.erase(std::make_pair(CLIENTTYPE_TERMINAL, clientid));

	return 0;
}
///
int CoreMsgTask::UpdateClientCount(int clienttype, int clientid)
{
	TClientMap::iterator it = _mapClient.find(std::make_pair(clienttype, clientid));
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

void CoreMsgTask::ShowPacket(int clienttype, std::ostream &os) const
{
	if(clienttype == CLIENTTYPE_TERMINAL)
		_taskCollectServer->Show(os);
	else
		_taskControllerServer->Show(os);
}

void CoreMsgTask::ShowStationID(std::ostream& os, const std::string& ano) const
{
    _objDataAccess->ShowStationID(os, ano);
}
void CoreMsgTask::ShowInfectantID(std::ostream& os, const std::string& nid) const
{
    _objDataAccess->ShowInfectantID(os, nid);
}

void CoreMsgTask::ShowClient(int clienttype, std::ostream &os) const
{
	for(TClientMap::const_iterator it = _mapClient.begin(); it != _mapClient.end(); ++ it)
	{
		if(it->first.first != clienttype)
			continue;
		os << "\n Terminal : [" << it->first.first << ":" << it->first.second << "] - " << it->second.count << "\n\t " << it->second.ip << ":" << it->second.port << " - " << it->second.update; 
	}
	os << std::endl;
}

void CoreMsgTask::ShowStateData(std::ostream& os) const
{
	os << "\n--- State Data ---";

	os << "\nState Size = " << _mapStateData.size();
	os << "\nIndex Size = " << _mapStateIndex.size();

	os << std::endl;
}

//

int CoreMsgTask::OnControllerServerMsgProc(const ACEX_Message& msg)
{
	if(msg.fparam() == FPARAM_PACKET)
	{
		size_t size = (size_t)(msg.sparam() & 0x0000FFFF);
		const char* buf = (const char*)msg.data();

		Packet packet;
		if(PacketProcessor::Analyse(std::string(buf, size), packet, _crcCheck) == 0)
		{
			UpdateClientCount(CLIENTTYPE_CONTROLLER, msg.sparam() >> 16);
			OnControllerPacket((msg.sparam() >> 16), packet);
		}
		else
		{
			ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnControllerServerMsgProc>Packet Process failed - stream : " << std::string(buf, size) << std::endl);
		}

		delete [] buf;
	}
	else if(msg.fparam() == FPARAM_SOCKET_CONNECT)
	{
		ACE_INET_Addr* addr =  reinterpret_cast<ACE_INET_Addr*>(msg.data());
		OnControllerConnect(msg.sparam(), addr->get_host_addr(), addr->get_port_number());
		delete addr;
	}
	else if(msg.fparam() == FPARAM_SOCKET_DISCONNECT)
	{
		OnControllerDisconnect(msg.sparam());
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnControllerServerMsgProc>Unknwon fparam - " << msg.fparam() << std::endl);
	}

	return 0;
}

int CoreMsgTask::OnControllerPacket(int clientid, const Packet& packet)
{
	ACEX_LOG_OS(LM_DEBUG, "<CoreMsgTask::OnControllerPacket>Get Controller Packet - " << packet << std::endl);

    Packet::TCPDataMap::const_iterator it = packet.CP.data.find(Packet::PD_TAG_QN);
    if(it != packet.CP.data.end())
    {
        RemoveStateData(it->second);
    }

    _objDataLoader->OnPacket(clientid, packet);

	return 0;
}

int CoreMsgTask::OnControllerConnect(int clientid, const std::string& ip, unsigned int port)
{
	ClientData_t data;
	data.ip = ip;
	data.port = port;
	data.update = ACE_OS::time(NULL);
	data.count = 0;

    _objDataLoader->OnControllerConnected(clientid);

	_mapController.insert(std::make_pair(std::make_pair(CLIENTTYPE_CONTROLLER, clientid), data));

	return 0;
}

int CoreMsgTask::OnControllerDisconnect(int clientid)
{
    _objDataLoader->OnControllerDisconnect(clientid);

	_mapController.erase(std::make_pair(CLIENTTYPE_CONTROLLER, clientid));

	return 0;
}

///
int CoreMsgTask::InsertStateData(int clientid, int type, const Packet* packet)
{
    StateData_t data;
    data.clientid = clientid;
    data.type = type;
    data.packet = packet;

    ACEX_Message msg(TASK_TIMER, FPARAM_STATEDATA, (++ _iStateDataCount)); 
    data.timer = this->regist_timer(msg, ACE_Time_Value(30));

    _mapStateIndex.insert(std::make_pair(packet->QN, _iStateDataCount));
    _mapStateData.insert(std::make_pair(_iStateDataCount, data));

    return 0;
}

int CoreMsgTask::RemoveStateData(const std::string& qn)
{
    TStateIndexMap::iterator it = _mapStateIndex.find(qn);
    if(it == _mapStateIndex.end())
        return -1;
    
    TStateDataMap::iterator i = _mapStateData.find(it->second);
    if(i == _mapStateData.end())
        return -1;

    this->remove_timer(i->second.timer);
    delete i->second.packet;

    _mapStateData.erase(i);

    _mapStateIndex.erase(it);

    return 0;
}

int CoreMsgTask::RemoveStateData(unsigned int state)
{
    TStateDataMap::iterator it = _mapStateData.find(state);
    if(it == _mapStateData.end())
        return -1;

//    this->remove_timer(it->second.timer);

    TStateIndexMap::iterator i = _mapStateIndex.find(it->second.packet->QN);
    if(i == _mapStateIndex.end())
        return -1;

    _mapStateIndex.erase(i);

    _objDataLoader->OnPacketTimeout(it->second.clientid, *it->second.packet);

    delete it->second.packet;

    _mapStateData.erase(it);

    return 0;
}

///
int CoreMsgTask::OnDataLoaderMsgProc(const ACEX_Message &msg)
{
    if(msg.fparam() == FPARAM_DATALOADER_RTTIMER)
    {
        _objDataLoader->OnTimer(msg.sparam(), DataLoader::TT_REALTIME);
    }
    else if(msg.fparam() == FPARAM_DATALOADER_PDTIMER)
    {
        _objDataLoader->OnTimer(msg.sparam(), DataLoader::TT_PERIOD);
    }
    else if(msg.fparam() == FPARAM_PACKET)
    {
        std::auto_ptr<Packet> packet(reinterpret_cast<Packet*>(msg.data()));
        if(packet.get() == NULL)
            return -1;

        std::string stream;
        if(PacketProcessor::Make(stream, *packet) != 0)
            return -1;

        if(_taskControllerServer->Send(msg.sparam(), stream) != 0)
            return -1;
        
        if(packet->CN == Packet::PD_CN_VALVECONTROL || packet->CN == Packet::PD_CN_ICFEEADD || packet->CN == Packet::PD_CN_VALVEREALDATA)
        {
            return InsertStateData(msg.sparam(), msg.fparam(), packet.release());
        }
        else
        {
            return 0;
        }
    }
    return 0;
}
