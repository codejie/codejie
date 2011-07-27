#ifndef __TOOLKIT_H__
#define __TOOLKIT_H__

#include <string>
#include <sstream>
#include "wx/wx.h"

namespace Toolkit
{

inline const std::string wxString2String(const wxString& str)
{
	char ch[2048];
//		memset(ch, 0, 1024);
	strcpy(ch, (const char*)str.mb_str(wxConvUTF8));
	return std::string(ch);
}

inline const wxString String2wxString(const std::string& str)
{
	return wxString(str.c_str(), wxConvUTF8);
}

inline std::string WString2String(const std::wstring& wstr)
{
	std::string str(wstr.begin(), wstr.end());
	return str;
}

inline std::wstring String2WString(const std::string& str)
{
	std::wstring wstr(str.begin(), str.end());
	return wstr;
}
	
template <class T>
std::string StringOf(const T& object)
{
	std::ostringstream os;
	os.flags(std::ios::showpoint);
	os << object;
	return(os.str());
}

template <class T>
void StringOf(const T& object, std::string& s)
{
	std::ostringstream os;
	os.flags(std::ios::showpoint);
	os << object;
	s = os.str();
}

}

#endif
