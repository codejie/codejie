#ifndef __ADAPTER_H__
#define __ADAPTER_H__

#include <string>

class CMainFrame;

class CAdapter
{
public:
	CAdapter() {}
	virtual ~CAdapter() {}
};

class CArchScanAdapter : public CAdapter
{
public:
	CArchScanAdapter(CMainFrame& frame)
		: CAdapter()
		, _stMainFrame(frame)
	{
	}
	virtual ~CArchScanAdapter() {}
	
public:
	void OnScanBegin(const std::string& rootfile);
	void OnScanEnd(unsigned int count);
	void OnScan(const char* name, const char* offset, unsigned int version, unsigned int size, unsigned int flag, unsigned int index, unsigned compsize, unsigned int count);
private:
	CMainFrame& _stMainFrame;
};

class CFileReadAdapter : public CAdapter
{
public:
	CFileReadAdapter(CMainFrame& frame)
		: CAdapter()
		, _stMainFrame(frame)
	{
	}
	virtual ~CFileReadAdapter() {}
public:
	void OnReadBegin(const std::string& file);
	void OnReadEnd();
	void OnRead(const char* data, size_t size);
private:
	CMainFrame& _stMainFrame;
};

#endif