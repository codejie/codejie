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

#include "ConfigLoader.h"

ConfigLoader::ConfigLoader()
: m_iLogSize(102400)
{
}

ConfigLoader::~ConfigLoader()
{
}

int ConfigLoader::Load(const std::string& filename)
{
	ACEX_Ini_Configuration ini;
	if(ini.open(filename) != 0)
		return -1;
	
	ACE_Configuration_Section_Key root =  ini.root_section();
	
	ACE_Configuration_Section_Key key;
	ACE_TString tmp;
	
	if(ini.open_section(root, "System", 0, key) != 0)
		return -1;
	
	if(ini.get_string_value(key, "LogFile", tmp) != 0)
		return -1;
	m_strLogFile = tmp.c_str();
	
	if(ini.get_string_value(key, "LogSize", tmp) != 0)
		return -1;
	m_iLogSize = ACE_OS::atoi(tmp.c_str());

	if(ini.get_string_value(key, "MMLPort", tmp) != 0)
		return -1;
	m_strMMLAddr = tmp.c_str();
	
	if(ini.get_string_value(key, "CollectAddr", tmp) != 0)
		return -1;
	m_strCollectAddr = tmp.c_str();
	if(ini.get_string_value(key, "ControllerAddr", tmp) != 0)
		return -1;
	m_strControllerAddr = tmp.c_str();
	if(ini.get_string_value(key, "CommandAddr", tmp) != 0)
		return -1;
	m_strCommandAddr = tmp.c_str();

	if(ini.get_string_value(key, "CheckCRC", tmp) != 0)
		return -1;
	m_bCheckCRC = (tmp.c_str() == "1");

	if(ini.open_section(root, "Database", 0, key) != 0)
		return -1;
	
	if(ini.get_string_value(key, "Server", tmp) != 0)
		return -1;
	m_strDBServer = tmp.c_str();

	if(ini.get_string_value(key, "User", tmp) != 0)
		return -1;
	m_strDBUser = tmp.c_str();
	
	if(ini.get_string_value(key, "Passwd", tmp) != 0)
		return -1;
	m_strDBPasswd = tmp.c_str();
	
	return 0;
}


void ConfigLoader::Show(std::ostream& os) const
{
    os << "\n---- Config ----";
    os << "\nLogFile = " << m_strLogFile;
    os << "\nLogSize = " << m_iLogSize;
    os << "\nMMLAddr = " << m_strMMLAddr;
    os << "\nCollectAddr = " << m_strCollectAddr;
	os << "\nControllerAddr = " << m_strControllerAddr;
    os << "\nCommandAddr = " << m_strCommandAddr;
    os << "\nCheckCRC = " << m_bCheckCRC;
    os << "\nDBServer = " << m_strDBServer;
    os << "\nDBUser = " << m_strDBUser;
    os << "\nDBPasswd = " << m_strDBPasswd << std::endl;
}



















