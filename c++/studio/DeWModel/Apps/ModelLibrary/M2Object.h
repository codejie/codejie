#ifndef __M2OBJECT_H__
#define __M2OBJECT_H__

#include <string>
#include <utility>
#include <vector>

#include "M2Structure.h"

class CM2Object
{
public:
	CM2Object() {};
	virtual ~CM2Object() {};

	int Load(const std::string& mpq, const std::string& file);

	int OutputHeaderInfo(std::vector<std::pair<std::string, std::string> >& vct) const;
protected:
	void Clear();

	int LoadHeader(CMPQFileWrapper& mf, M2::THeader& header);
	int LoadName(CMPQFileWrapper& mf, const M2::THeader& header, M2::TName& name);
	int LoadSequence(CMPQFileWrapper& mf, const M2::THeader& header, M2::TSequence& seq);
	int LoadAnimation(CMPQFileWrapper& mf, const M2::THeader& header, M2::TAnimation& anim);
	int LoadAnimLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAnimLookup &animlookup);
	int LoadBone(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBone& bone);
	int LoadBoneLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoneLookup& bonelookup);
	int LoadVertex(CMPQFileWrapper &mf, const M2::THeader &header, M2::TVertex& vertex);
	int LoadColor(CMPQFileWrapper &mf, const M2::THeader &header, M2::TColor& color);
	int LoadTexture(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexture& texture);
//	int LoadTexFileName(CMPQFileWrapper& mf, const M2::TTexture& texture, M2::TTexFileName& texfilename);
	int LoadTransparency(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTransparency& trans);
	int LoadTexAnim(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexAnim& texture);
	int LoadTexReplace(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexReplace& texture);
	int LoadRenderFlag(CMPQFileWrapper &mf, const M2::THeader &header, M2::TRenderFlag& render);
	int LoadBoneTable(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoneTable& bonetable);
	int LoadTexLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexLookup& texlookup);
	int LoadTexTable(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexTable& textable);
	int LoadTransLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTransLookup& translookup);
	int LoadTexAnimLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TTexAnimLookup& texanimlookup);
	int LoadBoundTriangle(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoundTriangle& boundtriangle);
	int LoadBoundVertex(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoundVertex& boundvertex);
	int LoadBoundNormal(CMPQFileWrapper &mf, const M2::THeader &header, M2::TBoundNormal& boundnormal);
	int LoadAttachment(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAttachment& attach);
	int LoadAttachLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAttachLookup& attach);
	int LoadAttachment2(CMPQFileWrapper &mf, const M2::THeader &header, M2::TAttachment2& attach);
	int LoadLight(CMPQFileWrapper &mf, const M2::THeader &header, M2::TLight& light);
	int LoadCamera(CMPQFileWrapper &mf, const M2::THeader &header, M2::TCamera& camera);
	int LoadCameraLookup(CMPQFileWrapper &mf, const M2::THeader &header, M2::TCameraLookup& camera);
	int LoadRibbon(CMPQFileWrapper &mf, const M2::THeader &header, M2::TRibbon& ribbon);
	int LoadParticle(CMPQFileWrapper &mf, const M2::THeader &header, M2::TParticle& particle);
public:
	M2::THeader _header;
	M2::TName _name;
	M2::TSequence _seq;
	M2::TAnimation _anim;
	M2::TAnimLookup _animlookup;
	M2::TBone _bone;
	M2::TBoneLookup _bonelookup;
	M2::TVertex _vertex;
	M2::TColor _color;
	M2::TTexture _texture;
//	M2::TTexFileName _texfilename;
	M2::TTransparency _transparency;
	M2::TTexAnim _texanim;
	M2::TTexReplace _texreplace;
	M2::TRenderFlag _renderflag;
	M2::TBoneTable _bonetable;
	M2::TTexLookup _texlookup;
	M2::TTexTable _textable;
	M2::TTransLookup _translookup;
	M2::TTexAnimLookup _texanimlookup;
	M2::TBoundTriangle _boundtriangle;
	M2::TBoundVertex _boundvertex;
	M2::TBoundNormal _boundnormal;
	M2::TAttachment _attachment;
	M2::TAttachLookup _attachlookup;
	M2::TAttachment2 _attachment2;
	M2::TLight _light;
	M2::TCamera _camera;
	M2::TCameraLookup _cameralookup;
	M2::TRibbon _ribbon;
	M2::TParticle _particle;
};

#endif

