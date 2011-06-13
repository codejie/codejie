/*
 * PacketProcessor.cpp
 *
 *  Created on: Jun 13, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"

#include "PacketAnalyser.h"


const std::string PacketProcessor::TAG_BEGIN		=	"##";
const std::string PacketProcessor::TAG_END		=	"\r\n";


PacketProcessor::PacketProcessor() {
}

PacketProcessor::~PacketProcessor() {
}

int PacketProcessor::Analyse(std::string& packet) const
{
	std::string::size_type pos = packet.find(TAG_END);
	while(pos != std::string::npos)
	{
		std::string str = packet.substr(0, pos);
		int datasize = 0;
		if(Check(str, datasize) == 0)
		{
			DataAnalyse(str.substr(6, datasize));
		}
		else
		{
			ACEX_LOG_OS(LM_WARNING, "<PacketAnalyser::Process>Illegal packet - " << str << std::endl);
		}

		packet = packet.substr(pos + TAG_END.size());
		if(!packet.empty())
			pos = packet.find(TAG_END);
	}
	return 0;
}

int PacketProcessor::Check(const std::string& packet, int& datasize) const
{
	if(packet.substr(0, 2) != TAG_BEGIN)
	{
		return -1;
	}

	datasize = ACE_OS::atoi(packet.substr(2, 4).c_str());
	if(datasize < 0 || datasize > 1024)
		return -1;

	if(DataCRCCheck(packet.substr(6, datasize), packet.substr(datasize + 6, 4)) != 0)
		return -1;

	return 0;
}

int PacketProcessor::DataCRCCheck(const std::string& data, const std::string& crc) const
{
	return -1;
}

int PacketProcessor::DataAnalyse(const std::string& packet) const
{
	return -1;
}















