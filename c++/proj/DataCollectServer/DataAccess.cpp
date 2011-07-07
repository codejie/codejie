/*
 * DataAccess.cpp
 *
 *  Created on: Jun 24, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"
#include "acex/Ini_Configuration.h"

#include "DataAccess.h"


void DataStatistic::Update(DataStatistic::DataType type, const std::string &inf)
{
    TDataMap::iterator it = _mapData.find(type);
    if(it == _mapData.end())
    {
        TInfectantDataMap m;
        m.insert(std::make_pair(inf, 1));
        _mapData.insert(std::make_pair(type, m));
    }
    else
    {
        TInfectantDataMap::iterator i = it->second.find(inf);
        if(i != it->second.end())
        {
            ++ i->second;
        }
        else
        {
            it->second.insert(std::make_pair(inf, 1));
        }
    }
}

void DataStatistic::Show(std::ostream &os) const
{
    for(TDataMap::const_iterator it = _mapData.begin(); it != _mapData.end(); ++ it)
    {
        if(it->second.size() == 0)
            continue;
        
        switch(it->first)
        {
        case DT_MINUTE:
            os << "\nMinute - ";
            break;
        case DT_HOUR:
            os << "\nHour - ";
            break;
        case DT_DAY:
            os << "\nDay - ";
            break;
        default:
            os << "\nUnknown - ";
        }
        for(TInfectantDataMap::const_iterator i = it->second.begin(); i != it->second.end(); ++ i)
        {
            os << "\n    " << i->first << " = " << i->second;
        }
    }
    os << std::endl;
}


////////////////////////////////////

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
        _env = new ocipp::Environment();
		if(_env == NULL)
			return -1;
		_strServer = server;
		_strUser = user;
		_strPasswd = passwd;
	}
	catch(const oracle::occi::SQLException& e)
	{
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Database environment init exception - " << e.getMessage() << std::endl);
		return -1;
	}

    if(Connect() != 0)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Connect Database server failed." << std::endl);
        return -1;
    }

    if(LoadDefColumn() != 0)
    {
        ACEX_LOG_OS(LM_ERROR, "<>Load Infectant Column configration failed." << std::endl);
        return -1;
    }
    return 0;
}

void DataAccess::Final()
{
    if(_env != NULL && _conn != NULL)
    {
    	Disconnect();
        delete _env, _env = NULL;
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
        _conn = _env->makeConnection(_strUser, _strPasswd, _strServer);
		if(_conn == NULL)
			return -1;
		_isconnected = true;
	}
	catch(const oracle::occi::SQLException& e)
	{
        ACEX_LOG_OS(LM_ERROR, "<>Database init connection exception - " << e.getMessage() << std::endl);
		return -1;
	}
	return 0;
}

void DataAccess::Disconnect()
{
	if(_env != NULL && _conn != NULL)
	{
		_env->destroyConnection(_conn);
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

        ocipp::Statement *stmt = _conn->makeStatement(sql);
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
        ACEX_LOG_OS(LM_ERROR, "<LoadDefColumnFromDB>Load Infectant column exception - " << e.getMessage() << std::endl);
        return -1;
    }

	return 0;
}

int DataAccess::LoadDefColumnFromConfig()
{
    ACEX_Ini_Configuration ini;
    if(ini.open(DEF_INFECTANTCOLUMN_CONFIGFILE) != 0)
    {
        ACEX_LOG_OS(LM_INFO, "<LoadDefColumnFromConfig>Default Infectant configuration file does not find or opens failed." << std::endl);
        return 0;
    }

    ACE_Configuration_Section_Key key;
    ACE_TString tmp, st, inf, col;
    int index = 0;
    //Station
    if(ini.open_section(ini.root_section(), "Station", 0, key) != 0)
    	return -1;

    index = 0;
    while(ini.enumerate_sections(key, index, tmp) == 0)
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
    while(ini.enumerate_sections(key, index, tmp) == 0)
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

void  DataAccess::ShowColumn(std::ostream &os) const
{
    os << "\n[Station-Infectant]";
    for(TStationInfectantMap::const_iterator it = _mapStationInfectant.begin(); it != _mapStationInfectant.end(); ++ it)
    {
        os << "\n  " << it->first.first << ":" << it->first.second << " - " << it->second;
    }
    os << "\n\n[Infectant]";
    for(TInfectantMap::const_iterator it = _mapInfectant.begin(); it != _mapInfectant.end(); ++ it)
    {
        os << "\n  " << it->first << " - " << it->second;
    }
    os << std::endl;
}

void DataAccess::Show(std::ostream& os) const
{
    _dataStatistic.Show(os);
}

int DataAccess::OnData(const Packet &packet)
{
    if(packet.CN == Packet::PD_CN_DAILYDATA)
    {
        return OnDailyData(packet);
    }
    else if(packet.CN == Packet::PD_CN_MINUTELYDATA)
    {
        return OnMinutelyData(packet);
    }
    else if(packet.CN == Packet::PD_CN_HOURLYDATA)
    {
        return OnHourlyData(packet);
    }
    else if(packet.CN == Packet::PD_CN_RUNTIMEDATA)
    {
        return OnRuntimeData(packet);
    }
    else
    {
        return OnUnknownData(packet);
    }
    return -1;
}

int DataAccess::OnUnknownData(const Packet &packet)
{
    ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnUnknownData>Recv UNKNOWN data - \n" << packet << std::endl);
    return 0;
}

int DataAccess::OnRuntimeData(const Packet &packet)
{
    ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnRuntimeData>Recv RUNTIME data - \n" << packet << std::endl);
    return 0;
}

int DataAccess::OnDailyData(const Packet &packet)
{
    if(_isconnected != true)
        return -1;
    try
    {
        if(packet.MN.empty())
            throw PacketException("MN is empty.", packet);

        const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATETIME);

        for(Packet::TCPItemMap::const_iterator it = packet.CP.item.begin(); it != packet.CP.item.end(); ++ it)
        {
            std::string col = "";
            if(SearchColumn(packet.MN, it->first, col) != 0)
                throw PacketException("can not find infectant column.", packet);

            const std::string val = GetPacketCPItemDayValue(packet, it->first, it->second);
            const std::string sql = "insert into T_MONITOR_REAL_DAY (STATION_ID, M_TIME, " + col + ") values (:1,:2,:3)";
            oracle::occi::Statement *smst = _conn->createStatement(sql);
        
            smst->setString(1, packet.MN);
            smst->setString(2, mtime);
            smst->setString(3, val);

            smst->executeUpdate();

            _dataStatistic.Update(DataStatistic::DT_DAY, it->first);
        }
    }
    catch(const PacketException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnDailyData>Process Packet exception - " << e << std::endl);
        return -1;
    }
    catch(const oracle::occi::SQLException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnDailyData>Process Data exception - " << e.getMessage() << std::endl);
        return -1;
    }

    return 0;
}

int DataAccess::OnMinutelyData(const Packet &packet)
{
    if(_isconnected != true)
        return -1;
    try
    {
        if(packet.MN.empty())
            throw PacketException("MN is empty.", packet);

        const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATETIME);

        for(Packet::TCPItemMap::const_iterator it = packet.CP.item.begin(); it != packet.CP.item.end(); ++ it)
        {
            const std::string val = GetPacketCPItemMinuteValue(packet, it->first, it->second);
            const std::string sql = "insert into T_MONITOR_REAL_MINUTE (STATION_ID, INFECTANT_ID, M_TIME, M_VALUE) values (:1,:2,:3,:4)";
            oracle::occi::Statement *smst = _conn->createStatement(sql);
        
            smst->setString(1, packet.MN);
            smst->setString(2, it->first);
            smst->setString(3, mtime);
            smst->setString(4, val);

            smst->executeUpdate();
            
            _dataStatistic.Update(DataStatistic::DT_MINUTE, it->first);
        }
    }
    catch(const PacketException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnMinutelyData>Process Packet exception - " << e << std::endl);
        return -1;
    }
    catch(const oracle::occi::SQLException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnMinutelyData>Process Data exception - " << e.getMessage() << std::endl);
        return -1;
    }

    return 0;
}

int DataAccess::OnHourlyData(const Packet &packet)
{
    if(_isconnected != true)
        return -1;
    try
    {
        if(packet.MN.empty())
            throw PacketException("MN is empty.", packet);

        const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATETIME);

        for(Packet::TCPItemMap::const_iterator it = packet.CP.item.begin(); it != packet.CP.item.end(); ++ it)
        {
            std::string col = "";
            if(SearchColumn(packet.MN, it->first, col) != 0)
                throw PacketException("can not find infectant column.", packet);

            const std::string val = GetPacketCPItemHourValue(packet, it->first, it->second);
            const std::string sql = "insert into T_MONITOR_REAL_HOUR (STATION_ID, M_TIME, " + col + ") values (:1,:2,:3)";
            oracle::occi::Statement *smst = _conn->createStatement(sql);
        
            smst->setString(1, packet.MN);
            smst->setString(2, mtime);
            smst->setString(3, val);

            smst->executeUpdate();

            _dataStatistic.Update(DataStatistic::DT_HOUR, it->first);
        }
    }
    catch(const PacketException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnHourlyData>Process Packet exception - " << e << std::endl);
        return -1;
    }
    catch(const oracle::occi::SQLException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnHourlyData>Process Data exception - " << e.getMessage() << std::endl);
        return -1;
    }

    return 0;
}

/////
const std::string& DataAccess::GetPacketCPDataValue(const Packet& packet, const std::string &tag) const
{
    Packet::TCPDataMap::const_iterator it = packet.CP.data.find(tag);
    if(it == packet.CP.data.end())
        throw PacketException("can not find cp tag : " + tag, packet);

    return it->second;
}

const std::string DataAccess::GetPacketCPItemMinuteValue(const Packet &packet, const std::string &item, const Packet::TCPItemDataMap &data) const
{
    //cou + min + avg + max
    std::string ret = "";
    Packet::TCPItemDataMap::const_iterator it = data.find("Cou");
    if(it == data.end())
        throw PacketException("can not find Cou value : " + item, packet);
    ret += it->second;

    it = data.find("Min");
    if(it == data.end())
        throw PacketException("can not find Min value : " + item, packet);
    ret += ("," + it->second);

    it = data.find("Avg");
    if(it == data.end())
        throw PacketException("can not find Avg value : " + item, packet);
    ret += ("," + it->second);

    it = data.find("Max");
    if(it == data.end())
        throw PacketException("can not find Max value : " + item, packet);
    ret += ("," + it->second);

    return ret;
}

const std::string DataAccess::GetPacketCPItemHourValue(const Packet &packet, const std::string &item, const Packet::TCPItemDataMap &data) const
{
    return GetPacketCPItemMinuteValue(packet, item, data);
}

const std::string DataAccess::GetPacketCPItemDayValue(const Packet &packet, const std::string &item, const Packet::TCPItemDataMap &data) const
{
    return GetPacketCPItemMinuteValue(packet, item, data);
}

