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

int PacketProcessor::Analyse(const std::string& stream, Packet& packet, bool checkcrc)
{
	ACEX_LOG_OS(LM_DEBUG, "<PacketProcessor::Analyse>Get packet stream - \n" << stream << std::endl);
	int datasize = 0;
	if(Check(stream, datasize, checkcrc) == 0)
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

int PacketProcessor::Make(std::string& stream, const Packet& packet)
{
	std::string str;
	if(packet.Make(str) != 0)
		return -1;
	std::ostringstream ostr;

	ostr << TAG_BEGIN;
    ostr.fill('0');
    ostr.width(4);
    ostr << str.size();
    ostr.width(0);
    ostr << str << DataCRC(str) << "\r\n";

	stream = ostr.str();
	return 0;
}

int PacketProcessor::Check(const std::string& stream, int& datasize, bool checkcrc)
{
	if(stream.substr(0, 2) != TAG_BEGIN)
	{
		return -1;
	}

	datasize = ACE_OS::atoi(stream.substr(2, 4).c_str());
	if(datasize < 0 || datasize > 1024)
		return -1;

    if(checkcrc == true)
    {
	    if(DataCRCCheck(stream.substr(6, datasize), stream.substr(datasize + 6, 4)) != 0)
		    return -1;
    }
	return 0;
}

int PacketProcessor::DataCRCCheck(const std::string& data, const std::string& crc)
{
	unsigned int check = Toolkit::CRC16((const unsigned char*)data.c_str(), data.size());
	std::ostringstream ostr;
	ostr << std::ios_base::hex << std::ios_base::uppercase << check;
	return ostr.str() == crc ? 0 : -1;
}

const std::string PacketProcessor::DataCRC(const std::string& data)
{
	unsigned int check = Toolkit::CRC16((const unsigned char*)data.c_str(), data.size());
	std::ostringstream ostr;
	ostr << std::hex << std::uppercase << check;
	return ostr.str();
}

int PacketProcessor::DataAnalyse(const std::string& stream, Packet& packet)
{
	return packet.Analyse(stream);
}















