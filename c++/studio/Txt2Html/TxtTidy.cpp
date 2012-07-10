


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

int TxtTidy::Load(const std::string &file)
{
	if(_ifs.is_open())
	{
		_ifs.close();
	}

	_ifs.open(file.c_str());
	if(!_ifs.is_open())
		return -1;
	return 0;
}


int TxtTidy::GetData(TxtTidy::TData &data)
{
	if(!_ifs.is_open())
		return -1;

	char buff[512];

	if(!_ifs.getline(buff, 512, SEPARATOR).good())
	{
		_ifs.close();
		return -1;
	}

	if(_ifs.eof())
	{
		_ifs.close();
		return -1;
	}

	std::string str = buff;
	std::string::size_type pos = str.find("/");
	if(pos == std::string::npos)
		return -1;
	data.word = str.substr(0, pos);

	return 0;
}