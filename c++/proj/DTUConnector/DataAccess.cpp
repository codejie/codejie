
#include "ace/OS_NS_time.h"

#include "acex/ACEX.h"

#include "Toolkit.h"
#include "DataAccess.h"

void TESTHR(HRESULT x) { if FAILED(x) _com_issue_error(x); }

void PrintComError(_com_error &e) {
   _bstr_t bstrSource(e.Source());
   _bstr_t bstrDescription(e.Description());

   // Print Com errors.
   printf("Error\n");
   printf("\tCode = %08lx\n", e.Error());
   printf("\tCode meaning = %s\n", e.ErrorMessage());
   printf("\tSource = %s\n", (LPCSTR) bstrSource);
   printf("\tDescription = %s\n", (LPCSTR) bstrDescription);
}

DataAccess::DataAccess()
: _ptrConn(NULL)
{
}

DataAccess::~DataAccess()
{
}

int DataAccess::Init(const std::string &server, const std::string &db, const std::string &user, const std::string &passwd)
{
	_strServer = server.c_str();
	_strDatabase = db.c_str();
	_strUser = user.c_str();
	_strPasswd = passwd.c_str();	

    if(FAILED(::CoInitialize(NULL)))
		return -1;
	
	return 0;
}

void DataAccess::Final()
{
	Disconnect();

	::CoUninitialize();
}

int DataAccess::Connect()
{
	try
	{
		TESTHR(_ptrConn.CreateInstance(__uuidof(ADODB::Connection)));
		_bstr_t strConn = "Driver= {SQL Server};Server=" + _strServer + ";Database=" + _strDatabase + ";uid=" + _strUser + ";pwd=" + _strPasswd + ";";

		_ptrConn->Open(strConn, "", "", ADODB::adModeUnknown);

	}
    catch (_com_error &e) {
//		PrintProviderError(pConnection);
		PrintComError(e);
		return -1;
    }
	return 0;
}

void DataAccess::Disconnect()
{
	if(_ptrConn != NULL)
	{
	   if(_ptrConn->State == ADODB::adStateOpen)
		   _ptrConn->Close();
	}
}

int DataAccess::LoadStation()
{
	try
	{
		ADODB::_CommandPtr cmd("ADODB.Command");
		cmd->ActiveConnection = _ptrConn;
		cmd->CommandText = "SELECT CompanyID,companyTelephone FROM TB_Company WHERE codType='1'";
		
		ADODB::_RecordsetPtr rset("ADODB.Recordset");
		rset = cmd->Execute(NULL, NULL, ADODB::adCmdText);
		while(!rset->ADOEOF)
		{
			_mapStation.insert(std::make_pair(_bstr_t(rset->Fields->GetItem("companyTelephone")->Value), _bstr_t(rset->Fields->GetItem("CompanyID")->Value)));
			rset->MoveNext();
		}
	}
    catch (_com_error &e) {
//		PrintProviderError(pConnection);
		PrintComError(e);
		return -1;
    }

	return 0;
}

int DataAccess::SearchStation(const std::string& tel, std::string& station) const
{
	TStationMap::const_iterator it = _mapStation.find(tel);
	if(it == _mapStation.end())
		return -1;
	station = it->second;
	return 0;
}

int DataAccess::OnData(const std::string &station, float value)
{
	try
	{
		ADODB::_CommandPtr cmd("ADODB.Command");
		cmd->ActiveConnection = _ptrConn;
		std::ostringstream ostr;
		ostr << "INSERT INTO T_MONITOR_REAL_MINUTE (station_id,infectant_id,m_time,m_value) VALUES (" << station << ",'32',TO_DATE('" << Toolkit::TimeToString(ACE_OS::time(NULL)) << ",'yyyymmddhh24miss','" << value << "')";
		cmd->CommandText = ostr.str().c_str();
		cmd->Execute(NULL, NULL, ADODB::adCmdText);
	}
    catch (_com_error &e) {
//		PrintProviderError(pConnection);
		PrintComError(e);
		return -1;
    }
	return 0;
}

