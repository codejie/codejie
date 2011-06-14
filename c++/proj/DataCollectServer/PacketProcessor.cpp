/*
 * PacketProcessor.cpp
 *
 *  Created on: Jun 13, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"

#include "Packet.h"
#include "PacketProcessor.h"


const std::string PacketProcessor::TAG_BEGIN		=	"##";
const std::string PacketProcessor::TAG_END			=	"\r\n";


PacketProcessor::PacketProcessor() {
}

PacketProcessor::~PacketProcessor() {
}

int PacketProcessor::Analyse(std::string& stream, Packet& packet) const
{
	std::string::size_type pos = stream.find(TAG_END);
	while(pos != std::string::npos)
	{
		std::string str = stream.substr(0, pos);
		int datasize = 0;
		if(Check(str, datasize) == 0)
		{
			if(DataAnalyse(str.substr(6, datasize), packet) != 0)
			{
				ACEX_LOG_OS(LM_WARNING, "<<PacketAnalyser::Process>Data analyse failed - " << str.substr(6, datasize) << std::endl);
			}
		}
		else
		{
			ACEX_LOG_OS(LM_WARNING, "<PacketAnalyser::Process>Illegal packet - " << str << std::endl);
		}

		stream = stream.substr(pos + TAG_END.size());
		if(!stream.empty())
			pos = stream.find(TAG_END);
	}
	return 0;
}

int PacketProcessor::Check(const std::string& stream, int& datasize) const
{
	if(stream.substr(0, 2) != TAG_BEGIN)
	{
		return -1;
	}

	datasize = ACE_OS::atoi(stream.substr(2, 4).c_str());
	if(datasize < 0 || datasize > 1024)
		return -1;

	if(DataCRCCheck(stream.substr(6, datasize), stream.substr(datasize + 6, 4)) != 0)
		return -1;

	return 0;
}

int PacketProcessor::DataCRCCheck(const std::string& data, const std::string& crc) const
{
	return -1;
}

int PacketProcessor::DataAnalyse(const std::string& stream, Packet& packet) const
{
	return packet.Analyse(stream);
}















