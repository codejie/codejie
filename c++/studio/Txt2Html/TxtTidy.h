#ifndef __TXTTIDY_H__
#define __TXTTIDY_H__

#include <string>
#include <iostream>
#include <fstream>
#include <vector>

class TxtTidy
{
public:
	static const char SEPARATOR	=	'|';
public:
	struct TData
	{
		std::string word;
		std::string symbol;
		std::vector<std::string> data;
	};
	
public:
	TxtTidy() {}
	virtual ~TxtTidy() {}

//	int Load(const std::string& file);
	int Tidy(const std::string& input, const std::string& output);
	int Load(const std::string& file);
	int GetData(TData& data);
private:
	int AnalyseData(const std::string& data, std::vector<std::string>& vct) const;
private:
	std::ifstream _ifs;
};

#endif