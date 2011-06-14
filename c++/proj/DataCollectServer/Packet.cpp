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
	//std::string tag;
	while(pos != std::string::npos)
	{
		tmp = packet.find("=", begin);
		if(tmp == std::string::npos)
			return -1;
		if(packet.substr(begin, tmp - begin) == "QN")
			QN = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == "PNUM")
			PNUM = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == "PNO")
			PNO = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == "ST")
			ST = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == "CN")
			CN = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == "PW")
			PW = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == "MN")
			MN = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == "Flag")
			Flag = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == "CP")
			if(CPAnalyse(packet.substr(tmp + 1, pos - tmp - 1)) != 0)
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
	//CP=&&DataTime=20040516021000;B01-Cou=200;101-Cou=2.5,101-Min=1.1,101-Avg=1.1,101-Max=1.1;102-Cou=2.5,102-Min=2.1,102-Avg=2.1,102-Max=2.1&&

	if(packet.substr(0, 2) != "&&" || packet.substr(packet.size() - 2) != "&&")
		return -1;

	std::string::size_type pos = packet.find(";");
	std::string::size_type begin = 2;

	while(pos != std::string::npos)
	{
		if(CPDataAnalyse(packet.substr(begin, pos - begin - 1)) != 0)
			return -1;

		begin = pos + 1;
		if(begin == packet.size() - 2)
			return 0;

		pos = packet.find(";", begin);
	}

	return CPDataAnalyse(packet.substr(begin, packet.size() - begin - 2));
}

int Packet::CPDataAnalyse(const std::string& data)
{
	std::string::size_type tmp = data.find("=");
	if(tmp == std::string::npos)
		return -1;

	if(data.substr(0, tmp).find("-") != std::string::npos)
	{
		std::string::size_type p = data.find(",");
		std::string::size_type b = 0;
		while(p != std::string::npos)
		{
			if(CPItemAnalyse(data.substr(b, p - b -1)) != 0)
				return -1;
			b = p + 1;
			if(b == data.size())
				return 0;

			p = data.find(",", b);
		}
		return CPItemAnalyse(data.substr(b));
	}
	else
	{
		CP.data.insert(std::make_pair(data.substr(0, tmp), data.substr(tmp + 1)));
	}
	return 0;
}

int Packet::CPItemAnalyse(const std::string& item)
{
	std::string::size_type tmp = item.find("=");
	if(tmp == std::string::npos)
		return -1;

	std::string::size_type t = item.find("-");

	ItemMap::iterator it = CP.item.find(item.substr(0, t));
	if(it != CP.item.end())
	{
		return it->second.insert(std::make_pair(item.substr(t + 1, tmp - t - 1), item.substr(tmp + 1))).second ? 0 : -1;
	}
	else
	{
		ItemDataMap m;
		m.insert(std::make_pair(item.substr(t + 1, tmp - t - 1), item.substr(tmp + 1)));
		return CP.item.insert(std::make_pair(item.substr(0, t), m)).second ? 0 : -1;
	}
}

void Packet::Show(std::ostream& os) const
{
	os << "\nQN = " << QN;
	os << "\nPNUM = " << PNUM;
	os << "\nPNO = " << PNO;
	os << "\nST = " << ST;
	os << "\nCN = " << CN;
	os << "\nPW = " << PW;
	os << "\nMN = " << MN;
	os << "\nFlag = " << Flag;

	os << "\nCP Data = ";

	//os << "\nCP = " << CP << std::endl;
}

