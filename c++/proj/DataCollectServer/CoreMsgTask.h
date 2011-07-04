/*
 * CoreMsgTask.h
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#ifndef __COREMSGTASK_H__
#define __COREMSGTASK_H__

#include "acex/Task.h"

#include "DataAccess.h"
#include "CollectServerTask.h"

class Packet;
class ConfigLoader;

class CoreMsgTask: public ACEX_Message_Task
{
public:
	CoreMsgTask();
	virtual ~CoreMsgTask();

	int Init(const ConfigLoader& config);

    void ShowData(bool stat, std::ostream& os) const;
    void ShowPacket(std::ostream& os) const;
protected:
	void Final();

protected:
	virtual int handle_msg(const ACEX_Message& msg);
    int OnTimerMsgProc(const ACEX_Message& msg);
    int OnAppTaskMsgProc(const ACEX_Message& msg);
    int OnCollectServerMsgProc(const ACEX_Message& msg);
private:
    int OnCollectPacket(const Packet& packet);
private:
    std::auto_ptr<DataAccess> _objDataAccess;
    std::auto_ptr<CollectServerTask> _taskCollectServer;
};

#endif /* __COREMSGTASK_H__ */
