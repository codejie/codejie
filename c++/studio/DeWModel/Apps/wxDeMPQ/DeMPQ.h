#ifndef __DEMPQ_H__
#define __DEMPQ_H__

#include <string>


class CArchScanAdapter;
class CFileReadAdapter;

class CDeMPQObject
{
public:
	CDeMPQObject();
	virtual ~CDeMPQObject();

	int OpenArch(const std::string& file);
	void CloseArch();
	bool IsOpen() const { return _bArchOpen; }

	int ScanArch(CArchScanAdapter* adapter, const std::string& filter = "*.*");
	int ReadFile(CFileReadAdapter* adapter, const std::string& file);
	int ExtractFile(const std::string& in, const std::string& out);

	const std::string& ArchFileName() const;

private:
	bool _bArchOpen;
	std::string _strFileName;
private:
	HANDLE _hMPQ;
};


#endif