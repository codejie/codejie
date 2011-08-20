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

int DataAccess::OnPacket(const Packet &packet)
{
    if(packet.ST == Packet::PD_ST_21)
    {//collect data
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
    }
    else if(packet.ST == Packet::PD_ST_91)
    {//control response
/*
        try
        {
            const std::string& ret = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_EXERTN);
            if(ret == "1")
            {
                if(packet.CN == Packet::PD_CN_VALVECONTROL)
                {
                    return OnValveControl(packet);
                }
                else if(packet.CN == Packet::PD_CN_ICFEEADD)
                {
                    return OnICFeeAdd(packet);
                }
                else if(packet.CN == Packet::PD_CN_VALVEREALDATA)
                {
                    return OnValveRealData(packet);
                }
                else
                {
                    return -1;
                }
            }
            else
            {
                return -1;
            }
        }
        catch(const PacketException& e)
        {
            ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnPacket>Get ExeRtn - " << e << "\n" << packet << std::endl);
            
            return -1;
        }
*/
    }
    else
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnPacket>Recv UNKNOWN ST - " << packet.ST << "\n" << packet << std::endl);
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

const std::string& DataAccess::GetPacketCPItemValue(const Packet& packet, const std::string& inf, const std::string& item) const
{
    Packet::TCPItemMap::const_iterator it = packet.CP.item.find(inf);
    if(it == packet.CP.item.end())
        throw PacketException("can not find cp - item : " + inf, packet);
    Packet::TCPItemDataMap::const_iterator i = it->second.find(item);
    if(i == it->second.end())
        throw PacketException("can not find cp - item - tag : " + item, packet);
    return i->second;
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
int DataAccess::GetValveControlData(Packet& packet)
{
	//select s_value from ic_fm_record where m_flag = '0'

    if(_isconnected != true)
        return -1;

    int ret = -1;

    try
    {
        const std::string sql = "select s_value from ic_fm_record where m_flag = '0'";

        ocipp::Statement *stmt = _conn->makeStatement(sql);
		
		std::string value;
		stmt->defineString(1, value);

		stmt->execute();

		while(stmt->getNext() == 0)
        {
            Packet::TCPItemDataMap m;
            m.insert(std::make_pair("Data", value));
            packet.CP.item.insert(std::make_pair("Valve", m));

            ret  = 0;
			break;
        }

		_conn->destroyStatement(stmt);
    }
    catch(ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::GetValveControlData>Load data exception - " << e << std::endl);
        return -1;
    }

	return ret;
}

int DataAccess::GetICFeeAddData(Packet &packet)
{
//select csn,SEWAGE_NUMBER,cod_number,nh_number,NH_NUMBER,CREATE_DATE from ic_fee_record where M_FLAG = '0'

    int ret = -1;

    if(_isconnected != true)
        return -1;

    try
    {
        const std::string sql = "select csn,SEWAGE_NUMBER,cod_number,nh_number,TO_CHAR(CREATE_DATE, 'yyyymmddhh24miss') from ic_fee_record where M_FLAG = '0'";

        ocipp::Statement *stmt = _conn->makeStatement(sql);
		
		std::string csn, sewage, cod, nh, date;
		stmt->defineString(1, csn);
        stmt->defineString(2, sewage);
        stmt->defineString(3, cod);
        stmt->defineString(4, nh);
        stmt->defineString(5, date);

		stmt->execute();

		while(stmt->getNext() == 0)
        {
            packet.CP.data.insert(std::make_pair("CSN", csn));
            packet.CP.data.insert(std::make_pair("DataTime", date));


            Packet::TCPItemDataMap m1;
            m1.insert(std::make_pair("Data", sewage));
            packet.CP.item.insert(std::make_pair("B01", m1));

            Packet::TCPItemDataMap m2;
            m2.insert(std::make_pair("Data", cod));
            packet.CP.item.insert(std::make_pair("011", m2));

            Packet::TCPItemDataMap m3;
            m3.insert(std::make_pair("Data", sewage));
            packet.CP.item.insert(std::make_pair("060", m3));

            Packet::TCPItemDataMap m4;
            m4.insert(std::make_pair("Data", "100"));
            packet.CP.item.insert(std::make_pair("Valve", m4));

            ret = 0;

			break;
        }

		_conn->destroyStatement(stmt);
    }
    catch(ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::GetFeeAddData>Load data exception - " << e << std::endl);
        return -1;
    }

	return ret;
}


int DataAccess::GetValveRealData(Packet &packet)
{
//select real_out_number,	real_cod_out_number,real_nh_out_number,
//				y_out_number,m_out_number,l_out_number,alo_y_out_number,alo_m_out_number,
//				alo_l_out_number,y_out_rate,m_out_rate,l_out_rate,y_cod_out_number,
//				m_cod_out_number,l_cod_out_number,alo_cod_y_out_number,alo_cod_m_out_number,
//				alo_cod_l_out_number,y_cod_out_rate,m_cod_out_rate,l_cod_out_rate,
//				y_nh_out_number,m_nh_out_number,l_nh_out_number,alo_nh_y_out_number,
//				alo_nh_m_out_number,alo_nh_l_out_number,y_nh_out_rate,m_nh_out_rate,
//				l_nh_out_rate,d_out_number,d_cod_out_number,d_nh_out_number,
//				is_alerm,is_color
//	 			from IC_MONITOR_REAL_MINREALwhere M_FLAG = '0'

    int ret = -1;

    if(_isconnected != true)
        return -1;

    try
    {
        const std::string sql = "select real_out_number,real_cod_out_number,real_nh_out_number,\
				y_out_number,m_out_number,l_out_number,alo_y_out_number,alo_m_out_number,d_out_number,alo_l_out_number,y_out_rate,m_out_rate,l_out_rate,\
                y_cod_out_number, m_cod_out_number,l_cod_out_number,d_cod_out_number,alo_cod_y_out_number,alo_cod_m_out_number,alo_cod_l_out_number,y_cod_out_rate,m_cod_out_rate,l_cod_out_rate,\
				y_nh_out_number,m_nh_out_number,l_nh_out_number,d_nh_out_number,alo_nh_y_out_number,alo_nh_m_out_number,alo_nh_l_out_number,y_nh_out_rate,m_nh_out_rate,l_nh_out_rate,\
                is_alerm,is_color from IC_MONITOR_REAL_MINREAL where M_FLAG = '0'";

        ocipp::Statement *stmt = _conn->makeStatement(sql);
		
		std::string r_b01, r_011, r_060;
        std::string y_b01, m_b01, l_b01, d_b01, alo_y_b01, alo_m_b01, alo_l_b01, y_b01_r, m_b01_r, l_b01_r;
        std::string y_011, m_011, l_011, d_011, alo_y_011, alo_m_011, alo_l_011, y_011_r, m_011_r, l_011_r;
        std::string y_060, m_060, l_060, d_060, alo_y_060, alo_m_060, alo_l_060, y_060_r, m_060_r, l_060_r;
        std::string alerm, color;

		stmt->defineString(1, r_b01);
        stmt->defineString(2, r_011);
        stmt->defineString(3, r_060);

        stmt->defineString(4, y_b01);
        stmt->defineString(5, m_b01);
        stmt->defineString(6, l_b01);
        stmt->defineString(7, d_b01);
        stmt->defineString(8, alo_y_b01);
        stmt->defineString(9, alo_m_b01);
        stmt->defineString(10, alo_l_b01);
        stmt->defineString(11, y_b01_r);
        stmt->defineString(12, m_b01_r);
        stmt->defineString(13, l_b01_r);

        stmt->defineString(14, y_011);
        stmt->defineString(15, m_011);
        stmt->defineString(16, l_011);
        stmt->defineString(17, d_011);
        stmt->defineString(18, alo_y_011);
        stmt->defineString(19, alo_m_011);
        stmt->defineString(20, alo_l_011);
        stmt->defineString(21, y_011_r);
        stmt->defineString(22, m_011_r);
        stmt->defineString(23, l_011_r);

        stmt->defineString(24, y_060);
        stmt->defineString(25, m_060);
        stmt->defineString(26, l_060);
        stmt->defineString(27, d_060);
        stmt->defineString(28, alo_y_060);
        stmt->defineString(29, alo_m_060);
        stmt->defineString(30, alo_l_060);
        stmt->defineString(31, y_060_r);
        stmt->defineString(32, m_060_r);
        stmt->defineString(33, l_060_r);

        stmt->defineString(34, alerm);
        stmt->defineString(35, color);

		stmt->execute();

		while(stmt->getNext() == 0)
        {
            Packet::TCPItemDataMap m1;
            m1.insert(std::make_pair("Real_Data", r_b01));
			m1.insert(std::make_pair("Y_Data", y_b01));
			m1.insert(std::make_pair("M_Data", m_b01));
			m1.insert(std::make_pair("L_Data", l_b01));
			m1.insert(std::make_pair("D_Data", d_b01));
			m1.insert(std::make_pair("Alo_Y_Data", alo_y_b01));
			m1.insert(std::make_pair("Alo_M_Data", alo_m_b01));
			m1.insert(std::make_pair("Alo_L_Data", alo_l_b01));
			m1.insert(std::make_pair("Y_Rate", y_b01));
			m1.insert(std::make_pair("M_Rate", m_b01));
			m1.insert(std::make_pair("L_Rate", l_b01));
            packet.CP.item.insert(std::make_pair("B01", m1));			

            Packet::TCPItemDataMap m2;
            m2.insert(std::make_pair("Real_Data", r_011));
			m2.insert(std::make_pair("Y_Data", y_011));
			m2.insert(std::make_pair("M_Data", m_011));
			m2.insert(std::make_pair("L_Data", l_011));
			m2.insert(std::make_pair("D_Data", d_011));
			m2.insert(std::make_pair("Alo_Y_Data", alo_y_011));
			m2.insert(std::make_pair("Alo_M_Data", alo_m_011));
			m2.insert(std::make_pair("Alo_L_Data", alo_l_011));
			m2.insert(std::make_pair("Y_Rate", y_011));
			m2.insert(std::make_pair("M_Rate", m_011));
			m2.insert(std::make_pair("L_Rate", l_011));
            packet.CP.item.insert(std::make_pair("011", m2));

            Packet::TCPItemDataMap m3;
            m3.insert(std::make_pair("Real_Data", r_060));
			m3.insert(std::make_pair("Y_Data", y_060));
			m3.insert(std::make_pair("M_Data", m_060));
			m3.insert(std::make_pair("L_Data", l_060));
			m3.insert(std::make_pair("D_Data", d_060));
			m3.insert(std::make_pair("Alo_Y_Data", alo_y_060));
			m3.insert(std::make_pair("Alo_M_Data", alo_m_060));
			m3.insert(std::make_pair("Alo_L_Data", alo_l_060));
			m3.insert(std::make_pair("Y_Rate", y_060));
			m3.insert(std::make_pair("M_Rate", m_060));
			m3.insert(std::make_pair("L_Rate", l_060));
            packet.CP.item.insert(std::make_pair("B01", m3));

            Packet::TCPItemDataMap m4;
			m4.insert(std::make_pair("Data", alerm));
			packet.CP.item.insert(std::make_pair("Alerm", m4));

            Packet::TCPItemDataMap m5;
			m5.insert(std::make_pair("Data", color));
			packet.CP.item.insert(std::make_pair("Color", m5));

            ret = 0;

			break;
        }

		_conn->destroyStatement(stmt);
    }
    catch(ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_ERROR, "<DataAccess::GetRealData>Load data exception - " << e << std::endl);
        return -1;
    }

	return ret;
}

int DataAccess::UpdateValveControlFlag(const Packet& packet)
{
	//update ic_fm_record  set m_flag = '1'

    if(_isconnected != true)
        return -1;

    try
    {
		const std::string& ret = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_EXERTN);
		if(ret == "1")
		{
			const std::string sql = "update ic_fm_record  set m_flag = '1' where m_flag = '0'";
			ocipp::Statement *stmt = _conn->makeStatement(sql);

			stmt->execute();

			_conn->destroyStatement(stmt);
		}
    }
	catch(const PacketException& pe)
	{
		ACEX_LOG_OS(LM_WARNING, "<DataAccess::UpdateValveControlDataFlag>Get tag data failed - " << pe << std::endl);
		return -1;
	}
    catch(const ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::UpdateValveControlDataFlag>update flag exception - " << e << std::endl);
        return -1;
    }

	return 0;
}

int DataAccess::UpdateICFeeAddFlag(const Packet& packet)
{
	//update ic_fee_record set M_FLAG = '1'

    if(_isconnected != true)
        return -1;

    try
    {
		const std::string& ret = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_EXERTN);
		if(ret == "1") 
		{
			const std::string sql = "update ic_fee_record set M_FLAG = '1' where m_flag = '0'";
			ocipp::Statement *stmt = _conn->makeStatement(sql);

			stmt->execute();

			_conn->destroyStatement(stmt);
		}
    }
	catch(const PacketException& pe)
	{
		ACEX_LOG_OS(LM_WARNING, "<DataAccess::UpdateFeeAddDataFlag>Get tag data failed - " << pe << std::endl);
		return -1;
	}
    catch(const ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::UpdateFeeAddDataFlag>update flag exception - " << e << std::endl);
        return -1;
    }

	return 0;
}

int DataAccess::UpdateValveRealDataFlag(const Packet& packet)
{
	//update IC_MONITOR_REAL_MINREAL set M_FLAG = '1'

    if(_isconnected != true)
        return -1;

    try
    {
		const std::string& ret = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_EXERTN);
		if(ret == "1")
		{
			const std::string sql = "update IC_MONITOR_REAL_MINREAL set M_FLAG = '1' where m_flag = '0'";
			ocipp::Statement *stmt = _conn->makeStatement(sql);

			stmt->execute();

			_conn->destroyStatement(stmt);
		}
    }
	catch(const PacketException& pe)
	{
		ACEX_LOG_OS(LM_WARNING, "<DataAccess::UpdateRealDataFlag>Get tag data failed - " << pe << std::endl);
		return -1;
	}
    catch(const ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::UpdateRealDataFlag>update flag exception - " << e << std::endl);
        return -1;
    }

	return 0;
}

int DataAccess::SetICFeeUploadData(const Packet &packet)
{
//insert into ic_card_record(station_id,sn_no,sewage_number,cod_number,nh_number,m_time)
//	values('3301091139','?','?','?','?','?')
    if(_isconnected != true)
        return -1;

    //const std::string& sid = packet.MN;
    //const std::string& ic  = GetPacketCPDataValue(packet, "CSN");
    //const std::string& sewage = GetPacketCPItemValue(packet, "B01", "Data");
    //const std::string& cod = GetPacketCPItemValue(packet, "011", "Data");
    //const std::string& nh = GetPacketCPItemValue(packet, "060", "Data");
    //const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATATIME);

    try
    {
		const std::string& sid = packet.MN;
		const std::string& ic  = GetPacketCPDataValue(packet, "CSN");
		const std::string& sewage = GetPacketCPItemValue(packet, "B01", "Data");
		const std::string& cod = GetPacketCPItemValue(packet, "011", "Data");
		const std::string& nh = GetPacketCPItemValue(packet, "060", "Data");
		const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATATIME);

        const std::string sql = "insert into ic_card_record(station_id,sn_no,sewage_number,cod_number,nh_number,m_time) values(:1, :2, :3, :4, :5, TO_DATE(:6, 'yyyymmddhh24miss'))";
        ocipp::Statement *stmt = _conn->makeStatement(sql);

        stmt->bindString(1, sid);
        stmt->bindString(2, ic);
        stmt->bindString(3, sewage);
        stmt->bindString(4, cod);
        stmt->bindString(5, nh);
        stmt->bindString(6, mtime);

		stmt->execute();

		_conn->destroyStatement(stmt);
    }
	catch(const PacketException& pe)
	{
		ACEX_LOG_OS(LM_WARNING, "<>Get tag data failed - " << pe << std::endl);
		return -1;
	}
    catch(const ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnICFeeUpload>insert data exception - " << e << std::endl);
        return -1;
    }

	return 0;
}

int DataAccess::SetValveUploadData(const Packet &packet)
{
//insert into ic_fm_info(station_id,m_time,f_e_value,f_out_value,cor_value)
//	values('3301091139','?','?','?','?')

    if(_isconnected != true)
        return -1;

    //const std::string& sid = packet.MN;
    //const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATATIME);
    //const std::string& ev = GetPacketCPItemValue(packet, "E_Valve", "Data");
    //const std::string& cv = GetPacketCPItemValue(packet, "C_Valve", "Data");
    //const std::string& v = GetPacketCPItemValue(packet, "Valve", "Data");

    try
    {
		const std::string& sid = packet.MN;
		const std::string& mtime = GetPacketCPDataValue(packet, Packet::PD_CP_TAG_DATATIME);
		const std::string& ev = GetPacketCPItemValue(packet, "E_Valve", "Data");
		const std::string& cv = GetPacketCPItemValue(packet, "C_Valve", "Data");
		const std::string& v = GetPacketCPItemValue(packet, "Valve", "Data");

        const std::string sql = "insert into ic_fm_info(station_id,m_time,f_e_value,f_out_value,cor_value) values(:1, TO_DATE(:2, 'yyyymmddhh24miss'), :3, :4, :5)";
        ocipp::Statement *stmt = _conn->makeStatement(sql);

        stmt->bindString(1, sid);
        stmt->bindString(2, mtime);
        stmt->bindString(3, ev);
        stmt->bindString(4, v);
        stmt->bindString(5, cv);

		stmt->execute();

		_conn->destroyStatement(stmt);
    }
	catch(const PacketException& pe)
	{
		ACEX_LOG_OS(LM_WARNING, "<>Get tag data failed - " << pe << std::endl);
		return -1;
	}
    catch(const ocipp::Exception& e)
    {
        ACEX_LOG_OS(LM_WARNING, "<DataAccess::OnValveUpload>insert data exception - " << e << std::endl);
        return -1;
    }

	return 0;
}
