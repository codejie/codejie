#ifndef __M2OBJECT_H__
#define __M2OBJECT_H__

//base on http://wowdev.org/wiki/index.php/M2 and http://squishythoughts.com/archives/category/projects/3d/

#include <fstream>
#include <iostream>
#include <string>
#include <vector>

#include "FileBuffer.h"

namespace M2
{

enum StructType {ST_BASE = 0, ST_HEADER, ST_NAME, ST_VERTEX, ST_VIEW, ST_BONE, ST_TEXTUREUNIT, ST_TEXTURE, ST_TEXTURENAME };

class CBase
{
public:
	CBase(StructType type = ST_BASE)
		: m_eType(type)
	{
	}
	virtual ~CBase() {}

	virtual int Read(CFileBuffer& fb) = 0;
	virtual size_t BlockSize() const { return 0; }
	virtual void Show(std::ostream& os) const;
public:
	StructType m_eType;
};

extern int operator >> (CFileBuffer& fb, CBase& base);
extern std::ostream& operator << (std::ostream& os, const CBase& base);

class CHeader : public CBase
{
public:
	struct DataBlock_t
	{
		unsigned int m_uiCount;
		unsigned int m_uiOffset;

		int Read(CFileBuffer& fb);
		virtual size_t BlockSize() const { return 324; }
		void Show(std::ostream& os) const;
	};
public:
	CHeader()
		: CBase(ST_HEADER)
	{
	}

	virtual int Read(CFileBuffer& fb);
	virtual void Show(std::ostream& os) const;
public:
	unsigned int m_uiIdent;
	unsigned int m_uiVersion;
	DataBlock_t m_stName;
	unsigned int m_uiModelType;
	DataBlock_t m_stGlobalSeqNo;
	DataBlock_t m_stAnimation;
	DataBlock_t m_stUnknownC;
	DataBlock_t m_stUnknownD;
	DataBlock_t m_stBone;
	DataBlock_t m_stUnknownF;
	DataBlock_t m_stVertex;
	DataBlock_t m_stView;
	DataBlock_t m_stColor;
	DataBlock_t m_stTexture;
	DataBlock_t m_stTransparency;
	DataBlock_t m_stUnknownL;
	DataBlock_t m_stTexAnim;
	DataBlock_t m_stUnknownK;
	DataBlock_t m_stRenderFlag;
	DataBlock_t m_stUnknownY;
	DataBlock_t m_stTextureTable;
	DataBlock_t m_stTextureUnit;
	DataBlock_t m_stTransLookup;
	DataBlock_t m_stTexAnimLookup;
	unsigned char m_uchUnknownChunk[14 * sizeof(float)];
	DataBlock_t m_stBoundingTriangle;
	DataBlock_t m_stBoundingVertex;
	DataBlock_t m_stBoundingNormal;
	DataBlock_t m_stUnknownO;
	DataBlock_t m_stUnknownP;
	DataBlock_t m_stUnknownQ;
	DataBlock_t m_stLight;
	DataBlock_t m_stCamera;
	DataBlock_t m_stCameraLookup;
	DataBlock_t m_stRibbonEmitter;
	DataBlock_t m_stParticleEmitter;
};

class CName : public CBase
{
public:
	CName()
		: CBase(ST_NAME)
	{
	}
	virtual int Read(CFileBuffer& fb);
	virtual void Show(std::ostream& os) const;
public:
	std::string m_strName;
};

class CVertex : public CBase
{
public:
	struct Vector3Data_t
	{
		float m_fX;
		float m_fY;
		float m_fZ;

		int Read(CFileBuffer& fb);
		void Show(std::ostream& os) const;
	};
	struct Vector2Data_t
	{
		float m_fX;
		float m_fY;

		int Read(CFileBuffer& fb);
		void Show(std::ostream& os) const;
	};
public:
	CVertex()
		: CBase(ST_VERTEX)
	{
	}

	virtual int Read(CFileBuffer& fb);
	virtual size_t BlockSize() const { return 48; }
	virtual void Show(std::ostream& os) const;
public:
	Vector3Data_t m_stPosition;
	unsigned int m_uiBoneWeight;
	unsigned int m_uiBoneIndex;
	Vector3Data_t m_stNormalVector;
	Vector2Data_t m_stCoordinate;
	float m_afUnknown[2];
};
typedef std::vector<CVertex> TVertexVector;

class CView : public CBase
{
public:
	struct DataBlock_t
	{
		unsigned int m_uiCount;
		unsigned int m_uiOffset;

		int Read(CFileBuffer& fb);
		void Show(std::ostream& os) const;
	};

	typedef unsigned short TIndexType;
	typedef std::vector<TIndexType> TIndexVector;

	typedef struct
	{
		unsigned short m_usXIndex;
		unsigned short m_usYIndex;
		unsigned short m_usZIndex;

		int Read(CFileBuffer& fb);
		void Show(std::ostream& os) const;
	}TTriangleType;
	typedef std::vector<TTriangleType> TTriangleVector;

	typedef unsigned int TVertexType;
	typedef std::vector<TVertexType> TVertexVector;

	typedef struct 
	{
		unsigned int m_uiID;
		unsigned short m_usVertexIndex;
		unsigned short m_usVertexCount;
		unsigned short m_usTriangleIndex;
		unsigned short m_usTriangleCount;
		char m_achUnknown[36];

		int Read(CFileBuffer& fb);
		void Show(std::ostream& os) const;
	}TMeshType;
	typedef std::vector<TMeshType> TMeshVector;

	typedef struct
	{
		unsigned short m_usFlag;
		unsigned short m_usRenderOrder;
		unsigned short m_usMeshIndex;
		unsigned short m_usUnknownA;
		unsigned short m_usColorIndex;
		unsigned short m_usRenderFlag;
		unsigned short m_usTextureUnit;
		unsigned short m_usUnknownB;
		unsigned short m_usTextureIndex;
		unsigned short m_usUnknownC;
		unsigned short m_usTransparency;
		unsigned short m_usAnimation;

		int Read(CFileBuffer& fb);
		void Show(std::ostream& os) const;
	}TTextureType;
	typedef std::vector<TTextureType> TTextureVector;
public:
	CView()
		: CBase(ST_VIEW)
	{
	}

	virtual int Read(CFileBuffer& fb);
	virtual size_t BlockSize() const { return 44; }
	virtual void Show(std::ostream& os) const;
public:
	DataBlock_t m_stIndex;
	DataBlock_t m_stTriangle;
	DataBlock_t m_stVertex;
	DataBlock_t m_stMesh;
	DataBlock_t m_stTexture;
	unsigned int m_uiUnknown;

	TIndexVector m_vctIndex;
	TTriangleVector m_vctTriangle;
	TVertexVector m_vctVertex;
	TMeshVector m_vctMesh;
	TTextureVector m_vctTexture;
};
typedef std::vector<CView> TViewVector;

class CBone : public CBase
{
public:
	struct Vector3Data_t
	{
		float m_fX;
		float m_fY;
		float m_fZ;

		int Read(CFileBuffer& fb);
		void Show(std::ostream& os) const;
	};
	struct QuatData_t 
	{
		float m_fX;
		float m_fY;
		float m_fZ;
		float m_fW;

		float Short2Float(short s) const;
		int Read(CFileBuffer& fb);
		void Show(std::ostream& os) const;
	};
public:
	CBone()
		: CBase(ST_BONE)
	{
	}

	virtual int Read(CFileBuffer& fb);
	virtual size_t BlockSize() const { return 112; }
	virtual void Show(std::ostream& os) const;

public:
	unsigned int m_uiIndex;
	unsigned int m_uiFlag;
	unsigned short m_usParentID;
	unsigned short m_usUnknownA;
	unsigned int m_uiUnknownB;
	Vector3Data_t m_stTranslation;
	QuatData_t m_stRotation;
	Vector3Data_t m_stScaling;
	Vector3Data_t m_stPivot;
};
typedef std::vector<CBone> TBoneVector;

class CTextureUnit : public CBase
{
public:
	typedef short TUnitType;
public:
	CTextureUnit()
		: CBase(ST_TEXTUREUNIT)
	{
	}
	virtual int Read(CFileBuffer& fb);
	virtual size_t BlockSize() const { return sizeof(TUnitType); }
	virtual void Show(std::ostream& os) const;
public:
	TUnitType m_stUnit;
};
typedef std::vector<CTextureUnit> TTextureUnitVector;

typedef CTextureUnit TTextureTableItem;
typedef std::vector<TTextureTableItem> TTextureTableItemVector;

class CTexture : public CBase
{
public:
	CTexture()
		: CBase(ST_TEXTURE)
	{
	}
	virtual int Read(CFileBuffer& fb);
	virtual size_t BlockSize() const { return 16; }
	virtual void Show(std::ostream& os) const;
public:
	unsigned int m_uiType;
	unsigned short m_usUnknown;
	unsigned short m_usFlag;
	unsigned int m_uiNameLength;
	unsigned int m_uiNameOffset;
	std::string m_strName;
};
typedef std::vector<CTexture> TTextureVector;


}


#endif
