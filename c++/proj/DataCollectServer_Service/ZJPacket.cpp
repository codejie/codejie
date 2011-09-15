
#include "Toolkit.h"
#include "ZJPacket.h"

namespace ZJ
{

CPacket::CPacket()
: m_uiDataLen(0)
, m_pDataBuf(NULL)
{
}

CPacket::~CPacket()
{
	DataFree();
}

int CPacket::Create(ACEX_InputCDR &input_cdr, CPacket *&packet)
{
	packet = new CPacket;

	return packet->Read(input_cdr);
}

size_t CPacket::HeaderLength()
{
	return 8;
}

size_t CPacket::Length() const
{
	return sizeof(m_uiStart)
			+ sizeof(m_uiLength)
			+ sizeof(m_uiOrdinal)
			+ sizeof(m_uiChecksum)
			//Body
			+ sizeof(m_ucVersion)
			+ sizeof(m_ucPacketType)
			+ sizeof(m_acReqAddr)
			+ sizeof(m_acRespAddr)
			+ sizeof(m_acPasswd)
			+ sizeof(m_usFuncNo)
			+ sizeof(m_uiExSerial)
			+ sizeof(m_uiRetCode)
			+ sizeof(m_uiDataLen)
			+ m_uiDataLen
			//End
			+ sizeof(m_acCRC)
			+ sizeof(m_uiEnd);
}

int CPacket::Read(ACEX_InputCDR &input_cdr)
{
	//Header
	input_cdr.read(m_uiStart);
	input_cdr.read(m_uiLength);
	input_cdr.read(m_uiOrdinal);
	input_cdr.read(m_uiChecksum);

	if(CheckChecksum() != 0)
		throw CPacketException(PEID_CHECKSUM_FAIL, "check checksum fail.");

	//Body
	input_cdr.read(m_ucVersion);
	input_cdr.read(m_ucPacketType);
	input_cdr.read(m_acReqAddr, 16);
	input_cdr.read(m_acRespAddr, 16);
	input_cdr.read(m_acPasswd, 6);
	input_cdr.read(m_usFuncNo);
	input_cdr.read(m_uiExSerial);
	input_cdr.read(m_uiRetCode);
	input_cdr.read(m_uiDataLen);
	if(m_uiDataLen == 0 || m_uiDataLen > 64 * 1024)
		return -1;
	DataAlloc();
	input_cdr.read(m_pDataBuf, m_uiDataLen);
	//End
	input_cdr.read(m_acCRC, 2);

	if(CheckCRC() != 0)
		throw CPacketException(PEID_CRC_FAIL, "check CRC fail.");

	input_cdr.read(m_uiEnd);

	return input_cdr.good_bit() ? 0 : -1;
}

int CPacket::Write(ACEX_OutputCDR &output_cdr)
{
	//Header
	output_cdr.write(m_uiStart);
	output_cdr.write(m_uiLength);
	output_cdr.write(m_uiOrdinal);

	SetChecksum();

	output_cdr.write(m_uiChecksum);
	//Body
	output_cdr.write(m_ucVersion);
	output_cdr.write(m_ucPacketType);
	output_cdr.write(m_acReqAddr, 16);
	output_cdr.write(m_acRespAddr, 16);
	output_cdr.write(m_acPasswd, 6);
	output_cdr.write(m_usFuncNo);
	output_cdr.write(m_uiExSerial);
	output_cdr.write(m_uiRetCode);
	output_cdr.write(m_uiDataLen);
	if(m_uiDataLen == 0 || m_uiDataLen > 64 * 1024)
		return -1;
	output_cdr.write(m_pDataBuf, m_uiDataLen);
	//End

	SetCRC();

	output_cdr.write(m_acCRC, 2);
	output_cdr.write(m_uiEnd);	

	return output_cdr.good_bit() ? 0 : -1;
}

void CPacket::Print(std::ostream &os) const
{
	//Header
	os << "\nStart = " << m_uiStart;
	os << "\nLength = " << m_uiLength;
	os << "\nOrdinal = " << m_uiOrdinal;
	os << "\nCheckSum = " << m_uiChecksum;
	//Body
	os << "\nVersion = " << (unsigned int)m_ucVersion;
	os << "\nPacketType = " << (unsigned int)m_ucPacketType;
	os << "\nReqAddr = ";
	Toolkit::PrintBinary(os, m_acReqAddr, 16);
	os << "\nRespAddr = ";
	Toolkit::PrintBinary(os, m_acRespAddr, 16);
	os << "\nPasswd = ";
	Toolkit::PrintBinary(os, m_acPasswd, 6);
	os << "\nFuncNo = " << m_usFuncNo;
	os << "\nExSerial = " << m_uiExSerial;
	os << "\nRetCode = " << m_uiRetCode;
	os << "\nDataLen = " << m_uiDataLen;
	os << "\nDataBuf = ";
	Toolkit::PrintBinary(os, m_pDataBuf, m_uiDataLen);
	//End
	os << "\nCRC = ";
	Toolkit::PrintBinary(os, m_acCRC, 2);
	os << "\nEnd = " << m_uiEnd;	
}

///
int CPacket::DataAlloc()
{
	m_pDataBuf = new char[m_uiDataLen];

	return 0;
}

void CPacket::DataFree()
{
	if(m_pDataBuf != NULL)
	{
		delete [] m_pDataBuf, m_pDataBuf = NULL;
		m_uiDataLen = 0;
	}
}

int CPacket::SetData(const char* data, unsigned int len)
{
	if(data == NULL || len == 0)
		return -1;

	DataFree();

	m_uiDataLen = len;
	DataAlloc();

	memcpy(m_pDataBuf, data, len);

	return 0;
}

int CPacket::AttachData(const char* data, unsigned int len)
{
	if(data == NULL || len == 0)
		return -1;

	DataFree();

	m_uiDataLen = len;
	m_pDataBuf = data;

	return 0;
}

int CPacket::CheckChecksum() const
{
	unsigned int checksum = ((m_uiLength << 19) + (m_uiLength >> 13)) ^ ((m_uiOrdinal << 25) + (m_uiOrdinal >> 7));
	return checksum == m_uiChecksum ? 0 : -1;
}

int CPacket::SetChecksum()
{
	m_uiChecksum = ((m_uiLength << 19) + (m_uiLength >> 13)) ^ ((m_uiOrdinal << 25) + (m_uiOrdinal >> 7));
	return 0;
}

int CPacket::CheckCRC() const
{
	return 0;
}

int CPacket::SetCRC()
{
	m_acCRC[0] = 0;
	m_acCRC[1] = 0;

	return 0;
}


}




