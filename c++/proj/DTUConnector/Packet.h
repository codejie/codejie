#ifndef __PACKET_H__
#define __PACKET_H__

#include <iostream>
#include <string>

class PacketException
{
public:
	PacketException(const std::string& msg, const char* buf, size_t size);
	virtual ~PacketException() {}

	void Show(std::ostream& os) const;
protected:
	std::string _msg;
	const char* _buf;
	size_t _size;
};

class Packet
{
public:
	static const size_t MIN_PACKET_SIZE			=	8;

	static const char PACKET_FLAG_HELLO		=	0x31;
	static const char PACKET_FLAG_DATARESP	=	0x01;
public:
	enum PacketType { PT_UNKNOWN = -1, PT_HELLO = 0, PT_DATAREQ, PT_DATARESP };
public:
	Packet(PacketType type)
		: _type(type)
	{
	}
	virtual ~Packet() {};

	static size_t Make(const char* buf, size_t size, Packet*& packet);
	static size_t Make(const Packet& packet, char*& buf);

	virtual void Show(std::ostream& os) const;
protected:
	virtual int Analyse(const char* buf, size_t size) { return -1; }
	virtual int Assemble(char* buf) const { return -1; }
public:
	PacketType _type;
};

class HelloPacket : public Packet
{
public:
	static const size_t SIZE		=	11;
public:
	HelloPacket();
	virtual ~HelloPacket() {}

	virtual void Show(std::ostream& os) const;
protected:
	virtual int Analyse(const char* buf, size_t size);
public:
	std::string _tele;
};

class DataReqPacket : public Packet
{
public:
	static const size_t SIZE		=	8;
public:
	DataReqPacket();
	virtual ~DataReqPacket() {}

	virtual void Show(std::ostream& os) const;
protected:
	virtual int Assemble(char* buf) const;
public:
	unsigned char _addr;
	unsigned char _read;
	unsigned char _reg[2];
	unsigned char _number[2];
	unsigned char _crc[2];
};

class DataRespPacket : public Packet
{
public:
	static const size_t SIZE		=	9;
public:
	DataRespPacket();
	virtual ~DataRespPacket();

	virtual void Show(std::ostream& os) const;
protected:
	virtual int Analyse(const char* buf, size_t size);
public:
	unsigned char _addr;
	unsigned char _read;
	unsigned char _number;
	unsigned char _value[4];
	unsigned char _crc[2];
};


extern std::ostream& operator << (std::ostream& os, const Packet& packet);
extern std::ostream& operator << (std::ostream& os, const PacketException& exception);

#endif
