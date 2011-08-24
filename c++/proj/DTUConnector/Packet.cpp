
#include "Toolkit.h"
#include "Packet.h"


PacketException::PacketException(const std::string& msg, const char* buf, size_t size)
: _msg(msg)
, _buf(buf)
, _size(size)
{
}

void PacketException::Show(std::ostream &os) const
{
	os << "\nException - " << _msg;
	if(_buf != NULL && _size != 0)
		os << "\nData - [" << _size << "]" << Toolkit::PrintBinary(os, _buf, _size);
}

///
size_t Packet::Make(const char *buf, size_t size, Packet *&packet)
{
	if(buf[0] == PACKET_FLAG_HELLO)
	{
		if(size < HelloPacket::SIZE)
			return 0;

		packet = new HelloPacket();
		if(packet->Analyse(buf, size) != 0)
		{
			delete packet, packet = NULL;
			throw PacketException("Analyse HELLO packet failed.", buf, size);
		}
		return size - HelloPacket::SIZE;
	}
	else if(buf[0] == PACKET_FLAG_DATARESP)
	{
		if(size < DataRespPacket::SIZE)
			return 0;

		packet = new DataRespPacket();
		if(packet->Analyse(buf, size) != 0)
		{
			delete packet, packet = NULL;
			throw PacketException("Analyse DATARESP packet failed.", buf, size);
		}
		return size - DataRespPacket::SIZE;
	}
	else 
	{
		throw PacketException("unknown buffer data.", buf, size);
	}
	return 0;
}

size_t Packet::Make(const Packet &packet, char *&buf)
{
    buf = NULL;

	if(packet._type == PT_DATAREQ)
	{
		buf = new char[DataReqPacket::SIZE];
		if(packet.Assemble(buf) != 0)
		{
			delete [] buf, buf = NULL;
			throw PacketException("Assemble DATAREQ packet failed.", NULL, 0);
		}
		return DataReqPacket::SIZE;
	}
	else
	{
		throw PacketException("Un-support packet.", NULL, 0);
	}
	return -1;
}

void Packet::Show(std::ostream& os) const
{
	os << "\n[Packet] type = " << _type;
}

///
HelloPacket::HelloPacket()
: Packet(Packet::PT_HELLO)
{
}

int HelloPacket::Analyse(const char *buf, size_t size)
{
	_tele.assign(buf, size);
	return 0;
}

void HelloPacket::Show(std::ostream &os) const
{
	Packet::Show(os);
	os << "\nTele = " << _tele;
}

///
DataReqPacket::DataReqPacket()
: Packet(Packet::PT_DATAREQ)
{
	_addr = 0x01;
	_read = 0x03;
	_reg[0] = 0x01;
	_reg[1] = 0xF4;
	_number[0] = 0x00;
	_number[1] = 0x02;
	_crc[0] = 0x84;
	_crc[1] = 0x05;
}

int DataReqPacket::Assemble(char *buf) const
{
	memcpy(buf, &_addr, sizeof(_addr));
	memcpy(buf + sizeof(_addr), &_read, sizeof(_read));
	memcpy(buf + sizeof(_addr) + sizeof(_read), _reg, sizeof(_reg));
	memcpy(buf + sizeof(_addr) + sizeof(_read) + sizeof(_reg), _number, sizeof(_number));
	memcpy(buf + sizeof(_addr) + sizeof(_read) + sizeof(_reg) + sizeof(_number), _crc, sizeof(_crc));

	return 0;	
}

void DataReqPacket::Show(std::ostream &os) const
{
	Packet::Show(os);
}

///
DataRespPacket::DataRespPacket()
: Packet(Packet::PT_DATARESP)
{
}

int DataRespPacket::Analyse(const char *buf, size_t size)
{
	memcpy(&_addr, buf, sizeof(_addr));
	memcpy(&_read, buf + sizeof(_addr), sizeof(_read));
	memcpy(&_number, buf + sizeof(_addr) + sizeof(_read), sizeof(_number));
	memcpy(_value, buf + sizeof(_addr) + sizeof(_read) + sizeof(_number), sizeof(_value));
	memcpy(_crc, buf + sizeof(_addr) + sizeof(_read) + sizeof(_number) + sizeof(_value), sizeof(_crc));
	return 0;
}

void DataRespPacket::Show(std::ostream &os) const
{
	Packet::Show(os);
}

///
std::ostream& operator << (std::ostream& os, const Packet& packet)
{
	packet.Show(os);
	return os;
}

std::ostream& operator << (std::ostream& os, const PacketException& e)
{
	e.Show(os);
	return os;
}