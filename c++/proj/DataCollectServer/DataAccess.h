/*
 * DataAccess.h
 *
 *  Created on: Jun 24, 2011
 *      Author: codejie
 */

#ifndef __DATAACCESS_H__
#define __DATAACCESS_H__

#include <iostream>
#include <string>

#include "occi.h"

class DataAccess
{
public:
	DataAccess();
	virtual ~DataAccess();

	int Init(const std::string& server, const std::string& user, const std::string& passwd);
	void Final();

	void Show(std::ostream& os) const;
private:
	int Connect();
	void Disconnect();

	int LoadDefColumn();

private:
    std::string _strServer;
    std::string _strUser;
    std::string _strPasswd;
private:
    oracle::occi::Environment* _env;
    oracle::occi::Connection* _conn;
    boolean _isconnected;
};

#endif /* __DATAACCESS_H__ */
