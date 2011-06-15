/*
 * CoreMsgTask.cpp
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"

#include "Defines.h"
#include "Packet.h"
#include "ConfigLoader.h"
#include "PacketProcessor.h"
#include "CoreMsgTask.h"

CoreMsgTask::CoreMsgTask()
: _taskCollectServer(NULL)
{
}

CoreMsgTask::~CoreMsgTask()
{
}

int CoreMsgTask::Init(const ConfigLoader& config)
{
	return -1;
}

void CoreMsgTask::Final()
{

}

int CoreMsgTask::handle_msg(const ACEX_Message& msg)
{
    if(msg.msg_id() == TASK_COLLECT_SERVER)
    {
        return OnCollectServerMsgProc(msg);
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
		size_t size = (size_t)msg.sparam();
		const char* buf = (const char*)msg.data();

		Packet packet;
		if(PacketProcessor::Analyse(std::string(buf, size), packet) == 0)
		{
			OnCollectPacket(packet);
		}
		else
		{
			ACEX_LOG_OS(LM_WARNING, "<CoreMsgTask::OnCollectServerMsgProc>Packet Process failed - stream : " << std::string(buf, size) << std::endl);
		}

		delete [] buf;
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
	return 0;
}
