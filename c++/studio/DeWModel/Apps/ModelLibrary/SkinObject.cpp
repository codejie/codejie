#include "stdafx.h"

#include <sstream>

#include "MPQFileWrapper.h"
#include "SkinObject.h"

int CSkinObject::Load(const std::string& mpq, const std::string &file)
{
	CMPQFileWrapper mf;

	if(mf.Attach(mpq, file) != 0)
		return -1;

	Clear();

	if(LoadHeader(mf, _header) != 0)
		return -1;
	
	if(LoadIndex(mf, _header, _index) != 0)
		return -1;
	if(LoadTriangle(mf, _header, _triangle) != 0)
		return -1;
	if(LoadProperty(mf, _header, _property) != 0)
		return -1;
	if(LoadSubMesh(mf, _header, _submesh) != 0)
		return -1;
	if(LoadTexture(mf, _header, _texture) != 0)
		return -1;

	return 0;
}

void CSkinObject::Clear()
{
	_header.Clear();
	_index.Clear();
	_triangle.Clear();
	_property.Clear();
	_submesh.Clear();
	_texture.Clear();
}

int CSkinObject::LoadHeader(CMPQFileWrapper &mf, Skin::THeader &header)
{
	return header.Read(mf);
}

int CSkinObject::LoadIndex(CMPQFileWrapper &mf, const Skin::THeader& header, Skin::TIndex &index)
{
	return index.Read(mf, header.m_stHeader.m_stIndex.offset, header.m_stHeader.m_stIndex.size);
}

int CSkinObject::LoadTriangle(CMPQFileWrapper &mf, const Skin::THeader &header, Skin::TTriangle &triangle)
{
	return triangle.Read(mf, header.m_stHeader.m_stTriangle.offset, header.m_stHeader.m_stTriangle.size);
}

int CSkinObject::LoadProperty(CMPQFileWrapper &mf, const Skin::THeader &header, Skin::TProperty &property)
{
	return property.Read(mf, header.m_stHeader.m_stProperty.offset, header.m_stHeader.m_stProperty.size);
}

int CSkinObject::LoadSubMesh(CMPQFileWrapper &mf, const Skin::THeader &header, Skin::TSubMesh &submesh)
{
	return submesh.Read(mf, header.m_stHeader.m_stSubMesh.offset, header.m_stHeader.m_stSubMesh.size);
}

int CSkinObject::LoadTexture(CMPQFileWrapper &mf, const Skin::THeader &header, Skin::TTexture &texture)
{
	return texture.Read(mf, header.m_stHeader.m_stTexture.offset, header.m_stHeader.m_stTexture.size);
}

int CSkinObject::OutputHeaderInfo(std::vector<std::pair<std::string,std::string> > &vct) const
{
	vct.clear();

	std::ostringstream ostr;

	ostr << _header.m_stHeader.m_acIdent[0] << _header.m_stHeader.m_acIdent[1] << _header.m_stHeader.m_acIdent[2] << _header.m_stHeader.m_acIdent[3];
	vct.push_back(std::make_pair("Ident", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stIndex.size << "," << _header.m_stHeader.m_stIndex.offset;
	vct.push_back(std::make_pair("Index", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTriangle.size << "," << _header.m_stHeader.m_stTriangle.offset;
	vct.push_back(std::make_pair("Triangle", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stProperty.size << "," << _header.m_stHeader.m_stProperty.offset;
	vct.push_back(std::make_pair("Property", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stSubMesh.size << "," << _header.m_stHeader.m_stSubMesh.offset;
	vct.push_back(std::make_pair("SubMesh", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTexture.size << "," << _header.m_stHeader.m_stTexture.offset;
	vct.push_back(std::make_pair("Texture", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_uiLOD;
	vct.push_back(std::make_pair("LOD", ostr.str()));

	return 0;
}