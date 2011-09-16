#ifndef __ZJCOLLECTSERVERTASK_H__
#define __ZJCOLLECTSERVERTASK_H__

#include "acex/NB_Tcp_Server.h"
#include "acex/Task.h"

#include "ZJPacket.h"

class ZJCollectServerTask : public ACEX_NB_Tcp_Server_Task
{
public:
    ZJCollectServerTask(ACEX_Message_Task* msgtask);
    virtual ~ZJCollectServerTask();

    int Open(const std::string& local);
    void Final();

    int Send(int clientid, const ZJ::Packet& packet);
protected:
	virtual int handle_connect(int client_id, ACEX_TcpStream& client);
	virtual int handle_close(int client_id, ACEX_TcpStream& client);
    virtual int handle_recv(int client_id, ACEX_TcpStream& client);
    virtual int handle_recv(int client_id, ACEX_InputCDR& input_cdr, ACEX_TcpStream& client);
    virtual int handle_recv(int client_id, ZJ::Packet* packet, ACEX_TcpStream& client);
    virtual int handle_lost(int error, int client_id, const char* packet, size_t packet_size);
private:
	size_t m_szLost;
private:
    ACEX_Message_Task* _taskMsg;
};


#endif
