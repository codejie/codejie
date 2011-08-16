/*
 * DataAccess.cpp
 *
 *  Created on: Jun 24, 2011
 *      Author: codejie
 */

#include "acex/ACEX.h"
#include "acex/Ini_Configuration.h"

#include "DataAccess.h"


void DataStatistic::UpdateInfectantData(DataStatistic::DataType type, const std::string &inf)
{
    TInfectantDataMap::iterator it = _mapInfectantData.find(type);
    if(it == _mapInfectantData.end())
    {
        TDataMap m;
        m.insert(std::make_pair(inf, 1));
        _mapInfectantData.insert(std::make_pair(type, m));
    }
    else
    {
        TDataMap::iterator i = it->second.find(inf);
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

void DataStatistic::UpdateStationData(DataStatistic::DataType type, const std::string &station)
{
    TStationDataMap::iterator it = _mapStationData.find(type);
    if(it == _mapStationData.end())
    {
        TDataMap m;
        m.insert(std::make_pair(station, 1));
        _mapStationData.insert(std::make_pair(type, m));
    }
    else
    {
        TDataMap::iterator i = it->second.find(station);
        if(i != it->second.end())
        {
            ++ i->second;
        }
        else
        {
            it->second.insert(std::make_pair(station, 1));
        }
    }
}

void DataStatistic::Show(std::ostream &os) const
{
	os << "\n---- Infectant Data Statistic ----";
    for(TInfectantDataMap::const_iterator it = _mapInfectantData.begin(); it != _mapInfectantData.end(); ++ it)
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
        for(TDataMap::const_iterator i = it->second.begin(); i != it->second.end(); ++ i)
        {
            os << "\n    " << i->first << " = " << i->second;
        }
    }
	os << "\n---- Station Data Statistic ----";
    for(TStationDataMap::const_iterator it = _mapStationData.begin(); it != _mapStationData.end(); ++ it)
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
        for(TDataMap::const_iterator i = it->second.begin(); i != it->second.end(); ++ i)
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
	catch(const ocipp::Exception& e)
	{
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Database environment init exception - " << e << std::endl);
		return -1;
	}

    if(Connect() != 0)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Connect Database server failed." << std::endl);
        return -1;
    }

	if(LoadStationID() != 0)
	{
		ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Load Station ID configration failed." << std::endl);
        return -1;
	}

	if(LoadInfectantID() != 0)
	{
		ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Load Infectant ID configration failed." << std::endl);
        return -1;
	}

    if(LoadDefColumn() != 0)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Init>Load Infectant Column configration failed." << std::endl);
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
	catch(const ocipp::Exception& e)
	{
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::Connect>Database init connection exception - " << e << std::endl);
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

int DataAccess::LoadStationID()
{
    if(_isconnected != true)
        return -1;

	try
	{
		const std::string sql = "select station_id, absoluteno from t_cfg_station_info";

		ocipp::Statement *stmt = _conn->makeStatement(sql);

		std::string sid, ano;
		stmt->defineString(1, sid);
		stmt->defineString(2, ano);

		stmt->execute();

		while(stmt->getNext() == 0)
		{
			if(!_mapStationID.insert(std::make_pair(ano, sid)).second)
			{
				ACEX_LOG_OS(LM_ERROR, "<DataAccess::LoadStationID>Load Station ID failed." << std::endl);
                return -1;
			}
		}

		_conn->destroyStatement(stmt);

	}
	catch (const ocipp::Exception& e)
	{
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::LoadStationID>Load Station ID exception - " << e << std::endl);
        return -1;
	}

	return 0;
}

int DataAccess::LoadInfectantID()
{
    if(_isconnected != true)
        return -1;

	try
	{
		const std::string sql = "select infectant_id, infectant_std03 from t_cfg_infectant_base where infectant_std03 is not NULL";

		ocipp::Statement *stmt = _conn->makeStatement(sql);

		std::string iid, sid;
		stmt->defineString(1, iid);
		stmt->defineString(2, sid);

		stmt->execute();

		while(stmt->getNext() == 0)
		{
			if(!_mapInfectantID.insert(std::make_pair(sid, iid)).second)
			{
				ACEX_LOG_OS(LM_ERROR, "<DataAccess::LoadInfectantID>Load Infectant ID failed." << std::endl);
                return -1;
			}
		}

		_conn->destroyStatement(stmt);

	}
	catch (const ocipp::Exception& e)
	{
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::LoadInfectantID>Load Infectant ID exception - " << e << std::endl);
        return -1;
	}

	return 0;
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
		
		std::string sid, iid, infcol;
		stmt->defineString(1, sid);
		stmt->defineString(2, iid);
		stmt->defineString(3, infcol);

		stmt->execute();

		while(stmt->getNext() == 0)
        {
            if(!_mapStationInfectant.insert(std::make_pair(std::make_pair(sid, iid), infcol)).second)
            {
                ACEX_LOG_OS(LM_ERROR, "<DataAccess::LoadDefColumnFromDB>Load Infectant column failed - Station:" << sid << " Infectant:" << iid << std::endl);
                return -1;
            }
        }

		_conn->destroyStatement(stmt);
    }
    catch(ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::LoadDefColumnFromDB>Load Infectant column exception - " << e << std::endl);
        return -1;
    }

	return 0;
}

int DataAccess::LoadDefColumnFromConfig()
{
    ACEX_Ini_Configuration ini;
    if(ini.open(DEF_INFECTANTCOLUMN_CONFIGFILE) != 0)
    {
        ACEX_LOG_OS(LM_INFO, "<DataAccess::LoadDefColumnFromConfig>Default Infectant configuration file does not find or opens failed." << std::endl);
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

int DataAccess::SearchStationID(const std::string& ano, std::string &station) const
{
	TStationIDMap::const_iterator it = _mapStationID.find(ano);
	if(it == _mapStationID.end())
		return -1;
	station = it->second;
	return 0;
}

const std::string& DataAccess::SearchInfectantID(const std::string& nid) const
{
	TInfectantMap::const_iterator it = _mapInfectantID.find(nid);
	if(it != _mapInfectantID.end())
		return it->second;
	else
		return nid;
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

void DataAccess::ShowStationID(std::ostream& os,const std::string& ano) const
{
	std::string id = "";
	if(SearchStationID(ano, id) == 0)
	{
		os << "\nabsoluteNo = " << ano << " - stationID = " << id << std::endl;
	}
	else
	{
		os << "\nabsoluteNo = " << ano << " - Undefined." << std::endl;
	}
}

void DataAccess::ShowInfectantID(std::ostream& os,const std::string& nid) const
{
	os << "\nStandardID = " << nid << " - InfectantID = " << SearchInfectantID(nid) << std::endl;
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
		//OnDailyData(packet);
		//OnHourlyData(packet);
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
    ACEX_LOG_OS(LM_INFO, "<DataAccess::OnRuntimeData>Recv RUNTIME data - \n" << packet << std::endl);

    return 0;
}

int DataAccess::OnDailyData(const Packet &packet)
{
    ACEX_LOG_OS(LM_DEBUG, "<DataAccess::OnDailyData>Recv Daily data - \n" << packet << std::endl);

    if(_isconnected != true)
        return -1;

    try
    {
        if(packet.MN.empty())
            throw PacketException("MN is empty.", packet);

		std::string sid;
		if(SearchStationID(packet.MN, sid) != 0)
			throw PacketException("MN is undefined.", packet);

        const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATATIME);

		std::string sql = "insert into T_MONITOR_REAL_DAY (STATION_ID,M_TIME";
		std::ostringstream ostr;
		ostr << ") values (:1,TO_DATE(:2,'yyyymmddhh24miss')";
		std::vector<std::string> vctval;
		int index = 0;
        for(Packet::TCPItemMap::const_iterator it = packet.CP.item.begin(); it != packet.CP.item.end(); ++ it, ++ index)
		{
            std::string col = "";
            if(SearchColumn(sid, SearchInfectantID(it->first), col) != 0)
                throw PacketException("can not find infectant column.", packet);
			sql += ("," + col);
			ostr << ",:" << (index + 3);
			vctval.push_back(GetPacketCPItemHourValue(packet, it->first, it->second));

			_dataStatistic.UpdateInfectantData(DataStatistic::DT_DAY, it->first);
		}
		ostr << ")";
		sql += ostr.str();

		ACEX_LOG_OS(LM_DEBUG, "<DataAccess::OnDailyData>SQL: " << sql << std::endl);

        ocipp::Statement *stmt = _conn->makeStatement(sql);
    
        stmt->bindString(1, sid);
        stmt->bindString(2, mtime);
		
		index = 0;
		for(std::vector<std::string>::const_iterator it = vctval.begin(); it != vctval.end(); ++ it, ++ index)
		{
			stmt->bindString(index + 3, *it);
		}
        stmt->execute();

		_conn->destroyStatement(stmt);

		_dataStatistic.UpdateStationData(DataStatistic::DT_DAY, sid);
    }
    catch(const PacketException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnDailyData>Process Packet exception - " << e << std::endl);
        return -1;
    }
    catch(const ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnDailyData>Process Data exception - " << e << std::endl);
        return -1;
    }

    return 0;
}

int DataAccess::OnMinutelyData(const Packet &packet)
{
    ACEX_LOG_OS(LM_DEBUG, "<DataAccess::OnMinutelyData>Recv Minutely data - \n" << packet << std::endl);

    if(_isconnected != true)
        return -1;
    try
    {
        if(packet.MN.empty())
            throw PacketException("MN is empty.", packet);

		std::string sid;
		if(SearchStationID(packet.MN, sid) != 0)
			throw PacketException("MN is undefined.", packet);

        const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATATIME);

        for(Packet::TCPItemMap::const_iterator it = packet.CP.item.begin(); it != packet.CP.item.end(); ++ it)
        {
            const std::string val = GetPacketCPItemMinuteValue(packet, it->first, it->second);
			const std::string sql = "insert into T_MONITOR_REAL_MINUTE (STATION_ID, INFECTANT_ID, M_TIME, M_VALUE) values (:1,:2,TO_DATE(:3,'yyyymmddhh24miss'),:4)";
            ocipp::Statement *stmt = _conn->makeStatement(sql);
        
            stmt->bindString(1, sid);
            stmt->bindString(2, SearchInfectantID(it->first));
            stmt->bindString(3, mtime);
            stmt->bindString(4, val);

            stmt->execute();
            
            _dataStatistic.UpdateInfectantData(DataStatistic::DT_MINUTE, it->first);

			_conn->destroyStatement(stmt);
        }

		_dataStatistic.UpdateStationData(DataStatistic::DT_MINUTE, sid);
    }
    catch(const PacketException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnMinutelyData>Process Packet exception - " << e << std::endl);
        return -1;
    }
    catch(const ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnMinutelyData>Process Data exception - " << e << std::endl);
        return -1;
    }

    return 0;
}

int DataAccess::OnHourlyData(const Packet &packet)
{
    ACEX_LOG_OS(LM_DEBUG, "<DataAccess::OnHourlyData>Recv Hourly data - \n" << packet << std::endl);

    if(_isconnected != true)
        return -1;
    try
    {
        if(packet.MN.empty())
            throw PacketException("MN is empty.", packet);

		std::string sid;
		if(SearchStationID(packet.MN, sid) != 0)
			throw PacketException("MN is undefined.", packet);

        const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATATIME);
		
		std::string sql = "insert into T_MONITOR_REAL_HOUR (STATION_ID,M_TIME";
		std::ostringstream ostr;
		ostr << ") values (:1,TO_DATE(:2,'yyyymmddhh24miss')";
		std::vector<std::string> vctval;
		int index = 0;
        for(Packet::TCPItemMap::const_iterator it = packet.CP.item.begin(); it != packet.CP.item.end(); ++ it, ++ index)
		{
            std::string col = "";
            if(SearchColumn(sid, SearchInfectantID(it->first), col) != 0)
                throw PacketException("can not find infectant column.", packet);
			sql += ("," + col);
			ostr << ",:" << (index + 3);
			vctval.push_back(GetPacketCPItemHourValue(packet, it->first, it->second));

			_dataStatistic.UpdateInfectantData(DataStatistic::DT_HOUR, it->first);
		}
		ostr << ")";
		sql += ostr.str();

		ACEX_LOG_OS(LM_DEBUG, "<DataAccess::OnHourlyData>SQL: " << sql << std::endl);

        ocipp::Statement *stmt = _conn->makeStatement(sql);
    
        stmt->bindString(1, sid);
        stmt->bindString(2, mtime);
		
		index = 0;
		for(std::vector<std::string>::const_iterator it = vctval.begin(); it != vctval.end(); ++ it, ++ index)
		{
			stmt->bindString(index + 3, *it);
		}
        stmt->execute();

		_conn->destroyStatement(stmt);		

        _dataStatistic.UpdateStationData(DataStatistic::DT_HOUR, sid);
    }
    catch(const PacketException& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnHourlyData>Process Packet exception - " << e << std::endl);
        return -1;
    }
    catch(const ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnHourlyData>Process Data exception - " << e << std::endl);
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
    if(it != data.end())
		ret += it->second;
	else
		ret += "0.0";

    it = data.find("Min");
    if(it != data.end())
	    ret += ("," + it->second);
	else
		ret += ",0.0";

    it = data.find("Avg");
    if(it != data.end())
	    ret += ("," + it->second);
	else
		ret += ",0.0";

    it = data.find("Max");
    if(it == data.end())
	    ret += ("," + it->second);
	else
		ret += ",0.0";

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

const std::string DataAccess::GetPacketCPItemRuntimeValue(const Packet &packet, const std::string &item, const Packet::TCPItemDataMap &data) const
{
    Packet::TCPItemDataMap::const_iterator it = data.find("Rtd");
    if(it != data.end())
		return it->second;
	else
		return "0.0";
}

///
int DataAccess::GetValveControlData(const std::string& nid, Packet& packet)
{
	//select s_value from ic_fm_record where m_flag = '0'

    if(_isconnected != true)
        return -1;

    try
    {
        const std::string sql = "select s_value from ic_fm_record where m_flag = '0'";

        ocipp::Statement *stmt = _conn->makeStatement(sql);
		
		std::string value;
		stmt->defineString(1, value);

		stmt->execute();

		while(stmt->getNext() == 0)
        {
			packet.
			break;
            if(!_mapStationInfectant.insert(std::make_pair(std::make_pair(sid, iid), infcol)).second)
            {
                ACEX_LOG_OS(LM_ERROR, "<DataAccess::LoadDefColumnFromDB>Load Infectant column failed - Station:" << sid << " Infectant:" << iid << std::endl);
                return -1;
            }
        }

		_conn->destroyStatement(stmt);
    }
    catch(ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::LoadDefColumnFromDB>Load Infectant column exception - " << e << std::endl);
        return -1;
    }

	return 0;
}
