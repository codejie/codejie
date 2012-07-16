
#include "Data2Html.h"

const std::string Data2Html::STR_1	= "<HTML><HEAD><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/><TITLE></TITLE></HEAD><BODY><DIV style=\"BACKGROUND-COLOR:#ffffff;FONT-FAMILY:Tahoma, Arial;HEIGHT:100%;FONT-SIZE:9pt\" dir=\"ltr\"><DIV style=\"PADDING-BOTTOM:0px;LINE-HEIGHT:1.2em;PADDING-LEFT:10px;WIDTH:100%;PADDING-RIGHT:10px;FONT-FAMILY:'Tahoma';FONT-SIZE:10.5pt;PADDING-TOP:10px\"><TABLE border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><TBODY><TR><TD style=\"BORDER-BOTTOM:#92b0dd 1px solid;BORDER-LEFT:#92b0dd 1px solid;LINE-HEIGHT:1em;BACKGROUND:#cfddf0;COLOR:#000080;FONT-SIZE:9pt;BORDER-TOP:#92b0dd 1px solid;BORDER-RIGHT:#92b0dd 1px solid\" nowrap><DIV style=\"MARGIN:0px 3px 1px 0px;\"><SPAN style=\"PADDING-BOTTOM:0px;PADDING-LEFT:2px;PADDING-RIGHT:4px;PADDING-TOP:0px\">";	
const std::string Data2Html::STR_2	= "</SPAN></DIV></TD><TD style=\"BORDER-BOTTOM:#92b0dd 1px solid\"></TD><TD style=\"BORDER-BOTTOM:#92b0dd 1px solid\" width=\"100%\" align=\"right\"><DIV style=\"WIDTH:11px;HEIGHT:11px;MARGIN-RIGHT:10px\"></DIV></TD><TD style=\"BORDER-BOTTOM:#92b0dd 1px solid\"><DIV style=\"WIDTH:11px;HEIGHT:11px;\"></DIV></TD></TR></TBODY></TABLE><DIV style=\"MARGIN:5px 0px\"><DIV style=\"WIDTH:100%\"><DIV style=\"LINE-HEIGHT:normal;FLOAT:left\"></DIV><DIV style=\"WIDTH:100%\"><DIV style=\"LINE-HEIGHT:normal;MARGIN:0px 0px 5px;COLOR:#808080\"><SPAN style=\"LINE-HEIGHT:normal;COLOR:#000000;FONT-SIZE:10.5pt\"><B>";	
const std::string Data2Html::STR_3	= "</B></SPAN><SPAN style=\"LINE-HEIGHT:normal;FONT-FAMILY:'Lingoes Unicode';FONT-SIZE:10.5pt\">&nbsp;[<FONT color=\"#009900\">";	
const std::string Data2Html::STR_4	= "</FONT>]</SPAN></DIV><DIV style=\"MARGIN:0px 0px 5px\">";	
const std::string Data2Html::STR_5	= "<DIV style=\"MARGIN:4px 0px\"><FONT color=\"#C04040\">";
const std::string Data2Html::STR_6	= "</FONT>&nbsp;";
const std::string Data2Html::STR_7	= "</DIV>";
const std::string Data2Html::STR_8	= "</DIV></DIV></DIV></DIV></DIV><DIV style=\"LINE-HEIGHT:0em;HEIGHT:10px;FONT-SIZE:0px\"></DIV></DIV></BODY></HTML>";


Data2Html::Data2Html()
: _dict("CET-4")
{
}

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

int Data2Html::GetData(std::string& word, std::string& html)
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
	if(Make(data, word, html) != 0)
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

	data.word = str.substr(begin, end - begin);

	begin = end + 2;
	end = str.find("%d", begin);
	if(end == std::string::npos)
		return -1;

	data.symbol = str.substr(begin, end - begin);

	begin = end + 2;

	while(end == std::string::npos) {
		if(SubAnalyse(data, str.substr(begin, end - begin)) != 0)
			return -1;
		//data.data.push_back(str.substr(begin, end - begin - 2));
		begin = end + 2;
		end = str.find("%d", begin);

	};
	if(SubAnalyse(data, str.substr(begin)) != 0)
		return -1;
	return 0;
}

int Data2Html::SubAnalyse(TxtTidy::TData& data, const std::string& str) const
{
	std::string::size_type pos = str.find_last_of(".");
	if(pos != std::string::npos)
	{
		data.data.push_back(std::make_pair(str.substr(0, pos + 1), str.substr(pos + 1)));
	}
	else
	{
		data.data.push_back(std::make_pair("", str));
	}
	return 0;
}

int Data2Html::Make(const TxtTidy::TData &data, std::string& word, std::string& html) const
{
	if(data.word == "abnormal")
	{
		_dict = "CET-6";
	}

	word = data.word;

	html = STR_1;
	html += _dict;
	
	html += STR_2;
	html +=  data.word;
	html += STR_3;

	html += data.symbol;
	html += STR_4;

	for(std::vector<std::pair<std::string, std::string> >::const_iterator it = data.data.begin(); it != data.data.end(); ++ it)
	{
		html += STR_5;
		html += it->first;
		html += STR_6;
		html += it->second;
		html += STR_7;
	}

	html += STR_8;

	return 0;
}