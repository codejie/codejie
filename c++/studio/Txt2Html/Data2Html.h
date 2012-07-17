#ifndef __DATA2HTML_H__
#define __DATA2HTML_H__

#include <string>
#include <fstream>

#include "wx/String.h"

#include "TxtTidy.h"
#include "Txt2DB.h"

class Data2Html
{
protected:
	static const wxString STR_1;
	static const wxString STR_2;
	static const wxString STR_3;
	static const wxString STR_4;
	static const wxString STR_5;
	static const wxString STR_6;
	static const wxString STR_7;
	static const wxString STR_8;
	
public:
	Data2Html();
	virtual ~Data2Html() {}

	int Load(const std::string& file);
	void Close();
	int GetData(wxString& word, wxString& html);//std::string& word, std::string& html);
protected:
	int Analyse(const std::string& str, TxtTidy::TData& data) const;
	int SubAnalyse(TxtTidy::TData& data, const std::string& str) const;
	int Make(const TxtTidy::TData &data, wxString& word, wxString& html) const;
private:
	std::ifstream _ifs;
	mutable wxString _dict;
};

#endif
