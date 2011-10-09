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
#include "ZJPacket.h"

class DataStatistic
{
public:
    enum DataType { DT_MINUTE = 0, DT_HOUR, DT_DAY };
protected:
    typedef std::map<std::string, size_t> TDataMap;
    typedef std::map<DataType, TDataMap> TInfectantDataMap;
	typedef std::map<DataType, TDataMap> TStationDataMap;

public:
    DataStatistic() {}
    virtual ~DataStatistic() {}

    void UpdateInfectantData(DataType type, const std::string& inf);
	void UpdateStationData(DataType type, const std::string& station);

    void Show(std::ostream& os) const;
protected:
    TInfectantDataMap _mapInfectantData;
	TStationDataMap _mapStationData;
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

    int OnPacket(const Packet& packet);

    void ShowColumn(std::ostream &os) const;
	void ShowStationID(std::ostream &os, const std::string& ano) const;
	void ShowInfectantID(std::ostream& os, const std::string& nid) const;
	void Show(std::ostream& os) const;

	int GetValveControlData(Packet& packet);
	int GetICFeeAddData(Packet& packet);
	int GetValveRealData(Packet& packet);

    int SetICFeeUploadData(const Packet& packet);
    int SetValveUploadData(const Packet& packet);

	int UpdateValveControlFlag(const Packet& packet);
	int UpdateICFeeAddFlag(const Packet& packet);
	int UpdateValveRealDataFlag(const Packet& packet);

    int OnZJRuntimeData(const ZJ::TTitleVector& title, const ZJ::TDataVector& data);
    int OnZJMinuteData(const ZJ::TTitleVector& title, const ZJ::TDataVector& data);
    int OnZJHourData(const ZJ::TTitleVector& title, const ZJ::TDataVector& data);
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

//    int OnICFeeUpload(const Packet& packet);
//    int OnValveUpload(const Packet& packet);

//	int OnValveControl(const Packet& packet);
//	int OnICFeeAdd(const Packet& packet);
//	int OnValveRealData(const Packet& packet);
private:
    const std::string& GetPacketCPDataValue(const Packet& packet, const std::string& tag) const;
    const std::string& GetPacketCPItemValue(const Packet& packet, const std::string& inf, const std::string& item) const;
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
