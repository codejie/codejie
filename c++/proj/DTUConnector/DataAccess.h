#ifndef __DATAACCESS_H__
#define __DATAACCESS_H__


#include <oledb.h>
#include <conio.h>
#include <icrsint.h>
#include <iostream>
#include <sstream>

#include <string>
#include <map>

#import "C:\Program Files\Common Files\System\ado\msado15.dll" rename("EOF", "ADOEOF")

class DataAccess
{
protected:
	typedef std::map<const std::string, const std::string> TStationMap;
public:
	DataAccess();
	virtual ~DataAccess();

	int Init(const std::string& server, const std::string& db, const std::string& user, const std::string& passwd);
	void Final();

	int SearchStation(const std::string& tel, std::string& station) const;
	int OnData(const std::string& tel, float value);
private:
	int Connect();
	void Disconnect();

	int LoadStation();
private:
    _bstr_t _strServer;
	_bstr_t _strDatabase;
    _bstr_t _strUser;
    _bstr_t _strPasswd;
private:
	ADODB::_ConnectionPtr _ptrConn;
private:
	TStationMap _mapStation;
};

#endif
