
#include "Defines.h"
#include "Toolkit.h"
#include "DataAccess.h"
#include "DataLoader.h"


DataLoader::DataLoader(ACEX_Message_Task *msgtask, DataAccess *access)
: _taskMsg(msgtask)
, _dataAccess(access)
{
}

DataLoader::~DataLoader()
{
}

int DataLoader::Init(int realtime, int period)
{
    _realtime = realtime;
    _period = period;

    return 0;
}


void DataLoader::Final()
{
	if(_taskMsg != NULL)
	{
		_taskMsg->clear_timer();
	}
}

int DataLoader::OnControllerConnected(int clientid)
{
    TClientDataMap::const_iterator it = _mapClientData.find(clientid);
    if(it != _mapClientData.end())
        return -1;

    ClientData data;
    data.rtTimer = RegTimer(TT_REALTIME, clientid);
    data.pdTimer = RegTimer(TT_PERIOD, clientid);

    _mapClientData.insert(std::make_pair(clientid, data));

    return 0;
}

int DataLoader::OnControllerDisconnect(int clientid)
{
    TClientDataMap::const_iterator it = _mapClientData.find(clientid);
    if(it == _mapClientData.end())
        return -1;

    UnregTimer(it->second.rtTimer);
    UnregTimer(it->second.pdTimer);

    _mapClientData.erase(it);

    return 0;
}

int DataLoader::RegTimer(DataLoader::TimerType type, int clientid)
{
    ACEX_Message msg(OBJ_DATALOADER, (type == TT_REALTIME ? FPARAM_DATALOADER_RTTIMER : FPARAM_DATALOADER_PDTIMER), clientid);

    int interval = (type == TT_REALTIME ? _realtime : _period);
    return _taskMsg->regist_timer(msg, ACE_Time_Value(interval), ACE_Time_Value(interval));
}

void DataLoader::UnregTimer(int timer)
{
    if(timer != -1)
        _taskMsg->remove_timer(timer);
}

int DataLoader::PutMsg(int clientid, int type, const Packet* packet)
{
    ACEX_Message msg(OBJ_DATALOADER, type, clientid, (void*)packet);
    _taskMsg->put_msg(msg);

    return 0;
}

int DataLoader::OnTimer(int clientid, DataLoader::TimerType type)
{
    Packet* packet = NULL;
    if(type == TT_REALTIME)
    {
        if(LoadValveControlData(clientid, packet) == 0)
        {
            PutMsg(clientid, FPARAM_DATA_VALVECONTROL, packet); 
        }
        if(LoadFeeAddData(clientid, packet) == 0)
        {
            PutMsg(clientid, FPARAM_DATA_FEEADD, packet);
        }
    }
    else
    {
        if(LoadRealData(clientid, packet) == 0)
        {
            PutMsg(clientid, FPARAM_DATA_REAL, packet);
        }
    }
    return 0;
}

int DataLoader::LoadValveControlData(int clientid, Packet*& packet)
{
	packet = new Packet();

    packet->QN = Toolkit::GetTimeOfDay();
    packet->ST = "91";
    packet->CN = Packet::PD_CN_VALVECONTROL;
    packet->PW = Packet::VALUE_DEFAULT_PW;
    packet->MN = Packet::VALUE_DEFAULT_MN;
    packet->Flag = "1";

	if(_dataAccess->GetValveControlData("", *packet) != 0)
	{
		delete packet, packet = NULL;
		return -1;
	}
	
	return 0;
}

int DataLoader::LoadFeeAddData(int clientid, Packet*& packet)
{
	packet = new Packet();

    packet->QN = Toolkit::GetTimeOfDay();
    packet->ST = "91";
	packet->CN = Packet::PD_CN_FEEADD;
    packet->PW = Packet::VALUE_DEFAULT_PW;
    packet->MN = Packet::VALUE_DEFAULT_MN;
    packet->Flag = "1";

	if(_dataAccess->GetValveControlData("", *packet) != 0)
	{
		delete packet, packet = NULL;
		return -1;
	}
	
	return 0;
}

int DataLoader::LoadRealData(int clientid, Packet*& packet)
{
	packet = new Packet();

    packet->QN = Toolkit::GetTimeOfDay();
    packet->ST = "91";
    packet->CN = Packet::PD_CN_REALDATA;
    packet->PW = Packet::VALUE_DEFAULT_PW;
    packet->MN = Packet::VALUE_DEFAULT_MN;
    packet->Flag = "1";

	if(_dataAccess->GetValveControlData("", *packet) != 0)
	{
		delete packet, packet = NULL;
		return -1;
	}
	
	return 0;
}
