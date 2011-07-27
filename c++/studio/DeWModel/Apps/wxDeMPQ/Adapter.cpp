
#pragma warning(disable:4996)

#include "Toolkit.h"
#include "MainFrame.h"

#include "Adapter.h"


void CArchScanAdapter::OnScanBegin(const std::string& rootfile)
{
	_stMainFrame.TreeRefresh(Toolkit::String2wxString(rootfile));
}

void CArchScanAdapter::OnScanEnd(unsigned int count)
{
	_stMainFrame.TreeUpdateOver(count);
}

void CArchScanAdapter::OnScan(const char *name, const char *offset, unsigned int version, unsigned int size, unsigned int flag, unsigned int index, unsigned int compsize, unsigned int count)
{
	MPQData::TFileData data;
	data.m_strName = name;
	data.m_pPointer = offset;
	data.m_uiVersion = version;
	data.m_uiSize = size;
	data.m_uiFlag = flag;
	data.m_uiIndex = index;
	data.m_uiCompSize = compsize;
	_stMainFrame.TreeAppend(data, count);
}

////
void CFileReadAdapter::OnReadBegin(const std::string &file)
{
	_stMainFrame.NoteDataReadBegin(Toolkit::String2wxString(file));	
}

void CFileReadAdapter::OnReadEnd()
{
	_stMainFrame.NoteDataReadEnd();
}

void CFileReadAdapter::OnRead(const char *data, size_t size)
{
	_stMainFrame.NoteDataRead(data, size);
}