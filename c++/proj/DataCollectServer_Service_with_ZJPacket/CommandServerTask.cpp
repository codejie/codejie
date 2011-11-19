
#include "acex/ACEX.h"

#include "Defines.h"
#include "CommandPacket.h"
#include "CommandServerTask.h"

CommandServerTask::CommandServerTask(ACEX_Message_Task* msgtask)
: _taskMsg(msgtask)
, _count(0)
{
}

CommandServerTask::~CommandServerTask()
{
}

int CommandServerTask::Open(const std::string& local)
{
	ACE_INET_Addr addr(local.c_str());
	if(open(addr) != 0)
	{
		return -1;
	}
	this->activate();
	return 0;
}

void CommandServerTask::Final()
{
	this->deactivate();
}

int CommandServerTask::handle_connect(int clientid, ACEX_TcpStream& client)
{
	ACE_INET_Addr* addr = new ACE_INET_Addr();
	client.get_remote_addr(*addr);

	ACEX_LOG_OS(LM_INFO, "<CommandServerTask::handle_connect>Client connect - clientid : [" << clientid << "] " << addr->get_host_addr() << ":" << addr->get_port_number() << std::endl);

	ACEX_Message msg(TASK_COMMAND_SERVER, FPARAM_SOCKET_CONNECT, clientid, addr);
	_taskMsg->put_msg(msg);

	return 0;
}

int CommandServerTask::handle_close(int clientid, ACEX_TcpStream& client)
{
	ACEX_LOG_OS(LM_INFO, "<CommandServerTask::handle_close>Client disconnect - clientid : " << clientid << std::endl);

	ACEX_Message msg(TASK_COMMAND_SERVER, FPARAM_SOCKET_DISCONNECT, clientid);
	_taskMsg->put_msg(msg);

	return 0;
}

int CommandServerTask::handle_recv(int clientid, ACEX_TcpStream& client)
{
	size_t in = client.in_avail();

    if(in > CommandPacket::CMD_POST.size())
    {
        char* buf = new char[in];
        client.read(buf, in);
        CommandPacket *packet = NULL;
        if(CommandPacket::Analyse(std::string(buf, in), packet) != 0)
        {
            delete [] buf;
            return -1;
        }
        delete [] buf;

		ACEX_Message msg(TASK_COMMAND_SERVER, FPARAM_PACKET, clientid, packet);
		_taskMsg->put_msg(msg);

        ++ _count;
    }

	return 0;
}

int CommandServerTask::handle_lost(int error, int clientid, const char* packet, size_t packet_size)
{
	ACEX_LOG_OS(LM_ERROR, "<CommandServerTask::handle_lost>Packet lost - error : " << error << " clientid : " << clientid << " size : " << packet_size << std::endl);

	return 0;
}

void CommandServerTask::Show(std::ostream &os) const
{
    os << "\nTotal Packet = " << _count << std::endl;
}
