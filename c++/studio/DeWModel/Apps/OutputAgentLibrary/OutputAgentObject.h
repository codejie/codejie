#pragma once

#include <string>

#ifdef OUTPUTAGENTLIBRARY_EXPORTS
#define OUTPUTAGENTLIBRARY_API __declspec(dllexport)
#else
#define OUTPUTAGENTLIBRARY_API __declspec(dllimport)
#endif

namespace OUTPUT
{

class OUTPUTAGENTLIBRARY_API COutputBase
{
public:
	COutputBase() {}
	virtual ~COutputBase() {}

	virtual void Output(const std::wstring& info) = 0;
};

const unsigned int LEVEL_DEBUG		=	1;
const unsigned int LEVEL_INFO		=	2;
const unsigned int LEVEL_WARN		=	4;
const unsigned int LEVEL_ERROR		=	8;

const unsigned int LEVEL_ALL		=	0xFFFFFFFF;

class OUTPUTAGENTLIBRARY_API COutputAgentObject
{
public:
	COutputAgentObject();
	virtual ~COutputAgentObject();

	void SetLevel(unsigned int level);
	unsigned int GetLevel() const;
	void SetOutput(COutputBase* output, bool release = false);
	const COutputBase * GetOutput() const;

	void Output(unsigned int level, const std::wstring& info);
protected:
	COutputBase* _output;
	unsigned int _level;
private:
	bool _release;
};

}

//#define OUTPUT(outputptr, level, info) \
//{\
//	if(outputptr != NULL) \
//	{\
//		std::wostringstream ostr; \
//		ostr << info; \
//		outputptr->Output(level, ostr.c_str()); \
//	}\
//}