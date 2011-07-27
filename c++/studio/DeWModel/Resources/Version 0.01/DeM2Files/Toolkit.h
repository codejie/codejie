#ifndef __TOOLKIT_H__
#define __TOOLKIT_H__

#include <iostream>
#include <string>

namespace Toolkit
{

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

#endif
