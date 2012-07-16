#ifndef __DATA2HTML_H__
#define __DATA2HTML_H__

#include <string>
#include <fstream>

#include "TxtTidy.h"
#include "Txt2DB.h"

class Data2Html
{
protected:
	static const std::string STR_1;
	static const std::string STR_2;
	static const std::string STR_3;
	static const std::string STR_4;
	static const std::string STR_5;
	static const std::string STR_6;
	static const std::string STR_7;
	static const std::string STR_8;
	
public:
	Data2Html();
	virtual ~Data2Html() {}

	int Load(const std::string& file);
	void Close();
	int GetData(std::string& word, std::string& html);
protected:
	int Analyse(const std::string& str, TxtTidy::TData& data) const;
	int SubAnalyse(TxtTidy::TData& data, const std::string& str) const;
	int Make(const TxtTidy::TData &data, std::string& word, std::string& html) const;
private:
	std::ifstream _ifs;
	mutable std::string _dict;
};

#endif
