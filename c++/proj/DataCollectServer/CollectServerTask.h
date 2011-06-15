/*
 * CollectServerTask.h
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#ifndef __COLLECTSERVERTASK_H__
#define __COLLECTSERVERTASK_H__

#include "acex/NB_Tcp_Server.h"
#include "acex/Task.h"

class CollectServerTask: public ACEX_NB_Tcp_Server_Task
{
public:
	CollectServerTask(ACEX_Message_Task& msgtask);
	virtual ~CollectServerTask();

	int Open(const std::string& local);

protected:
	virtual int handle_connect(int clientid, ACEX_TcpStream& client);
	virtual int handle_close(int clientid, ACEX_TcpStream& client);
	virtual int handle_recv(int clientid, ACEX_TcpStream& client);
	virtual int handle_lost(int error, int clientid, const char* packet, size_t packet_size);
private:
    ACEX_Message_Task& _taskMsg;
};

#endif /* __COLLECTSERVERTASK_H__ */
