#ifndef __DATA2HTML_H__
#define __DATA2HTML_H__

#include <string>
#include <wx/String.h>

#include "TxtTidy.h"

class Data2Html
{
public:
	Data2Html() {};
	virtual ~Data2Html() {}

	int Load(const std::string& file);

	int Make(const TxtTidy::TData &data, wxString& word, wxString &html) const;
};

#endif
