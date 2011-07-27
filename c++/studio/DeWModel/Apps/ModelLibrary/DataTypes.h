//#ifndef __DATATYPES_H__
//#define __DATATYPES_H__

#pragma once

#pragma warning(disable: 4251)

#include <string>

#include "vec3d.h"
#include "MPQFileWrapper.h"


template<typename T>
struct base_t
{
	T data;

	int Read(CMPQFileWrapper& mf)
	{
		mf.Read(data);
		return 0;
	}
};

typedef base_t<unsigned int> UInt32_t;
typedef base_t<int> Int32_t;
typedef base_t<unsigned short> UInt16_t;
typedef base_t<short> Int16_t;
typedef base_t<vec3d> Vec3d_t;

struct DataPair_t
{
	unsigned int size;
	unsigned int offset;

	int Read(CMPQFileWrapper& mf)
	{
		mf.Read(size);
		mf.Read(offset);
		return 0;
	}
};

namespace M2
{

struct Header_t
{
	unsigned char m_acIdent[4];
	unsigned int m_uiVersion;
	DataPair_t m_stName;
	unsigned int m_uiModelFlag;
	DataPair_t m_stSequence;
	DataPair_t m_stAnim;
	DataPair_t m_stAnimLookup;
	DataPair_t m_stBone;
	DataPair_t m_stBoneLookup;
	DataPair_t m_stVertex;
	unsigned int m_uiView;
	DataPair_t m_stColor;
	DataPair_t m_stTexture;
	DataPair_t m_stTransparency;
	DataPair_t m_stTexAnim;
	DataPair_t m_stTexReplace;
	DataPair_t m_stRenderFlag;
	DataPair_t m_stBoneTable;
	DataPair_t m_stTexLookup;
	DataPair_t m_stTexTable;
	DataPair_t m_stTransLookup;
	DataPair_t m_stTexAnimLookup;
	char m_acUnknFloat[14 * sizeof(float)];
	DataPair_t m_stBoundTriangle;
	DataPair_t m_stBoundVertex;
	DataPair_t m_stBoundNormal;
	DataPair_t m_stAttachment;
	DataPair_t m_stAttachLookup;
	DataPair_t m_stAttachment2;
	DataPair_t m_stLight;
	DataPair_t m_stCamera;
	DataPair_t m_stCameraLookup;
	DataPair_t m_stRibbon;
	DataPair_t m_stParticle;

	int Read(CMPQFileWrapper& mf);
};

struct Animation_t
{
	unsigned short m_usID;
	unsigned short m_usSubID;
	unsigned int m_uiLength;
	float m_fMovingSpeed;
	unsigned int m_uiLoopType;
	unsigned int m_uiFlag;
	unsigned int m_uiUnkn;
	unsigned int m_uiUnkn2;
	unsigned int m_uiPlaySpeed;
	vec3d m_v3Box;
	vec3d m_v3Box2;
	float m_fRadius;
	unsigned short m_usNextID;
	unsigned short m_usIndex;

	int Read(CMPQFileWrapper& mf);
};

struct AnimBlock_t
{
	unsigned short m_usType;
	unsigned short m_usSeq;
	DataPair_t m_stTimestamp;
	DataPair_t m_stKey;

	int Read(CMPQFileWrapper& mf);
};

struct Bone_t
{
	int m_iAnimID;
	unsigned int m_uiFlag;
	short m_sParent;
	unsigned short m_usGeoID;
	int m_iUnkn;
	AnimBlock_t m_stTranslation;
	AnimBlock_t m_stRotation;
	AnimBlock_t m_stScaling;
	vec3d m_v3Pivot;

	int Read(CMPQFileWrapper& mf);
};

struct Vertex_t
{
	vec3d m_v3Pos;
	unsigned char m_acWeight[4];
	unsigned char m_acBone[4];
	vec3d m_v3Normal;
	vec2d m_v2TextureCoord;
	int m_iUnkn;
	int m_iUnkn2;

	int Read(CMPQFileWrapper& mf);
};

struct Color_t
{
	AnimBlock_t m_stColor;
	AnimBlock_t m_stOpacity;

	int Read(CMPQFileWrapper& mf);
};

struct Texture_t
{
	unsigned int m_uiType;
	unsigned int m_uiFlag;
	DataPair_t m_stName;
	std::string m_strName;

	int Read(CMPQFileWrapper& mf);
};

//struct TexFileName_t
//{
//	std::string m_strName;
//
//	int Read(CMPQFileWrapper& mf);
//};

struct Transparency_t
{
	AnimBlock_t m_stTrans;

	int Read(CMPQFileWrapper& mf);
};

struct TexAnim_t
{
	AnimBlock_t m_stTranslation;
	AnimBlock_t m_stRotation;
	AnimBlock_t m_stScaling;

	int Read(CMPQFileWrapper& mf);
};

struct RenderFlag_t
{
	unsigned short m_usFlag;
	unsigned short m_usMode;

	int Read(CMPQFileWrapper& mf);
};

struct BoundTriangle_t
{
	unsigned int m_usA;
	unsigned int m_usB;
	unsigned int m_usC;

	int Read(CMPQFileWrapper& mf);
};

struct Attachment_t
{
	unsigned int m_uiID;
	int m_iBoneID;
	vec3d m_v3Pos;
	AnimBlock_t m_stData;

	int Read(CMPQFileWrapper& mf);
};

struct Attachment2_t
{
	char m_acID[4];
	unsigned int m_uiID;
	int m_iBoneID;
	vec3d m_v3Pos;
	unsigned int m_usType;
	unsigned int m_usSeq;
	DataPair_t m_stRange;
	DataPair_t m_stTime;

	int Read(CMPQFileWrapper& mf);
};

struct Light_t
{
	unsigned short m_usType;
	int m_iBoneID;
	vec3d m_v3Pos;
	AnimBlock_t m_stAmbColor;
	AnimBlock_t m_stAmbIntensity;
	AnimBlock_t m_stColor;
	AnimBlock_t m_stIntensity;
	AnimBlock_t m_stAttStart;
	AnimBlock_t m_stAttEnd;
	AnimBlock_t m_stUnkn;

	int Read(CMPQFileWrapper& mf);
};

struct Camera_t
{
	int m_iID;
	float m_fFov;
	float m_fFarClip;
	float m_fNearClip;
	AnimBlock_t m_stTransPos;
	vec3d m_v3Pos;
	AnimBlock_t m_stTransTarget;
	vec3d m_v3TargetPos;
	AnimBlock_t m_stRot;

	int Read(CMPQFileWrapper& mf);
};

struct Ribbon_t
{
	int m_iID;
	int m_iBoneID;
	vec3d m_v3Pos;
	DataPair_t m_stTexture;
	DataPair_t m_stBlendRef;
	AnimBlock_t m_stColor;
	AnimBlock_t m_stOpacity;
	AnimBlock_t m_stAbove;
	AnimBlock_t m_stBlow;
	float m_fResoluation;
	float m_fLength;
	float m_fAngle;
	short m_sRender;
	short m_sRender2;
	AnimBlock_t m_stUnkn;
	AnimBlock_t m_stUnkn2;
	int m_iUnkn;

	int Read(CMPQFileWrapper& mf);
};

struct FakeAnimBlock_t
{
	DataPair_t m_stTimestamp;
	DataPair_t m_stKey;

	int Read(CMPQFileWrapper& mf);
};

struct Particle_t
{
	int m_iID;
	unsigned int m_uiFlag;
	vec3d m_v3Pos;
	short m_sBoneID;
	short m_sTexture;
	DataPair_t m_stName;
	DataPair_t m_stUnkn;
	short m_sBlend;
	short m_sEmitter;
	short m_sParticle;
	short m_sTexRot;
	short m_sTexCol;
	short m_sTexRow;
	AnimBlock_t m_stSpeed;
	AnimBlock_t m_sSpeedVar;
	AnimBlock_t	m_stVerticalRange;
	AnimBlock_t m_stHorizontalRange;
	AnimBlock_t m_stGravity;
	AnimBlock_t m_stLifeSpan;
	int m_iUnkn;
	AnimBlock_t m_stRate;
	int m_iUnkn2;
	AnimBlock_t m_stAreaLength;
	AnimBlock_t m_stAreaWidth;
	AnimBlock_t m_stGravity2;
	FakeAnimBlock_t m_stColor;
	FakeAnimBlock_t m_stTimestamp;
	FakeAnimBlock_t m_stSize;
	int m_aiUnkn[10];
	float m_afUnkn[3];
	vec3d m_v3Scale;
	float m_fSlowDown;
	float m_afUnkn2[2];
	float m_fRot;
	float m_afUnkn3[2];
	vec3d m_v3Rot;
	vec3d m_v3Rot2;
	vec3d m_v3Trans;
	float m_afUnkn4[6];
	AnimBlock_t m_stEnabled;

	int Read(CMPQFileWrapper& mf);
};

}//M2

namespace Skin
{

struct Header_t
{
	char m_acIdent[4];
	DataPair_t m_stIndex;
	DataPair_t m_stTriangle;
	DataPair_t m_stProperty;
	DataPair_t m_stSubMesh;
	DataPair_t m_stTexture;
	unsigned int m_uiLOD;

	int Read(CMPQFileWrapper& mf);
};

struct SubMesh_t
{
	unsigned int m_uiID;
	unsigned short m_usVertexStart;
	unsigned short m_usVertexCount;
	unsigned short m_usTriangleStart;
	unsigned short m_usTriangleCount;
	unsigned short m_usBoneCount;
	unsigned short m_usBoneStart;
	unsigned short m_usUnkn;
	unsigned short m_usBoneRoot;
	vec3d m_v3Pos;
	float m_afUnkn[4];

	int Read(CMPQFileWrapper& mf);
};

struct Texture_t
{
	unsigned short m_usFlag;
	unsigned short m_usRenderOrder;
	unsigned short m_usSubMeshIndex;
	unsigned short m_usSubMeshIndex2;
	short m_sColorIndex;
	unsigned short m_usRenderFlag;
	unsigned short m_usTexIndex;
	unsigned short m_usUnkn;
	unsigned short m_usTexture;
	unsigned short m_usTexIndex2;
	unsigned short m_usTransIndex;
	unsigned short m_usTexAnimIndex;

	int Read(CMPQFileWrapper& mf);
};

}//Skin

//#endif
