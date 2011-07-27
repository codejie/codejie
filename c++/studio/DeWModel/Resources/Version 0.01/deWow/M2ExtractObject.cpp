/*----------------------------------------------------------------------------
*
*  Project:             Catchfly_Server
*     File:             M2ExtractObject.cpp
*   Author:             Jie(cnjiesbox@hotmail.com)
* Revision:             0.0.0.1
*  Created:             2007-11-26 15:11:60
*   Update:             2007-11-26 15:11:60
*
-----------------------------------------------------------------------------*/
#include <sys/types.h>
#include <sys/stat.h>
#include <direct.h>
#include <stdlib.h>


#include "StormLib.h"

#include "Toolkit.h"

#include "M2FileObject.h"
#include "XFileObject.h"
#include "BLPFileObject.h"
#include "M2ExtractObject.h"

CM2ExtractObject::CM2ExtractObject(const std::string& output /* =  */)
: _strOutputRoot(output)
, _pstOutput(NULL)
{
}

CM2ExtractObject::~CM2ExtractObject()
{
}

void CM2ExtractObject::OutputStream(std::ostream &os)
{
	_pstOutput = &os;
}

void CM2ExtractObject::OutputInfo(OutputLevel level, const std::string& info) const
{
	if(_pstOutput != NULL)
		*_pstOutput << info;
}

int CM2ExtractObject::CheckPath(const std::string &path) const
{
	struct _stat st;
	if(_stat(path.c_str(), &st) != 0)
	{
		if(_mkdir(path.c_str()) != 0)
			return -1;
	}
	else
	{
		if((st.st_mode & _S_IFDIR) != _S_IFDIR)
			return -1;
	}
	return 0;	
}

int CM2ExtractObject::AddMPQFile(const std::string& file)
{
	_vctMPQFile.push_back(file);
	return 0;
}

int CM2ExtractObject::AddM2Filter(const std::string &filter)
{
	_vctM2Filter.push_back(filter);
	return 0;
}

void CM2ExtractObject::ResetData()
{
	_vctMPQFile.clear();
	_vctM2Filter.clear();
}

int CM2ExtractObject::Extract()
{
	for(TStringVector::const_iterator it = _vctMPQFile.begin(); it != _vctMPQFile.end(); ++ it)
	{
		if(ExtractM2FromMPQ(*it) != 0)
		{
			OutputInfo(OL_WARNING, "\nExtract mpq file failed.");
		}
	}
	return 0;
}

int CM2ExtractObject::ExtractM2FromMPQ(const std::string &mpq)
{
	HANDLE hmpq;
	HANDLE hFile;
	
	if(!SFileOpenArchive(mpq.c_str(), 0, 0, &hmpq))
		return -1;

	for(TStringVector::const_iterator it = _vctM2Filter.begin(); it != _vctM2Filter.end(); ++ it)
	{
		SFILE_FIND_DATA sf;
		HANDLE hfind = SFileFindFirstFile(hmpq, it->c_str(), &sf, NULL);
		if(hfind == NULL)
		{
			SFileFindClose(hmpq);
			return -1;
		}
		while(1)
		{
			if(ExtractM2Data(hmpq, sf.cFileName, _strOutputRoot) != 0)
			{
				OutputInfo(OL_WARNING, "\nExtract M2 data failed.");
			}
			if(!SFileFindNextFile(hfind, &sf))
				break;
		}
		SFileFindClose(hfind);
	}
	SFileCloseArchive(hmpq);
	return 0;
}

int CM2ExtractObject::ExtractM2Data(HANDLE hmpq, const std::string& m2, const std::string& output)
{
	std::string str = m2;
	std::string::size_type pos = str.find_last_of('\\');

	if(pos != std::string::npos)
		str = str.substr(pos + 1);
	std::string path = _strOutputRoot + str.substr(0, str.find("."));
	if(CheckPath(path) != 0)
	{
		OutputInfo(OL_WARNING, "\nCheck path failed.");
		return -1;
	}
	str =  path + str;
	if(!SFileExtractFile(hmpq, m2.c_str(), str.c_str()))
	{
		OutputInfo(OL_WARNING, "\nExtract M2 file failed.");
		return -1;
	}
	TStringVector vctBLP;
	if(ConvertM2ToX(str, vctBLP) != 0)
	{
		OutputInfo(OL_WARNING, "\nConvert M2 file failed.");
		return -1;
	}

	if(ExtractBLPData(hmpq, path, vctBLP) != 0)
	{
		OutputInfo(OL_WARNING, "\nExtract BLP data failed.");
		return -1;
	}
	if(ConvertBLPToPNG(path, vctBLP) != 0)
	{
		OutputInfo(OL_WARNING, "\nConvert BLP file failed.");
	}

	return 0;
}

int CM2ExtractObject::ConvertM2ToX(const std::string& m2, TStringVector& vctblp)
{
	//m2 file
	CM2FileObject m2file;
	try
	{
		if(m2file.Load(m2) != 0)
		{
			OutputInfo(OL_WARNING, "\nM2 file load failed.");
			return -1;
		}
	}
	catch (CM2FileObjectException& e)
	{
		OutputInfo(OL_WARNING, "\nM2 file load exception.");
		return -1;
	}

	//x file
	CXFileObject xfile;
	for(M2::TVertexVector::const_iterator it = m2file.m_vctVertex.begin(); it != m2file.m_vctVertex.end(); ++ it)
	{
		xfile.AddVertex(it->m_stPosition.m_fX, it->m_stPosition.m_fY, it->m_stPosition.m_fZ);
		xfile.AddMeshNormal(it->m_stNormalVector.m_fX, it->m_stNormalVector.m_fY, it->m_stNormalVector.m_fZ);
		xfile.AddTextureCoord(it->m_stCoordinate.m_fX, it->m_stCoordinate.m_fY);
	}

	for(M2::TTextureVector::const_iterator it = m2file.m_vctTexture.begin(); it != m2file.m_vctTexture.end(); ++ it)
	{
		CXFileObject::TMaterialType meterial;
		meterial.m_fPower = 0.0;
		meterial.m_strFilename = it->m_strName;
		xfile.AddMaterial(meterial);

		//blp file
		vctblp.push_back(meterial.m_strFilename);
	}
	for(M2::TTextureTableItemVector::const_iterator it = m2file.m_vctTextureTable.begin(); it != m2file.m_vctTextureTable.end(); ++ it)
	{
		xfile.AddMaterialIndex(it->m_stUnit);
	}

	for(M2::TViewVector::const_iterator it = m2file.m_vctView.begin(); it != m2file.m_vctView.end(); ++ it)
	{
		for(M2::CView::TTriangleVector::const_iterator i = it->m_vctTriangle.begin(); i != it->m_vctTriangle.end(); ++ i)
		{
			xfile.AddTriangle(it->m_vctIndex[i->m_usXIndex], it->m_vctIndex[i->m_usYIndex], it->m_vctIndex[i->m_usZIndex]);
		}

		for(M2::CView::TMeshVector::const_iterator i = it->m_vctMesh.begin(); i != it->m_vctMesh.end(); ++ i)
		{
			CXFileObject::TSubMeshType mesh;
			for(unsigned int p = 0; p < i->m_usVertexCount; ++ p)
				mesh.m_vctVertexIndex.push_back(p + i->m_usVertexIndex);
			for(unsigned int p = 0; p < i->m_usTriangleCount; ++ p)
				mesh.m_vctTriangleIndex.push_back(p + i->m_usTriangleIndex);
			xfile.AddMesh(mesh);
		}
		for(M2::CView::TTextureVector::const_iterator i = it->m_vctTexture.begin(); i != it->m_vctTexture.end(); ++ i)
		{
			xfile.AddMeshTextureIndex(i->m_usMeshIndex, i->m_usTextureIndex);
		}

		break;//just do one time
	}

	if(xfile.Output(m2 + ".x") != 0)
	{
		OutputInfo(OL_WARNING, "\nX file output failed.");
		return -1;
	}

	return 0;
}

int CM2ExtractObject::ExtractBLPData(HANDLE hmpq, const std::string& path, const TStringVector& vctblp)
{
	SFILE_FIND_DATA sf;
	HANDLE hfind = SFileFindFirstFile(hmpq, "*.blp", &sf, NULL);
	if(hfind == NULL)
	{
		SFileFindClose(hmpq);
		return -1;
	}
	while(1)
	{
		std::string str = sf.cFileName;
		str = Toolkit::StrUppercase(str);
		for(TStringVector::const_iterator it = vctblp.begin(); it != vctblp.end(); ++ it)
		{
			if(str.find(*it) != std::string::npos)
			{
				std::string::size_type pos = str.find_last_of('\\');
				if(pos != std::string::npos)
					str = str.substr(pos + 1);
				str = _strOutputRoot + str;
				if(!SFileExtractFile(hmpq, sf.cFileName, str.c_str()))
				{
					OutputInfo(OL_WARNING, "\nExtract BLP file failed.");
					break;
				}
			}
		}

		if(!SFileFindNextFile(hfind, &sf))
			break;
	}
	SFileFindClose(hfind);
	return 0;
}

int CM2ExtractObject::ConvertBLPToPNG(const std::string &path, const CM2ExtractObject::TStringVector &vctblp)
{
	for(TStringVector::const_iterator it = vctblp.begin(); it != vctblp.end(); ++ it)
	{
		std::string str = *it;
		std::string::size_type pos = str.find_last_of('\\');
		if(pos != std::string::npos)
			str = str.substr(pos + 1);
		str = path + str;

		CBLPFileObject blpfile;
		if(blpfile.Load(str) != 0)
			return -1;
		str += ".PNG";
		if(blpfile.Convert2PNG(str) != 0)
			return -1;
	}
	return 0;
}

