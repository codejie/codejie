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
        _env->terminateConnection(_conn);
        _conn = NULL;
        oracle::occi::Environment::terminateEnvironment(_env);
        _env = NULL;
    }
}
