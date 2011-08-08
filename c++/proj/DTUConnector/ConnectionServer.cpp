
#include "acex/ACEX.h"

#include "Defines.h"
#include "Packet.h"
#include "ConnectionServer.h"

ConnectionServer::ConnectionServer(ACEX_Message_Task* msgtask)
: _taskMsg(msgtask)
, _recv(0)
, _count(0)
{
}

ConnectionServer::~ConnectionServer()
{
}

int ConnectionServer::Open(const std::string& local)
{
	ACE_INET_Addr addr(local.c_str());
	if(open(addr) != 0)
	{
		return -1;
	}
	this->activate();
	return 0;
}

void ConnectionServer::Final()
{
	this->deactivate();
}

int ConnectionServer::handle_connect(int clientid, ACEX_TcpStream& client)
{
	ACE_INET_Addr* addr = new ACE_INET_Addr();
	client.get_remote_addr(*addr);

	ACEX_LOG_OS(LM_INFO, "<ConnectionServer::handle_connect>Client connect - clientid : [" << clientid << "] " << addr->get_host_addr() << ":" << addr->get_port_number() << std::endl);

	ACEX_Message msg(TASK_SERVER, FPARAM_SOCKET_CONNECT, clientid, addr);
	_taskMsg->put_msg(msg);

	return 0;
}

int ConnectionServer::handle_close(int clientid, ACEX_TcpStream& client)
{
	ACEX_LOG_OS(LM_INFO, "<ConnectionServer::handle_close>Client disconnect - clientid : " << clientid << std::endl);

	ACEX_Message msg(TASK_SERVER, FPARAM_SOCKET_DISCONNECT, clientid);
	_taskMsg->put_msg(msg);

	return 0;
}

int ConnectionServer::handle_recv(int clientid, ACEX_TcpStream& client)
{
	size_t in = client.in_avail();
	
	if((in + _recv) > MAX_BUFFER_SIZE)
	{
		ACEX_LOG_OS(LM_ERROR, "<ConnectionServer::handle_recv>Packet too long - clientid : " << clientid << std::endl);
		return -1;
	}

	while(in >= Packet::MIN_PACKET_SIZE)
	{
		client.read(_buffer + _recv, in);
		_recv += in;

		Packet * packet = NULL;
		try
		{
			_recv -= Packet::Make(_buffer, _recv, packet);
			if(packet != NULL)
			{
				ACEX_Message msg(TASK_SERVER, FPARAM_PACKET, clientid, packet);
				_taskMsg->put_msg(msg);
			}
		}
		catch(const PacketException& e)
		{
			ACEX_LOG_OS(LM_ERROR, "<ConnectionServer::handle_recv>Analyse Packet exception : " << e << std::endl);
			return -1;
		}

		in = client.in_avail();
	}
	return 0;
}

int ConnectionServer::handle_lost(int error, int clientid, const char* packet, size_t packet_size)
{
	ACEX_LOG_OS(LM_ERROR, "<ConnectionServer::handle_lost>Packet lost - error : " << error << " clientid : " << clientid << " size : " << packet_size << std::endl);

	return 0;
}

void ConnectionServer::Show(std::ostream &os) const
{
    os << "\nTotal Packet = " << _count << std::endl;
}
