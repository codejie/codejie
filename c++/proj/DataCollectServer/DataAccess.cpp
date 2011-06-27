/*
 * DataAccess.cpp
 *
 *  Created on: Jun 24, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"
#include "acex/Ini_Configuration.h"

#include "DataAccess.h"


const std::string DataAccess::DEF_INFECTANTCOLUMN_CONFIGFILE    =   "InfectantColumn.ini";

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
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Database environment init exception - " << e.what() << std::endl);
		return -1;
	}

    if(Connect() != 0)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Connect Database server failed." << std::endl);
        return -1;
    }
    return LoadDefColumn();
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
    if(LoadDefColumnFromDB() != 0)
        return -1;
    if(LoadDefColumnFromConfig() != 0)
        return -1;
    return 0;
}

int DataAccess::LoadDefColumnFromDB()
{
    if(_isconnected != true)
        return -1;

    try
    {
        const std::string sql = "select station_id, infectant_id, infectant_column from t_cfg_monitor_param";

        oracle::occi::Statement *stmt = _conn->createStatement(sql);
        oracle::occi::ResultSet *rset = stmt->executeQuery();

        while(rset->next())
        {
            if(!_mapStationInfectant.insert(std::make_pair(std::make_pair(rset->getString(1), rset->getString(2)), rset->getString(3))).second)
            {
                ACEX_LOG_OS(LM_ERROR, "<LoadDefColumnFromDB>Load Infectant column failed - Station:" << rset->getString(1) << " Infectant:" << rset->getString(2) << std::endl);
                return -1;
            }
        }
    }
    catch(oracle::occi::SQLException& e)
    {
        ACEX_LOG_OS(LM_ERROR, "<LoadDefColumnFromDB>Load Infectant column exception - " << e.what() << std::endl);
        return -1;
    }

	return 0;
}

int DataAccess::LoadDefColumnFromConfig()
{
    ACEX_Ini_Configuration ini;
    if(ini.open(DEF_INFECTANTCOLUMN_CONFIGFILE) != 0)
    {
        ACEX_LOG_OS(LM_INFO, "<>Default Infectant configuration file does not find or opens failed." << std::endl);
        return 0;
    }

    ACE_Configuration_Section_Key key;
    ACE_TString tmp, st, inf, col;
    int index = 0;
    //Station
    if(ini.open_section(ini.root_section(), "Station", 0, key) != 0)
    	return -1;

    index = 0;
    while(ini.enumerate_sections(key, index, tmp) != 0)
    {
    	ACE_Configuration_Section_Key k;
    	if(ini.open_section(key, tmp.c_str(), 0, k) != 0)
    		return -1;

       	if(ini.get_string_value(k, "Station", st) != 0)
        		return -1;
       	if(ini.get_string_value(k, "Infectant", inf) != 0)
    		return -1;
    	if(ini.get_string_value(k, "Column", col) != 0)
    		return -1;

    	if(!_mapStationInfectant.insert(std::make_pair(std::make_pair(st.c_str(), inf.c_str()), col.c_str())).second)
    		return -1;

    	++ index;
    }
    //Infectant
    if(ini.open_section(ini.root_section(), "Infectant", 0, key) != 0)
    	return -1;

    index = 0;
    while(ini.enumerate_sections(key, index, tmp) != 0)
    {
    	ACE_Configuration_Section_Key k;
    	if(ini.open_section(key, tmp.c_str(), 0, k) != 0)
    		return -1;

    	if(ini.get_string_value(k, "Infectant", inf) != 0)
    		return -1;
    	if(ini.get_string_value(k, "Column", col) != 0)
    		return -1;

    	if(!_mapInfectant.insert(std::make_pair(inf.c_str(), col.c_str())).second)
    		return -1;

    	++ index;
    }


    
    return 0;
}

int DataAccess::SearchColumn(const std::string& station, const std::string& infectant, std::string& column) const
{
	TStationInfectantMap::const_iterator it = _mapStationInfectant.find(std::make_pair(station, infectant));
	if(it != _mapStationInfectant.end())
	{
		column = it->second;
		return 0;
	}

	TInfectantMap::const_iterator i = _mapInfectant.find(infectant);
	if(i != _mapInfectant.end())
	{
		column = i->second;
		return 0;
	}
	return -1;
}



