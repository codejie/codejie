#pragma once

#include <string>
#include <sstream>

#include "wx/wx.h"

#include "OutputAgentObject.h"

class COutput : public OUTPUT::COutputBase
{
public:
	COutput(wxTextCtrl* text)
		:_text(text)
	{
	}
	virtual ~COutput() {}

	virtual void Output(const std::wstring& info)
	{
		if(_text != NULL)
		{
			_text->AppendText(WString2wxString(info));
		}
	}
protected:
	const wxString WString2wxString(const std::wstring& str)
	{
		return wxString(str.c_str(), wxConvISO8859_1);
	}
private:
	wxTextCtrl* _text;
};

OUTPUT::COutputAgentObject g_stOutputAgent;

#define OUTPUT(level, info) \
{\
	std::wostringstream ostr; \
	ostr << info; \
	g_stOutputAgent.Output(level, ostr.str());\
}