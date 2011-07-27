#ifndef __FILEBUFFER_H__
#define __FILEBUFFER_H__

//#include <fstream>
#include <iostream>
#include <string>

class CFileBuffer
{
public:
	CFileBuffer();
	virtual ~CFileBuffer();

	int Attach(const std::string& filename);

	int Seek(size_t offset);
	bool Good() const;

	int Read(float& f);
	int Read(int& i);
	int Read(unsigned int& ui);
	int Read(short& s);
	int Read(unsigned short& us);
	int Read(char& c);
	int Read(unsigned char& uc);
	int Read(char* data, size_t size);
	int Read(unsigned char* data, size_t size);
	int Read(std::string& str);

private:
	void Detch();
private:
	std::string _strFileName;
	FILE* _pFile;
//	std::ifstream _stFileStream;
	size_t _szFileSize;
};

#endif
