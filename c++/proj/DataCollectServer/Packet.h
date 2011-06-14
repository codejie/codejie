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
	typedef std::map<std::string, std::string> CPDataMap; //cp + data

	typedef std::map<std::string, std::string> ItemDataMap; //item parameter + data
	typedef std::map<std::string, ItemDataMap> ItemMap; //item + itemdata

	struct CPData
	{
		CPDataMap data;
		ItemMap item;
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


#endif /* PACKET_H_ */
