#ifndef __SKINOBJECT_H__
#define __SKINOBJECT_H__

#include <string>
#include <utility>
#include <vector>

#include "SkinStructure.h"

class CSkinObject
{
public:
	CSkinObject() {}
	virtual ~CSkinObject() {}

	int Load(const std::string& mpq, const std::string& file);

	int OutputHeaderInfo(std::vector<std::pair<std::string, std::string> >& vct) const;

protected:
	void Clear();
	int LoadHeader(CMPQFileWrapper& mf, Skin::THeader& header);
	int LoadIndex(CMPQFileWrapper& mf, const Skin::THeader& header, Skin::TIndex& index);
	int LoadTriangle(CMPQFileWrapper& mf, const Skin::THeader& header, Skin::TTriangle& triangle);
	int LoadProperty(CMPQFileWrapper& mf, const Skin::THeader& header, Skin::TProperty& property);
	int LoadSubMesh(CMPQFileWrapper& mf, const Skin::THeader& header, Skin::TSubMesh& submesh);
	int LoadTexture(CMPQFileWrapper& mf, const Skin::THeader& header, Skin::TTexture& texture);
public:
	Skin::THeader _header;
	Skin::TIndex _index;
	Skin::TTriangle _triangle;
	Skin::TProperty _property;
	Skin::TSubMesh _submesh;
	Skin::TTexture _texture;
};

#endif
