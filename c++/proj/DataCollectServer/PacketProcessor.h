/*
 * PacketProcessor.h
 *
 *  Created on: Jun 13, 2011
 *      Author: codejie
 */

#ifndef __PACKETANALYSER_H__
#define __PACKETANALYSER_H__

#include <string>
#include <iostream>

class Packet;

class PacketProcessorStatistic
{

};

class PacketProcessor
{
protected:
	static const std::string TAG_BEGIN;
	static const std::string TAG_END;
public:
	PacketProcessor();
	virtual ~PacketProcessor();

	int Init();

	int Analyse(std::string& stream, Packet& packet) const;
	int Make(std::string& stream, const Packet& packet) const;

	void Show(std::ostream& os) const;
protected:
	int Check(const std::string& stream, int& datasize) const;
	int DataCRCCheck(const std::string& data, const std::string& crc) const;
	int DataAnalyse(const std::string& stream, Packet& packet) const;
};

#endif /* __PACKETANALYSER_H__ */
