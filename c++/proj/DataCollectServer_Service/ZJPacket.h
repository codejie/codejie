#ifndef __ZJPACKET_H__
#define __ZJPACKET_H__

#include <iostream>
#include <exception>
#include <string>

#include "acex/CDR_Stream.h"

namespace ZJ
{

const unsigned int PEID_CHECKSUM_FAIL	=	1;
const unsigned int PEID_CRC_FAIL		=	2;

class CPacketException
{
public:
	CPacketException(unsigned int id, const std::string& info = "")
		: m_id(id), m_info(info)
	{
	}
	virtual ~CPacketException() {}
protected:
    CPacketException(const CPacketException &right);
	CPacketException& operator=(const CPacketException &right) { return *this; }
public:
	unsigned int m_id;
	std::string m_info;
};

class CPacket
{
public:
	CPacket();
	virtual ~CPacket();

public:
	static int Create(ACEX_InputCDR &input_cdr, CPacket*& packet);
	static size_t HeaderLength();
	virtual size_t Length() const;
	virtual int Read(ACEX_InputCDR& input_cdr);
	virtual int Write(ACEX_OutputCDR& output_cdr);

	int SetData(const char* data, unsigned int len);
	int AttachData(const char* data, unsigned int len);

	virtual void Print(std::ostream& os) const;
protected:
	int DataAlloc();
	void DataFree();

	int CheckChecksum() const;
	int SetChecksum();

	int CheckCRC() const;
	int SetCRC();
public:
	//header
	unsigned int m_uiStart;
	unsigned int m_uiLength;
	unsigned int m_uiOrdinal;
	unsigned int m_uiChecksum;
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
	char m_acCRC[2];
	unsigned int m_uiEnd;
};

}

#endif
