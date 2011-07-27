#ifndef __TOOLKIT_H__
#define __TOOLKIT_H__

#include <string>

namespace Toolkit
{

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

}

#endif