#ifndef __MODELOBJECT_H__
#define __MODELOBJECT_H__

#include "M2Object.h"
#include "SkinObject.h"
#include "TextureObject.h"

#ifdef MODELLIBRARY_EXPORTS
#define MODELLIBRARY_API __declspec(dllexport)
#else
#define MODELLIBRARY_API __declspec(dllimport)
#endif


class MODELLIBRARY_API CModelObject
{
public:
	CModelObject();
	virtual ~CModelObject();

	int Init(const std::wstring& path, const std::wstring& dbcfield, const std::wstring& wdbfield, bool findmpq = true);

	int LoadModel(const std::wstring& mpq, const std::string& m2);
	int ToXFile(const std::wstring& file);

	int OutputM2HeaderInfo(std::vector<std::pair<std::string, std::string> >& vct) const;
	int OutputSkinHeaderInfo(std::vector<std::pair<std::string, std::string> >& vct) const;
	int OutputTextureInfo(std::vector<CTextureObject::Texture_t>& vct) const;
private:
	int LoadM2Object(const std::wstring& mpq, const std::string& file);
	int LoadSkinObject(const std::wstring& mpq, const std::string& file);
	int LoadTextureObject(const CM2Object& m2, const std::string& file);
	int ExtractBLPFile(const std::wstring& mpq, const std::string& blp, const std::wstring& png);
private:
	std::wstring _strMPQ;
	std::string _strM2;
	CM2Object _objM2;
	CSkinObject _objSkin;
	CTextureObject _objTexture;
private:
	std::wstring _strDBCField;
	std::wstring _strWDBField;
	bool _bFindMPQ;
	typedef std::vector<std::wstring> TMPQVector;
	TMPQVector _vctMPQ;
private:
	int ScanMPQPath(const std::wstring& path);
};


//typedef class CModelObject MODELLIBRARY_API TAPI;

#endif
