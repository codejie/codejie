#include "M2FileObject.h"

CM2FileObject::CM2FileObject()
{
}

CM2FileObject::~CM2FileObject()
{
}

int CM2FileObject::Load(const std::string &filename)
{
	CFileBuffer fb;
	if(fb.Attach(filename) != 0)
		throw CM2FileObjectException("attach file '" + filename + "' failed.");

	LoadHeader(fb);
	LoadName(fb);
	LoadVertex(fb);
	LoadView(fb);
	LoadBone(fb);
	LoadTextureUnit(fb);
	LoadTextureTable(fb);
	LoadTexture(fb);

	return 0;
}

int CM2FileObject::LoadHeader(CFileBuffer& fb)
{
	//Header
	if(m_stHeader.Read(fb) != 0)
		throw CM2FileObjectException("read HEADER data failed.");
	return 0;
}

int CM2FileObject::LoadName(CFileBuffer& fb)
{
	//Name
	if(fb.Seek(m_stHeader.m_stName.m_uiOffset) != 0)
		throw CM2FileObjectException("seek NAME data failed.");
	if(m_stName.Read(fb) != 0)
		throw CM2FileObjectException("read NAME data failed.");
	if((m_stName.m_strName.size() + 1) != m_stHeader.m_stName.m_uiCount)
		throw CM2FileObjectException("check Name data failed.");
	return 0;
}

int CM2FileObject::LoadVertex(CFileBuffer& fb)
{
	//Vertex
	m_vctVertex.clear();
	for(unsigned int i = 0; i < m_stHeader.m_stVertex.m_uiCount; ++ i)
	{
		M2::CVertex v;
		if(fb.Seek(m_stHeader.m_stVertex.m_uiOffset + i * v.BlockSize()) != 0)
			throw CM2FileObjectException("seek VERTEX data failed.");
		if(v.Read(fb) != 0)
			throw CM2FileObjectException("read VERTEX data failed.");
		m_vctVertex.push_back(v);
	}
	return 0;
}

int CM2FileObject::LoadView(CFileBuffer& fb)
{
	//View
	for(unsigned int i = 0; i < m_stHeader.m_stView.m_uiCount; ++ i)
	{
		M2::CView v;
		if(fb.Seek(m_stHeader.m_stView.m_uiOffset + i * v.BlockSize()) != 0)
			throw CM2FileObjectException("seek VIEW data failed.");
		if(v.Read(fb) != 0)
			throw CM2FileObjectException("read VIEW data failed.");
		m_vctView.push_back(v);
	}
	return 0;
}

int CM2FileObject::LoadBone(CFileBuffer& fb)
{
	for(unsigned int i = 0; i < m_stHeader.m_stBone.m_uiCount; ++ i)
	{
		M2::CBone b;
		if(fb.Seek(m_stHeader.m_stBone.m_uiOffset + i * b.BlockSize()) != 0)
			throw CM2FileObjectException("seek BONE data failed.");
		if(b.Read(fb) != 0)
			throw CM2FileObjectException("read BONE data failed.");
		m_vctBone.push_back(b);
	}
	return 0;
}

int CM2FileObject::LoadTextureUnit(CFileBuffer& fb)
{
	for(unsigned int i = 0; i < m_stHeader.m_stTextureUnit.m_uiCount; ++ i)
	{
		M2::CTextureUnit t;
		if(fb.Seek(m_stHeader.m_stTextureUnit.m_uiOffset + i * t.BlockSize()) != 0)
			throw CM2FileObjectException("seek TEXTUREUNIT data failed.");
		if(t.Read(fb) != 0)
			throw CM2FileObjectException("read TEXTUREUNIT data failed.");
		m_vctTextureUnit.push_back(t);
	}
	return 0;
}

int CM2FileObject::LoadTextureTable(CFileBuffer &fb)
{
	for(unsigned int i = 0; i < m_stHeader.m_stTextureTable.m_uiCount; ++ i)
	{
		M2::TTextureTableItem t;
		if(fb.Seek(m_stHeader.m_stTextureTable.m_uiOffset + i * t.BlockSize()) != 0)
			throw CM2FileObjectException("seek TEXTURETABLEITEM data failed.");
		if(t.Read(fb) != 0)
			throw CM2FileObjectException("read TEXTURETABLEITEM data failed.");
		m_vctTextureTable.push_back(t);
	}
	return 0;
}

int CM2FileObject::LoadTexture(CFileBuffer& fb)
{
	for(unsigned int i = 0; i < m_stHeader.m_stTexture.m_uiCount; ++ i)
	{
		M2::CTexture t;
		if(fb.Seek(m_stHeader.m_stTexture.m_uiOffset + i * t.BlockSize()) != 0)
			throw CM2FileObjectException("seek TEXTURE data failed.");
		if(t.Read(fb) != 0)
			throw CM2FileObjectException("read TEXTURE data failed.");

		m_vctTexture.push_back(t);
	}
	return 0;
}
