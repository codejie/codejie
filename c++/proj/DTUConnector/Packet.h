#ifndef __PACKET_H__
#define __PACKET_H__


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
	enum PacketType { PT_UNKNOWN = -1, PT_HELLO = 0, PT_DATAREQ, PT_DATARESP };
public:
	Packet(PacketType type);
	virtual ~Packet();

	static size_t Make(const char* buf, size_t size, Packet*& packet);
	static size_t Make(const Packet& packet, char*& buf);

	virtual void Show(std::ostream& os) const = 0;
protected:
	virtual int Analyse(const char* buf, size_t size) { return -1; }
	virtual int Assemble(char*& buf, size_t& size) { return -1; }
public:
	PacketType _type;
};

class HelloPacket : public Packet
{
public:
	HelloPacket();
	virtual ~HelloPacket();

protected:
	virtual int Analyse(const char* buf, size_t size);
public:
	std::string _tele;
};

class DataReqPacket : public Packet
{
public:
	DataReqPacket();
	virtual ~DataReqPacket();
protected:
	virtual int Assemble(char*& buf, size_t& size);
};

class DataRespPacket : public Packet
{
public:
	DataRespPacket();
	virtual ~DataRespPacket();
protected:
	virtual int Analyse(const char* buf, size_t size);
};


extern std::ostream& operator << (std::ostream& os, const Packet& packet);
extern std::ostream& operator << (std::ostream& os, const PacketException& exception);

#endif
