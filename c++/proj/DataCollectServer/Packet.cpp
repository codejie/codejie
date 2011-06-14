/*
 * Packet.cpp
 *
 *  Created on: Jun 13, 2011
 *      Author: jie
 */

#include "Packet.h"

Packet::Packet()
{

}

Packet::~Packet()
{

}

int Packet::Analyse(const std::string& packet)
{
	std::string::size_type pos = packet.find(";");
	std::string::size_type begin = 0;
	std::string::size_type tmp = std::string::npos;
	std::string tag;
	while(pos != std::string::npos)
	{
		tmp = packet.find("=", begin);
		if(tmp == std::string::npos)
			return -1;
		if(packet.substr(begin, tmp -1) == "QN")
			QN = packet.substr(tmp + 1, pos - 1);
		else if(packet.substr(begin, tmp -1) == "PNUM")
			PNUM = packet.substr(tmp + 1, pos - 1);
		else if(packet.substr(begin, tmp -1) == "PNO")
			PNO = packet.substr(tmp + 1, pos - 1);
		else if(packet.substr(begin, tmp -1) == "ST")
			ST = packet.substr(tmp + 1, pos - 1);
		else if(packet.substr(begin, tmp -1) == "CN")
			CN = packet.substr(tmp + 1, pos - 1);
		else if(packet.substr(begin, tmp -1) == "PW")
			PW = packet.substr(tmp + 1, pos - 1);
		else if(packet.substr(begin, tmp -1) == "MN")
			MN = packet.substr(tmp + 1, pos - 1);
		else if(packet.substr(begin, tmp -1) == "Flag")
			Flag = packet.substr(tmp + 1, pos - 1);
		else if(packet.substr(begin, tmp -1) == "CP")
			if(CPAnalyse(packet.substr(tmp + 1, pos -1)) != 0)
				return 01;
		else
			return -1;

		begin = pos + 1;
		if(begin == packet.size())
			return 0;

		pos = packet.find(";", begin);
	}

	return CPAnalyse(packet.substr(begin));
}

int Packet::CPAnalyse(const std::string& packet)
{
	return -1;
}
