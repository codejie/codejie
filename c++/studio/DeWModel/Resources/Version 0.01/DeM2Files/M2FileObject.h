#ifndef __M2FILEOBJECT_H__
#define __M2FILEOBJECT_H__

#include <string>
#include <exception>

#include "M2Object.h"
#include "FileBuffer.h"

class CM2FileObjectException : public std::exception
{
public:
	CM2FileObjectException(const std::string& what)
		: exception(what.c_str())
	{
	}
	CM2FileObjectException(const char* const& what)
		: exception(what)
	{
	}
};

class CM2FileObject
{
public:
	CM2FileObject();
	virtual ~CM2FileObject();

	int Load(const std::string& filename);

	void Show(std::ostream& os) const;
protected:
	int LoadHeader(CFileBuffer& fb);
	int LoadName(CFileBuffer& fb);
	int LoadVertex(CFileBuffer& fb);
	int LoadView(CFileBuffer& fb);
	int LoadBone(CFileBuffer& fb);
	int LoadTextureUnit(CFileBuffer& fb);
	int LoadTextureTable(CFileBuffer& fb);
	int LoadTexture(CFileBuffer& fb);
public:
	std::string m_strFilename;
	M2::CHeader m_stHeader;
	M2::CName m_stName;
	M2::TVertexVector m_vctVertex;
	M2::TViewVector m_vctView;
	M2::TBoneVector m_vctBone;
	M2::TTextureUnitVector m_vctTextureUnit;
	M2::TTextureTableItemVector m_vctTextureTable;
	M2::TTextureVector m_vctTexture;
};

#endif
