#include "stdafx.h"

#include "Toolkit.h"

#include "XFileObject.h"
#include "BLPFileObject.h"
#include "ModelObject.h"

CModelObject::CModelObject()
{
}

CModelObject::~CModelObject()
{
}

int CModelObject::Init(const std::wstring& path, const std::wstring& dbcfield, const std::wstring& wdbfield, bool findmpq)
{
	_strDBCField = dbcfield;
	_strWDBField = wdbfield;
	_bFindMPQ = findmpq;

	_vctMPQ.clear();

	if(ScanMPQPath(path) != 0)
		return -1;
	return _objTexture.Init(_vctMPQ, _strDBCField, _strWDBField);
}

int CModelObject::LoadModel(const std::wstring &mpq, const std::string &m2)
{
	_strMPQ = mpq;
	_strM2 = m2;


	std::string skinfile = _strM2;
	std::string::size_type pos = skinfile.find_last_of(".");
	if(pos == std::string::npos)
		return -1;
	skinfile = skinfile.substr(0, pos);
	skinfile += "00.skin";

	if(LoadM2Object(_strMPQ, _strM2) != 0)
		return -1;
	if(LoadSkinObject(_strMPQ, skinfile) != 0)
		return -1;
	if(LoadTextureObject(_objM2, _strM2) != 0)
		return -1;

	//if(ExtractBLPFile(mpq, blpfile) != 0)
	//	return -1;

	return 0;
}

int CModelObject::ToXFile(const std::wstring &file)
{
	std::wstring path = file;
	std::wstring::size_type pos = path.find_last_of(L"\\");
	if(pos != std::wstring::npos)
	{
		path = path.substr(0, pos + 1);
	}
	else
	{
		path = L"";
	}

	CXFileObject x;
	if(x.Output(_objM2, _objSkin, _objTexture, file) == 0)
	{
		_objTexture.Extract(path);

		return 0;
	}
	return -1;
}

int CModelObject::LoadM2Object(const std::wstring& mpq, const std::string& file)
{
	std::string m = Toolkit::WString2String(mpq);
	std::string f = file;
	return _objM2.Load(m, f);
}

int CModelObject::LoadSkinObject(const std::wstring& mpq, const std::string& file)
{
	std::string m = Toolkit::WString2String(mpq);
	std::string f = file;
	return _objSkin.Load(m, f);
}

int CModelObject::LoadTextureObject(const CM2Object& m2, const std::string& file)
{
	return _objTexture.LoadTexture(m2._texture, file, m2._name.m_strName);
}

int CModelObject::ExtractBLPFile(const std::wstring& mpq, const std::string& blp, const std::wstring& png)
{
	std::string m = Toolkit::WString2String(mpq);
	std::string b = blp;
	std::string p = Toolkit::WString2String(png);
	CBLPFileObject obj;
	
	int ret = obj.Extract2PNG(m, b, p);
	if(ret != 0 && _bFindMPQ)
	{
		for(TMPQVector::iterator it	= _vctMPQ.begin(); it != _vctMPQ.end(); ++ it)
		{
			m = Toolkit::WString2String(*it);
			ret = obj.Extract2PNG(m, b, p);
			if(ret == 0)
			{
				if(it != _vctMPQ.begin())
					std::swap(_vctMPQ.begin(), it);
				break;
			}
		}
	}

	return ret;
//	return obj.Extract2PNG(m, b, p);
}

int CModelObject::ScanMPQPath(const std::wstring &path)
{
	std::wstring str = path + L"\\*";
	WIN32_FIND_DATA fdata;
	memset(&fdata, 0, sizeof(WIN32_FIND_DATA));
	HANDLE h = ::FindFirstFile(str.c_str(), &fdata);
	if(h == INVALID_HANDLE_VALUE)
		return -1;
	do
	{
		if((fdata.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) == FILE_ATTRIBUTE_DIRECTORY)
		{
			if((memcmp(fdata.cFileName, ".", 1) != 0) && (memcmp(fdata.cFileName, "..", 2)))
				ScanMPQPath(path + L"\\" + fdata.cFileName);
		}
		else if((fdata.dwFileAttributes & FILE_ATTRIBUTE_ARCHIVE) == FILE_ATTRIBUTE_ARCHIVE)
		{
			str = fdata.cFileName;
			//std::wstring::size_type pos = str.find(L".MPQ"); 
			if(str.find(L".MPQ") != std::wstring::npos)
				_vctMPQ.push_back(path + L"\\" + str);
		}
	}while(FindNextFile(h, &fdata) == TRUE);
	::FindClose(h);
	return 0;
}

///////////////////////
int CModelObject::OutputM2HeaderInfo(std::vector<std::pair<std::string,std::string> > &vct) const
{
	return _objM2.OutputHeaderInfo(vct);
}

int CModelObject::OutputSkinHeaderInfo(std::vector<std::pair<std::string,std::string> > &vct) const
{
	return _objSkin.OutputHeaderInfo(vct);
}

int CModelObject::OutputTextureInfo(std::vector<CTextureObject::Texture_t>& vct) const
{
	return _objTexture.OutputInfo(vct);
}

