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

#include "occi.h"

#include "Packet.h"

class DataAccess
{
protected:
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
	void Show(std::ostream& os) const;
private:
	int Connect();
	void Disconnect();

	int LoadDefColumn();
    int LoadDefColumnFromDB();
    int LoadDefColumnFromConfig();

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
private:
    std::string _strServer;
    std::string _strUser;
    std::string _strPasswd;
private:
    oracle::occi::Environment* _env;
    oracle::occi::Connection* _conn;
    bool _isconnected;
private:
    TStationInfectantMap _mapStationInfectant;
    TInfectantMap _mapInfectant;
};

#endif /* __DATAACCESS_H__ */
