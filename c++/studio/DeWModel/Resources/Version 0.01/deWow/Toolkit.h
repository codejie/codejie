/*----------------------------------------------------------------------------
*
*  Project:             Catchfly_Server
*     File:             Toolkit.h
*   Author:             Jie(cnjiesbox@hotmail.com)
* Revision:             0.0.0.1
*  Created:             2007-11-24 07:30:60
*   Update:             2007-11-24 07:30:60
*
-----------------------------------------------------------------------------*/
#ifndef __TOOLKIT_H__
#define __TOOLKIT_H__

#include <vector>
#include <string>
#include <iostream>

namespace Toolkit
{
const std::string StrUppercase(const std::string& str);

int GetPathList(const std::string& root, const std::string& filter, std::vector<std::string>& vct);
int GetFileList(const std::string& path, const std::string& filter, bool recursion, std::vector<std::string>& vct);
std::ostream& PrintBinary(std::ostream& os, const char* pData, size_t iSize);

}

#endif
