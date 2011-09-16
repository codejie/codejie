
#include "acex/ACEX.h"

#include "Defines.h"
#include "ZJCollectServerTask.h"



ZJCollectServerTask::ZJCollectServerTask(ACEX_Message_Task* msgtask)
: _taskMsg(msgtask)
{
}

ZJCollectServerTask::~ZJCollectServerTask()
{
}

int ZJCollectServerTask::Open(const std::string& local)
{
	ACE_INET_Addr addr(local.c_str());
	if(open(addr) != 0)
	{
		return -1;
	}
	this->activate();
	return 0;
}

void ZJCollectServerTask::Final()
{
	this->deactivate();
}

int ZJCollectServerTask::Send(int clientid, const ZJ::Packet& packet)
{
    ACEX_LOG_OS(LM_DEBUG, "<ZJCollectServerTask::Send>Send Packet - clientid : " << clientid << "\n" << packet << std::endl);

    ACEX_OutputCDR output_cdr(packet.Length());
	if(packet.Write(output_cdr))
        return (ACEX_NB_Tcp_Server_Task::send(clientid, output_cdr.buffer(), output_cdr.size()) == ACEX_NB_Tcp_Server_Task::SEND_SUCCESS ? 0 : -1);
	
	return -1;
}

int ZJCollectServerTask::handle_connect(int clientid, ACEX_TcpStream& client)
{
	ACE_INET_Addr* addr = new ACE_INET_Addr();
	client.get_remote_addr(*addr);

	ACEX_LOG_OS(LM_INFO, "<ZJCollectServerTask::handle_connect>Client connect - clientid : [" << clientid << "] " << addr->get_host_addr() << ":" << addr->get_port_number() << std::endl);

	ACEX_Message msg(TASK_ZJCOLLECT_SERVER, FPARAM_SOCKET_CONNECT, clientid, addr);
	_taskMsg->put_msg(msg);

	return 0;
}

int ZJCollectServerTask::handle_close(int clientid, ACEX_TcpStream& client)
{
	ACEX_LOG_OS(LM_INFO, "<ZJCollectServerTask::handle_close>Client disconnect - clientid : " << clientid << std::endl);

	ACEX_Message msg(TASK_ZJCOLLECT_SERVER, FPARAM_SOCKET_DISCONNECT, clientid);
	_taskMsg->put_msg(msg);

	return 0;
}

int ZJCollectServerTask::handle_recv(int clientid, ACEX_TcpStream& client)
{
	size_t to_read = client.in_avail();

    while (to_read >= ZJ::Packet::HeaderLength()) 
	{
        ACEX_InputCDR head_cdr(client.gptr(), ZJ::Packet::HeaderLength());
        size_t packet_size = ZJ::Packet::PacketLength(head_cdr);
		if (packet_size > client.buf_size() || packet_size <= 0)
		{
			ACE_DEBUG((LM_DEBUG, "packet size(%d) error, client [%d] be closed.\n", packet_size, clientid));
			return -1;
		}

		if (to_read < packet_size) 
			return 0;

		ACEX_InputCDR input_cdr(packet_size);
		client.read(const_cast<char*>(input_cdr.buffer()), packet_size);
		
		if (handle_recv(clientid, input_cdr, client) != 0)
			return -1;

		to_read = client.in_avail();
	}

	return 0;
}

int ZJCollectServerTask::handle_recv(int clientid, ACEX_InputCDR& input_cdr, ACEX_TcpStream& client)
{
    ZJ::Packet* packet = 0;
    int res = ZJ::Packet::Create(input_cdr, packet);
	if (res == 0 && packet != 0)
		return handle_recv(clientid, packet, client);

	if (res == -1)
	{
		ACE_DEBUG((LM_DEBUG, "packet create error, client [%d] be closed.\n", clientid));
		return -1;
	}

	ACE_DEBUG((LM_DEBUG, "packet create error, the packet is ignored.\n"));
	return 0;
}

int ZJCollectServerTask::handle_recv(int clientid, ZJ::Packet* packet, ACEX_TcpStream& client)
{
    ACEX_LOG_OS(LM_DEBUG, "<ZJCollectServerTask::handle_recv>Recv from client - clientid : " << clientid << "\n" << *packet);

	ACEX_Message oMsg(TASK_ZJCOLLECT_SERVER, FPARAM_PACKET, clientid, packet);
	_taskMsg->put_msg(oMsg);

    return 0;
}

int ZJCollectServerTask::handle_lost(int error, int clientid, const char* packet, size_t packet_size)
{
	ACEX_LOG_OS(LM_ERROR, "<ZJCollectServerTask::handle_lost>Packet lost - error : " << error << " clientid : " << clientid << " size : " << packet_size << std::endl);

	return 0;
}