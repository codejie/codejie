#include "stdafx.h"

#include "M2Structure.h"
#include "SkinStructure.h"

#include "XFileObject.h"

using namespace X;

CXFileObject::CXFileObject()
{
}

CXFileObject::~CXFileObject()
{
}

int CXFileObject::Output(const CM2Object &m2, const CSkinObject &skin, const CTextureObject& texture, const std::wstring &file)
{
	if(InitVertex(m2) != 0)
		return -1;
	if(InitVertexIndex(skin) != 0)
		return -1;
	if(InitTriangle(skin) != 0)
		return -1;
	if(InitTexture(texture) != 0)
		return -1;
	if(InitMesh(skin) != 0)
		return -1;

	std::ofstream ofs;
	if(CreateXFile(file, ofs) == 0)
	{
		OutputMaterial(ofs);
		OutputFrame(ofs);

		CloseXFile(ofs);
	}

	return 0;
}

int CXFileObject::InitVertex(const CM2Object &m2)
{
	for(M2::TVertex::TVector::const_iterator it = m2._vertex.m_vct.begin(); it != m2._vertex.m_vct.end(); ++ it)
	{
		Vertex_t vec;// z-up to y-up : x,y,z = x, -z, y
		vec.m_v3Pos = it->m_v3Pos;
		//vec.m_v3Pos.x = -it->m_v3Pos.x;
		//vec.m_v3Pos.y = it->m_v3Pos.z;
		//vec.m_v3Pos.z = -it->m_v3Pos.y;
		vec.m_v3Normal = it->m_v3Normal;
		//for(int i = 0; i < 4; ++ i)
		//{
		//	if(it->m_acWeight[i] > 0)
		//	{
		//		vec.m_v3Pos += it->m_acWeight[i];
		//		vec.m_v3Normal += it->m_acWeight[i];
		//	}
		//}
		vec.m_v2TexCoord = it->m_v2TextureCoord;
		_vctVertex.push_back(vec);
	}

	return 0;
}

int CXFileObject::InitVertexIndex(const CSkinObject& skin)
{
	for(Skin::TIndex::TVector::const_iterator it = skin._index.m_vct.begin(); it != skin._index.m_vct.end(); ++ it)
	{
		_vctVexIndex.push_back(it->data);
	}
	return 0;
}


int CXFileObject::InitTriangle(const CSkinObject &skin)
{
	for(Skin::TTriangle::TVector::const_iterator it = skin._triangle.m_vct.begin(); it != skin._triangle.m_vct.end();)
	{
		Triangle_t tri;
		tri.m_usA = it->data;
		++ it;
		tri.m_usB = it->data;
		++ it;
		tri.m_usC = it->data;
		++ it;
		_vctTriangle.push_back(tri);
	}
	return 0;
}

int CXFileObject::InitTexture(const CTextureObject& texture)
{
	for(CTextureObject::TTextureVector::const_iterator it = texture.TextureVector().begin(); it != texture.TextureVector().end(); ++ it)
	{
		Texture_t tex;
		tex.m_uiType = it->m_uiType;;
		tex.m_strFileName = it->m_strName + ".png";
		_vctTexture.push_back(tex);
	}

	return 0;
}

int CXFileObject::InitMesh(const CSkinObject &skin)
{
	unsigned short index = 0;
	for(Skin::TSubMesh::TVector::const_iterator it = skin._submesh.m_vct.begin(); it != skin._submesh.m_vct.end(); ++ it)
	{
		TMeshMap::iterator i = _mapMesh.find(it->m_uiID);
		if(i == _mapMesh.end())
		{
			i = _mapMesh.insert(std::make_pair(it->m_uiID, TMeshIndexVector())).first;
		}
		MeshIndex_t mesh;
		mesh.m_usIndex = (index ++);
		mesh.m_usVexStart = it->m_usVertexStart;
		mesh.m_usVexCount = it->m_usVertexCount;
		mesh.m_usTriStart = it->m_usTriangleStart / 3;
		mesh.m_usTriCount = it->m_usTriangleCount / 3;
		mesh.m_usTexture = 0;//skin._texture.m_vct[mesh.m_usIndex].m_usTexture;
		for(Skin::TTexture::TVector::const_iterator t = skin._texture.m_vct.begin(); t != skin._texture.m_vct.end(); ++ t)
		{
			if(t->m_usSubMeshIndex == mesh.m_usIndex)
			{
				mesh.m_usTexture = t->m_usTexture;
				break;
			}
		}

		i->second.push_back(mesh);
	}
	
	return 0;
}

///
int CXFileObject::CreateXFile(const std::wstring& file, std::ofstream& ofs) const
{
	ofs.open(file.c_str(), std::ios::out | std::ios::trunc);
	if(!ofs.is_open())
		return -1;

	//Init flag
	ofs.precision(6);
	//Header
	ofs << STR_X_HEADER << std::endl;

	return ofs.good() ? 0 : -1;
}


void CXFileObject::CloseXFile(std::ofstream &ofs) const
{
	if(ofs.is_open())
		ofs.close();
}

int CXFileObject::OutputMaterial(std::ostream& os) const
{
	int index = 0;
	for(X::TTextureVector::const_iterator it = _vctTexture.begin(); it != _vctTexture.end(); ++ it)
	{
		os << "Material Material" << index ++  << " {" << std::endl;
		os << STR_TAB2 << "0.000000;0.000000;0.000000;1.000000;;" << std::endl;
		os << STR_TAB2 << "0.000000;" << std::endl;
		os << STR_TAB2 << "0.000000;0.000000;0.000000;;" << std::endl;
		os << STR_TAB2 << "0.000000;0.000000;0.000000;;" << std::endl;
		//TextureFilename
		os << STR_TAB2 << "TextureFilename {" << std::endl;
		os << STR_TAB4 << "\"" << it->m_strFileName << "\";" << std::endl;
		os << STR_TAB2 << "}" << std::endl;
		os << "}" << std::endl;
	}

	return 0;
}

//int CXFileObject::OutputFrame(std::ostream &os) const
//{
//	for(TMeshMap::const_iterator it = _mapMesh.begin(); it != _mapMesh.end(); ++ it)
//	{	
//		//Frame
//		os << std::endl << "Frame Frame" << it->first << " {" << std::endl;
//		os << STR_TAB2 << "FrameTransformMatrix {" << std::endl;
//		os << STR_TAB4 << "1.000000,0.000000,0.000000,0.000000," << std::endl;
//		os << STR_TAB4 << "0.000000,1.000000,0.000000,0.000000," << std::endl;
//		os << STR_TAB4 << "0.000000,0.000000,1.000000,0.000000," << std::endl;
//		os << STR_TAB4 << "0.000000,0.000000,0.000000,1.000000;;" << std::endl;
//		os << STR_TAB2 << "} " << std::endl;
//
//		//Mesh
//		OutputMesh(os, it->second);
//
//		//Frame close
//		os << "}" << std::endl;
//	}
//
//	return os.good() ? 0 : -1;
//}
//
//int CXFileObject::OutputMesh(std::ostream &os, const X::TMeshIndexVector& vct) const
//{
//	os << STR_TAB2 << "Mesh {" << std::endl;
//	//Number
//	unsigned short vnum = 0, tnum = 0;
//	for(TMeshIndexVector::const_iterator i = vct.begin(); i != vct.end(); ++ i)
//	{
//		vnum += i->m_usVexCount;
//		tnum += i->m_usTriCount;
//	}
//	//Vertex
//	os << STR_TAB4 << vnum << ";" << std::endl;
//	for(TMeshIndexVector::const_iterator i = vct.begin(); i != vct.end(); ++ i)
//	{
//		for(unsigned short index = 0; index < i->m_usVexCount;)
//		{
//			os << STR_TAB4;
//			OutputVertex(os, _vctVertex[index + i->m_usVexStart]);
//			if((++ index) != i->m_usVexCount)
//				os << "," << std::endl;
//			else
//				os << ";" << std::endl;
//		}
//	}
//	//Triangle
//	os << STR_TAB4 << tnum << ";" << std::endl;
//	unsigned short vbase = 0;
//	for(TMeshIndexVector::const_iterator i = vct.begin(); i != vct.end(); ++ i)
//	{
//		for(unsigned short index = 0; index < i->m_usTriCount;)
//		{
//			os << STR_TAB4;
//			OutputTriangle(os, _vctTriangle[index + i->m_usTriStart], i->m_usVexStart - vbase);
//			if((++ index) != i->m_usTriCount)
//				os << "," << std::endl;
//			else
//				os << ";" << std::endl;
//		}
//		vbase += i->m_usVexCount;
//	}
//	//MeshNormal
//	OutputMeshNormal(os, vnum, tnum, vct);
//
//	os << STR_TAB2 << "}" << std::endl;//Mesh
//	
//	return 0;
//}
//
//int CXFileObject::OutputMeshNormal(std::ostream &os, unsigned short vnum, unsigned short tnum, const X::TMeshIndexVector &vct) const
//{
//	os << STR_TAB4 << "MeshNormals { " << std::endl;
//	
//		//Vertex
//		os << STR_TAB6 << vnum << ";" << std::endl;
//		for(TMeshIndexVector::const_iterator i = vct.begin(); i != vct.end(); ++ i)
//		{
//			for(unsigned short index = 0; index < i->m_usVexCount;)
//			{
//				os << STR_TAB6;
//				OutputVertexNormal(os, _vctVertex[index + i->m_usVexStart]);
//				if((++ index) != i->m_usVexCount)
//					os << "," << std::endl;
//				else
//					os << ";" << std::endl;
//			}
//		}
//		//Triangle
//		os << STR_TAB4 << tnum << ";" << std::endl;
//		unsigned short vbase = 0;
//		for(TMeshIndexVector::const_iterator i = vct.begin(); i != vct.end(); ++ i)
//		{
//			for(unsigned short index = 0; index < i->m_usTriCount;)
//			{
//				os << STR_TAB4;
//				OutputTriangleNormal(os, _vctTriangle[index + i->m_usTriStart], i->m_usVexStart - vbase);
//				if((++ index) != i->m_usTriCount)
//					os << "," << std::endl;
//				else
//					os << ";" << std::endl;
//			}
//			vbase += i->m_usVexCount;
//		}
//		os << STR_TAB4 << "}" << std::endl;
//	return 0;
//}


int CXFileObject::OutputFrame(std::ostream &os) const
{
	for(TMeshMap::const_iterator it = _mapMesh.begin(); it != _mapMesh.end(); ++ it)
	{	
		for(TMeshIndexVector::const_iterator i = it->second.begin(); i != it->second.end(); ++ i)
		{
			OutputMesh(os, it->first, *i);
		}
	}

	return os.good() ? 0 : -1;
}

int CXFileObject::OutputMesh(std::ostream &os, unsigned short id, const X::MeshIndex_t& mesh) const
{
	//Frame
	os << std::endl << "Frame Frame" << id << "_" << mesh.m_usIndex << " {" << std::endl;
	os << STR_TAB2 << "FrameTransformMatrix {" << std::endl;
	os << STR_TAB4 << "1.000000,0.000000,0.000000,0.000000," << std::endl;
	os << STR_TAB4 << "0.000000,1.000000,0.000000,0.000000," << std::endl;
	os << STR_TAB4 << "0.000000,0.000000,1.000000,0.000000," << std::endl;
	os << STR_TAB4 << "0.000000,0.000000,0.000000,1.000000;;" << std::endl;
	os << STR_TAB2 << "} " << std::endl;


	os << STR_TAB2 << "Mesh {" << std::endl;
	//Vertex
	os << STR_TAB4 << mesh.m_usVexCount << ";" << std::endl;
	for(unsigned short index = 0; index < mesh.m_usVexCount;)
	{
		os << STR_TAB4;
		OutputVertex(os,  _vctVertex[_vctVexIndex[index + mesh.m_usVexStart]]);
		if((++ index) != mesh.m_usVexCount)
			os << "," << std::endl;
		else
			os << ";" << std::endl;
	}
	//Triangle
	os << STR_TAB4 << mesh.m_usTriCount << ";" << std::endl;
	for(unsigned short index = 0; index < mesh.m_usTriCount;)
	{
		os << STR_TAB4;
		OutputTriangle(os, _vctTriangle[index + mesh.m_usTriStart], mesh.m_usVexStart);
		if((++ index) != mesh.m_usTriCount)
			os << "," << std::endl;
		else
			os << ";" << std::endl;
	}

	//MeshNormal
	OutputMeshNormal(os, mesh);
	//MeshTextureCoords
	OutputMeshTextureCoord(os, mesh);
	//MeshMaterialList
	OutputMeshMaterialList(os, mesh);

	//Mesh close
	os << STR_TAB2 << "}" << std::endl;
	

	//Frame close
	os << "}" << std::endl;

	return 0;
}

int CXFileObject::OutputMeshNormal(std::ostream &os, const X::MeshIndex_t &mesh) const
{
	os << STR_TAB4 << "MeshNormals { " << std::endl;
	
	//Vertex
	os << STR_TAB6 << mesh.m_usVexCount << ";" << std::endl;
	for(unsigned short index = 0; index < mesh.m_usVexCount;)
	{
		os << STR_TAB6;
		OutputVertexNormal(os, _vctVertex[_vctVexIndex[index + mesh.m_usVexStart]]);
		if((++ index) != mesh.m_usVexCount)
			os << "," << std::endl;
		else
			os << ";" << std::endl;
	}

	//Triangle
	os << STR_TAB6 << mesh.m_usTriCount << ";" << std::endl;
	for(unsigned short index = 0; index < mesh.m_usTriCount;)
	{
		os << STR_TAB6;
		OutputTriangleNormal(os, _vctTriangle[index + mesh.m_usTriStart], mesh.m_usVexStart);
		if((++ index) != mesh.m_usTriCount)
			os << "," << std::endl;
		else
			os << ";" << std::endl;
	}
	os << STR_TAB4 << "}" << std::endl;
	return 0;
}

int CXFileObject::OutputMeshTextureCoord(std::ostream &os, const X::MeshIndex_t &mesh) const
{
	os << STR_TAB4 << "MeshTextureCoords {" << std::endl;
	os << STR_TAB6 << mesh.m_usVexCount << ";" << std::endl;
	for(unsigned short index = 0; index < mesh.m_usVexCount;)
	{
		os << STR_TAB6;
		OutputVertexTextureCoord(os, _vctVertex[_vctVexIndex[index + mesh.m_usVexStart]]);
		if((++ index) != mesh.m_usVexCount)
			os << "," << std::endl;
		else
			os << ";" << std::endl;
	}

	os << STR_TAB4 << "}" << std::endl;

	return 0;
}

int CXFileObject::OutputMeshMaterialList(std::ostream& os, const X::MeshIndex_t& mesh) const
{
	os << STR_TAB4 << "MeshMaterialList {" << std::endl;

	os << STR_TAB6 << 1 << ";" << std::endl;
	os << STR_TAB6 << mesh.m_usTriCount << ";" << std::endl;
	for(unsigned short index = 0; index < mesh.m_usTriCount;)
	{
		os << STR_TAB6 << "0";
		if((++ index) != mesh.m_usTriCount)
			os << "," << std::endl;
		else
			os << ";" << std::endl;
	}

	//Material
	os << STR_TAB6 << "{ Material" << mesh.m_usTexture << "}" << std::endl;

	//os << STR_TAB6 << "Material {" << std::endl;
	//os << STR_TAB8 << "0.000000;0.000000;0.000000;1.000000;;" << std::endl;
	//os << STR_TAB8 << "0.000000;" << std::endl;
	//os << STR_TAB8 << "0.000000;0.000000;0.000000;;" << std::endl;
	//os << STR_TAB8 << "0.000000;0.000000;0.000000;;" << std::endl;
	////TextureFilename
	//os << STR_TAB8 << "TextureFilename {" << std::endl;
	//os << STR_TAB10 << "\"" <<_vctTexture[mesh.m_usTexture].m_strFileName << "\";" << std::endl;
	//os << STR_TAB8 << "}" << std::endl;

	//os << STR_TAB6 << "}" << std::endl;

	os << STR_TAB4 << "}" << std::endl;

	return 0;
}

////////
void CXFileObject::OutputVertex(std::ostream &os, const Vertex_t &vex) const
{
	os << vex.m_v3Pos.x << ";" << vex.m_v3Pos.y << ";" << vex.m_v3Pos.z << ";";
}

void CXFileObject::OutputTriangle(std::ostream &os, const Triangle_t &tri, unsigned short base) const
{
	os << "3;" << tri.m_usA - base << "," << tri.m_usB - base << "," << tri.m_usC - base << ";";
}

void CXFileObject::OutputVertexNormal(std::ostream &os, const Vertex_t &vex) const
{
	os << vex.m_v3Normal.x << ";" << vex.m_v3Normal.y << ";" << vex.m_v3Normal.z << ";";
}

void CXFileObject::OutputTriangleNormal(std::ostream &os, const Triangle_t &tri, unsigned short base) const
{
	os << "3;" << tri.m_usA - base << "," << tri.m_usB - base << "," << tri.m_usC - base << ";";
}

void CXFileObject::OutputVertexTextureCoord(std::ostream &os, const X::Vertex_t &vex) const
{
	os << vex.m_v2TexCoord.x << ";" << vex.m_v2TexCoord.y << ";";	
}
