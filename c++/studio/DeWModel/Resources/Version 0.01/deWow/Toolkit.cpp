/*----------------------------------------------------------------------------
*
*  Project:             Catchfly_Server
*     File:             Toolkit.cpp
*   Author:             Jie(cnjiesbox@hotmail.com)
* Revision:             0.0.0.1
*  Created:             2007-11-24 07:34:60
*   Update:             2007-11-24 07:34:60
*
-----------------------------------------------------------------------------*/
#include <Windows.h>

#include "Toolkit.h"

namespace Toolkit
{

const std::string StrUppercase(const std::string& str)
{
	std::string tmp = str;
	for(std::string::size_type i = 0; i < tmp.size(); ++ i)
	{
		if(tmp[i] >= 'a' && tmp[i] <= 'z')
			tmp[i] -= 32;
	}
	return str;
}

int GetPathList(const std::string& root, const std::string& filter, std::vector<std::string>& vct)
{
	//find all directory
	std::string str = root;
	std::string sf = str;
	if(sf[sf.size() - 1] != '\'')
		sf += "\\";
	sf += filter;

	WIN32_FIND_DATAA data;
	HANDLE handle  = FindFirstFileA((LPCSTR)sf.c_str(), &data);
	if( handle == INVALID_HANDLE_VALUE)
		return -1;
	vct.push_back(str);
	bool f = true;
	while(f)
	{
		if((data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) == FILE_ATTRIBUTE_DIRECTORY)
		{
			if(strcmp(data.cFileName, ".") != 0 && strcmp(data.cFileName, "..") != 0)
			{
				str = str + "\\" + data.cFileName;
//				vct.push_back(str);
				if(GetPathList(str, filter, vct) != 0)
					break;
			}
		}
		f = (FindNextFileA(handle, &data) == TRUE ? true : false);
		if(GetLastError() == ERROR_NO_MORE_FILES)
			break;
	}
	FindClose(handle);
	return 0;
}

int GetFileList(const std::string& path, const std::string& filter, bool recursion, std::vector<std::string>& vct)
{
	if(!recursion)
	{
		std::string str = path;
		if(str[str.size() - 1] != '\'')
			str += "\\";
		str += filter;

		WIN32_FIND_DATAA data;
		HANDLE handle  = FindFirstFileA((LPCSTR)str.c_str(), &data);
		if( handle == INVALID_HANDLE_VALUE)
			return -1;
		bool f = true;
		while(f)
		{
			if((data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != FILE_ATTRIBUTE_DIRECTORY)
			{
				vct.push_back(path + data.cFileName);
			}

			f = (FindNextFileA(handle, &data) == TRUE ? true : false);
			if(!f && GetLastError() != ERROR_NO_MORE_FILES)
				return -1;
		}
		FindClose(handle);
	}
	else
	{
		std::vector<std::string> vctpath;
		if(GetPathList(path, "*", vctpath) != 0)
			return -1;
		for(std::vector<std::string>::const_iterator it = vctpath.begin(); it != vctpath.end(); ++ it)
		{
			GetFileList(*it, filter, false, vct);
		}
	}
	return 0;	
}


std::ostream& PrintBinary(std::ostream& os, const char* pData, size_t iSize)
{
	if(pData == NULL || iSize == 0)
		return os;
	const unsigned char* data = (const unsigned char*)pData;
	long lflag = os.flags();
	char cfill = os.fill();

	os.flags(std::ios::hex | std::ios::uppercase);
	os.fill('0');

	unsigned int iCount = 0;
	const std::string strSep1 = "   ";
	const std::string strSep2 = "- ";
	const std::string strSep3 = " ";
	const std::string strSep4 = "  ";
	size_t iTmp = 0;
	size_t i, j;
	for(i = 0; i < iSize / 16; ++i)
	{
		j = 0;
		while(j < 16)
		{
			if(j == 0)
			{
				os.width(4);
				os << 16 * (iCount ++) << strSep1;
			}
			else if(j == 8)
			{
				os << strSep2;
			}

			os.width(2);
			os << (unsigned int)data[i * 16 + j] << strSep3;
			++j;
		}
		j = 0;
		while(j < 16)
		{
			if(j == 0)
			{
				os << strSep1;
			}
			else if(j == 8)
			{
				os << strSep3;
			}
			iTmp = i * 16 + j;
			if(data[iTmp] >= char(0x20))
				os << data[iTmp];
			else
				os << ".";
			++j;
		}

		os << "\n";
	}

	size_t iRest = iSize - iSize / 16 * 16;
	size_t iDone = iSize / 16 * 16;
	j = 0;
	bool b = true;
	while(j < 16)
	{
		if(j < iRest)
		{
			if(j == 0)
			{
				os.width(4);
				os << 16 * (iCount ++) << strSep1;
			}
			else if(j == 8)
			{
				os << strSep2;
				b = false;
			}

			os.width(2);
			os << (unsigned int)data[iDone + j] << strSep3;
		}
		else
		{
			os << strSep1;
		}
		++j;
	}
	if(b)
		os << strSep4;

	//	os << strSep2;

	j = 0;
	while(j < iRest)
	{
		if(j == 0)
		{
			os << strSep1;
		}
		else if(j == 8)
		{
			os << strSep3;
		}
		iTmp = iDone + j;
		if(data[iTmp] >= char(0x20))
			os << data[iTmp];
		else
			os << ".";
		++j;
	}
	os << "\n";

	os.fill(cfill);
	os.flags(lflag);

	return os;
}

}
