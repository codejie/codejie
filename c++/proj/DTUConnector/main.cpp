

#import "C:\Program Files\Common Files\System\ado\msado15.dll" rename("EOF", "ADOEOF")

#include <oledb.h>
#include <conio.h>
#include <icrsint.h>
#include <iostream>

int main()
{
	if(FAILED(::CoInitialize(NULL)))
		return -1;

	ADODB::_ConnectionPtr _ptrConn("ADODB.Connection");

	//_ptrConn.CreateInstance("ADODB.Connection");
	//_bstr_t strConn = "Provider=SQLOLEDB;Server=172.70.1.13;Database=xqls;uid=sa;pwd=1234;";
	_bstr_t strConn = "Driver= {SQL Server};Server=JIE-MAC\\SQLEXPRESS;Database=tempdb;uid=sa;pwd=sa;";

	_ptrConn->Open(strConn, "", "", ADODB::adModeUnknown);

	_bstr_t sql = "CREATE TABLE T_TEST (id INTEGER, value TEXT)";
	VARIANT ra;

	try
	{
		ADODB::_CommandPtr _ptrCmd("ADODB.Command");
		_ptrCmd->ActiveConnection = _ptrConn;
		//_ptrCmd->CommandText = sql;1
		//_ptrCmd->Execute(NULL, NULL, ADODB::adCmdText);

		_ptrCmd->CommandText = "INSERT INTO T_TEST VALUES(1, 'one')";
		_ptrCmd->Execute(NULL, NULL, ADODB::adCmdText);

		ADODB::_RecordsetPtr _ptrRS("ADODB.Recordset");
		sql = "SELECT * FROM T_TEST";
		_ptrCmd->CommandText = sql;
		_ptrRS = _ptrCmd->Execute(NULL, NULL, ADODB::adCmdText);

		while(!_ptrRS->ADOEOF)
		{
			VARIANT pos;
			pos.intVal = 0;
			int i = _ptrRS->Fields->GetItem("id")->Value;
			std::cout << "i = " << i << std::endl;
			_ptrRS->MoveNext();
		}
		//_ptrConn->Execute(sql, &ra, ADODB::adCmdText);
	}
	catch(_com_error &e)
	{
		std::cout << e.Description() << std::endl;
	}

	::CoUninitialize();


	return 0;
}