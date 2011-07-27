#include <sys/stat.h>
//#include <Winsock2.h>

#include "FileBuffer.h"


CFileBuffer::CFileBuffer()
: _strFileName("")
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
	
	_stFileStream.open(filename.c_str());
	if(!_stFileStream.good())
		return -1;

	_strFileName = filename;

	return 0;
}

void CFileBuffer::Detch()
{
	if(!_stFileStream.is_open())
		return;
	_stFileStream.close();
}

int CFileBuffer::Seek(size_t offset)
{
	_stFileStream.seekg(offset, std::ios_base::beg);

	if(!_stFileStream.good())
		return -1;
	
	return 0;
}

bool CFileBuffer::Good() const
{
	if(!_stFileStream.is_open())
		return false;
	return _stFileStream.good();
}

int CFileBuffer::Read(float& f)
{
	_stFileStream.read((char*)&f, sizeof(f));

	if(!_stFileStream.good())
		return -1;

	//f = ntohl(f);

	return 0;
}

int CFileBuffer::Read(int& i)
{
	_stFileStream.read((char*)&i, sizeof(i));

	if(!_stFileStream.good())
		return -1;

//	i = ntohl(i);

	return 0;
}

int CFileBuffer::Read(unsigned int &ui)
{
	_stFileStream.read((char*)&ui, sizeof(ui));

	if(!_stFileStream.good())
		return -1;

//	ui = ntohl(ui);

	return 0;
}

int CFileBuffer::Read(short &s)
{
	_stFileStream.read((char*)&s, sizeof(s));

	if(!_stFileStream.good())
		return -1;

//	s = ntohs(s);

	return 0;
}

int CFileBuffer::Read(unsigned short &us)
{
	_stFileStream.read((char*)&us, sizeof(us));

	if(!_stFileStream.good())
		return -1;

//	us = ntohs(us);

	return 0;
}

int CFileBuffer::Read(char &c)
{
	_stFileStream.read(&c, sizeof(c));	

	if(!_stFileStream.good())
		return -1;

	return 0;
}

int CFileBuffer::Read(unsigned char &uc)
{
	_stFileStream.read((char*)&uc, sizeof(uc));	

	if(!_stFileStream.good())
		return -1;

	return 0;
}

int CFileBuffer::Read(char* data, size_t size)
{
	_stFileStream.read(data, size);	

	if(!_stFileStream.good())
		return -1;

	return 0;
}

int CFileBuffer::Read(unsigned char* data, size_t size)
{
	_stFileStream.read((char*)data, size);	

	if(!_stFileStream.good())
		return -1;

	return 0;
}

int CFileBuffer::Read(std::string &str)
{
	while(1)
	{
		char ch;
		_stFileStream.read(&ch, sizeof(ch));
		if(!_stFileStream.good())
			return -1;
		if(ch != '\0')
			str += ch;//.insert(str.size(), ch, 1);
		else
			break;
	}
	return 0;
}

