#include "stdafx.h"

#include "StormLib.h"

#include "MPQFileWrapper.h"

CMPQFileWrapper::CMPQFileWrapper()
: _hMPQ(NULL), _hFile(NULL)
{
}

CMPQFileWrapper::~CMPQFileWrapper()
{
	Detach();
}

int CMPQFileWrapper::Attach(const std::string &mpq, const std::string &file)
{
	Detach();

	_strMPQName = mpq;
	_strFileName = file;

	if(SFileOpenArchive(_strMPQName.c_str(), 0, 0, &_hMPQ) == FALSE)
		return F_RMPQ_OPEN;
	if(SFileOpenFileEx(_hMPQ, _strFileName.c_str(), 0, &_hFile) == FALSE)
		return F_RFILE_OPEN;
	return S_ROK;
}

void CMPQFileWrapper::Detach()
{
	if(_hFile != NULL)
	{
		SFileCloseFile(_hFile);
		_hFile = NULL;
	}
	if(_hMPQ != NULL)
	{
		SFileCloseArchive(_hMPQ);
		_hMPQ = NULL;
	}
}

int CMPQFileWrapper::Seek(size_t offset)
{
	long up = 0; 
	if(SFileSetFilePointer(_hFile, offset, &up, FILE_BEGIN) == -1)
		return F_RFILE_SEEK;
	return S_ROK;
}

int CMPQFileWrapper::Skip(size_t offset)
{
	long up = 0;
	if(SFileSetFilePointer(_hFile, offset, &up, FILE_CURRENT) == -1)
		return F_RFILE_SEEK;
	return S_ROK;
}

int CMPQFileWrapper::GetSize(size_t& size)
{
	DWORD up = 0;
	size = SFileGetFileSize(_hFile, &up);
	if(size == (size_t)-1)
		return F_RFILE_SEEK;
	return 0;
}

int CMPQFileWrapper::Read(float& f)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &f, sizeof(f), &out, NULL) == FALSE)
		return F_RFILE_READ;
	//f = ntohl(f);
	return S_ROK;
}

int CMPQFileWrapper::Read(int& i)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &i, sizeof(i), &out, NULL) == FALSE)
		return F_RFILE_READ;
	//i = ntohl(i);
	return S_ROK;
}

int CMPQFileWrapper::Read(unsigned int& ui)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &ui, sizeof(ui), &out, NULL) == FALSE)
		return F_RFILE_READ;
	//ui = ntohl(ui);
	return S_ROK;
}

int CMPQFileWrapper::Read(short& s)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &s, sizeof(s), &out, NULL) == FALSE)
		return F_RFILE_READ;
	//s = ntohl(s);
	return S_ROK;
}

int CMPQFileWrapper::Read(unsigned short& us)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &us, sizeof(us), &out, NULL) == FALSE)
		return F_RFILE_READ;
	//us = ntohl(us);
	return S_ROK;
}

int CMPQFileWrapper::Read(char& c)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &c, sizeof(c), &out, NULL) == FALSE)
		return F_RFILE_READ;
	return S_ROK;
}

int CMPQFileWrapper::Read(unsigned char& uc)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &uc, sizeof(uc), &out, NULL) == FALSE)
		return F_RFILE_READ;
	return S_ROK;
}

int CMPQFileWrapper::Read(char* data, size_t size)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, data, size, &out, NULL) == FALSE)
		return F_RFILE_READ;
	return S_ROK;
}

int CMPQFileWrapper::Read(unsigned char* data, size_t size)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, data, size, &out, NULL) == FALSE)
	{
		int i = GetLastError();
		return i;
		//return F_RFILE_READ;
	}
	return S_ROK;
}


int CMPQFileWrapper::Read(std::string &str)
{
	str = "";
	DWORD out = 0;
	char ch;
	while(1)
	{
		if(SFileReadFile(_hFile, &ch, sizeof(ch), &out, NULL) == FALSE)
			return F_RFILE_READ;
		if(ch != '\0')
			str += ch;
		else
			break;
	}
	return S_ROK;
}


int CMPQFileWrapper::Read(vec3d &v3)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &v3.x, sizeof(v3.x), &out, NULL) == FALSE)
		return F_RFILE_READ;
	if(SFileReadFile(_hFile, &v3.y, sizeof(v3.y), &out, NULL) == FALSE)
		return F_RFILE_READ;
	if(SFileReadFile(_hFile, &v3.z, sizeof(v3.z), &out, NULL) == FALSE)
		return F_RFILE_READ;
	return S_ROK;
}

int CMPQFileWrapper::Read(vec2d &v2)
{
	DWORD out = 0;
	if(SFileReadFile(_hFile, &v2.x, sizeof(v2.x), &out, NULL) == FALSE)
		return F_RFILE_READ;
	if(SFileReadFile(_hFile, &v2.y, sizeof(v2.y), &out, NULL) == FALSE)
		return F_RFILE_READ;
	return S_ROK;
}

int CMPQFileWrapper::Locate(const std::string& mpq, const std::string& file, std::string& path, size_t& size, int& flag)
{
	HANDLE hmpq;
	if(SFileOpenArchive(mpq.c_str(), 0, 0, &hmpq) == FALSE)
		return F_RMPQ_OPEN;

	SFILE_FIND_DATA sf;
	HANDLE hFind = SFileFindFirstFile(hmpq, file.c_str(), &sf, NULL);
	if(hFind == NULL)
	{
		SFileCloseArchive(hmpq);
		return F_RMPQ_OPEN;
	}

	path = sf.cFileName;
	size = sf.dwFileSize;
	flag = sf.dwFileFlags;

	SFileFindClose(hFind);
	SFileCloseArchive(hmpq);

	return S_ROK;
}

