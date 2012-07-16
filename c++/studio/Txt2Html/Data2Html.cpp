
#include "Data2Html.h"

int Data2Html::Load(const std::string& file)
{
	Close();

	_ifs.open(file.c_str());
	if(!_ifs.is_open())
		return -1;

	return 0;
}

void Data2Html::Close()
{
	if(_ifs.is_open())
		_ifs.close();
}

int Data2Html::GetData(Txt2DB& db)
{
	if(!_ifs.is_open())
		return -1;

	char buf[512];
	if(!_ifs.getline(buf, 512, '|').good())
		return -1;
	if(_ifs.eof())
		return -1;

	TxtTidy::TData data;
	if(Analyse(buf, data) != 0)
		return -1;
	if(Make(data, db) != 0)
		return -1;

	return 0;
}

int Data2Html::Analyse(const std::string& str, TxtTidy::TData& data) const
{
	std::string::size_type begin = str.find("%w");
	if(begin == std::string::npos)
		return -1;
	begin = begin + 2;
	
	std::string::size_type end = str.find("%s", begin);
	if(end == std::string::npos)
		return -1;

	data.word = str.substr(begin, end - begin - 2);

	begin = end + 2;
	end = str.find("%d", begin);
	if(end == std::string::npos)
		return -1;

	data.symbol = str.substr(begin, end - begin - 2);

	begin = end + 2;

	while(end == std::string::npos) {
		data.data.pop_back(str.substr(begin, end - begin - 2));
		begin = end + 2;
		end = str.find("%d", begin);

	};
	data.data.push_back(str.substr(begin)));

}

int Data2Html::Make(const TxtTidy::TData &data, Txt2DB& db) const
{
//	word = wxString(data.word, 

	return 0;
}