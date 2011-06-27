/*
 * DataAccess.cpp
 *
 *  Created on: Jun 24, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"

#include "DataAccess.h"

DataAccess::DataAccess()
: _env(NULL)
, _conn(NULL)
, _isconnected(false)
{
}

DataAccess::~DataAccess()
{
	Final();
}

int DataAccess::Init(const std::string& server, const std::string& user, const std::string& passwd)
{
	try
	{
		_env = oracle::occi::Environment::createEnvironment();
		if(_env == NULL)
			return -1;
		_strServer = server;
		_strUser = user;
		_strPasswd = passwd;
	}
	catch(oracle::occi::SQLException& e)
	{
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Database environment init failed - " << e.what() << std::endl);
		return -1;
	}
	return 0;
}

void DataAccess::Final()
{
    if(_env != NULL && _conn != NULL)
    {
    	Disconnect();
        oracle::occi::Environment::terminateEnvironment(_env);
        _env = NULL;
    }
}

int DataAccess::Connect()
{
	if(_isconnected == true)
	{
		ACEX_LOG_OS(LM_WARNING, "<DataAccess::Connect>Database has connected." << std::endl);
		return 0;
	}
	if(_env == NULL)
		return -1;
	if(_conn != NULL)
		Disconnect();
	try
	{
		_conn = _env->createConnection(_strUser, _strPasswd, _strServer);
		if(_conn == NULL)
			return -1;
		_isconnected = true;
	}
	catch(oracle::occi::SQLException& e)
	{
		ACEX_LOG_OS(LM_ERROR, "<>Database init connection failed." << std::endl);
		return -1;
	}
	return 0;
}

void DataAccess::Disconnect()
{
	if(_env != NULL && _conn != NULL)
	{
		_env->terminateConnection(_conn);
		_conn = NULL;
		_isconnected = false;
	}
}

int DataAccess::LoadDefColumn()
{
	return -1;
}

int DataAccess::searchColumn(const std::string& station, const std::string& infectant, std::string& column) const
{
	TStationInfectantMap::const_iterator it = _mapStationInfectant.find(std::make_pair(station infectant));
	if(it != _mapStationInfectant.end())
	{
		column = it->second;
		return 0;
	}

	TInfectantMap::const_iterator i = _mapInfectant.find(infectant);
	if(it != _mapInfectant.end())
	{
		column = i->second;
		return 0;
	}
	return -1;
}



