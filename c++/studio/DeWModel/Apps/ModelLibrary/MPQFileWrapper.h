#ifndef __MPQFILEWRAPPER_H__
#define __MPQFILEWRAPPER_H__

//refence : http://www.zezula.net/en/mpq/stormlib.html#SFileOpenFileEx
#include <windows.h>

#include <string>
#include "vec3d.h"

class CMPQFileWrapper
{
public:
	static const int S_ROK				=	0;
	static const int F_RMPQ_OPEN		=	-1;
	static const int F_RFILE_OPEN		=	-11;
	static const int F_RFILE_SEEK		=	-12;
	static const int F_RFILE_READ		=	-13;

public:
	CMPQFileWrapper();
	virtual ~CMPQFileWrapper();

	int Attach(const std::string& mpq, const std::string& file);
	int Locate(const std::string& mpq, const std::string& file, std::string& path, size_t& size, int& flag);

	int Seek(size_t offset);
	int Skip(size_t offset);
	int GetSize(size_t& size);

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

	int Read(vec3d& v3);
	int Read(vec2d& v2);
private:
	void Detach();
private:
	std::string _strMPQName;
	std::string _strFileName;
	HANDLE _hMPQ;
	HANDLE _hFile;
};

#endif
