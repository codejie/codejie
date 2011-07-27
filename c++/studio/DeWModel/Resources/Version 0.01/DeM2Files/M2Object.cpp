#include "Toolkit.h"
#include "M2Object.h"

namespace M2
{

int operator >> (CFileBuffer& fb, CBase& base)
{
	return base.Read(fb);
}

std::ostream& operator << (std::ostream& os, const CBase& base)
{
	base.Show(os);
	return os;
}

//////////////////////////////////////////////////////////////////////////
//CBase
void CBase::Show(std::ostream &os) const
{
	os << "\nType = " << m_eType;
}

//CHeader
int CHeader::DataBlock_t::Read(CFileBuffer& fb)
{
	fb.Read(m_uiCount);
	fb.Read(m_uiOffset);

	return fb.Good() ? 0 : -1;
}

void CHeader::DataBlock_t::Show(std::ostream &os) const
{
	os << m_uiCount << std::hex << "(0x" << m_uiOffset << ")" << std::dec;
}

int CHeader::Read(CFileBuffer& fb)
{
	fb.Read(m_uiIdent);
	fb.Read(m_uiVersion);
	m_stName.Read(fb);
	fb.Read(m_uiModelType);
	m_stGlobalSeqNo.Read(fb);
	m_stAnimation.Read(fb);
	m_stUnknownC.Read(fb);
	m_stUnknownD.Read(fb);
	m_stBone.Read(fb);
	m_stUnknownF.Read(fb);
	m_stVertex.Read(fb);
	m_stView.Read(fb);
	m_stColor.Read(fb);
	m_stTexture.Read(fb);
	m_stTransparency.Read(fb);
	m_stUnknownL.Read(fb);
	m_stTexAnim.Read(fb);
	m_stUnknownK.Read(fb);
	m_stRenderFlag.Read(fb);
	m_stUnknownY.Read(fb);
	m_stTextureTable.Read(fb);
	m_stTextureUnit.Read(fb);
	m_stTransLookup.Read(fb);
	m_stTexAnimLookup.Read(fb);
	fb.Read(m_uchUnknownChunk, 14 * sizeof(float));
	m_stBoundingTriangle.Read(fb);
	m_stBoundingVertex.Read(fb);
	m_stBoundingNormal.Read(fb);
	m_stUnknownO.Read(fb);
	m_stUnknownP.Read(fb);
	m_stUnknownQ.Read(fb);
	m_stLight.Read(fb);
	m_stCamera.Read(fb);
	m_stCameraLookup.Read(fb);
	m_stRibbonEmitter.Read(fb);
	m_stParticleEmitter.Read(fb);

	return fb.Good() ? 0 : -1;
}

void CHeader::Show(std::ostream& os) const
{
	CBase::Show(os);

	os << "\nIdent = " << m_uiIdent;
	os << "\nVersion = " << m_uiVersion;
	os << "\nName = ";
	m_stName.Show(os);
	os << "\nModelType = " << m_uiModelType;
	os << "\nGlobalSeqNo = ";
	m_stGlobalSeqNo.Show(os);
	os << "\nAnimation = ";
	m_stAnimation.Show(os);
	os << "\nUnknownC = ";
	m_stUnknownC.Show(os);
	os << "\nUnknownD = ";
	m_stUnknownD.Show(os);
	os << "\nBone = ";
	m_stBone.Show(os);
	os << "\nUnknownF = ";
	m_stUnknownF.Show(os);
	os << "\nVertex = ";
	m_stVertex.Show(os);
	os << "\nView = ";
	m_stView.Show(os);
	os << "\nColor = ";
	m_stColor.Show(os);
	os << "\nTexture = ";
	m_stTexture.Show(os);
	os << "\nTransparency = ";
	m_stTransparency.Show(os);
	os << "\nUnknownL = ";
	m_stUnknownL.Show(os);
	os << "\nTexAnim = ";
	m_stTexAnim.Show(os);
	os << "\nUnknownK = ";
	m_stUnknownK.Show(os);
	os << "\nRenderFlag = ";
	m_stRenderFlag.Show(os);
	os << "\nUnknownY = ";
	m_stUnknownY.Show(os);
	os << "\nTextureTable = ";
	m_stTextureTable.Show(os);
	os << "\nTextureUnit = ";
	m_stTextureUnit.Show(os);
	os << "\nTransLookup = ";
	m_stTransLookup.Show(os);
	os << "\nTexAnimLookup = ";
	m_stTexAnimLookup.Show(os);
	os << "\nUnknownChunk = \n";
	Toolkit::PrintBinary(os, (const char*)m_uchUnknownChunk, 14 * sizeof(float));
	os << "\nBoundingTriangle = ";
	m_stBoundingTriangle.Show(os);
	os << "\nBoundingVertex = ";
	m_stBoundingVertex.Show(os);
	os << "\nBoundingNormal = ";
	m_stBoundingNormal.Show(os);
	os << "\nUnknownO = ";
	m_stUnknownO.Show(os);
	os << "\nUnknownP = ";
	m_stUnknownP.Show(os);
	os << "\nUnknownQ = ";
	m_stUnknownQ.Show(os);
	os << "\nLight = ";
	m_stLight.Show(os);
	os << "\nCamera = ";
	m_stCamera.Show(os);
	os << "\nCameraLookup = ";
	m_stCameraLookup.Show(os);
	os << "\nRibbonEmitter = ";
	m_stRibbonEmitter.Show(os);
	os << "\nParticleEmitter = ";
	m_stParticleEmitter.Show(os);
}
//CName
int CName::Read(CFileBuffer& fb)
{
	m_strName = "";

	fb.Read(m_strName);
	return fb.Good() ? 0 : -1;
}

void CName::Show(std::ostream &os) const
{
	os << "\nName = " << m_strName;
}

//CVertex
int CVertex::Vector3Data_t::Read(CFileBuffer &fb)
{
	fb.Read(m_fX);
	fb.Read(m_fY);
	fb.Read(m_fZ);

	//the X, Y, Z values become (X, -Z, Y). 
	//float tmp = m_fZ;
	//m_fZ = m_fY;
	//m_fY = - tmp;

	return fb.Good() ? 0 : -1;
}

void CVertex::Vector3Data_t::Show(std::ostream &os) const
{
	os << m_fX << "," << m_fY << "," << m_fZ;
}

int CVertex::Vector2Data_t::Read(CFileBuffer &fb)
{
	fb.Read(m_fX);
	fb.Read(m_fY);
	return fb.Good() ? 0 : -1;
}

void CVertex::Vector2Data_t::Show(std::ostream &os) const
{
	os << m_fX << "," << m_fY;
}

int CVertex::Read(CFileBuffer &fb)
{
	m_stPosition.Read(fb);
	fb.Read(m_uiBoneWeight);
	fb.Read(m_uiBoneIndex);
	m_stNormalVector.Read(fb);
	m_stCoordinate.Read(fb);
	fb.Read(m_afUnknown[0]);
	fb.Read(m_afUnknown[1]);

	return fb.Good() ? 0 : -1;
}

void CVertex::Show(std::ostream &os) const
{
	CBase::Show(os);

	os << "\nPosition = ";
	m_stPosition.Show(os);
	os << "\nBoneWeight = " << m_uiBoneWeight;
	os << "\nBoneIndex = " << m_uiBoneIndex;
	os << "\nNormalVector = ";
	m_stNormalVector.Show(os);
	os << "\nCoordinate = ";
	m_stCoordinate.Show(os);
	os << "\nUnknown = " << m_afUnknown[0] << "," << m_afUnknown[1];	
}

//CView
int CView::DataBlock_t::Read(CFileBuffer &fb)
{
	fb.Read(m_uiCount);
	fb.Read(m_uiOffset);

	return fb.Good() ? 0 : -1;
}

void CView::DataBlock_t::Show(std::ostream& os) const
{
	os << m_uiCount << std::hex << "(0x" << m_uiOffset << ")" << std::dec;
}

int CView::TTriangleType::Read(CFileBuffer &fb)
{
	fb.Read(m_usXIndex);
	fb.Read(m_usYIndex);
	fb.Read(m_usZIndex);

	return fb.Good() ? 0 : -1;
}

void CView::TTriangleType::Show(std::ostream &os) const
{
	os << m_usXIndex << "," << m_usYIndex << "," << m_usZIndex;
}

int CView::TMeshType::Read(CFileBuffer &fb)
{
	fb.Read(m_uiID);
	fb.Read(m_usVertexIndex);
	fb.Read(m_usVertexCount);
	fb.Read(m_usTriangleIndex);
	fb.Read(m_usTriangleCount);
	fb.Read(m_achUnknown, 36);

	return fb.Good() ? 0 : -1;
}

void CView::TMeshType::Show(std::ostream &os) const
{
	os << "\n\tID = " << m_uiID;
	os << "\n\tVertexIndex = " << m_usVertexIndex;
	os << "\n\tVertexCount = " << m_usVertexCount;
	os << "\n\tTriangleIndex = " << m_usTriangleIndex;
	os << "\n\tTriangleCount = " << m_usTriangleCount;
	os << "\n\tUnknown = \n";
	Toolkit::PrintBinary(os, m_achUnknown, 36);
}

int CView::TTextureType::Read(CFileBuffer& fb)
{
	fb.Read(m_usFlag);
	fb.Read(m_usRenderOrder);
	fb.Read(m_usMeshIndex);
	fb.Read(m_usUnknownA);
	fb.Read(m_usColorIndex);
	fb.Read(m_usRenderFlag);
	fb.Read(m_usTextureUnit);
	fb.Read(m_usUnknownB);
	fb.Read(m_usTextureIndex);
	fb.Read(m_usUnknownC);
	fb.Read(m_usTransparency);
	fb.Read(m_usAnimation);

	return fb.Good() ? 0 : -1;
}

void CView::TTextureType::Show(std::ostream& os) const
{
	os << "\n\tFlag = " << m_usFlag;
	os << "\n\tRenderOrder = " << m_usRenderOrder;
	os << "\n\tMeshIndex = " << m_usMeshIndex;
	os << "\n\tUnknownA = " << m_usUnknownA;
	os << "\n\tColorIndex = " << m_usColorIndex;
	os << "\n\tRenderFlag = " << m_usRenderFlag;
	os << "\n\tTextureUnit = " << m_usTextureUnit;
	os << "\n\tUnknownB = " << m_usUnknownB;
	os << "\n\tTextureIndex = " << m_usTextureIndex;
	os << "\n\tUnknownC = " << m_usUnknownC;
	os << "\n\tTransparency = " << m_usTransparency;
	os << "\n\tAnimation = " << m_usAnimation;
}

int CView::Read(CFileBuffer& fb)
{
	m_stIndex.Read(fb);
	m_stTriangle.Read(fb);
	m_stVertex.Read(fb);
	m_stMesh.Read(fb);
	m_stTexture.Read(fb);
	fb.Read(m_uiUnknown);

	m_vctIndex.clear();
	fb.Seek(m_stIndex.m_uiOffset);
	for(unsigned int i = 0; i < m_stIndex.m_uiCount; ++ i)
	{
		TIndexType index;
		if(fb.Read(index) != 0)
			return -1;
		m_vctIndex.push_back(index);
	}

	m_vctTriangle.clear();
	fb.Seek(m_stTriangle.m_uiOffset);
	for(unsigned int i = 0; i < m_stTriangle.m_uiCount / 3 ; ++ i)
	{
		TTriangleType tri;
		if(tri.Read(fb) != 0)
			return -1;
		m_vctTriangle.push_back(tri);
	}

	m_vctVertex.clear();
	fb.Seek(m_stVertex.m_uiOffset);
	for(unsigned int i = 0; i < m_stVertex.m_uiCount; ++ i)
	{
		TVertexType ver;		
		if(fb.Read(ver) != 0)
			return -1;
		m_vctVertex.push_back(ver);
	}

	m_vctMesh.clear();
	fb.Seek(m_stMesh.m_uiOffset);
	for(unsigned int i = 0; i < m_stMesh.m_uiCount; ++ i)
	{
		TMeshType mesh;
		if(mesh.Read(fb) != 0)
			return -1;
		m_vctMesh.push_back(mesh);
	}

	m_vctTexture.clear();
	fb.Seek(m_stTexture.m_uiOffset);
	for(unsigned int i = 0; i < m_stTexture.m_uiCount; ++ i)
	{
		TTextureType text;
		if(text.Read(fb) != 0)
			return -1;
		m_vctTexture.push_back(text);
	}

	return fb.Good() ? 0 : -1;
}

void CView::Show(std::ostream &os) const
{
	CBase::Show(os);

	os << "\nIndex = ";
	m_stIndex.Show(os);
	os << "\nTriangle = ";
	m_stTriangle.Show(os);
	os << "\nVertex = ";
	m_stVertex.Show(os);
	os << "\nMesh = ";
	m_stMesh.Show(os);
	os << "\nTexture = ";
	m_stTexture.Show(os);
	os << "\nUnknown = " << m_uiUnknown;

	int i = 0;
	os << "\nData = ";
	os << "\nIndex Vector -- size:" << m_vctIndex.size() << "\n";
	for(TIndexVector::const_iterator it = m_vctIndex.begin(); it != m_vctIndex.end(); ++ it)
	{
		os << *it << ",";
	}

	i = 0;
	os << "\nTriangle Vector -- size:" << m_vctTriangle.size();
	for(TTriangleVector::const_iterator it = m_vctTriangle.begin(); it != m_vctTriangle.end(); ++ it)
	{
		os << "\nTriangle(" << ++ i << ") = ";
		it->Show(os);
	}

	os << "\nVertex Vector -- size:" << m_vctVertex.size() << "\n";
	for(TVertexVector::const_iterator it = m_vctVertex.begin(); it != m_vctVertex.end(); ++ it)
	{
		os << *it << ",";
	}

	i = 0;
	os << "\nMesh Vector -- size:" << m_vctMesh.size();
	for(TMeshVector::const_iterator it = m_vctMesh.begin(); it != m_vctMesh.end(); ++ it)
	{
		os << "\nMesh(" << i << ") = ";
		it->Show(os);
	}

	i = 0;
	os << "\nTexture Vector -- size:" << m_vctTexture.size();
	for(TTextureVector::const_iterator it = m_vctTexture.begin(); it != m_vctTexture.end(); ++ it)
	{
		os << "\nTexture(" << i << ") = ";
		it->Show(os);
	}
}

//CBone
int CBone::Vector3Data_t::Read(CFileBuffer &fb)
{
	fb.Read(m_fX);
	fb.Read(m_fY);
	fb.Read(m_fZ);

	return fb.Good() ? 0 : -1;
}

void CBone::Vector3Data_t::Show(std::ostream &os) const
{
	os << m_fX << "," << m_fY << "," << m_fZ;
}

float CBone::QuatData_t::Short2Float(short s) const
{
	float f = 0.0;
	if(s > 0)
		f = s - 32767;
	else
		f = s + 32767;

	return (f / float(32767.0));
}

int CBone::QuatData_t::Read(CFileBuffer& fb)
{
	short s = 0;
	fb.Read(s);
	m_fX = Short2Float(s);
	fb.Read(s);
	m_fY = Short2Float(s);
	fb.Read(s);
	m_fZ = Short2Float(s);
	fb.Read(s);
	m_fW = Short2Float(s);

	return fb.Good() ? 0 : -1;
}

void CBone::QuatData_t::Show(std::ostream& os) const
{
	os << m_fX << "," << m_fY << "," << m_fZ << "," << m_fW;
}

int CBone::Read(CFileBuffer &fb)
{
	fb.Read(m_uiIndex);
	fb.Read(m_uiFlag);
	fb.Read(m_usParentID);
	fb.Read(m_usUnknownA);
	fb.Read(m_uiUnknownB);
	m_stTranslation.Read(fb);
	fb.Skip(0x0C);
	m_stRotation.Read(fb);
	fb.Skip(0x0C);
	m_stScaling.Read(fb);
	fb.Skip(0x10);
	m_stPivot.Read(fb);

	return 0;	
}

void CBone::Show(std::ostream &os) const
{
	CBase::Show(os);

	os << "\nIndex = " << m_uiIndex;
	os << "\nFlag = " << m_uiFlag;
	os << "\nParentID = " << m_usParentID;
	os << "\nUnknownA = " << m_usUnknownA;
	os << "\nUnknownB = " << m_uiUnknownB;
	os << "\nTranslation = ";
	m_stTranslation.Show(os);
	os << "\nRotation = ";
	m_stRotation.Show(os);
	os << "\nScaling = ";
	m_stScaling.Show(os);
	os << "\nPivot = ";
	m_stPivot.Show(os);
}

//CTextureTable
int CTextureUnit::Read(CFileBuffer &fb)
{
	fb.Read(m_stUnit);

	return fb.Good() ? 0 : -1;
}

void CTextureUnit::Show(std::ostream& os) const
{
	CBase::Show(os);

	os << "\nItem = " << m_stUnit;
}

//CTexture
int CTexture::Read(CFileBuffer &fb)
{
	fb.Read(m_uiType);
	fb.Read(m_usUnknown);
	fb.Read(m_usFlag);
	fb.Read(m_uiNameLength);
	fb.Read(m_uiNameOffset);

	if(m_uiType == 0)
	{
		if(fb.Seek(m_uiNameOffset) != 0)
			return -1;
		fb.Read(m_strName);
		if((m_strName.size() + 1) != m_uiNameLength)
			return -1;
	}
	else
	{
		m_strName = "Unknown.";
	}

	return fb.Good() ? 0 : -1;
}

void CTexture::Show(std::ostream &os) const
{
	CBase::Show(os);
	os << "\nType = " << m_uiType;
	os << "\nUnknown = " << m_usUnknown;
	os << "\nFlag = " << m_usFlag;
	os << "\nNameLength = " << m_uiNameLength;
	os << "\nNameOffset = " << m_uiNameOffset;
	os << "\nName = " << m_strName;
}


}