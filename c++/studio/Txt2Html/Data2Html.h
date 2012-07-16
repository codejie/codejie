#ifndef __DATA2HTML_H__
#define __DATA2HTML_H__

#include <string>
#include <fstream>

#include <wx/String.h>

#include "TxtTidy.h"
#include "TxtDB.h"

class Data2Html
{
public:
	Data2Html() {};
	virtual ~Data2Html() {}

	int Load(const std::string& file);
	int GetData(Txt2DB& db);
protected:
	void Close();
	int Analyse(const std::string& str, TxtTidy::TData& data) const;
	int Make(const TxtTidy::TData &data, Txt2DB& db) const;
private:
	std::ifstream _ifs;
};

#endif
