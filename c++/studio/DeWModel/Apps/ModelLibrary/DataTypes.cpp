#include "stdafx.h"

#include "DataTypes.h"


namespace M2
{

int Header_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_acIdent, 4);
	mf.Read(m_uiVersion);
	m_stName.Read(mf);
	mf.Read(m_uiModelFlag);
	m_stSequence.Read(mf);
	m_stAnim.Read(mf);
	m_stAnimLookup.Read(mf);
	m_stBone.Read(mf);
	m_stBoneLookup.Read(mf);
	m_stVertex.Read(mf);
	mf.Read(m_uiView);
	m_stColor.Read(mf);
	m_stTexture.Read(mf);
	m_stTransparency.Read(mf);
	m_stTexAnim.Read(mf);
	m_stTexReplace.Read(mf);
	m_stRenderFlag.Read(mf);
	m_stBoneTable.Read(mf);
	m_stTexLookup.Read(mf);
	m_stTexTable.Read(mf);
	m_stTransLookup.Read(mf);
	m_stTexAnimLookup.Read(mf);
	mf.Read(m_acUnknFloat, 14 * sizeof(float));
	m_stBoundTriangle.Read(mf);
	m_stBoundVertex.Read(mf);
	m_stBoundNormal.Read(mf);
	m_stAttachment.Read(mf);
	m_stAttachLookup.Read(mf);
	m_stAttachment2.Read(mf);
	m_stLight.Read(mf);
	m_stCamera.Read(mf);
	m_stCameraLookup.Read(mf);
	m_stRibbon.Read(mf);
	m_stParticle.Read(mf);

	return 0;
}


int Animation_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_usID);
	mf.Read(m_usSubID);
	mf.Read(m_uiLength);
	mf.Read(m_fMovingSpeed);
	mf.Read(m_uiLoopType);
	mf.Read(m_uiFlag);
	mf.Read(m_uiUnkn);
	mf.Read(m_uiUnkn2);
	mf.Read(m_uiPlaySpeed);
	mf.Read(m_v3Box);
	mf.Read(m_v3Box2);
	mf.Read(m_fRadius);
	mf.Read(m_usNextID);
	mf.Read(m_usIndex);

	return 0;
}

int AnimBlock_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_usType);
	mf.Read(m_usSeq);
	m_stTimestamp.Read(mf);
	m_stKey.Read(mf);

	return 0;
}

int Bone_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_iAnimID);
	mf.Read(m_uiFlag);
	mf.Read(m_sParent);
	mf.Read(m_usGeoID);
	mf.Read(m_iUnkn);
	m_stTranslation.Read(mf);
	m_stRotation.Read(mf);
	m_stScaling.Read(mf);
	mf.Read(m_v3Pivot);

	return 0;
}

int Vertex_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_v3Pos);
	mf.Read(m_acWeight, 4);
	mf.Read(m_acBone, 4);
	mf.Read(m_v3Normal);
	mf.Read(m_v2TextureCoord);
	mf.Read(m_iUnkn);
	mf.Read(m_iUnkn2);

	return 0;
}

int Color_t::Read(CMPQFileWrapper& mf)
{
	m_stColor.Read(mf);
	m_stOpacity.Read(mf);

	return 0;
}

int Texture_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_uiType);
	mf.Read(m_uiFlag);
	m_stName.Read(mf);
	if(m_uiType == 0)
	{
		mf.Seek(m_stName.offset);
		mf.Read(m_strName);
	}
	else
	{
		m_strName = "Unkn";
	}

	return 0;
}

//int TexFileName_t::Read(CMPQFileWrapper& mf)
//{
//	return mf.Read(m_strName);
//}

int Transparency_t::Read(CMPQFileWrapper& mf)
{
	m_stTrans.Read(mf);

	return 0;
}

int TexAnim_t::Read(CMPQFileWrapper& mf)
{
	m_stTranslation.Read(mf);
	m_stRotation.Read(mf);
	m_stScaling.Read(mf);

	return 0;
}

int RenderFlag_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_usFlag);
	mf.Read(m_usMode);

	return 0;
}

int BoundTriangle_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_usA);
	mf.Read(m_usB);
	mf.Read(m_usC);

	return 0;
}

int Attachment_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_uiID);
	mf.Read(m_iBoneID);
	mf.Read(m_v3Pos);
	m_stData.Read(mf);

	return 0;
}

int Attachment2_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_acID, 4);
	mf.Read(m_uiID);
	mf.Read(m_iBoneID);
	mf.Read(m_v3Pos);
	mf.Read(m_usType);
	mf.Read(m_usSeq);
	m_stRange.Read(mf);
	m_stTime.Read(mf);

	return 0;
}

int Light_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_usType);
	mf.Read(m_iBoneID);
	mf.Read(m_v3Pos);
	m_stAmbColor.Read(mf);
	m_stAmbIntensity.Read(mf);
	m_stColor.Read(mf);
	m_stIntensity.Read(mf);
	m_stAttStart.Read(mf);
	m_stAttEnd.Read(mf);
	m_stUnkn.Read(mf);

	return 0;
}

int Camera_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_iID);
	mf.Read(m_fFov);
	mf.Read(m_fFarClip);
	mf.Read(m_fNearClip);
	m_stTransPos.Read(mf);
	mf.Read(m_v3Pos);
	m_stTransTarget.Read(mf);
	mf.Read(m_v3TargetPos);
	m_stRot.Read(mf);

	return 0;
}

int Ribbon_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_iID);
	mf.Read(m_iBoneID);
	mf.Read(m_v3Pos);
	m_stTexture.Read(mf);
	m_stBlendRef.Read(mf);
	m_stColor.Read(mf);
	m_stOpacity.Read(mf);
	m_stAbove.Read(mf);
	m_stBlow.Read(mf);
	mf.Read(m_fResoluation);
	mf.Read(m_fLength);
	mf.Read(m_fAngle);
	mf.Read(m_sRender);
	mf.Read(m_sRender2);
	m_stUnkn.Read(mf);
	m_stUnkn2.Read(mf);
	mf.Read(m_iUnkn);

	return 0;
}

int FakeAnimBlock_t::Read(CMPQFileWrapper& mf)
{
	m_stTimestamp.Read(mf);
	m_stKey.Read(mf);

	return 0;
}

int Particle_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_iID);
	mf.Read(m_uiFlag);
	mf.Read(m_v3Pos);
	mf.Read(m_sBoneID);
	mf.Read(m_sTexture);
	m_stName.Read(mf);
	m_stUnkn.Read(mf);
	mf.Read(m_sBlend);
	mf.Read(m_sEmitter);
	mf.Read(m_sParticle);
	mf.Read(m_sTexRot);
	mf.Read(m_sTexCol);
	mf.Read(m_sTexRow);
	m_stSpeed.Read(mf);
	m_sSpeedVar.Read(mf);
	m_stVerticalRange.Read(mf);
	m_stHorizontalRange.Read(mf);
	m_stGravity.Read(mf);
	m_stLifeSpan.Read(mf);
	mf.Read(m_iUnkn);
	m_stRate.Read(mf);
	mf.Read(m_iUnkn2);
	m_stAreaLength.Read(mf);
	m_stAreaWidth.Read(mf);
	m_stGravity2.Read(mf);
	m_stColor.Read(mf);
	m_stTimestamp.Read(mf);
	m_stSize.Read(mf);
	mf.Read((char*)m_aiUnkn, sizeof(int) *10);
	mf.Read((char*)m_afUnkn, sizeof(float) * 3);
	mf.Read(m_v3Scale);
	mf.Read(m_fSlowDown);
	mf.Read((char*)m_afUnkn2, sizeof(float) * 2);
	mf.Read(m_fRot);
	mf.Read((char*)m_afUnkn3, sizeof(float) * 2);
	mf.Read(m_v3Rot);
	mf.Read(m_v3Rot2);
	mf.Read(m_v3Trans);
	mf.Read((char*)m_afUnkn4, sizeof(float) * 6);
	m_stEnabled.Read(mf);

	return 0;
}

}//M2

namespace Skin
{

int Header_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_acIdent, 4);
	m_stIndex.Read(mf);
	m_stTriangle.Read(mf);
	m_stProperty.Read(mf);
	m_stSubMesh.Read(mf);
	m_stTexture.Read(mf);
	mf.Read(m_uiLOD);

	return 0;
}

int SubMesh_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_uiID);
	mf.Read(m_usVertexStart);
	mf.Read(m_usVertexCount);
	mf.Read(m_usTriangleStart);
	mf.Read(m_usTriangleCount);
	mf.Read(m_usBoneCount);
	mf.Read(m_usBoneStart);
	mf.Read(m_usUnkn);
	mf.Read(m_usBoneRoot);
	mf.Read(m_v3Pos);
	mf.Read((char*)m_afUnkn, sizeof(float) * 4);

	return 0;
}

int Texture_t::Read(CMPQFileWrapper& mf)
{
	mf.Read(m_usFlag);
	mf.Read(m_usRenderOrder);
	mf.Read(m_usSubMeshIndex);
	mf.Read(m_usSubMeshIndex2);
	mf.Read(m_sColorIndex);
	mf.Read(m_usRenderFlag);
	mf.Read(m_usTexIndex);
	mf.Read(m_usUnkn);
	mf.Read(m_usTexture);
	mf.Read(m_usTexIndex2);
	mf.Read(m_usTransIndex);
	mf.Read(m_usTexAnimIndex);

	return 0;
}

}//skin
