
#include <string>
#include <iostream>

#include "wx/String.h"

#include "TxtTidy.h"
#include "Txt2DB.h"
#include "Data2Html.h"


int main()
{
	//TxtTidy tidy;
	//tidy.Tidy("c.txt", "b.txt");
//	tidy.Load("b.txt");
//	TxtTidy::TData data;
//	while(tidy.GetData(data) == 0)
//	{
//		std::cout << data.word << "[" << data.symbol << "]:" << data.data.size() << std::endl;
//	}

	Txt2DB db;
	db.Create(wxT("c46.db"));
	Data2Html data;
	data.Load("d.txt");
	wxString word, html;
	while(data.GetData(word, html) == 0)
	{
		db.Push(word, html);//wxString(word.c_str(), wxConvUTF8), wxString(html.c_str(), wxConvUTF8));
	}
	data.Close();
	db.Close();

	return 0;
}