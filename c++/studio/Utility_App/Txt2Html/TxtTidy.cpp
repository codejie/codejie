
#include <sstream>

#include "TxtTidy.h"

int TxtTidy::Tidy(const std::string& input, const std::string& output)
{
	std::ifstream ifs(input.c_str());
	if(!ifs.is_open())
		return -1;
	std::ofstream ofs(output.c_str());
	if(!ofs.is_open())
	{
		ifs.close();
		return -1;
	}

	size_t size = 0;
	char ch;
	bool flag = false;
	bool lf = false;

	std::ostringstream ostr;

	while(true)
	{
		ifs.get(ch);
		if(ifs.eof())
			break;
		
		if(ch == 0x0a)
		{
			if(flag == true)
			{
//				ofs.put(SEPARATOR);
				flag = false;
				continue;
			}

			ifs.get(ch);
			if(ifs.eof())
				break;
			lf = true;
		}
		
		if(ch == '#'/* && lf == true*/)
		{
			if(AnalyseData(ofs, ostr.str()) != 0)
				return -1;
			ostr.str("");
			flag = true;
			continue;
		}
		else if(ch == 0x0a)
		{
			flag = false;
			lf = true;
			continue;
		}
		else if(ch == 0x20 || ch == 0x09)
		{
			continue;
		}
		else if(flag == false)
		{
			if(ch > 0 && lf == true)
			{
				//ofs.put(SEPARATOR);
				if(AnalyseData(ofs, ostr.str()) != 0)
					return -1;
				ostr.str("");
			}
			//ofs.put(ch);
			ostr << ch;
		}

		lf = false;

/*
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

			ifs.get(ch);
			if(ch == '#')
			{
				flag = true;
				continue;
			}
			if(ch == 0x0a)
			{
				flag = false;
				continue;
			}
			if(ch == 0x20 || ch == 0x09)
			{
				continue;
			}
			if(ch > 0)
			{
				ofs.put(SEPARATOR);
				ofs.put(ch);
			}
			else
			{
				ofs.put(ch);
			}

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
*/
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

	if(AnalyseData(ofs, ostr.str()) != 0)
		return -1;


	ifs.close();
	ofs.close();

	return 0;
}

int TxtTidy::AnalyseData(std::ofstream& ofs, const std::string& data) const
{
	if(data.size() > 0)
	{
		std::string::size_type pos = data.find("/");
		if(pos == std::string::npos)
			return -1;
		ofs << "%w" << data.substr(0, pos);

		std::cout << data.substr(0, pos) << std::endl;

		std::string::size_type end = data.find("/", pos + 1);
		if(end == std::string::npos || end >= data.size() -1)
			return -1;
		ofs << "%s" << data.substr(pos + 1, end - pos - 1);
		AnalyseSubData(ofs, data.substr(end + 1));
		ofs << "|";
	}
	return 0;
}

int TxtTidy::AnalyseSubData(std::ofstream& ofs, const std::string& data) const
{
	std::string::size_type begin = 0, end = 0, t;
	std::string::size_type pos = data.find(".");
	if(pos == std::string::npos)
	{
		ofs << "%d" << data;
		return 0;
	}
	end = pos + 1;
//	int i = 0;
	while(end < data.size())//ascii
	{
		//if(++ i > 5)
		//{
		//	std::cout << "=========" << std::endl;
		//	break;
		//}
		if(data[end] > 0 && data[end] != '&' && data[end] != '/' && data[end] != '(' && data[end] != ')' && data[end] != '.' && data[end] != '=' && data[end] != ',' && data[end] != ';' && !::isdigit(data[end]))// ascii
//		if(data[end] > 0 && (data[end] == 'n' || data[end] == 'v' || data[end] == ')' && data[end] != '.')// ascii
		{
			ofs << "%d" << data.substr(begin, end - begin);
			begin = end;
			end = data.find(".", begin + 1);
		}

		++ end;
	}

	ofs << "%d" << data.substr(begin, end - begin);
	return 0;
}