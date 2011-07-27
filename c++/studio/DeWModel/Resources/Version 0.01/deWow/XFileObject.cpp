#include <fstream>

#include "XFileObject.h"


const std::string CXFileObject::FMT_HEADER = "xof 0303txt 0032";

CXFileObject::CXFileObject()
{
}

CXFileObject::~CXFileObject()
{
}

int CXFileObject::AddVertex(float x, float y, float z)
{
	return AddVertex(TVertexType(x, y, z));
}

int CXFileObject::AddVertex(const CXFileObject::TVertexType &vertex)
{
	_vctVertex.push_back(vertex);
	return 0;
}

int CXFileObject::AddTriangle(unsigned int a, unsigned int b, unsigned int c)
{
	return AddTriangle(TTriangleType(a, b, c));
}

int CXFileObject::AddTriangle(const TTriangleType& triangle)
{
	_vctTriangle.push_back(triangle);
	return 0;
}

int CXFileObject::AddMeshNormal(float x, float y, float z)
{
	return AddMeshNormal(TMeshNormalType(x, y, z));
}

int CXFileObject::AddMeshNormal(const TMeshNormalType& normal)
{
	_vctMeshNormal.push_back(normal);
	return 0;
}

int CXFileObject::AddTextureCoord(float x, float y)
{
	return AddTextureCoord(TTextureCoordType(x, y));
}

int CXFileObject::AddTextureCoord(const TTextureCoordType& coord)
{
	_vctTextureCoord.push_back(coord);
	return 0;
}

int CXFileObject::AddMaterial(const CXFileObject::TMaterialType &meterial)
{
	_vctMaterial.push_back(meterial);
	return 0;
}

int CXFileObject::AddMaterialIndex(unsigned short material)
{
	_vctMaterialIndex.push_back(material);
	return 0;
}

int CXFileObject::AddMesh(const TSubMeshType& mesh)
{
	_vctSubMesh.push_back(mesh);
	return 0;
}

int CXFileObject::AddMeshTextureIndex(unsigned short mesh, unsigned short texture)
{
	_vctSubMesh[mesh].m_setTextureIndex.insert(texture);
	return 0;
}

void CXFileObject::Clear()
{
	_vctVertex.clear();
	_vctTriangle.clear();
	_vctMeshNormal.clear();
	_vctTextureCoord.clear();
	_vctMaterial.clear();

	_vctSubMesh.clear();
}

int CXFileObject::Output(const std::string &filename) const
{
	std::ofstream ofile(filename.c_str());
	if(!ofile.is_open())
		return -1;

	OutputHeader(ofile);
	OutputMeshTemplate(ofile);

	ofile.close();

	return 0;
}

int CXFileObject::OutputHeader(std::ofstream& ofile) const
{
	//header
	ofile << FMT_HEADER;
	return 0;
}

int CXFileObject::OutputMaterialTemplate(std::ofstream& ofile) const
{
	return 0;
}

int CXFileObject::OutputMeshTemplate(std::ofstream &ofile) const
{
	//Mesh Template
	ofile.precision(6);
	//Mesh
	unsigned int index = 0;
	for(TSubMeshVector::const_iterator it = _vctSubMesh.begin(); it != _vctSubMesh.end(); ++ it, ++ index)
	{
		ofile << "\n\n//M2Mesh Object " << index << " begin.";
		ofile << "\nMesh M2Mesh" << index << " {";

		//vertex
		ofile << "\n\t" << it->m_vctVertexIndex.size() << ";";
		for(TMeshVertexIndexVector::const_iterator i = it->m_vctVertexIndex.begin(); i != it->m_vctVertexIndex.end();)
		{
			ofile << "\n\t" << std::fixed << _vctVertex[*i].m_fX << ";" << _vctVertex[*i].m_fY << ";" << _vctVertex[*i].m_fZ << ";";
			if((++ i) != it->m_vctVertexIndex.end())
				ofile << ",";
			else
				ofile << ";";
		}
		//Face
		ofile << "\n\n\t" << it->m_vctTriangleIndex.size() / 3 << ";";
		for(TMeshTriangleIndexVector::const_iterator i = it->m_vctTriangleIndex.begin(); i != it->m_vctTriangleIndex.end();)
		{
			ofile << "\n\t3;" << _vctTriangle[(*i) / 3].m_uiIndexA - it->m_vctVertexIndex[0] << ",";
			++ i;
				ofile << _vctTriangle[(*i) / 3].m_uiIndexB - it->m_vctVertexIndex[0] << ",";
			++ i;
				ofile << _vctTriangle[(*i) / 3].m_uiIndexC - it->m_vctVertexIndex[0] << ";";
			++ i;
			if(i != it->m_vctTriangleIndex.end())
				ofile << ",";
			else
				ofile << ";";
		}
		//MeshNormals
		ofile << "\n\n\tMeshNormals {";
		ofile << "\n\t\t" << it->m_vctVertexIndex.size() << ";";
		for(TMeshVertexIndexVector::const_iterator i = it->m_vctVertexIndex.begin(); i != it->m_vctVertexIndex.end();)
		{
			ofile << "\n\t\t" << std::fixed << _vctMeshNormal[*i].m_fX << ";" << _vctMeshNormal[*i].m_fY << ";" << _vctMeshNormal[*i].m_fZ << ";";
			if((++ i) != it->m_vctVertexIndex.end())
				ofile << ",";
			else
				ofile << ";";
		}

		ofile << "\n\n\t\t" << it->m_vctTriangleIndex.size() / 3 << ";";
		for(TMeshTriangleIndexVector::const_iterator i = it->m_vctTriangleIndex.begin(); i != it->m_vctTriangleIndex.end();)
		{
			ofile << "\n\t\t3;" << _vctTriangle[(*i) / 3].m_uiIndexA - it->m_vctVertexIndex[0] << ",";
			++ i;
			ofile << _vctTriangle[(*i) / 3].m_uiIndexB - it->m_vctVertexIndex[0] << ",";
			++ i;
			ofile << _vctTriangle[(*i) / 3].m_uiIndexC - it->m_vctVertexIndex[0] << ";";
			++ i;
			if(i != it->m_vctTriangleIndex.end())
				ofile << ",";
			else
				ofile << ";";
		}
		ofile << "\n\t}";
	
		//TextureCoords
		ofile << "\n\n\tMeshTextureCoords {";
		ofile << "\n\t\t" << it->m_vctVertexIndex.size() << ";";
		for(TMeshVertexIndexVector::const_iterator i = it->m_vctVertexIndex.begin(); i != it->m_vctVertexIndex.end();)
		{
			ofile << "\n\t\t" << _vctTextureCoord[*i].m_fX << ";" << _vctTextureCoord[*i].m_fY << ";";
			if((++ i) != it->m_vctVertexIndex.end())
				ofile << ",";
			else
				ofile << ";";
		}
		ofile << "\n\t}";
	
		//MeshMetrialList
		ofile << "\n\n\tMeshMaterialList {";
		ofile << "\n\t\t" << it->m_setTextureIndex.size() << ";";
		ofile << "\n\t\t" << it->m_vctTriangleIndex.size() / 3 << ";";
		for(TMeshTriangleIndexVector::const_iterator i = it->m_vctTriangleIndex.begin(); i != it->m_vctTriangleIndex.end();)
		{
			ofile << "\n\t\t" << 0;
			i += 3;
			if(i != it->m_vctTriangleIndex.end())
				ofile << ",";
			else
				ofile << ";";
		}

		for(TMeshTextureIndexSet::const_iterator i = it->m_setTextureIndex.begin(); i != it->m_setTextureIndex.end(); ++ i)
		{
			ofile << "\n\t\tMaterial {";
			ofile << "\n\t\t\t" << _vctMaterial[_vctMaterialIndex[*i]].m_stFaceColor.m_fRed << ";" << _vctMaterial[_vctMaterialIndex[*i]].m_stFaceColor.m_fGreen << ";" << _vctMaterial[_vctMaterialIndex[*i]].m_stFaceColor.m_fBlue << ";" << _vctMaterial[_vctMaterialIndex[*i]].m_stFaceColor.m_fAlpha << ";;";
			ofile << "\n\t\t\t" << _vctMaterial[_vctMaterialIndex[*i]].m_fPower << ";";
			ofile << "\n\t\t\t" << _vctMaterial[_vctMaterialIndex[*i]].m_stSpecularColor.m_fRed << ";" << _vctMaterial[_vctMaterialIndex[*i]].m_stSpecularColor.m_fGreen << ";" << _vctMaterial[_vctMaterialIndex[*i]].m_stSpecularColor.m_fBlue << ";;";
			ofile << "\n\t\t\t" << _vctMaterial[_vctMaterialIndex[*i]].m_stEmissiveColor.m_fRed << ";" << _vctMaterial[_vctMaterialIndex[*i]].m_stEmissiveColor.m_fGreen << ";" << _vctMaterial[_vctMaterialIndex[*i]].m_stEmissiveColor.m_fBlue << ";;";
			//TextureFilename
			std::string str = _vctMaterial[_vctMaterialIndex[*i]].m_strFilename;
			if(!str.empty())
			{
				std::string::size_type pos = str.find_last_of("\\");
				if(pos != std::string::npos)
					str = str.substr(pos + 1);
				str = str + ".PNG";
				ofile << "\n\t\t\tTextureFilename {";
				ofile << "\n\t\t\t\t\"" << str << "\";";
				ofile << "\n\t\t\t}";
			}
			ofile << "\n\t\t}";
		}
		ofile << "\n\t}";


		ofile << "\n}";
		ofile << "\n//M2Mesh Object " << index << " end.";
	}

	return 0;
///////////////////////////////////////////////////////////
	ofile << "\nMesh M2Mesh {";
	//Vertex
	ofile << "\n\t" << _vctVertex.size() << ";";
	for(TVertexVector::const_iterator it = _vctVertex.begin(); it != _vctVertex.end();)
	{
		ofile << "\n\t" << std::fixed << it->m_fX << ";" << it->m_fY << ";" << it->m_fZ << ";";
		if((++ it) != _vctVertex.end())
			ofile << ",";
		else
			ofile << ";";
	}
	//Face
	ofile << "\n\n\t" << _vctTriangle.size() << ";";
	for(TTriangleVector::const_iterator it = _vctTriangle.begin(); it != _vctTriangle.end();)
	{
		ofile << "\n\t3;" << it->m_uiIndexA << "," << it->m_uiIndexB << "," << it->m_uiIndexC << ";";
		if((++ it) != _vctTriangle.end())
			ofile << ",";
		else
			ofile << ";";
	}
	//MeshNormals
	ofile << "\n\n\tMeshNormals {";
	ofile << "\n\t\t" << _vctMeshNormal.size() << ";";
	for(TVertexVector::const_iterator it = _vctMeshNormal.begin(); it != _vctMeshNormal.end();)
	{
		ofile << "\n\t\t" << std::fixed << it->m_fX << ";" << it->m_fY << ";" << it->m_fZ << ";";
		if((++ it) != _vctMeshNormal.end())
			ofile << ",";
		else
			ofile << ";";
	}
	ofile << "\n\n\t\t" << _vctTriangle.size() << ";";
	for(TTriangleVector::const_iterator it = _vctTriangle.begin(); it != _vctTriangle.end();)
	{
		ofile << "\n\t\t3;" << it->m_uiIndexA << "," << it->m_uiIndexB << "," << it->m_uiIndexC << ";";
		if((++ it) != _vctTriangle.end())
			ofile << ",";
		else
			ofile << ";";
	}
	ofile << "\n\t}";
	//TextureCoords
	ofile << "\n\n\tMeshTextureCoords {";
	ofile << "\n\t\t" << _vctTextureCoord.size() << ";";
	for(TTextureCoordVector::const_iterator it = _vctTextureCoord.begin(); it != _vctTextureCoord.end();)
	{
		ofile << "\n\t\t" << it->m_fX << ";" << it->m_fY << ";";
		if((++ it) != _vctTextureCoord.end())
			ofile << ",";
		else
			ofile << ";";
	}
	ofile << "\n\t}";
	
	//MeshMetrialList
	ofile << "\n\n\tMeshMaterialList {";
	ofile << "\n\t\t" << _vctMaterial.size() << ";";
//	ofile << "\n\t\t" << _mapMaterial.size() << ";";
	//for(TMaterialMap::const_iterator it = _mapMaterial.begin(); it != _mapMaterial.end();)
	//{
	//	ofile << "\n\t\t" << it->second;
	//	if((++ it) != _mapMaterial.end())
	//		ofile << ",";
	//	else
	//		ofile << ";";
	//}
	//Material
	for(TMaterialVector::const_iterator it = _vctMaterial.begin(); it != _vctMaterial.end(); ++ it)
	{
		ofile << "\n\t\tMaterial {";
		ofile << "\n\t\t\t" << it->m_stFaceColor.m_fRed << ";" << it->m_stFaceColor.m_fGreen << ";" << it->m_stFaceColor.m_fBlue << ";" << it->m_stFaceColor.m_fAlpha << ";;";
		ofile << "\n\t\t\t" << it->m_fPower << ";";
		ofile << "\n\t\t\t" << it->m_stSpecularColor.m_fRed << ";" << it->m_stSpecularColor.m_fGreen << ";" << it->m_stSpecularColor.m_fBlue << ";;";
		ofile << "\n\t\t\t" << it->m_stEmissiveColor.m_fRed << ";" << it->m_stEmissiveColor.m_fGreen << ";" << it->m_stEmissiveColor.m_fBlue << ";;";
		//TextureFilename
		std::string str = it->m_strFilename;
		if(!str.empty())
		{
			std::string::size_type pos = str.find_last_of("\\");
			if(pos != std::string::npos)
				str = str.substr(pos + 1);
			pos = str.find(".BLP");
			if(pos != std::string::npos)
				str = str.substr(0, pos) + ".TGA";
			str = str + ".PNG";
			ofile << "\n\t\t\tTextureFilename {";
			ofile << "\n\t\t\t\t\"" << str << "\";";
			//just for test
	//		ofile << "\n\t\t\t\t\"" << "BE_Meat.jpg" << "\";";
			ofile << "\n\t\t\t}";
		}

		ofile << "\n\t\t}";

	}

	ofile << "\n\t}";

	ofile << "\n}";

	return 0;
}
