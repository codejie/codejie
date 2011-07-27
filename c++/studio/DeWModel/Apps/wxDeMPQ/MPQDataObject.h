#ifndef __MPQDATAOBJECT_H__
#define __MPQDATAOBJECT_H__


class CFileBuffer;

namespace MPQData
{

struct TFileData
{
	std::string m_strName;
    const char* m_pPointer;
    unsigned int m_uiVersion;
	unsigned int m_uiSize;
	unsigned int m_uiFlag;
	unsigned int m_uiIndex;
	unsigned int m_uiCompSize;
};

enum DataType { DT_DBC_HEADER = 0, DT_WDB_HEADER };

class CData
{
public:
	CData(DataType type);
	virtual ~CData();

	virtual size_t Size() const = 0;
	virtual int Read(CFileBuffer& buffer);
public:
	DataType m_eType;
};

int operator >> (CFileBuffer& buffer, CData& data);

class CDBCHeader : public CData
{
public:
	CDBCHeader();
	virtual ~CDBCHeader() {}

	virtual size_t Size() const;
	virtual int Read(CFileBuffer& buffer);
public:
	char m_acSignature[4];
	int m_iRecords;
	int m_iFields;
	int m_iRecordSize;
	int m_iBlockSize;
};

class CWDBHeader : public CData
{
public:
	CWDBHeader();
	virtual ~CWDBHeader() {}

	virtual size_t Size() const;
	virtual int Read(CFileBuffer& buffer);
public:
	char m_acSignature[4];
	int m_iClientVersion;
	char m_acLanguage[4];
	int m_iRowSize;
	int m_iUnkn1;
};

}

#endif