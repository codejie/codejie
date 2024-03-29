
#include "acex/ACEX.h"

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
            PutMsg(clientid, FPARAM_PACKET, packet); 
        }
        if(LoadICFeeAddData(clientid, packet) == 0)
        {
            PutMsg(clientid, FPARAM_PACKET, packet);
        }
    }
    else
    {
        if(LoadValveRealData(clientid, packet) == 0)
        {
            PutMsg(clientid, FPARAM_PACKET, packet);
        }
    }
    return 0;
}

int DataLoader::LoadValveControlData(int clientid, Packet*& packet)
{
	packet = new Packet();

    packet->QN = Toolkit::GetTimeOfDay();
    packet->ST = Packet::PD_ST_91;
    packet->CN = Packet::PD_CN_VALVECONTROL;
    packet->PW = Packet::VALUE_DEFAULT_PW;
    packet->MN = Packet::VALUE_DEFAULT_MN;
    packet->Flag = "1";

	if(_dataAccess->GetValveControlData(*packet) != 0)
	{
		delete packet, packet = NULL;
		return -1;
	}
	
	return 0;
}

int DataLoader::LoadICFeeAddData(int clientid, Packet*& packet)
{
	packet = new Packet();

    packet->QN = Toolkit::GetTimeOfDay();
    packet->ST = "91";
	packet->CN = Packet::PD_CN_ICFEEADD;
    packet->PW = Packet::VALUE_DEFAULT_PW;
    packet->MN = Packet::VALUE_DEFAULT_MN;
    packet->Flag = "1";

    if(_dataAccess->GetICFeeAddData(*packet) != 0)
	{
		delete packet, packet = NULL;
		return -1;
	}
	
	return 0;
}

int DataLoader::LoadValveRealData(int clientid, Packet*& packet)
{
	packet = new Packet();

    packet->QN = Toolkit::GetTimeOfDay();
    packet->ST = "91";
    packet->CN = Packet::PD_CN_VALVEREALDATA;
    packet->PW = Packet::VALUE_DEFAULT_PW;
    packet->MN = Packet::VALUE_DEFAULT_MN;
    packet->Flag = "1";

    if(_dataAccess->GetValveRealData(*packet) != 0)
	{
		delete packet, packet = NULL;
		return -1;
	}
	
	return 0;
}


int DataLoader::OnPacket(int clientid, const Packet &packet)
{
	try
	{
		if(packet.ST == Packet::PD_ST_91)
		{
/*			if(packet.CN == Packet::PD_CN_VALVECONTROL || packet.CN == Packet::PD_CN_ICFEEADD || packet.CN == Packet::PD_CN_VALVEREALDATA)
			{
				return _dataAccess->OnPacket(packet);
			}
			else*/ 
			if(packet.CN == Packet::PD_CN_ICFEEUPLOAD)
			{
				int ret = _dataAccess->SetICFeeUploadData(packet);
				return OnICFeeUpload(clientid, packet, ret);
			}
			else if(packet.CN == Packet::PD_CN_VALVEUPLOAD)
			{
				int ret = _dataAccess->SetValveUploadData(packet);
				return OnValveUpload(clientid, packet, ret);
			}
			else if(packet.CN == Packet::PD_CN_VALVECONTROL)
			{
				return _dataAccess->UpdateValveControlFlag(packet);
			}
			else if(packet.CN == Packet::PD_CN_ICFEEADD)
			{
				return _dataAccess->UpdateICFeeAddFlag(packet);
			}
			else if(packet.CN == Packet::PD_CN_VALVEREALDATA)
			{
				return _dataAccess->UpdateValveRealDataFlag(packet);
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}
    catch(const PacketException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataLoader::OnPacket>Get ExeRtn - " << e << "\n" << packet << std::endl);
        
        return -1;
    }
    return 0;
}

int DataLoader::OnICFeeUpload(int clientid, const Packet &packet, int ret)
{
    Packet* resp = new Packet();

    resp->ST = "91";
    resp->CN = Packet::PD_CN_ICFEEUPLOAD;
    resp->PW = packet.PW;
    resp->MN = packet.MN;

    resp->CP.data.insert(std::make_pair(Packet::PD_TAG_QN, packet.QN));
    resp->CP.data.insert(std::make_pair(Packet::PD_CP_TAG_EXERTN, (ret == 0 ? "1" : "0")));

    PutMsg(clientid, FPARAM_PACKET, resp);

    return 0;
}

int DataLoader::OnValveUpload(int clientid, const Packet &packet, int ret)
{
    Packet* resp = new Packet();

    resp->ST = "91";
    resp->CN = Packet::PD_CN_VALVEUPLOAD;
    resp->PW = packet.PW;
    resp->MN = packet.MN;

    resp->CP.data.insert(std::make_pair(Packet::PD_TAG_QN, packet.QN));
    resp->CP.data.insert(std::make_pair(Packet::PD_CP_TAG_EXERTN, (ret == 0 ? "1" : "0")));

    PutMsg(clientid, FPARAM_PACKET, resp);

    return 0;
}

int DataLoader::OnPacketTimeout(int clientid, const Packet &packet)
{
    ACEX_LOG_OS(LM_WARNING, "<>Packet resp timeout - clientid : " << clientid << "\n" << packet << std::endl);
    return 0;
}
