
#ifndef __COMMANDSERVERTASK_H__
#define __COMMANDSERVERTASK_H__

#include <iostream>

#include "acex/NB_Tcp_Server.h"
#include "acex/Task.h"

class CommandServerTask: public ACEX_NB_Tcp_Server_Task
{
public:
	CommandServerTask(ACEX_Message_Task* msgtask);
	virtual ~CommandServerTask();

	int Open(const std::string& local);

	void Final();

    void Show(std::ostream& os) const;
protected:
	virtual int handle_connect(int clientid, ACEX_TcpStream& client);
	virtual int handle_close(int clientid, ACEX_TcpStream& client);
	virtual int handle_recv(int clientid, ACEX_TcpStream& client);
	virtual int handle_lost(int error, int clientid, const char* packet, size_t packet_size);
private:
    ACEX_Message_Task* _taskMsg;
private:
    size_t _count;
};

#endif

