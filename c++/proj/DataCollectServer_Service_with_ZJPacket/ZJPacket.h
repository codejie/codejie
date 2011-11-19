#ifndef __ZJPACKET_H__
#define __ZJPACKET_H__

#include <iostream>
#include <exception>
#include <string>
#include <vector>

#include "acex/CDR_Stream.h"

namespace ZJ
{

const unsigned int PEID_CHECKSUM_FAIL	=	1;
const unsigned int PEID_CRC_FAIL		=	2;

const unsigned short FUNCNO_DATA_RUNTIME    =   2411;
const unsigned short FUNCNO_DATA_MINUTE     =   2404;
const unsigned short FUNCNO_DATA_HOUR       =   2401;

class PacketException
{
public:
	PacketException(unsigned int id, const std::string& info = "")
		: m_id(id), m_info(info)
	{
	}
	virtual ~PacketException() {}

	virtual void Show(std::ostream& os) const {};
protected:
	PacketException(const PacketException &right) {};
	PacketException& operator=(const PacketException &right) { return *this; }
public:
	unsigned int m_id;
	std::string m_info;
};

typedef std::vector<std::string> TTitleVector;
typedef std::vector<std::string> TDataVector;

class Packet
{
public:
	Packet();
	virtual ~Packet();

public:
	static int Create(ACEX_InputCDR &input_cdr, Packet*& packet);
	static size_t HeaderLength();
    static size_t PacketLength(ACEX_InputCDR &input_cdr);
	virtual size_t Length() const;
	virtual int Read(ACEX_InputCDR& input_cdr);
	virtual int Write(ACEX_OutputCDR& output_cdr) const;

	int SetData(const char* data, unsigned int len);
	int AttachData(const char* data, unsigned int len);

    int Decode(TTitleVector &title, TDataVector &data) const;

	virtual void Show(std::ostream& os) const;
protected:
	int DataAlloc();
	void DataFree();

	int CheckChecksum() const;
	int SetChecksum() const;

	int CheckCRC() const;
	int SetCRC() const;
public:
	//header
	unsigned int m_uiStart;
	unsigned int m_uiLength;
	unsigned int m_uiOrdinal;
	mutable unsigned int m_uiChecksum;
	//Body
	unsigned char m_ucVersion;
	unsigned char m_ucPacketType;
	char m_acReqAddr[16];
	char m_acRespAddr[16];
	char m_acPasswd[6];
	unsigned short m_usFuncNo;
	unsigned int m_uiExSerial;
	unsigned int m_uiRetCode;
	unsigned int m_uiDataLen;
	char* m_pDataBuf;
	//End
	mutable char m_acCRC[2];
	unsigned int m_uiEnd;
};

}

extern std::ostream& operator << (std::ostream& os, const ZJ::Packet& packet);
extern std::ostream& operator << (std::ostream& os, const ZJ::PacketException& exception);

#endif
