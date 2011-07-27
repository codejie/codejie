#include "StormLib.h"

#include "Adapter.h"
#include "DeMPQ.h"


CDeMPQObject::CDeMPQObject()
: _bArchOpen(false)
, _strFileName("")
, _hMPQ(NULL)
{
}

CDeMPQObject::~CDeMPQObject()
{
	CloseArch();
}

const std::string& CDeMPQObject::ArchFileName() const
{
	return _strFileName;
}

int CDeMPQObject::OpenArch(const std::string &file)
{
	if(_bArchOpen)
		CloseArch();
	if(SFileOpenArchive(file.c_str(), 0, 0, &_hMPQ) == FALSE)
	{
		return -1;
	}
	
	_strFileName = file;
	_bArchOpen = true;

	return 0;
}

void CDeMPQObject::CloseArch()
{
	if(_bArchOpen && _hMPQ != NULL)
	{
		SFileCloseArchive(_hMPQ);
		_hMPQ = NULL;
		_bArchOpen = false;
	}
}

int CDeMPQObject::ScanArch(CArchScanAdapter* adapter, const std::string& filter)
{
	if(!_bArchOpen || (_hMPQ == NULL))
		return -1;

	if(adapter != NULL)
	{
		adapter->OnScanBegin(_strFileName);
	}

	size_t count = 0;
	SFILE_FIND_DATA sf;
	HANDLE hFind = SFileFindFirstFile(_hMPQ, filter.c_str(), &sf, NULL);
	if(hFind == NULL)
		return -1;
	BOOL bFind = FALSE;
	do
	{
		++ count;

		if(adapter != NULL)
		{
			adapter->OnScan(sf.cFileName, sf.szPlainName, sf.lcLocale, sf.dwFileSize, sf.dwFileFlags, sf.dwBlockIndex, sf.dwCompSize, count);
		}
	}while(SFileFindNextFile(hFind, &sf) != FALSE);
	if(adapter != NULL)
		adapter->OnScanEnd(count);
	SFileFindClose(hFind);

	return 0;
}

int CDeMPQObject::ReadFile(CFileReadAdapter* adapter, const std::string& file)
{
	if(!_bArchOpen || (_hMPQ == NULL))
		return -1;
	
	HANDLE hFile = 0;
	if(SFileOpenFileEx(_hMPQ, file.c_str(), 0, &hFile) == FALSE)
		return -1;
	if(adapter != NULL)
		adapter->OnReadBegin(file);

	char data[2048];
	DWORD size = 0;

	while(SFileReadFile(hFile, data, 2048, &size, NULL) != FALSE)
	{
		if(adapter != NULL)
			adapter->OnRead(data, size);
		size = 0;
	}
	if(size > 0)
	{
		if(adapter != NULL)
			adapter->OnRead(data, size);
	}

	SFileCloseFile(hFile);

	if(adapter != NULL)
		adapter->OnReadEnd();
	return 0;
}

int CDeMPQObject::ExtractFile(const std::string& in, const std::string& out)
{
	if(!_bArchOpen || (_hMPQ == NULL))
		return -1;
	return SFileExtractFile(_hMPQ, in.c_str(), out.c_str()) == TRUE ? 0 : -1;
}