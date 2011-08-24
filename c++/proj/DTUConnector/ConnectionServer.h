#ifndef __CONNECTIONSERVER_H__
#define __CONNECTIONSERVER_H__

#include <iostream>

#include "acex/NB_Tcp_Server.h"
#include "acex/Task.h"

class Packet;

class ConnectionServer: public ACEX_NB_Tcp_Server_Task
{
private:
	static const size_t MAX_BUFFER_SIZE		=	64;
public:
	ConnectionServer(ACEX_Message_Task* msgtask);
	virtual ~ConnectionServer();

	int Open(const std::string& local);
    int SendPacket(int clientid, const Packet& packet);
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
	char _buffer[MAX_BUFFER_SIZE];
	size_t _recv;
    size_t _count;
};

#endif
