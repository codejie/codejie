#ifndef __FILEWRAPPER_H__
#define __FILEWRAPPER_H__

#include <windows.h>

#include <string>
#include <exception>
#include <fstream>

#include "DataTypes.h"

#include "FieldManager.h"


class CFileWrapperException : std::exception
{
public:
	CFileWrapperException(const std::string& msg)
		: std::exception(msg.c_str())
	{
	}
};

enum FileType { FT_DBC, FT_WDB };

class CFileWrapper
{
public:
	enum RetCode { RC_OK = 0, RC_FAIL = -1 };
public:
	CFileWrapper(FileType type)
		: m_eType(type),_mapField(NULL)
	{
	}
	virtual ~CFileWrapper()
	{
		CloseFile();
	}

	int InitFieldMap(const CDBFieldManager::TFieldMap* field);

	virtual int Query(TQueryData& query, TResultData& result) = 0;
protected:
	virtual int GetInt(int& data) = 0;
	virtual int GetFloat(float& data) = 0;
	virtual int GetString(std::string& data) = 0;
	virtual int GetByte(char& ch) = 0;
	virtual int GetBuffer(char* data, size_t size) = 0;
protected:
	virtual int ParseFile() = 0;
	virtual void CloseFile() {};// = 0;

	virtual int Seek(size_t offset) = 0;
	virtual int Skip(size_t offset) = 0;
public:
	FileType m_eType;
protected:
	const CDBFieldManager::TFieldMap* _mapField;
};

class CDBCFileWrapper : public CFileWrapper
{
public:
	CDBCFileWrapper();
	virtual ~CDBCFileWrapper();

	int LoadFile(const std::wstring& mpq, const std::string& dbc);
	virtual int Query(TQueryData& query, TResultData& result);
protected:
	virtual int GetInt(int& data);
	virtual int GetFloat(float& data);
	virtual int GetString(std::string& data);
	virtual int GetByte(char& ch);
	virtual int GetBuffer(char* data, size_t size);
protected:
	virtual int ParseFile();
	virtual void CloseFile();

	virtual int Seek(size_t offset);
	virtual int Skip(size_t offset);
private:
	int ReadHeader(DBCHeader_t& header);
	int Check(const TQueryData &query, TResultData &result) const;
public:
	std::wstring m_strMPQFile;
	std::string m_strDBCFile;
private:
	HANDLE _hMPQ;
	HANDLE _hDBC;
private:
	DBCHeader_t _stHeader;
};


class CWDBFileWrapper : public CFileWrapper
{
public:
	CWDBFileWrapper();
	virtual ~CWDBFileWrapper();

	int LoadFile(const std::wstring& wdb);

	virtual int Query(TQueryData& query, TResultData& result);
protected:
	virtual int GetInt(int& data);
	virtual int GetFloat(float& data);
	virtual int GetString(std::string& data);
	virtual int GetByte(char& data);
	virtual int GetBuffer(char* data, size_t size);
protected:
	virtual int ParseFile();
	virtual void CloseFile();

	virtual int Seek(size_t offset);
	virtual int Skip(size_t offset);
	int SkipCString();
private:
	int ReadHeader(WDBHeader_t& header);
	int Check(const TQueryData &query, TResultData &result) const;
public:
	std::wstring m_strWDBFile;
private:
	std::ifstream _stIFStream;
	WDBHeader_t _stHeader;

};

#endif
