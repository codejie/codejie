#ifndef __SQLPP_H__
#define __SQLPP_H__

#include <iostream>
#include <string>

namespace sqlpp
{

class Exceptin
{
};

class Connection
{
public:
	Connection();
	virtual ~Connection();

	int Connec(const std::string& server, const std::string& db, const std::string& user, const std::string& passwd);
	
protected:
	void Final();
}


}

#endif

