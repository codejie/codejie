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
}

int CoreMsgTask::Init(const ConfigLoader& config)
{

	return 0;
}

void CoreMsgTask::Final()
{

}

int CoreMsgTask::handle_msg(const ACEX_Message& msg)
{

    if(msg.msg_id() == TASK_TIMER)
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
