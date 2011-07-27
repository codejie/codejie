#ifndef __FIELDMANAGER_H__
#define __FIELDMANAGER_H__

#include <map>

#include "DataTypes.h"

//enum FieldType { FT_UNKNOWN = -1, FT_INTEGER = 0, FT_STRING, FT_FLOAT, FT_BIT, FT_BYTE, FT_CSTRING, FT_AMOUNT };

struct FieldAttr_t
{
	FieldAttr_t()
		: m_strTitle(""), m_iPos(-1), m_eType(FT_UNKNOWN), m_iSize(0), m_iSkipByte(0)
	{
	}
	FieldAttr_t(const FieldAttr_t& attr)
		: m_strTitle(attr.m_strTitle), m_iPos(attr.m_iPos), m_eType(attr.m_eType), m_iSize(attr.m_iSize), m_iSkipByte(attr.m_iSkipByte)
	{
	}

	std::string m_strTitle;
	int m_iPos;
	FieldType m_eType;
	int m_iSize;
	int m_iSkipByte;
};

class CDBFieldManager
{
public:
	typedef std::map<int, FieldAttr_t> TFieldMap;
	struct DBFile_t
	{
//		bool m_bValid;
		std::string m_strNote;
		TFieldMap m_mapField;
	};
	typedef std::map<std::string, DBFile_t> TDBMap;
public:
	CDBFieldManager();
	virtual ~CDBFieldManager();

	int Load(const std::wstring& xml, bool reload = false);
	const TFieldMap* FindFieldMap(const std::string& dbc) const;
private:
	int ParseConfig(const std::wstring& xml);
private:
	TDBMap _mapDB;
};

#endif
