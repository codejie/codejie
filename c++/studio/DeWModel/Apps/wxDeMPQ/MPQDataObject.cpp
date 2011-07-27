#include "FileBuffer.h"

#include "MPQDataObject.h"

namespace MPQData
{

CData::CData(MPQData::DataType type)
: m_eType(type)
{
}

CData::~CData()
{
}

int CData::Read(CFileBuffer& buffer)
{
	return buffer.Good() ? 0 : -1;
};
////

int operator >> (CFileBuffer& buffer, CData& data)
{
	return data.Read(buffer);
}

////
CDBCHeader::CDBCHeader()
: CData(DT_DBC_HEADER)
{
	memset(m_acSignature, 0, 4);
}

size_t CDBCHeader::Size() const
{
	return 20;
}

int CDBCHeader::Read(CFileBuffer &buffer)
{
	buffer.Read(m_acSignature, 4);
	buffer.Read(m_iRecords);
	buffer.Read(m_iFields);
	buffer.Read(m_iRecordSize);
	buffer.Read(m_iBlockSize);

	return CData::Read(buffer);
}

////
CWDBHeader::CWDBHeader()
: CData(DT_WDB_HEADER)
{
}

size_t CWDBHeader::Size() const
{
	return 20;
}

int CWDBHeader::Read(CFileBuffer &buffer)
{
	buffer.Read(m_acSignature, 4);
	buffer.Read(m_iClientVersion);
	buffer.Read(m_acLanguage, 4);
	buffer.Read(m_iRowSize);
	buffer.Read(m_iUnkn1);

	return CData::Read(buffer);
}

}