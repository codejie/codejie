/*
 *  ConfigLoader.cpp
 *  MonitorDataPostServer
 *
 *  Created by  Jie on 3/4/11.
 *  Copyright 2011 __MyCompanyName__. All rights reserved.
 *
 */

#include "acex/ACEX.h"
#include "acex/Ini_Configuration.h"

#include "DataLoader.h"
#include "TerminalManager.h"

#include "ConfigLoader.h"

ConfigLoader theConfig;

ConfigLoader::ConfigLoader()
: _iDataLoadPeriod(60)
, _iLogSize(102400)
{
}

ConfigLoader::~ConfigLoader()
{
}

int ConfigLoader::Load(const std::string& filename, DataLoader& loader, TerminalManager& manager)
{
	ACEX_Ini_Configuration ini;
	if(ini.open(filename) != 0)
		return -1;
	
	ACE_Configuration_Section_Key root =  ini.root_section();
	
	ACE_Configuration_Section_Key key;
	ACE_TString tmp;
	
	if(ini.open_section(root, "System", 0, key) != 0)
		return -1;
	
	if(ini.get_string_value(key, "ServiceID", tmp) !=0)
		return -1;
	_strServiceID = tmp.c_str();
	if(_strServiceID.empty())
		return -1;

	if(ini.get_string_value(key, "ServiceAddr", tmp) !=0)
		return -1;
	_strServiceAddr = tmp.c_str();
	if(_strServiceAddr.empty())
		return -1;
	
	if(ini.get_string_value(key, "DataLoadPeriod", tmp) != 0)
		return -1;
	_iDataLoadPeriod = ACE_OS::atoi(tmp.c_str());
	
	if(ini.get_string_value(key, "LogFile", tmp) != 0)
		return -1;
	_strLogFile = tmp.c_str();
	
	if(ini.get_string_value(key, "LogSize", tmp) != 0)
		return -1;
	_iLogSize = ACE_OS::atoi(tmp.c_str());

	if(ini.get_string_value(key, "MMLPort", tmp) != 0)
		return -1;
	_strMMLAddr = tmp.c_str();
	
	ACE_TString tmp1, tmp2;
	
	if(ini.open_section(root, "Database", 0, key) != 0)
		return -1;
	
	if(ini.get_string_value(key, "Server", tmp) != 0)
		return -1;
	if(ini.get_string_value(key, "User", tmp1) != 0)
		return -1;
	if(ini.get_string_value(key, "Passwd", tmp2) != 0)
		return -1;
	if(loader.SetDBConfig(tmp.c_str(), tmp1.c_str(), tmp2.c_str()) != 0)
		return -1;
	
	if(ini.open_section(root, "Terminal", 0, key) != 0)
		return -1;
	
	int index = 0;
	ACE_TString subname;
	while(ini.enumerate_sections(key, index, subname) == 0)
	{
		ACE_Configuration_Section_Key subkey;
		if(ini.open_section(key, subname.c_str(), 0, subkey) != 0)
			return -1;
		if(ini.get_string_value(subkey, "Index", tmp) != 0)
			return -1;
		if(ini.get_string_value(subkey, "Addr", tmp1) != 0)
			return -1;
		if(ini.get_string_value(subkey, "Key", tmp2) != 0)
			return -1;
		if(manager.Add(tmp.c_str(), tmp1.c_str(), tmp2.c_str()) != 0)
			return -1;
		
		++ index;
	}
	
	//ini.Close();
	
	return 0;
}

void ConfigLoader::Show(std::ostream& os) const
{
    os << "\n---- Config ----";
    os << "\nServiceID = " << _strServiceID;
    os << "\nServiceAddr = " << _strServiceAddr;
    os << "\nDataLoadPeriod = " << _iDataLoadPeriod;
    os << "\nLogFile = " << _strLogFile;
    os << "\nLogSize = " << _iLogSize;
    os << "\nMMLAddr = " << _strMMLAddr;
}



















