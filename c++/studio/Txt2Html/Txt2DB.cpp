
#include "Txt2DB.h"

int Txt2DB::Create(const wxString &file)
{
	Close();

	try
	{
		_db.Open(file);
		if(!_db.IsOpen())
			return -1;
	}
	catch(const TException& e)
	{
		return -1;
	}

	return 0;
}

int Txt2DB::Push(const wxString& word, const wxString& html)
{
	//wxString word = wxString(data.word.c_str(), wxConvUTF8);

	return 0;
}

void Txt2DB::Close()
{
	if(_db.IsOpen())
	{
		_db.Close();
	}
}