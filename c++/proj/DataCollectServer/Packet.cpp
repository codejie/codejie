/*
 * Packet.cpp
 *
 *  Created on: Jun 13, 2011
 *      Author: jie
 */

#include <sstream>

#include "Packet.h"

const std::string Packet::PD_TAG_QN                 =   "QN";
const std::string Packet::PD_TAG_PNUM               =   "PNUM";
const std::string Packet::PD_TAG_PNO                =   "PNO";
const std::string Packet::PD_TAG_ST                 =   "ST";
const std::string Packet::PD_TAG_CN                 =   "CN";
const std::string Packet::PD_TAG_PW                 =   "PW";
const std::string Packet::PD_TAG_MN                 =   "MN";
const std::string Packet::PD_TAG_FLAG               =   "Flag";
const std::string Packet::PD_TAG_CP                 =   "CP";


const std::string Packet::PD_CN_RUNTIMEDATA         =   "2011";
const std::string Packet::PD_CN_DAILYDATA           =   "2031";
const std::string Packet::PD_CN_MINUTELYDATA        =   "2051";
const std::string Packet::PD_CN_HOURLYDATA          =   "2061";

const std::string Packet::PD_CN_VALVECONTROL        =   "3050";
const std::string Packet::PD_CN_FEEADD              =   "3051";
const std::string Packet::PD_CN_REALDATA            =   "3052";
const std::string Packet::PD_CN_FEEUPLOAD           =   "3053";
const std::string Packet::PD_CN_VALVEDATA           =   "3054";

const std::string Packet::PD_CP_TAG_DATATIME =   "DataTime";


const std::string Packet::VALUE_DEFAULT_MN          =   "88888880000001";
const std::string Packet::VALUE_DEFAULT_PW          =   "123456";


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

	while(pos != std::string::npos)
	{
		tmp = packet.find("=", begin);
		if(tmp == std::string::npos)
			return -1;
		if(packet.substr(begin, tmp - begin) == PD_TAG_QN)
			QN = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == PD_TAG_PNUM)
			PNUM = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == PD_TAG_PNO)
			PNO = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == PD_TAG_ST)
			ST = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == PD_TAG_CN)
			CN = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == PD_TAG_PW)
			PW = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == PD_TAG_MN)
			MN = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == PD_TAG_FLAG)
			Flag = packet.substr(tmp + 1, pos - tmp - 1);
		else if(packet.substr(begin, tmp - begin) == PD_TAG_CP)
			return CPAnalyse(packet.substr(tmp + 1));

		begin = pos + 1;
		if(begin == packet.size())
			return 0;

		pos = packet.find(";", begin);
	}

	return CPAnalyse(packet.substr(begin));
}

int Packet::Make(std::string &packet) const
{
//QN=20040516010101001;ST=32;CN=1012;PW=123456;MN=88888880000001;Flag=3;CP=&&MFlag=50&&
	std::ostringstream ostr;
	
	if(!QN.empty())
		ostr << PD_TAG_QN <<"=" << QN << ";";
	if(!PNUM.empty())
		ostr << PD_TAG_PNUM <<"=" << PNUM << ";";
	if(!PNO.empty())
		ostr << PD_TAG_PNO <<"=" << PNO << ";";
	if(!ST.empty())
		ostr << PD_TAG_ST <<"=" << ST << ";";
	if(!CN.empty())
		ostr << PD_TAG_CN <<"=" << CN << ";";
	if(!PW.empty())
		ostr << PD_TAG_PW <<"=" << PW << ";";
	if(!MN.empty())
		ostr << PD_TAG_MN <<"=" << MN << ";";
	if(!Flag.empty())
		ostr << PD_TAG_FLAG <<"=" << Flag << ";";

	if(CP.data.size() > 0)
	{
		ostr << PD_TAG_CP << "=&&" << CPMake() << "&&";
	}
	packet = ostr.str();

	return 0;
}
	//

int Packet::CPAnalyse(const std::string& packet)
{
	//CP=&&DataTime=20040516021000;B01-Cou=200;101-Cou=2.5,101-Min=1.1,101-Avg=1.1,101-Max=1.1;102-Cou=2.5,102-Min=2.1,102-Avg=2.1,102-Max=2.1&&

	if(packet.substr(0, 2) != "&&" || packet.substr(packet.size() - 2) != "&&")
		return -1;

	std::string::size_type pos = packet.find(";");
	std::string::size_type begin = 2;

	while(pos != std::string::npos)
	{
		if(CPDataAnalyse(packet.substr(begin, pos - begin)) != 0)
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
			if(CPItemAnalyse(data.substr(b, p - b)) != 0)
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

	TCPItemMap::iterator it = CP.item.find(item.substr(0, t));
	if(it != CP.item.end())
	{
		return it->second.insert(std::make_pair(item.substr(t + 1, tmp - t - 1), item.substr(tmp + 1))).second ? 0 : -1;
	}
	else
	{
		TCPItemDataMap m;
		m.insert(std::make_pair(item.substr(t + 1, tmp - t - 1), item.substr(tmp + 1)));
		return CP.item.insert(std::make_pair(item.substr(0, t), m)).second ? 0 : -1;
	}
}

const std::string Packet::CPMake() const
{
	std::ostringstream ostr;
//Data
	TCPDataMap::const_iterator it = CP.data.begin();
	while(it != CP.data.end())
	{
		ostr << it->first << "=" << it->second;

		++ it;
		if(it != CP.data.end())
		{
			ostr << ";";
		}
		else
		{
			if(CP.item.size() > 0)
				ostr << ";";
			break;
		}		
	}
//Item
	TCPItemMap::const_iterator i = CP.item.begin();
	while(i != CP.item.end())
	{
		TCPItemDataMap::const_iterator ii = i->second.begin();
		while(ii != i->second.end())
		{
			ostr << i->first << "-" << ii->first << "=" << ii->second;
			++ ii;
			if(ii != i->second.end())
			{
				ostr << ",";
			}
			else
			{
				break;
			}
		}

		++ i;
		if(i != CP.item.end())
		{
			ostr << ";";
		}
		else
		{
			break;
		}
	}

	return ostr.str();
}

///
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

	for(TCPDataMap::const_iterator it = CP.data.begin(); it != CP.data.end(); ++ it)
	{
		os << "\n  " << it->first << " = " << it->second;
	}

	os << "\nCP Item = ";
	for(TCPItemMap::const_iterator it = CP.item.begin(); it != CP.item.end(); ++ it)
	{
		os << "\n  " << it->first;
		for(TCPItemDataMap::const_iterator i = it->second.begin(); i != it->second.end(); ++ i)
		{
			os << "\n    " << i->first << " = " << i->second;
		}
	}

	os << std::endl;
}

//////
void PacketException::Show(std::ostream& os) const
{
    os << "\nException - " << _msg;
    os << "\nPacket - \n" << _packet;
}

//////
std::ostream& operator << (std::ostream& os, const Packet& packet)
{
	packet.Show(os);
	return os;
}

std::ostream& operator << (std::ostream& os, const PacketException& exception)
{
    exception.Show(os);
    return os;
}

