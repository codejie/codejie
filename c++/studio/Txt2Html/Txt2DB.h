#ifndef __TXT2DB_H__
#define __TXT2DB_H__

#include <wx/String.h>

#include "wx/wxsqlite3.h"

#include "TxtTidy.h"

class Txt2DB
{
protected:
    typedef wxSQLite3Database TDatabase;
    typedef wxSQLite3Statement TQuery;
    typedef wxSQLite3ResultSet TResult;
    typedef wxSQLite3Exception TException;
public:
	Txt2DB() {}
	virtual ~Txt2DB() {}

	int Create(const wxString& file);
	int Push(const TxtTidy::TData& data);
	void Close();
private:
	TDatabase _db;
};

#endif
