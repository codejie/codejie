#ifndef __CONFIGLOADER_H__
#define __CONFIGLOADER_H__

#include <string>
#include <iostream>
#include <utility>
#include <vector>

class ConfigLoader
{
public:
	typedef std::pair<int, std::string> TDistributeServerDataPair;
	typedef std::vector<TDistributeServerDataPair> TDistributeServerDataVector;
public:
    ConfigLoader();
    virtual ~ConfigLoader();

    int Load(const std::string& filename);

    void Show(std::ostream& os) const;
public:
    std::string m_strLogFile;
    unsigned int m_iLogSize;
    std::string m_strMMLAddr;
    std::string m_strCollectAddr;
	std::string m_strControllerAddr;
    std::string m_strCommandAddr;

    bool m_bCheckCRC;

    std::string m_strDBServer;
    std::string m_strDBUser;
    std::string m_strDBPasswd;

    int m_iRealtimeInterval;
    int m_iPeriodInterval;

	TDistributeServerDataVector m_vctDistributeServerData;
};

#endif
