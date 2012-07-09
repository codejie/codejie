
#include <iostream>
#include <fstream>

#include "TxtTidy.h"

int TxtTidy::Tidy(const std::string& input, const std::string& output)
{
	std::ifstream ifs(input.c_str());
	if(!ifs.is_open())
		return -1;
	std::ofstream ofs(output.c_str());
	if(!ofs.is_open())
		return -1;

	size_t size = 0;
	char ch;
	bool flag = false;
	while(true)
	{
		ifs.get(ch);
		if(ifs.eof())
			break;
		if(ch == '#')
		{
			flag = true;
		}
		else if(ch == 0x0a)
		{
			if(flag == true)
			{
				flag = false;
				continue;
			}
			//ifs.get(ch);
			//if(ch == 0x0a)
			//{
				ifs.get(ch);
				if(ch > 0)
				{
					ofs.put('|');
					ofs.put(ch);
				}
				else
				{
					ofs.put(ch);
				}
			//}
			//continue;
		}
		else if(ch == 0x20 || ch == 0x09)
		{
			continue;
		}
		else
		{
			if(flag == false)
				ofs.put(ch);
		}
		//if(ch == 0x0a || ch == 0x0d || ch == 0x20 || ch == 0x09)
		//	continue;
		//ofs.put(ch);
//		std::string line;
//		ifs >> line;
//
////		if(line[0] == '#')
//			std::cout << line[0] << std::endl;
//
//		std::cout << "size = " << size << std::endl;
//		++ size;
	}

	ifs.close();
	ofs.close();

	return 0;
}