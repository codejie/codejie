#ifndef __MPQDBCFIELDOBJECT_H__
#define __MPQDBCFIELDOBJECT_H__

#include <string>
#include <map>

#include "FileBuffer.h"
#include "DBCFieldDialog.h"

namespace DBCField
{
	struct FieldAttr_t;
	class CField;
}

class CMPQDBCFieldManager
{
public:
	typedef std::map<int, DBCField::CField*> TFieldMap;
	struct DBCField_t
	{
		bool m_bValid;
		std::string m_strNote;
		TFieldMap m_mapField;
	};
	typedef std::map<std::string, DBCField_t> TDBCMap;

public:
	CMPQDBCFieldManager();
	virtual ~CMPQDBCFieldManager();

	int Load(const std::string& xml, bool reload = false);
	const TFieldMap* FindDBCFields(const std::string& dbc) const;
	const TFieldMap* FindDBCFields(const std::string& dbc, bool& valid) const;
	const TFieldMap* FindDBCFields(const std::string& dbc, bool& valid, std::string& notes) const;

	int SetValid(const std::string& dbc, bool valid);
private:
	int ParseXML(const std::string& xml);
	DBCField::CField* MakeDBCField(const DBCField::FieldAttr_t& attr) const;
	void Destory();
private:
	friend class CDBCFieldDialog;
	TDBCMap _mapDBC;
};
                               

namespace DBCField
{

enum FieldType { FT_INTEGER, FT_STRING, FT_FLOAT, FT_BIT, FT_BYTE, FT_CSTRING, FT_AMOUNT };

struct FieldAttr_t
{
	FieldAttr_t()
		: m_strTitle(""), m_iPos(-1), m_strType(""), m_iSize(0), m_iSkipByte(0)
	{
	}
	FieldAttr_t(const FieldAttr_t& attr)
		: m_strTitle(attr.m_strTitle), m_iPos(attr.m_iPos), m_strType(attr.m_strType), m_iSize(attr.m_iSize), m_iSkipByte(attr.m_iSkipByte)
	{
	}

	std::string m_strTitle;
	int m_iPos;
	std::string m_strType;
	int m_iSize;
	int m_iSkipByte;
};

class CField
{
public:
	CField(FieldType type, const FieldAttr_t& attr)
		: m_eType(type), m_stAttr(attr)
	{
		m_stAttr.m_iSize = 4;
	}
	virtual ~CField() {}

	virtual int Data2String(std::string& str, CFileBuffer& fb, int offset, int strpos = -1) const;
	static int DefaultData2String(std::string& str, CFileBuffer& fb, int offset);
	virtual int GetIntData(int& data, std::string& str, CFileBuffer& fb, int offset, int strpos = -1) const;
public:
	FieldType m_eType;
	FieldAttr_t m_stAttr;
};

class CIntegerField : public CField
{
public:
	CIntegerField(const FieldAttr_t& attr)
		: CField(FT_INTEGER, attr)
	{
	}
};

class CStringField : public CField
{
public:
	CStringField(const FieldAttr_t& attr)
		: CField(FT_STRING, attr)
	{
	}
	virtual int Data2String(std::string& str, CFileBuffer& fb, int offset, int strpos = -1) const;
};

class CFloatField : public CField
{
public:
	CFloatField(const FieldAttr_t& attr)
		: CField(FT_FLOAT, attr)
	{
	}
	virtual int Data2String(std::string& str, CFileBuffer& fb, int offset, int strpos = -1) const;
};

class CBitField : public CField
{
public:
	CBitField(const FieldAttr_t& attr)
		: CField(FT_BIT, attr)
	{
	}
	virtual int Data2String(std::string& str, CFileBuffer& fb, int offset, int strpos = -1) const;
};

class CByteField : public CField
{
public:
	CByteField(const FieldAttr_t& attr)
		: CField(FT_BYTE, attr)
	{
		m_stAttr.m_iSize = 1;
	}
	virtual int Data2String(std::string& str, CFileBuffer& fb, int offset, int strpos = -1) const;
};

class CCStringField : public CField
{
public:
	CCStringField(const FieldAttr_t& attr)
		: CField(FT_CSTRING, attr)
	{
		m_stAttr.m_iSize = 0;
	}
	virtual int Data2String(std::string& str, CFileBuffer& fb, int offset, int strpos = -1) const;
};

class CAmountField : public CField
{
public:
	CAmountField(const FieldAttr_t& attr)
		: CField(FT_AMOUNT, attr)
	{
	}
};

}
#endif