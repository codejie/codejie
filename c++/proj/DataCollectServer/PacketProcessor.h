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

class PacketProcessor
{
public:
	static const size_t MIN_SIZE			=	8;
	static const size_t MAX_SIZE			=	MIN_SIZE + 1024;
	static const std::string TAG_BEGIN;
	static const std::string TAG_END;
public:
	PacketProcessor();
	virtual ~PacketProcessor();

	static int Analyse(const std::string& stream, Packet& packet);
	static int Make(std::string& stream, const Packet& packet);
protected:
	static int Check(const std::string& stream, int& datasize);
	static int DataCRCCheck(const std::string& data, const std::string& crc);
	static int DataAnalyse(const std::string& stream, Packet& packet);
};

#endif /* __PACKETANALYSER_H__ */
