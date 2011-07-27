/*----------------------------------------------------------------------------
*
*  Project:             Catchfly_Server
*     File:             M2ExtractObject.h
*   Author:             Jie(cnjiesbox@hotmail.com)
* Revision:             0.0.0.1
*  Created:             2007-11-26 14:51:60
*   Update:             2007-11-26 14:51:60
*
-----------------------------------------------------------------------------*/
#ifndef __M2EXTRACTOBJECT_H__
#define __M2EXTRACTOBJECT_H__

#include <Windows.h>

#include <string>
#include <ostream>
#include <vector>

class CM2ExtractObject
{
public:
	enum OutputLevel { OL_DEBUG = 0, OL_INFO, OL_WARNING, OL_ERROR };
	typedef std::vector<std::string> TStringVector;
public:
	CM2ExtractObject(const std::string& output = ".\\");
	virtual ~CM2ExtractObject();

	int AddMPQFile(const std::string& file);
	int AddM2Filter(const std::string& filter);
	void ResetData();

	int Extract();

	void OutputStream(std::ostream& os);
protected:
	void OutputInfo(OutputLevel level, const std::string& info) const;
	int CheckPath(const std::string& path) const;
	int ExtractM2FromMPQ(const std::string& mpq);
	int ExtractM2Data(HANDLE hmpq, const std::string& m2, const std::string& output);
	int ConvertM2ToX(const std::string& m2, TStringVector& vctblp);
	int ExtractBLPData(HANDLE hmpq, const std::string& path, const TStringVector& vctblp);
	int ConvertBLPToPNG(const std::string& path, const TStringVector& vctblp);
protected:
	std::string _strOutputRoot;
	TStringVector _vctMPQFile;
	TStringVector _vctM2Filter;
private:
	std::ostream* _pstOutput;
};

#endif
