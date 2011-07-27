#include <string>
#include <iostream>

#include "M2FileObject.h"
#include "XFileObject.h"

int main()
{
	std::string indir = ".\\M2\\";
	std::string outdir = ".\\Output\\";
	std::string filename = "Illidan.M2";

	CM2FileObject m2file;
	try
	{
		if(m2file.Load(indir + filename) != 0)
		{
			std::cout << "load M2 file '" << filename << "' failed." << std::endl;
			return -1;
		}
	}
	catch (CM2FileObjectException& e)
	{
		std::cout << "load M2 file '" << filename << "' exception : " << e.what() << std::endl;
		return -1;
	}

	std::cout << "\nHeader view size : " << m2file.m_vctView.size() << std::endl;

	//std::cout << "\nVertex size: " << m2file.m_vctVertex.size();
	//for(M2::TVertexVector::const_iterator it = m2file.m_vctVertex.begin(); it != m2file.m_vctVertex.end(); ++ it)
	//{
	//	std::cout << it->m_afUnknown[1] << " | ";
	//}

	std::cout << "\nView = ";
	//m2file.m_vctView[0].Show(std::cout);
	std::cout << "\nHeader view texture size : " << m2file.m_vctView[0].m_vctTexture.size() << std::endl;
	int i = 0;
	for(M2::CView::TTextureVector::const_iterator it = m2file.m_vctView[0].m_vctTexture.begin(); it != m2file.m_vctView[0].m_vctTexture.end(); ++ it)
	{
		//it->Show(std::cout);
	//	std::cout << it->m_usTextureUnit << " | ";
		if(it->m_usFlag == 0x10)
		{
			it->Show(std::cout);
		}
	}
	std::cout << "\nHeader view static texture size : " << i << std::endl;

	std::cout << "\nTexture unit size : " << m2file.m_vctTextureUnit.size() << std::endl;
	for(M2::TTextureUnitVector::const_iterator it = m2file.m_vctTextureUnit.begin(); it != m2file.m_vctTextureUnit.end(); ++ it)
	{
		it->Show(std::cout);
	}
	std::cout << "\n----------------";


	std::cout << "\nTextureTableItem size : " << m2file.m_vctTextureTable.size() << std::endl;
	for(M2::TTextureUnitVector::const_iterator it = m2file.m_vctTextureTable.begin(); it != m2file.m_vctTextureTable.end(); ++ it)
	{
		it->Show(std::cout);
	}

	std::cout << "\nTexture size : " << m2file.m_vctTexture.size();
	for(M2::TTextureVector::const_iterator it = m2file.m_vctTexture.begin(); it != m2file.m_vctTexture.end(); ++ it)
	{
		//it->Show(std::cout);
		std::cout << "\nTexture Name : " << it->m_strName;
	}

//	return 0;

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

	


	xfile.Output(outdir + filename + ".x");
	std::cout << "\nconvert " << m2file.m_stName.m_strName << " succ." << std::endl;

//	char ch;
//	std::cin >> ch;

	return 0;
}