#ifndef __DATATYPES_H__
#define __DATATYPES_H__

#ifdef DBQUERYLIBRARY_EXPORTS
#define DBQUERYLIBRARY_API __declspec(dllexport)
#else
#define DBQUERYLIBRARY_API __declspec(dllimport)
#endif

#include <string>

enum DBQUERYLIBRARY_API FieldType { FT_UNKNOWN = -1, FT_INTEGER = 0, FT_STRING, FT_FLOAT, FT_BIT, FT_BYTE, FT_CSTRING, FT_AMOUNT };

struct DBParam_t
{
	int m_iPos;
	FieldType m_eFieldType;
	int m_iValue;
	std::string m_strValue;
	float m_fValue;
};

typedef DBParam_t DBQUERYLIBRARY_API TQueryData;
typedef DBParam_t DBQUERYLIBRARY_API TResultData;


struct DBCHeader_t 
{
	char m_acSignature[4];
	int m_iRecords;
	int m_iFields;
	int m_iRecordSize;
	int m_iBlockSize;
};

struct WDBHeader_t
{
	char m_acSignature[4];
	int m_iClientVersion;
	char m_acLanguage[4];
	int m_iRowSize;
	int m_iUnkn1;
};

#endif
