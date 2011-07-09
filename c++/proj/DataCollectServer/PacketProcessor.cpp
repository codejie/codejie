/*
 * PacketProcessor.cpp
 *
 *  Created on: Jun 13, 2011
 *      Author: codejie
 */

#include <sstream>

#include "acex/ACEX.h"

#include "Toolkit.h"
#include "Packet.h"
#include "PacketProcessor.h"


const std::string PacketProcessor::TAG_BEGIN		=	"##";
const std::string PacketProcessor::TAG_END			=	"\r\n";


PacketProcessor::PacketProcessor() {
}

PacketProcessor::~PacketProcessor() {
}

int PacketProcessor::Analyse(const std::string& stream, Packet& packet)
{
	ACEX_LOG_OS(LM_DEBUG, "<PacketProcessor::Analyse>Get packet stream - \n" << stream << std::endl);
	int datasize = 0;
	if(Check(stream, datasize) == 0)
	{
		if(DataAnalyse(stream.substr(6, datasize), packet) == 0)
		{
			ACEX_LOG_OS(LM_DEBUG, "<PacketProcessor::Analyse>Analyse result packet - \n" << packet << std::endl);
		}
		else
		{
			ACEX_LOG_OS(LM_WARNING, "<<PacketAnalyser::Process>Data analyse failed - " << stream.substr(6, datasize) << std::endl);
		}
	}
	else
	{
		ACEX_LOG_OS(LM_WARNING, "<PacketAnalyser::Process>Illegal packet - " << stream << std::endl);
	}

	return 0;
}

int PacketProcessor::Check(const std::string& stream, int& datasize)
{
	if(stream.substr(0, 2) != TAG_BEGIN)
	{
		return -1;
	}

	datasize = ACE_OS::atoi(stream.substr(2, 4).c_str());
	if(datasize < 0 || datasize > 1024)
		return -1;

//	if(DataCRCCheck(stream.substr(6, datasize), stream.substr(datasize + 6, 4)) != 0)
//		return -1;

	return 0;
}

int PacketProcessor::DataCRCCheck(const std::string& data, const std::string& crc)
{
	unsigned int check = Toolkit::CRC16((const unsigned char*)data.c_str(), data.size());
	std::ostringstream ostr;
	ostr << std::ios::hex << check;
	return ostr.str() == crc ? 0 : -1;
}

int PacketProcessor::DataAnalyse(const std::string& stream, Packet& packet)
{
	return packet.Analyse(stream);
}















