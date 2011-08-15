#ifndef __CONFIGLOADER_H__
#define __CONFIGLOADER_H__

#include <string>
#include <iostream>

class ConfigLoader
{
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

    std::string m_strDBServer;
    std::string m_strDBUser;
    std::string m_strDBPasswd;
};

#endif
