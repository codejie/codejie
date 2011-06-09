#ifndef __CONFIGLOADER_H__
#define __CONFIGLOADER_H__

#include <string>
#include <iostream>

#include "acex/ACEX.h"
#include "acex/Ini_Configuration.h"

class DataLoader;
class TerminalManager;

class ConfigLoader
{
public:
    ConfigLoader();
    virtual ~ConfigLoader();

    int Load(const std::string& filename, DataLoader& loader, TerminalManager& manager);

    const std::string& GetServiceID() const { return _strServiceID; }
    const std::string& GetServiceAddr() const { return _strServiceAddr; }
    unsigned int GetDataLoadPeriod() const { return _iDataLoadPeriod; }
	
	const std::string& GetLogFile() const { return _strLogFile; }
	unsigned int GetLogSize() const { return _iLogSize; }
	
	const std::string& GetMMLAddr() const {  return _strMMLAddr; }

    void Show(std::ostream& os) const;
protected:
	std::string _strServiceID;
    std::string _strServiceAddr;
    unsigned int _iDataLoadPeriod;
	std::string _strLogFile;
	unsigned int _iLogSize;
	std::string _strMMLAddr;
};

extern ConfigLoader theConfig;

#endif
