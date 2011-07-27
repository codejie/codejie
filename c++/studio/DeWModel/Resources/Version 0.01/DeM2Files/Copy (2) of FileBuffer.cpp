#include <sys/stat.h>
//#include <Winsock2.h>

#include "FileBuffer.h"


CFileBuffer::CFileBuffer()
: _strFileName("")
, _pFile(NULL)
, _szFileSize(0)
{
}

CFileBuffer::~CFileBuffer()
{
	Detch();
}

int CFileBuffer::Attach(const std::string& filename)
{
	struct stat st;
	if(stat(filename.c_str(), &st) != 0)
		return -1;
	if((st.st_mode & _S_IFREG) != _S_IFREG)
		return -1;
	_szFileSize = st.st_size;

	_pFile = fopen(filename.c_str(), "rb");
	if(_pFile == NULL)
		return -1;

	_strFileName = filename;

	return 0;
}

void CFileBuffer::Detch()
{
	if(_pFile != NULL)
		fclose(_pFile), _pFile = NULL;
}

int CFileBuffer::Seek(size_t offset)
{
	if(fseek(_pFile, offset, SEEK_SET) != 0)
		return -1;
	
	return 0;
}

bool CFileBuffer::Good() const
{
	if(ferror(_pFile) != 0)
		return false;
	if(feof(_pFile) != 0)
		return false;
	return true;
}

int CFileBuffer::Read(float& f)
{
	fread((void*)&f, sizeof(f), 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

	//f = ntohl(f);

	return 0;
}

int CFileBuffer::Read(int& i)
{
	fread((void*)&i, sizeof(i), 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

//	i = ntohl(i);

	return 0;
}

int CFileBuffer::Read(unsigned int &ui)
{
	fread((void*)&ui, sizeof(ui), 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

//	ui = ntohl(ui);

	return 0;
}

int CFileBuffer::Read(short &s)
{
	fread((void*)&s, sizeof(s), 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

//	s = ntohs(s);

	return 0;
}

int CFileBuffer::Read(unsigned short &us)
{
	fread((void*)&us, sizeof(us), 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

//	us = ntohs(us);

	return 0;
}

int CFileBuffer::Read(char &c)
{
	fread((void*)&c, sizeof(c), 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

	return 0;
}

int CFileBuffer::Read(unsigned char &uc)
{
	fread((void*)&uc, sizeof(uc), 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

	return 0;
}

int CFileBuffer::Read(char* data, size_t size)
{
	fread((void*)data, size, 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

	return 0;
}

int CFileBuffer::Read(unsigned char* data, size_t size)
{
	fread((void*)data, size, 1, _pFile);

	if(ferror(_pFile) != 0)
		return -1;

	return 0;
}

int CFileBuffer::Read(std::string &str)
{
	while(1)
	{
		char ch;
		fread((void*)&ch, sizeof(ch), 1, _pFile);
		if(ferror(_pFile) != 0)
			return -1;
		if(ch != '\0')
			str += ch;//.insert(str.size(), ch, 1);
		else
			break;
	}
	return 0;
}

