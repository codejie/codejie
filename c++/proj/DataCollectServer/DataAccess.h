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
#include <map>

//#include "occi.h"
#include "ocipp.h"

#include "Packet.h"

class DataStatistic
{
public:
    enum DataType { DT_MINUTE = 0, DT_HOUR, DT_DAY };
protected:
    typedef std::map<std::string, size_t> TInfectantDataMap;
    typedef std::map<DataType, TInfectantDataMap> TDataMap;

public:
    DataStatistic() {}
    virtual ~DataStatistic() {}

    void Update(DataType type, const std::string& inf);

    void Show(std::ostream& os) const;
protected:
    TDataMap _mapData;
};

class DataAccess
{
protected:
	typedef std::map<const std::string, const std::string> TStationIDMap;//stationno + stationid

	typedef std::map<const std::string, const std::string> TInfectantIDMap;//nationalid + localid

    static const std::string DEF_INFECTANTCOLUMN_CONFIGFILE;

	typedef std::pair<const std::string, const std::string> TStationInfectantPair;
	typedef std::map<const TStationInfectantPair, const std::string> TStationInfectantMap;//station+infectantid + column
	typedef std::map<const std::string, const std::string> TInfectantMap;//infectantid + column

public:
	DataAccess();
	virtual ~DataAccess();

	int Init(const std::string& server, const std::string& user, const std::string& passwd);
	void Final();

    int OnData(const Packet& packet);

    void ShowColumn(std::ostream &os) const;
	void ShowStationID(std::ostream &os, const std::string& ano) const;
	void ShowInfectantID(std::ostream& os, const std::string& nid) const;
	void Show(std::ostream& os) const;
private:
	int Connect();
	void Disconnect();

	int LoadStationID();
	int LoadInfectantID();
	int LoadDefColumn();
    int LoadDefColumnFromDB();
    int LoadDefColumnFromConfig();

	int SearchStationID(const std::string& ano, std::string& station) const;
	const std::string& SearchInfectantID(const std::string& nid) const;
	int SearchColumn(const std::string& station, const std::string& infectant, std::string& column) const;
private:
    int OnDailyData(const Packet& packet);
    int OnMinutelyData(const Packet& packet);
    int OnHourlyData(const Packet& packet);
    int OnRuntimeData(const Packet& packet);
    int OnUnknownData(const Packet& packet);
private:
    const std::string& GetPacketCPDataValue(const Packet& packet, const std::string& tag) const;
    const std::string GetPacketCPItemMinuteValue(const Packet& packet, const std::string& item, const Packet::TCPItemDataMap& data) const;
    const std::string GetPacketCPItemHourValue(const Packet& packet, const std::string& item, const Packet::TCPItemDataMap& data) const;
    const std::string GetPacketCPItemDayValue(const Packet& packet, const std::string& item, const Packet::TCPItemDataMap& data) const;
	const std::string GetPacketCPItemRuntimeValue(const Packet& packet, const std::string& item, const Packet::TCPItemDataMap& data) const;
private:
    std::string _strServer;
    std::string _strUser;
    std::string _strPasswd;
private:
    ocipp::Environment* _env;
    ocipp::Connection* _conn;
    bool _isconnected;
private:
	TStationIDMap _mapStationID;
	TInfectantIDMap _mapInfectantID;
    TStationInfectantMap _mapStationInfectant;
    TInfectantMap _mapInfectant;

    DataStatistic _dataStatistic;
};

#endif /* __DATAACCESS_H__ */
