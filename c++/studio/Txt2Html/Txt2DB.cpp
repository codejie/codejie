
#include "Txt2DB.h"

int Txt2DB::Create(const wxString &file)
{
	Close();

	try
	{
		_db.Open(file, wxEmptyString);
		if(!_db.IsOpen())
			return -1;

        const char* sql = "CREATE TABLE info (item INTEGER PRIMARY KEY AUTOINCREMENT, value VARCHAR(32))";
        _db.ExecuteUpdate(sql);

        sql = "CREATE TABLE word (srcid INTEGER, word VARCHAR(32))";
        _db.ExecuteUpdate(sql);

        sql = "CREATE TABLE src (srcid INTEGER PRIMARY KEY AUTOINCREMENT, html TEXT)";
        _db.ExecuteUpdate(sql);

        _qsrc = _db.PrepareStatement("INSERT INTO src (html) VALUES (?)");
        _qword = _db.PrepareStatement("INSERT INTO word (srcid, word) VALUES (?, ?)");
	}
	catch(const TException& e)
	{
		return -1;
	}

	return 0;
}

int Txt2DB::Push(const wxString& word, const wxString& html)
{
	try
	{
		_qsrc.ClearBindings();

		_qsrc.Bind(1, html);
		_qsrc.ExecuteUpdate();
		long srcid = _db.GetLastRowId().ToLong();

		_qword.ClearBindings();
		_qword.Bind(1, srcid);
		_qword.Bind(2, word);
		_qword.ExecuteUpdate();

	}
	catch(const TException& e)
	{
		return -1;
	}

	return 0;
}

void Txt2DB::Close()
{
	if(_db.IsOpen())
	{
		_db.Close();
	}
}