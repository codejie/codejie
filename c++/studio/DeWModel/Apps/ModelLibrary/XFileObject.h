#ifndef __XFILEOBJECT_H__
#define __XFILEOBJECT_H__

#include <string>
#include <vector>
#include <map>
//#include <ostream>
#include <fstream>

#include "vec3d.h"

#include "M2Object.h"
#include "SkinObject.h"
#include "TextureObject.h"

namespace X
{

const std::string STR_X_HEADER		= "xof 0303txt 0032";
const std::string STR_TAB2			= "  ";
const std::string STR_TAB4			= "    ";
const std::string STR_TAB6			= "      ";
const std::string STR_TAB8			= "        ";
const std::string STR_TAB10			= "          ";

struct Vertex_t
{
	vec3d m_v3Pos;
	vec3d m_v3Normal;
	vec2d m_v2TexCoord;
};

typedef std::vector<Vertex_t> TVertexVector;

typedef std::vector<unsigned short> TVertexIndexVector;

struct Triangle_t
{
	unsigned short m_usA;
	unsigned short m_usB;
	unsigned short m_usC;
};

typedef std::vector<Triangle_t> TTriangleVector;

struct Texture_t
{
	unsigned int m_uiType;
	std::string m_strFileName;
};

typedef std::vector<Texture_t> TTextureVector;//Texture Index + Texture;


struct MeshIndex_t
{
	unsigned short m_usIndex;
	unsigned short m_usVexStart;
	unsigned short m_usVexCount;
	unsigned short m_usTriStart;
	unsigned short m_usTriCount;
	unsigned short m_usTexture;
};

typedef std::vector<MeshIndex_t> TMeshIndexVector;
typedef std::map<unsigned int, TMeshIndexVector> TMeshMap;//ID + Triangle


}


class CXFileObject
{
public:
	CXFileObject();
	virtual ~CXFileObject();

	int Output(const CM2Object& m2, const CSkinObject& skin, const CTextureObject& texture, const std::wstring& file);
private:
	int InitVertex(const CM2Object& m2);
	int InitVertexIndex(const CSkinObject& skin);
	int InitTriangle(const CSkinObject& skin);
	int InitTexture(const CTextureObject& texture);
	int InitMesh(const CSkinObject& skin);
private:
	int CreateXFile(const std::wstring& file, std::ofstream& ofs) const;
	void CloseXFile(std::ofstream& ofs) const;
	int OutputMaterial(std::ostream& os) const;
	int OutputFrame(std::ostream& os) const;
private:
//	int OutputMesh(std::ostream& os, const X::TMeshIndexVector& vct) const;
//	int OutputMeshNormal(std::ostream& os, unsigned short vnum, unsigned short tnum, const X::TMeshIndexVector& vct) const;

	int OutputMesh(std::ostream& os, unsigned short id, const X::MeshIndex_t& mesh) const;
	int OutputMeshNormal(std::ostream& os, const X::MeshIndex_t& mesh) const;
	int OutputMeshTextureCoord(std::ostream& os, const X::MeshIndex_t& mesh) const;
	int OutputMeshMaterialList(std::ostream& os, const X::MeshIndex_t& mesh) const;
private:
	void OutputVertex(std::ostream& os, const X::Vertex_t& vex) const;
	void OutputTriangle(std::ostream& os, const X::Triangle_t& tri, unsigned short base) const;
	void OutputVertexNormal(std::ostream& os, const X::Vertex_t& vex) const;
	void OutputTriangleNormal(std::ostream& os, const X::Triangle_t& tri, unsigned short base) const;
	void OutputVertexTextureCoord(std::ostream& os, const X::Vertex_t& vex) const; 
protected:
	X::TVertexVector _vctVertex;
	X::TVertexIndexVector _vctVexIndex;
	X::TTriangleVector _vctTriangle;
	X::TTextureVector _vctTexture;
	X::TMeshMap _mapMesh;
};

#endif
