#include "stdafx.h"

#include <sstream>

#include "MPQFileWrapper.h"
#include "M2Object.h"

int CM2Object::Load(const std::string& mpq, const std::string& file)
{
	CMPQFileWrapper mf;

	if(mf.Attach(mpq, file) != 0)
		return -1;

	Clear();

	if(LoadHeader(mf, _header) != 0)
		return -1;

	if(LoadName(mf, _header, _name) != 0)
		return -1;
	if(LoadSequence(mf, _header, _seq) != 0)
		return -1;
	if(LoadAnimation(mf, _header, _anim) != 0)
		return -1;
	if(LoadAnimLookup(mf, _header, _animlookup) != 0)
		return -1;
	if(LoadBone(mf, _header, _bone) != 0)
		return -1;
	if(LoadBoneLookup(mf, _header, _bonelookup) != 0)
		return -1;
	if(LoadVertex(mf, _header, _vertex) != 0)
		return -1;
	if(LoadColor(mf, _header, _color) != 0)
		return -1;
	if(LoadTexture(mf, _header, _texture) != 0)
		return -1;
	//if(LoadTexFileName(mf, _texture, _texfilename) != 0)
	//	return -1;
	if(LoadTransparency(mf, _header, _transparency) != 0)
		return -1;
	if(LoadTexAnim(mf, _header, _texanim) != 0)
		return -1;
	if(LoadTexReplace(mf, _header, _texreplace) != 0)
		return -1;

	if(LoadRenderFlag(mf, _header, _renderflag) != 0)
		return -1;
	if(LoadBoneTable(mf, _header, _bonetable) != 0)
		return -1;
	if(LoadTexLookup(mf, _header, _texlookup) != 0)
		return -1;
	if(LoadTexTable(mf, _header, _textable) != 0)
		return -1;
	if(LoadTransLookup(mf, _header, _translookup) != 0)
		return -1;
	if(LoadTexAnimLookup(mf, _header, _texanimlookup) != 0)
		return -1;
	if(LoadBoundTriangle(mf, _header, _boundtriangle) != 0)
		return -1;
	if(LoadBoundVertex(mf, _header, _boundvertex) != 0)
		return -1;
	if(LoadBoundNormal(mf, _header, _boundnormal) != 0)
		return -1;
	if(LoadAttachment(mf, _header, _attachment) != 0)
		return -1;
	if(LoadAttachLookup(mf, _header, _attachlookup) != 0)
		return -1;
	if(LoadAttachment2(mf, _header, _attachment2) != 0)
		return -1;
	if(LoadLight(mf, _header, _light) != 0)
		return -1;
	if(LoadCamera(mf, _header, _camera) != 0)
		return -1;
	if(LoadCameraLookup(mf, _header, _cameralookup) != 0)
		return -1;
	if(LoadRibbon(mf, _header, _ribbon) != 0)
		return -1;
	if(LoadParticle(mf, _header, _particle) != 0)
		return -1;

	return 0;
}

void CM2Object::Clear()
{
	_header.Clear();
	_name.Clear();
	_seq.Clear();
	_anim.Clear();
	_animlookup.Clear();
	_bone.Clear();
	_bonelookup.Clear();
	_vertex.Clear();
	_color.Clear();
	_texture.Clear();
//	_texfilename.Clear();
	_transparency.Clear();
	_texanim.Clear();
	_texreplace.Clear();
	_renderflag.Clear();
	_bonetable.Clear();
	_texlookup.Clear();
	_textable.Clear();
	_translookup.Clear();
	_texanimlookup.Clear();
	_boundtriangle.Clear();
	_boundvertex.Clear();
	_boundnormal.Clear();
	_attachment.Clear();
	_attachlookup.Clear();
	_attachment2.Clear();
	_light.Clear();
	_camera.Clear();
	_cameralookup.Clear();
	_ribbon.Clear();
	_particle.Clear();
}

int CM2Object::LoadHeader(CMPQFileWrapper& mf, M2::THeader &header)
{
	return header.Read(mf);
}

int CM2Object::LoadName(CMPQFileWrapper& mf, const M2::THeader& header, M2::TName& name)
{
	return name.Read(mf, header.m_stHeader.m_stName.offset, header.m_stHeader.m_stName.size);
}

int CM2Object::LoadSequence(CMPQFileWrapper &mf, const M2::THeader &header, M2::TSequence &seq)
{
	return seq.Read(mf, header.m_stHeader.m_stSequence.offset, header.m_stHeader.m_stSequence.size);
}

int CM2Object::LoadAnimation(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAnimation &anim)
{
	return anim.Read(mf, header.m_stHeader.m_stAnim.offset, header.m_stHeader.m_stAnim.size);
}

int CM2Object::LoadAnimLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAnimLookup &animlookup)
{
	return animlookup.Read(mf, header.m_stHeader.m_stAnimLookup.offset, header.m_stHeader.m_stAnimLookup.size);
}

int CM2Object::LoadBone(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBone &bone)
{
	return bone.Read(mf, header.m_stHeader.m_stBone.offset, header.m_stHeader.m_stBone.size);
}

int CM2Object::LoadBoneLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoneLookup &bonelookup)
{
	return bonelookup.Read(mf, header.m_stHeader.m_stBoneLookup.offset, header.m_stHeader.m_stBoneLookup.size);
}

int CM2Object::LoadVertex(CMPQFileWrapper &mf, const M2::THeader &header, M2::TVertex &vertex)
{
	return vertex.Read(mf, header.m_stHeader.m_stVertex.offset, header.m_stHeader.m_stVertex.size);
}

int CM2Object::LoadColor(CMPQFileWrapper &mf, const M2::THeader &header, M2::TColor &color)
{
	return color.Read(mf, header.m_stHeader.m_stColor.offset, header.m_stHeader.m_stColor.size);
}

int CM2Object::LoadTexture(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexture &texture)
{
	return texture.Read(mf, header.m_stHeader.m_stTexture.offset, header.m_stHeader.m_stTexture.size);
}

//int CM2Object::LoadTexFileName(CMPQFileWrapper &mf, const M2::TTexture &texture, M2::TTexFileName &texfilename)
//{
//	return texfilename.Read(mf, texture);
//}

int CM2Object::LoadTransparency(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTransparency &trans)
{
	return trans.Read(mf, header.m_stHeader.m_stTransparency.offset, header.m_stHeader.m_stTransparency.size);
}

int CM2Object::LoadTexAnim(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexAnim& texture)
{
	return texture.Read(mf, header.m_stHeader.m_stTexAnim.offset, header.m_stHeader.m_stTexAnim.size);
}

int CM2Object::LoadTexReplace(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexReplace& texture)
{
	return texture.Read(mf, header.m_stHeader.m_stTexReplace.offset, header.m_stHeader.m_stTexReplace.size);
}

int CM2Object::LoadRenderFlag(CMPQFileWrapper &mf, const M2::THeader &header, M2::TRenderFlag& render)
{
	return render.Read(mf, header.m_stHeader.m_stRenderFlag.offset, header.m_stHeader.m_stRenderFlag.size);
}

int CM2Object::LoadBoneTable(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoneTable& bonetable)
{
	return bonetable.Read(mf, header.m_stHeader.m_stBoneTable.offset, header.m_stHeader.m_stBoneTable.size);
}

int CM2Object::LoadTexLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexLookup& texlookup)
{
	return texlookup.Read(mf, header.m_stHeader.m_stTexLookup.offset, header.m_stHeader.m_stTexLookup.size);
}

int CM2Object::LoadTexTable(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexTable& textable)
{
	return textable.Read(mf, header.m_stHeader.m_stTexTable.offset, header.m_stHeader.m_stTexTable.size);
}

int CM2Object::LoadTransLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTransLookup& translookup)
{
	return translookup.Read(mf, header.m_stHeader.m_stTransLookup.offset, header.m_stHeader.m_stTransLookup.size);
}

int CM2Object::LoadTexAnimLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexAnimLookup& texanimlookup)
{
	return texanimlookup.Read(mf, header.m_stHeader.m_stTexAnimLookup.offset, header.m_stHeader.m_stTexAnimLookup.size);
}

int CM2Object::LoadBoundTriangle(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoundTriangle& boundtriangle)
{
	return boundtriangle.Read(mf, header.m_stHeader.m_stBoundTriangle.offset, header.m_stHeader.m_stBoundTriangle.size);
}

int CM2Object::LoadBoundVertex(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoundVertex& boundvertex)
{
	return boundvertex.Read(mf, header.m_stHeader.m_stBoundVertex.offset, header.m_stHeader.m_stBoundVertex.size);
}

int CM2Object::LoadBoundNormal(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoundNormal& boundnormal)
{
	return boundnormal.Read(mf, header.m_stHeader.m_stBoundNormal.offset, header.m_stHeader.m_stBoundNormal.size);
}

int CM2Object::LoadAttachment(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAttachment& attach)
{
	return attach.Read(mf, header.m_stHeader.m_stAttachment.offset, header.m_stHeader.m_stAttachment.size);
}

int CM2Object::LoadAttachLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAttachLookup& attach)
{
	return attach.Read(mf, header.m_stHeader.m_stAttachLookup.offset, header.m_stHeader.m_stAttachLookup.size);
}

int CM2Object::LoadAttachment2(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAttachment2& attach)
{
	return attach.Read(mf, header.m_stHeader.m_stAttachment2.offset, header.m_stHeader.m_stAttachment2.size);
}

int CM2Object::LoadLight(CMPQFileWrapper &mf, const M2::THeader &header, M2::TLight& light)
{
	return light.Read(mf, header.m_stHeader.m_stLight.offset, header.m_stHeader.m_stLight.size);
}

int CM2Object::LoadCamera(CMPQFileWrapper &mf, const M2::THeader &header, M2::TCamera& camera)
{
	return camera.Read(mf, header.m_stHeader.m_stCamera.offset, header.m_stHeader.m_stCamera.size);
}

int CM2Object::LoadCameraLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TCameraLookup& camera)
{
	return camera.Read(mf, header.m_stHeader.m_stCameraLookup.offset, header.m_stHeader.m_stCameraLookup.size);
}

int CM2Object::LoadRibbon(CMPQFileWrapper &mf, const M2::THeader &header, M2::TRibbon& ribbon)
{
	return ribbon.Read(mf, header.m_stHeader.m_stRibbon.offset, header.m_stHeader.m_stRibbon.size);
}

int CM2Object::LoadParticle(CMPQFileWrapper &mf, const M2::THeader &header, M2::TParticle& particle)
{
	return particle.Read(mf, header.m_stHeader.m_stParticle.offset, header.m_stHeader.m_stParticle.size);
}

int CM2Object::OutputHeaderInfo(std::vector<std::pair<std::string,std::string> > &vct) const
{
	vct.clear();

	std::ostringstream ostr;

	ostr << _header.m_stHeader.m_acIdent[0] << _header.m_stHeader.m_acIdent[1] << _header.m_stHeader.m_acIdent[2] << _header.m_stHeader.m_acIdent[3];
	vct.push_back(std::make_pair("Ident", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_uiVersion;
	vct.push_back(std::make_pair("Version", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stName.size << "," << _header.m_stHeader.m_stName.offset;
	vct.push_back(std::make_pair("Name", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_uiModelFlag;
	vct.push_back(std::make_pair("ModelFlag", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stSequence.size << "," << _header.m_stHeader.m_stSequence.offset;
	vct.push_back(std::make_pair("Sequence", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stAnim.size << "," << _header.m_stHeader.m_stAnim.offset;
	vct.push_back(std::make_pair("Anim", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stAnimLookup.size << "," << _header.m_stHeader.m_stAnimLookup.offset;
	vct.push_back(std::make_pair("AnimLookup", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stBone.size << "," << _header.m_stHeader.m_stBone.offset;
	vct.push_back(std::make_pair("Bone", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stBoneLookup.size << "," << _header.m_stHeader.m_stBoneLookup.offset;
	vct.push_back(std::make_pair("BoneLookup", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stVertex.size << "," << _header.m_stHeader.m_stVertex.offset;
	vct.push_back(std::make_pair("Vertex", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_uiView;
	vct.push_back(std::make_pair("m_uiView", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stColor.size << "," << _header.m_stHeader.m_stColor.offset;
	vct.push_back(std::make_pair("Color", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTexture.size << "," << _header.m_stHeader.m_stTexture.offset;
	vct.push_back(std::make_pair("Texture", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTransparency.size << "," << _header.m_stHeader.m_stTransparency.offset;
	vct.push_back(std::make_pair("Transparency", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTexAnim.size << "," << _header.m_stHeader.m_stTexAnim.offset;
	vct.push_back(std::make_pair("TexAnim", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTexReplace.size << "," << _header.m_stHeader.m_stTexReplace.offset;
	vct.push_back(std::make_pair("TexReplace", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stRenderFlag.size << "," << _header.m_stHeader.m_stRenderFlag.offset;
	vct.push_back(std::make_pair("RenderFlag", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stBoneTable.size << "," << _header.m_stHeader.m_stBoneTable.offset;
	vct.push_back(std::make_pair("BoneTable", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTexLookup.size << "," << _header.m_stHeader.m_stTexLookup.offset;
	vct.push_back(std::make_pair("TexLookup", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTexTable.size << "," << _header.m_stHeader.m_stTexTable.offset;
	vct.push_back(std::make_pair("TexTable", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTransLookup.size << "," << _header.m_stHeader.m_stTransLookup.offset;
	vct.push_back(std::make_pair("TransLookup", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stTexAnimLookup.size << "," << _header.m_stHeader.m_stTexAnimLookup.offset;
	vct.push_back(std::make_pair("TexAnimLookup", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stBoundTriangle.size << "," << _header.m_stHeader.m_stBoundTriangle.offset;
	vct.push_back(std::make_pair("BoundTriangle", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stBoundVertex.size << "," << _header.m_stHeader.m_stBoundVertex.offset;
	vct.push_back(std::make_pair("BoundVertex", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stBoundNormal.size << "," << _header.m_stHeader.m_stBoundNormal.offset;
	vct.push_back(std::make_pair("BoundNormal", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stAttachment.size << "," << _header.m_stHeader.m_stAttachment.offset;
	vct.push_back(std::make_pair("Attachment", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stAttachLookup.size << "," << _header.m_stHeader.m_stAttachLookup.offset;
	vct.push_back(std::make_pair("AttachLookup", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stAttachment2.size << "," << _header.m_stHeader.m_stAttachment2.offset;
	vct.push_back(std::make_pair("Attachment2", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stLight.size << "," << _header.m_stHeader.m_stLight.offset;
	vct.push_back(std::make_pair("Light", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stCamera.size << "," << _header.m_stHeader.m_stCamera.offset;
	vct.push_back(std::make_pair("Camera", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stCameraLookup.size << "," << _header.m_stHeader.m_stCameraLookup.offset;
	vct.push_back(std::make_pair("CameraLookup", ostr.str()));

	ostr.str("");
	ostr << _header.m_stHeader.m_stRibbon.size << "," << _header.m_stHeader.m_stRibbon.offset;
	vct.push_back(std::make_pair("Ribbon", ostr.str()));


	ostr.str("");
	ostr << _header.m_stHeader.m_stParticle.size << "," << _header.m_stHeader.m_stParticle.offset;
	vct.push_back(std::make_pair("Particle", ostr.str()));

	return 0;
}
