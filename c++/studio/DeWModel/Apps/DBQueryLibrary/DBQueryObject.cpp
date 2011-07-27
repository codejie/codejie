#include "stdafx.h"

#include "Toolkit.h"

#include "FileWrapper.h"
#include "DBQueryObject.h"

CDBQueryObject::CDBQueryObject()
{
}

CDBQueryObject::~CDBQueryObject()
{
}

int CDBQueryObject::InitConfig(const std::wstring &config)
{
	return _stFieldManager.Load(config);
}

int CDBQueryObject::QueryDBC(const std::wstring &mpq, const std::string &dbc, TQueryData &query, TResultData &result) const
{
	std::string str = dbc;
	std::string::size_type pos = str.find("\\");
	if(pos != std::string::npos)
		str = str.substr(pos + 1);

	const CDBFieldManager::TFieldMap* mapfield = _stFieldManager.FindFieldMap(str);
	if(mapfield == NULL)
		return -1;

	CDBCFileWrapper wrapper;
	if(wrapper.LoadFile(mpq, dbc) != 0)
		return -1;
	if(wrapper.InitFieldMap(mapfield) != 0)
		return -1;
	return wrapper.Query(query, result);
}

int CDBQueryObject::QueryWDB(const std::wstring &wdb, TQueryData &query, TResultData &result) const
{
	std::string str = Toolkit::WString2String(wdb);
	std::string::size_type pos = str.find_last_of("\\");
	if(pos != std::string::npos)
		str = str.substr(pos + 1);

	const CDBFieldManager::TFieldMap* mapfield = _stFieldManager.FindFieldMap(str);
	if(mapfield == NULL)
		return -1;

	CWDBFileWrapper wrapper;
	if(wrapper.LoadFile(wdb) != 0)
		return -1;
	if(wrapper.InitFieldMap(mapfield) != 0)
		return -1;
	return wrapper.Query(query, result);

}