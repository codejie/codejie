#ifndef __DBQUERYOBJECT_H__
#define __DBQUERYOBJECT_H__

#ifdef DBQUERYLIBRARY_EXPORTS
#define DBQUERYLIBRARY_API __declspec(dllexport)
#else
#define DBQUERYLIBRARY_API __declspec(dllimport)
#endif

#include <string>

#include "DataTypes.h"
#include "FieldManager.h"

class DBQUERYLIBRARY_API CDBQueryObject
{
public:
	CDBQueryObject();
	virtual ~CDBQueryObject();

	int InitConfig(const std::wstring& config);

	int QueryDBC(const std::wstring& mpq, const std::string& dbc, TQueryData& query, TResultData& result) const;
	int QueryWDB(const std::wstring& wdb, TQueryData& query, TResultData& result) const;
private:
	CDBFieldManager _stFieldManager;
};


#endif
