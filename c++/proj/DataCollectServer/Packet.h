/*
 * Packet.h
 *
 *  Created on: Jun 13, 2011
 *      Author: jie
 */

#ifndef __PACKET_H__
#define __PACKET_H__

#include <iostream>
#include <string>
#include <map>

class Packet
{
public:
    static const std::string PD_TAG_QN;
    static const std::string PD_TAG_PNUM;
    static const std::string PD_TAG_PNO;
    static const std::string PD_TAG_ST;
    static const std::string PD_TAG_CN;
    static const std::string PD_TAG_PW;
    static const std::string PD_TAG_MN;
    static const std::string PD_TAG_FLAG;
    static const std::string PD_TAG_CP;

    static const std::string PD_CN_RUNTIMEDATA;
    static const std::string PD_CN_DAILYDATA;
    static const std::string PD_CN_MINUTELYDATA;
    static const std::string PD_CN_HOURLYDATA;

    static const std::string PD_CN_VALVECONTROL;
    static const std::string PD_CN_ICFEEADD;
    static const std::string PD_CN_VALVEREALDATA;
    static const std::string PD_CN_ICFEEUPLOAD;
    static const std::string PD_CN_VALVEUPLOAD;

    static const std::string PD_CP_TAG_DATATIME;
    static const std::string PD_CP_TAG_EXERTN;

    static const std::string PD_ST_21;
    static const std::string PD_ST_91;

    static const std::string VALUE_DEFAULT_MN;
    static const std::string VALUE_DEFAULT_PW;

public:
	typedef std::map<std::string, std::string> TCPDataMap; //cp + data

	typedef std::map<std::string, std::string> TCPItemDataMap; //item parameter + data
	typedef std::map<std::string, TCPItemDataMap> TCPItemMap; //item + itemdata

	struct CPData
	{
		TCPDataMap data;
		TCPItemMap item;
	};
public:
	Packet();
	virtual ~Packet();

	int Analyse(const std::string& packet);
	int Make(std::string& packet) const;

	void Show(std::ostream& os) const;
protected:
	int CPAnalyse(const std::string& packet);
	int CPDataAnalyse(const std::string& data);
	int CPItemAnalyse(const std::string& item);

	const std::string CPMake() const;
public:
	std::string QN;
	std::string PNUM;
	std::string PNO;
	std::string ST;
	std::string CN;
	std::string PW;
	std::string MN;
	std::string Flag;

	CPData CP;
};

class PacketException
{
public:
    PacketException(const std::string& msg, const Packet& packet)
        : _msg(msg), _packet(packet)
    {
    }
    void Show(std::ostream& os) const;
protected:
    std::string _msg;
    const Packet& _packet;

};

/////
extern std::ostream& operator << (std::ostream& os, const Packet& packet);
extern std::ostream& operator << (std::ostream& os, const PacketException& exception);

#endif /* PACKET_H_ */
