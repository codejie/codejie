#include "stdafx.h"

#include "OutputAgentObject.h"

namespace OUTPUT
{

COutputAgentObject::COutputAgentObject()
: _output(NULL), _level(LEVEL_ALL), _release(false)
{
}

COutputAgentObject::~COutputAgentObject()
{
	if(_release && _output != NULL)
	{
		delete _output, _output = NULL;
		_release = false;
	}
}

void COutputAgentObject::SetLevel(unsigned int level)
{
	_level = level;
}

unsigned int COutputAgentObject::GetLevel() const
{
	return _level;
}

void COutputAgentObject::SetOutput(COutputBase* output, bool release)
{
	_output = output;
	_release = release;
}

const COutputBase* COutputAgentObject::GetOutput() const
{
	return _output;
}

void COutputAgentObject::Output(unsigned int level, const std::wstring& info)
{
	if(_output == NULL)
		return;
	if((level & _level) == 0)
		return;
	_output->Output(info);
}


}