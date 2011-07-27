#ifndef __M2STRUCTURE_H__
#define __M2STRUCTURE_H__

#include <vector>

#include "DataTypes.h"
#include "MPQFileWrapper.h"

namespace M2
{
enum StructType { ST_BASE, ST_HEADER, ST_NAME, ST_SEQUENCE, ST_ANIMATION, ST_ANIMLOOKUP,
					ST_BONE, ST_BONELOOKUP, ST_VERTEX, ST_COLOR, ST_TEXTURE, /*ST_TEXFILENAME,*/ ST_TRANSPARENCY,
					ST_TEXANIM, ST_TEXREPLACE, ST_RENDERFLAG, ST_BONETABLE, ST_TEXLOOKUP, ST_TEXTABLE,
					ST_TRANSLOOKUP, ST_TEXANIMLOOKUP, ST_BOUNDTRIANGLE, ST_BOUNDVERTEX, ST_BOUNDNORMAL, ST_ATTACHMENT,
					ST_ATTACHLOOKUP, ST_ATTACHMENT2, ST_LIGHT, ST_CAMERA, ST_CAMERALOOKUP, ST_RIBBON, ST_PARTICLE};

class CBase
{
public:
	CBase(StructType type)
		: m_eType(type)
	{
	}
	virtual ~CBase() {}
public:
	StructType m_eType;
};


template<StructType ST, typename T>
class CSubStruct : public CBase
{
public:
	typedef std::vector<T> TVector;
public:
	CSubStruct() : CBase(ST) {}
	virtual ~CSubStruct() {}

	int Read(CMPQFileWrapper& mf, unsigned int offset, unsigned int size)
	{
		T data;
		mf.Seek(offset);
		for(unsigned int i = 0; i < size; ++ i)
		{
			data.Read(mf);
			m_vct.push_back(data);
		}
		return 0;
	}

	void Clear() { m_vct.clear(); }
public:
	TVector m_vct;
};

template<>
class CSubStruct<ST_HEADER, Header_t> : public CBase
{
public:
	CSubStruct() : CBase(ST_HEADER) {}
	virtual ~CSubStruct() {}

	int Read(CMPQFileWrapper& mf)
	{
		return m_stHeader.Read(mf);
	}
	void Clear() { memset(&m_stHeader, 0, sizeof(Header_t)); }
public:
	Header_t m_stHeader;
};

template<>
class CSubStruct<ST_NAME, std::string> : public CBase
{
public:
	CSubStruct()	: CBase(ST_NAME) {}
	virtual ~CSubStruct() {}

	int Read(CMPQFileWrapper& mf, unsigned int offset, unsigned int size)
	{
		mf.Seek(offset);
		return mf.Read(m_strName);
	}
	void Clear() { m_strName.clear(); }
public:
	std::string m_strName;
};

typedef CSubStruct<ST_HEADER, Header_t> THeader;
typedef CSubStruct<ST_NAME, std::string> TName;
typedef CSubStruct<ST_SEQUENCE, UInt32_t> TSequence;
typedef CSubStruct<ST_ANIMATION, Animation_t> TAnimation;
typedef CSubStruct<ST_ANIMLOOKUP, UInt16_t> TAnimLookup;
typedef CSubStruct<ST_BONE, Bone_t> TBone;
typedef CSubStruct<ST_BONELOOKUP, Int16_t> TBoneLookup;
typedef CSubStruct<ST_VERTEX, Vertex_t> TVertex;
typedef CSubStruct<ST_COLOR, Color_t> TColor;
typedef CSubStruct<ST_TEXTURE, Texture_t> TTexture;
typedef CSubStruct<ST_TRANSPARENCY, Transparency_t> TTransparency;
typedef CSubStruct<ST_TEXANIM, TexAnim_t> TTexAnim;
typedef CSubStruct<ST_TEXREPLACE, Int16_t> TTexReplace;
typedef CSubStruct<ST_RENDERFLAG, RenderFlag_t> TRenderFlag;
typedef CSubStruct<ST_BONETABLE, UInt16_t> TBoneTable;
typedef CSubStruct<ST_TEXLOOKUP, UInt16_t> TTexLookup;
typedef CSubStruct<ST_TEXTABLE, UInt16_t> TTexTable;
typedef CSubStruct<ST_TRANSLOOKUP, UInt16_t> TTransLookup;
typedef CSubStruct<ST_TEXANIMLOOKUP, UInt16_t> TTexAnimLookup;
typedef CSubStruct<ST_BOUNDTRIANGLE, UInt32_t> TBoundTriangle;
typedef CSubStruct<ST_BOUNDVERTEX, Vec3d_t> TBoundVertex;
typedef CSubStruct<ST_BOUNDNORMAL, Vec3d_t> TBoundNormal;
typedef CSubStruct<ST_ATTACHMENT, Attachment_t> TAttachment;
typedef CSubStruct<ST_ATTACHLOOKUP, UInt16_t> TAttachLookup;
typedef CSubStruct<ST_ATTACHMENT2, Attachment2_t> TAttachment2;
typedef CSubStruct<ST_LIGHT, Light_t> TLight;
typedef CSubStruct<ST_CAMERA, Camera_t> TCamera;
typedef CSubStruct<ST_CAMERALOOKUP, UInt16_t> TCameraLookup;
typedef CSubStruct<ST_RIBBON, Ribbon_t> TRibbon;
typedef CSubStruct<ST_PARTICLE, Particle_t> TParticle;

//
//class CTexFileName : public CBase
//{
//public:
//	typedef std::vector<std::string> TVector;
//public:
//	CTexFileName() : CBase(ST_TEXFILENAME) {}
//	virtual ~CTexFileName() {}
//
//	int Read(CMPQFileWrapper& mf, const TTexture& texture)
//	{
//		for(TTexture::TVector::const_iterator it = texture.m_vct.begin(); it != texture.m_vct.end(); ++ it)
//		{
//			std::string str;
//			if(it->m_uiType == 0)
//			{
//				mf.Seek(it->m_stName.offset);
//				mf.Read(str);
//			}
//			else
//			{
//				//todo
//				str = "Unkn";
//			}
//			m_vct.push_back(str);
//		}
//		return 0;
//	}
//
//	void Clear() { m_vct.clear(); }
//public:
//	TVector m_vct;
//};
//
//typedef CTexFileName TTexFileName;


}

#endif
