/*
 * CollectServerTask.cpp
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"

#include "Defines.h"
#include "PacketProcessor.h"
#include "CollectServerTask.h"

CollectServerTask::CollectServerTask(ACEX_Message_Task* msgtask)
: _taskMsg(msgtask)
{
}

CollectServerTask::~CollectServerTask()
{
}

int CollectServerTask::Open(const std::string& local)
{
	ACE_INET_Addr addr(local.c_str());
	if(open(addr) != 0)
	{
		return -1;
	}
	return 0;
}

void CollectServerTask::Final()
{
	this->deactivate();
}

int CollectServerTask::handle_connect(int clientid, ACEX_TcpStream& client)
{
	ACEX_LOG_OS(LM_INFO, "<CollectServerTask::handle_connect>Client connect - clientid : " << clientid << std::endl);
	return 0;
}

int CollectServerTask::handle_close(int clientid, ACEX_TcpStream& client)
{
	ACEX_LOG_OS(LM_INFO, "<CollectServerTask::handle_close>Client disconnect - clientid : " << clientid << std::endl);

	return 0;
}

int CollectServerTask::handle_recv(int clientid, ACEX_TcpStream& client)
{
	size_t in = client.in_avail();

	while(in >= PacketProcessor::MIN_SIZE)
	{
		std::string recv(client.gptr(), in);
		std::string::size_type pos = recv.find(PacketProcessor::TAG_END);
		if(pos != std::string::npos && pos <= PacketProcessor::MAX_SIZE)
		{
			char* buf = new char[pos];
			client.read(buf, pos);

			ACEX_Message msg(TASK_COLLECT_SERVER, FPARAM_PACKET, pos, buf);
			_taskMsg->put_msg(msg);
		}
		else if(pos > PacketProcessor::MAX_SIZE)
		{
			ACEX_LOG_OS(LM_ERROR, "<CollectServerTask::handle_recv>Packet too long - clientid : " << clientid << std::endl);
			return -1;
		}
		in = client.in_avail();
	}
	return 0;
}

int CollectServerTask::handle_lost(int error, int clientid, const char* packet, size_t packet_size)
{
	ACEX_LOG_OS(LM_ERROR, "<CollectServerTask::handle_lost>Packet lost - error : " << error << " clientid : " << clientid << " size : " << packet_size << std::endl);

	return 0;
}
