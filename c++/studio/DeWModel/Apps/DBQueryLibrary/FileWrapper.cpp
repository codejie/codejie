#include "stdafx.h"

#include <malloc.h>

#include "StormLib.h"

#include "Toolkit.h"
#include "FileWrapper.h"


int CFileWrapper::InitFieldMap(const CDBFieldManager::TFieldMap *field)
{
	if(field == NULL)
		return -1;
	_mapField = field;

	return 0;
}

///////////////////////////////////////////////////////////////////////////

CDBCFileWrapper::CDBCFileWrapper()
: CFileWrapper(FT_DBC)
, _hMPQ(NULL), _hDBC(NULL)
{
}

CDBCFileWrapper::~CDBCFileWrapper()
{
	CloseFile();
}

int CDBCFileWrapper::LoadFile(const std::wstring &mpq, const std::string &dbc)
{
	CloseFile();

	m_strMPQFile = mpq;
	m_strDBCFile = dbc;

	if(SFileOpenArchive((Toolkit::WString2String(m_strMPQFile)).c_str(), 0, 0, &_hMPQ) == FALSE)
		return -1;
	if(SFileOpenFileEx(_hMPQ, m_strDBCFile.c_str(), 0, &_hDBC) == FALSE)
		return -1;

	return ParseFile();
}

void CDBCFileWrapper::CloseFile()
{
	if(_hDBC != NULL)
	{
		SFileCloseFile(_hDBC);
		_hDBC = NULL;
	}
	if(_hMPQ != NULL)
	{
		SFileCloseArchive(_hMPQ);
		_hMPQ = NULL;
	}
}

int CDBCFileWrapper::ParseFile()
{
	if(_hMPQ == NULL || _hDBC == NULL)
		return -1;

	if(ReadHeader(_stHeader) != 0)
		return -1;
	return 0;
}

int CDBCFileWrapper::ReadHeader(DBCHeader_t &header)
{
	Seek(0);
	if(GetBuffer(header.m_acSignature, sizeof(header.m_acSignature)) != 0)
		return -1;
	if(GetInt(header.m_iRecords) != 0)
		return -1;
	if(GetInt(header.m_iFields) != 0)
		return -1;
	if(GetInt(header.m_iRecordSize) != 0)
		return -1;
	if(GetInt(header.m_iBlockSize) != 0)
		return -1;

	return 0;
}

int CDBCFileWrapper::Check(const TQueryData &query, TResultData &result) const
{
	if(_mapField == NULL)
		return -1;
	if(query.m_iPos > _stHeader.m_iFields)
		return -1;
	if(result.m_iPos > _stHeader.m_iFields)
		return -1;
	return 0;
}

int CDBCFileWrapper::Query(TQueryData &query, TResultData &result)
{
	if(Check(query, result) != 0)
		return -1;
	
	size_t qoffset = 0, roffset = 0;
	size_t tmp = 0;
	for(int i = 0; i <= ((query.m_iPos < result.m_iPos) ? result.m_iPos : query.m_iPos); ++ i)
	{
		CDBFieldManager::TFieldMap::const_iterator it = _mapField->find(i);
		if(it != _mapField->end())
			tmp = it->second.m_iSize;
		else
			tmp = 4;

		if(i < query.m_iPos)
		{
			qoffset += tmp;
		}

		if(i < result.m_iPos)
		{
			roffset += tmp;
		}
		if(i == query.m_iPos)
			query.m_eFieldType = it->second.m_eType;
		if(i == result.m_iPos)
			result.m_eFieldType = it->second.m_eType;
	}

	if(query.m_eFieldType == FT_UNKNOWN)
		query.m_eFieldType = FT_INTEGER;

	if(result.m_eFieldType == FT_UNKNOWN)
		result.m_eFieldType = FT_INTEGER;

	size_t offset = sizeof(_stHeader);
	for(int i = 0; i < _stHeader.m_iRecords; ++ i)
	{
		if(i > 0)
			offset += _stHeader.m_iRecordSize;
		//query
		if(Seek(offset + qoffset) != 0)
			return -1;
		if(query.m_eFieldType == FT_STRING)
		{
			std::string data;
			if(GetString(data) != 0)
				return -1;
			if(data != query.m_strValue)
				continue;
		}
		else if(query.m_eFieldType == FT_FLOAT)
		{
			float data;
			if(GetFloat(data) != 0)
				return -1;
			if(data != query.m_fValue)
				continue;
		}
		else if(query.m_eFieldType == FT_BYTE)
		{
			char data;
			if(GetByte(data) != 0)
				return -1;
			if(data != query.m_iValue)
				continue;
		}
		else //integer
		{
			int data;
			if(GetInt(data) != 0)
				return -1;
			if(data != query.m_iValue)
				continue;
		}
		//result
		if(Seek(offset + roffset) != 0)
			return -1;
		if(result.m_eFieldType == FT_STRING)
		{
			if(GetString(result.m_strValue) != 0)
				return -1;
		}
		else if(result.m_eFieldType == FT_FLOAT)
		{
			if(GetFloat(result.m_fValue) != 0)
				return -1;
		}
		else if(result.m_eFieldType == FT_BYTE)
		{
			char data;
			if(GetByte(data) != 0)
				return -1;
			result.m_iValue = data;
		}
		else //integer
		{
			if(GetInt(result.m_iValue) != 0)
				return -1;
		}
		return 0;
	}
	return -1;
}


int CDBCFileWrapper::Seek(size_t offset)
{
	long up = 0;
	if(SFileSetFilePointer(_hDBC, offset, &up, FILE_BEGIN) == -1)
		return -1;
	return 0;
}

int CDBCFileWrapper::Skip(size_t offset)
{
	long up = 0;
	if(SFileSetFilePointer(_hDBC, offset, &up, FILE_CURRENT) == -1)
		return -1;
	return 0;
};

int CDBCFileWrapper::GetInt(int& data)
{
	DWORD out = 0;
	if(SFileReadFile(_hDBC, &data, sizeof(data), &out, NULL) == FALSE)
		return -1;
	//i = ntohl(i);
	return 0;	
}

int CDBCFileWrapper::GetFloat(float& data)
{
	DWORD out = 0;
	if(SFileReadFile(_hDBC, &data, sizeof(data), &out, NULL) == FALSE)
		return -1;
	return 0;
}

int CDBCFileWrapper::GetString(std::string& data)
{
	DWORD out = 0;
	int off  = -1;
	if(SFileReadFile(_hDBC, &off, sizeof(off), &out, NULL) == FALSE)
		return -1;

	int strpos = sizeof(_stHeader) + _stHeader.m_iRecords * _stHeader.m_iRecordSize;

	long up = 0;
	if(SFileSetFilePointer(_hDBC, off + strpos, &up, FILE_BEGIN) == -1)
		return -1;

	char ch;
	while(1)
	{
		if(SFileReadFile(_hDBC, &ch, sizeof(ch), &out, NULL) == FALSE)
			return -1;
		if(ch != '\0')
			data += ch;
		else
			break;
	}

	return 0;
}

int CDBCFileWrapper::GetByte(char& data)
{
	DWORD out = 0;
	if(SFileReadFile(_hDBC, &data, sizeof(data), &out, NULL) == FALSE)
		return -1;
	return 0;
}

int CDBCFileWrapper::GetBuffer(char *data, size_t size)
{
	DWORD out = 0;
	if(SFileReadFile(_hDBC, data, size, &out, NULL) == FALSE)
		return -1;

	return 0;
}

///////////////////////////////////////////////////////////////////
CWDBFileWrapper::CWDBFileWrapper()
: CFileWrapper(FT_WDB)
{
}

CWDBFileWrapper::~CWDBFileWrapper()
{
	CloseFile();
}

int CWDBFileWrapper::LoadFile(const std::wstring &wdb)
{
	CloseFile();

	m_strWDBFile = wdb;

	_stIFStream.open((Toolkit::WString2String(m_strWDBFile)).c_str(), std::ios::in | std::ios::binary);
	if(!_stIFStream.is_open())
		return -1;

	return ParseFile();
}

void CWDBFileWrapper::CloseFile()
{
	if(_stIFStream.is_open())
		_stIFStream.close();
}

int CWDBFileWrapper::ParseFile()
{
	if(!_stIFStream.good())
		return -1;
	return ReadHeader(_stHeader);
}

int CWDBFileWrapper::ReadHeader(WDBHeader_t &header)
{
	Seek(0);
	if(GetBuffer(header.m_acSignature, sizeof(header.m_acSignature)) != 0)
		return -1;
	if(GetInt(header.m_iClientVersion) != 0)
		return -1;
	if(GetBuffer(header.m_acLanguage, sizeof(header.m_acLanguage)) != 0)
		return -1;
	if(GetInt(header.m_iRowSize) != 0)
		return -1;
	if(GetInt(header.m_iUnkn1) != 0)
		return -1;
	return 0;
}

int CWDBFileWrapper::Check(const TQueryData &query, TResultData &result) const
{
	if(_mapField == NULL)
		return -1;

	if(query.m_iPos >= _mapField->size())
		return -1;
	if(query.m_iPos >= _mapField->size())
		return -1;

	return 0;
}

int CWDBFileWrapper::Query(TQueryData &query, TResultData &result)
{
	if(Check(query, result) != 0)
		return -1;

	//CDBFieldManager::TFieldMap::const_iterator it = _mapField->find(query.m_iPos);
	//if(it == _mapField->end())
	//	return -1;
	//query.m_eFieldType = it->second.m_eType;

	//it = _mapField->find(result.m_iPos);
	//if(it == _mapField->end())
	//	return -1;
	//result.m_eFieldType = it->second.m_eType;
	
	size_t offset = sizeof(WDBHeader_t);
	int id = 0;
	int size = -8;

	while(_stIFStream.good())
	{
		offset += (size + 8);

		//query
		if(Seek(offset) != 0)
			return -1;

		if(GetInt(id) != 0)
			return -1;
		if(GetInt(size) != 0)
			return -1;

		if(query.m_iPos == 0)
		{
			if(id != query.m_iValue)
				continue;
			query.m_eFieldType = FT_INTEGER;
		}
		else if(query.m_iPos == 1)
		{
			if(size != query.m_iValue)
				continue;
			query.m_eFieldType = FT_INTEGER;
		}
		else
		{
			size_t rec = 2;
			CDBFieldManager::TFieldMap::const_iterator it = _mapField->begin();
			++ it;
			++ it;
			while(rec < query.m_iPos)
			{
				if(it->second.m_eType == FT_CSTRING)
				{
					SkipCString();
				}
				else if(it->second.m_eType == FT_AMOUNT)
				{
					Skip(it->second.m_iSize);
					Skip(it->second.m_iSkipByte);
				}
				else
				{
					Skip(it->second.m_iSize);
				}
				++ rec;
				++ it;
			}
			query.m_eFieldType = it->second.m_eType;

			if(query.m_eFieldType == FT_INTEGER)
			{
				int data;
				if(GetInt(data) != 0)
					return -1;
				if(data != query.m_iValue)
					continue;
			}
			else if(query.m_eFieldType == FT_CSTRING)
			{
				std::string data;
				if(GetString(data) != 0)
					return -1;
				if(data != query.m_strValue)
					continue;
			}
			else if(query.m_eFieldType == FT_FLOAT)
			{
				float data;
				if(GetFloat(data) != 0)
					return -1;
				if(data != query.m_fValue)
					continue;
			}
			else if(query.m_eFieldType == FT_BYTE)
			{
				char data;
				if(GetByte(data) != 0)
					return -1;
				if(data != query.m_iValue)
					continue;
			}
			else //integer
			{
				return -1;
			}
		}

		//result
		if(Seek(offset + sizeof(int) * 2) != 0)
			return -1;
		if(result.m_iPos == 0)
		{
			result.m_iValue = id;
			result.m_eFieldType = FT_INTEGER;
		}
		else if(query.m_iPos == 1)
		{
			result.m_iValue = size;
			result.m_eFieldType = FT_INTEGER;
		}
		else
		{
			size_t rec = 2;
			CDBFieldManager::TFieldMap::const_iterator it = _mapField->begin();
			++ it;
			++ it;
			while(rec < result.m_iPos)
			{
				if(it->second.m_eType == FT_CSTRING)
				{
					SkipCString();
				}
				else if(it->second.m_eType == FT_AMOUNT)
				{
					Skip(it->second.m_iSize);
					Skip(it->second.m_iSkipByte);
				}
				else
				{
					Skip(it->second.m_iSize);
				}
				++ rec;
				++ it;
			}
			result.m_eFieldType = it->second.m_eType;

			if(result.m_eFieldType == FT_INTEGER)
			{
				if(GetInt(result.m_iValue) != 0)
					return -1;
			}
			else if(result.m_eFieldType == FT_CSTRING)
			{
				if(GetString(result.m_strValue) != 0)
					return -1;
			}
			else if(result.m_eFieldType == FT_FLOAT)
			{
				if(GetFloat(result.m_fValue) != 0)
					return -1;
			}
			else if(result.m_eFieldType == FT_BYTE)
			{
				char data;
				if(GetByte(data) != 0)
					return -1;
				result.m_iValue = data;
			}
			else 
			{
				return -1;
			}
		}
		return 0;
	}

	return -1;
}

int CWDBFileWrapper::Seek(size_t offset)
{
	_stIFStream.seekg(offset, std::ios_base::beg);
	if(!_stIFStream.good())
		return -1;
	return 0;
}

int CWDBFileWrapper::Skip(size_t offset)
{
	_stIFStream.seekg(offset, std::ios_base::cur);
	if(!_stIFStream.good())
		return -1;
	return 0;
}

int CWDBFileWrapper::SkipCString()
{
	char ch;
	while(_stIFStream.good())
	{
		_stIFStream.read(&ch, sizeof(ch));
		if(ch == '\0')
			return 0;
	}
	return -1;
}

int CWDBFileWrapper::GetInt(int &data)
{
	_stIFStream.read((char*)&data, sizeof(data));
	if(!_stIFStream.good())
		return -1;
	return 0;
}

int CWDBFileWrapper::GetFloat(float& data)
{
	_stIFStream.read((char*)&data, sizeof(data));
	if(!_stIFStream.good())
		return -1;
	return 0;
}

int CWDBFileWrapper::GetByte(char& data)
{
	_stIFStream.read(&data, sizeof(data));
	if(!_stIFStream.good())
		return -1;
	return 0;
}

int CWDBFileWrapper::GetString(std::string& data)
{
	char ch;
	while(_stIFStream.good())
	{
		_stIFStream.read(&ch, sizeof(ch));
		if(!_stIFStream.good())
			return -1;
		if(ch != '\0')
			data += ch;//.insert(str.size(), ch, 1);
		else
			return 0;
	}
	return -1;
}

int CWDBFileWrapper::GetBuffer(char *data, size_t size)
{
	_stIFStream.read(data, size);
	if(!_stIFStream.good())
		return -1;
	return 0;
}