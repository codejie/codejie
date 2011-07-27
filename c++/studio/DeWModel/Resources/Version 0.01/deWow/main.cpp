/*----------------------------------------------------------------------------
*
*  Project:             Catchfly_Server
*     File:             main.cpp
*   Author:             Jie(cnjiesbox@hotmail.com)
* Revision:             0.0.0.1
*  Created:             2007-11-24 07:24:60
*   Update:             2007-11-24 07:24:60
*
-----------------------------------------------------------------------------*/
#include <iostream>

#include <iostream>
#include <string>


#include "Toolkit.h"
#include "M2ExtractObject.h"


int ExtractM2FromMPQ(std::vector<std::string>& vctmpq, const std::string& filter, const std::string& output)
{
	CM2ExtractObject extract(output);
	extract.OutputStream(std::cout);
	for(std::vector<std::string>::const_iterator it = vctmpq.begin(); it != vctmpq.end(); ++ it)
		extract.AddMPQFile(*it);
	extract.AddM2Filter(filter);
	extract.Extract();

	return 0;
}

int main()
{
	std::string input = "";
	std::cout << "\nInput WOW directory : ";
	std::cin >> input;

	if(!input.empty())
	{
		std::vector<std::string> vct;
		if(Toolkit::GetFileList(input, "base-enCN.MPQ", false, vct) != 0)
			return -1;
		for(std::vector<std::string>::const_iterator it = vct.begin(); it != vct.end(); ++ it)
			std::cout << *it << std::endl;
		ExtractM2FromMPQ(vct, "*.m2", ".\\output\\");
	}

	char ch;
	std::cout << "\n\nInput ANY character to quit...";
	std::cin >> ch;

	return 0;
}