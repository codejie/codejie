#ifndef __XFILEOBJECT_H__
#define __XFILEOBJECT_H__

#include <iostream>
#include <vector>
#include <string>
#include <map>
#include <set>

class CXFileObject
{
public:
	static const std::string FMT_HEADER;
	
	struct Vector2Data_t
	{
		Vector2Data_t(float x, float y)
			: m_fX(x), m_fY(y)
		{			 
		}
		float m_fX;
		float m_fY;
	};

	struct Vector3Data_t
	{
		Vector3Data_t(float x, float y, float z)
			: m_fX(x), m_fY(y), m_fZ(z)
		{
		}
		float m_fX;
		float m_fY;
		float m_fZ;
	};
	struct RGBAData_t
	{
		RGBAData_t(float r = 0.0, float g = 0.0, float b = 0.0, float a = 1.0)
			: m_fRed(r), m_fGreen(g), m_fBlue(b), m_fAlpha(a)
		{
		}
		float m_fRed;
		float m_fGreen;
		float m_fBlue;
		float m_fAlpha;
	};
	struct RGBData_t
	{
		RGBData_t(float r = 0.0, float g = 0.0, float b = 0.0)
			: m_fRed(r), m_fGreen(g), m_fBlue(b)
		{
		}
		float m_fRed;
		float m_fGreen;
		float m_fBlue;
	};

	typedef Vector3Data_t TVertexType;
	typedef std::vector<TVertexType> TVertexVector;

	typedef struct TriangleType_t 
	{
		TriangleType_t(unsigned int a, unsigned b, unsigned c)
			: m_uiIndexA(a), m_uiIndexB(b), m_uiIndexC(c)
		{
		}
		unsigned int m_uiIndexA;
		unsigned int m_uiIndexB;
		unsigned int m_uiIndexC;
	}TTriangleType;
	typedef std::vector<TTriangleType> TTriangleVector;

	typedef Vector3Data_t TMeshNormalType;
	typedef std::vector<TMeshNormalType> TMeshNormalVecotr;

	typedef Vector2Data_t TTextureCoordType;
	typedef std::vector<TTextureCoordType> TTextureCoordVector;

	typedef std::vector<unsigned short> TMaterialIndexVector;

	typedef struct MaterialType_t
	{
		MaterialType_t()
			: m_fPower(20.0), m_strFilename("")
		{
		}
		RGBAData_t m_stFaceColor;
		float m_fPower;
		RGBData_t m_stSpecularColor;
		RGBData_t m_stEmissiveColor;
		std::string m_strFilename;
	} TMaterialType;
	typedef std::vector<TMaterialType> TMaterialVector;
	typedef std::map<unsigned int, unsigned int> TMaterialMap;//face index + meterial index

	typedef std::vector<unsigned short> TMeshVertexIndexVector;
	typedef std::vector<unsigned short> TMeshTriangleIndexVector;
	typedef std::set<unsigned short> TMeshTextureIndexSet;
	
	typedef struct  
	{
		TMeshVertexIndexVector m_vctVertexIndex;
		TMeshTriangleIndexVector m_vctTriangleIndex;
		TMeshTextureIndexSet m_setTextureIndex;
	}TSubMeshType;
	typedef std::vector<TSubMeshType> TSubMeshVector;

public:
	CXFileObject();
	virtual ~CXFileObject();

	int AddVertex(float x, float y, float z);
	int AddVertex(const TVertexType& vertex);
	int AddTriangle(unsigned int a, unsigned int b, unsigned int c);
	int AddTriangle(const TTriangleType& triangle);
	int AddMeshNormal(float x, float y, float z);
	int AddMeshNormal(const TMeshNormalType& normal);
	int AddTextureCoord(float x, float y);
	int AddTextureCoord(const TTextureCoordType& coord);
	int AddMaterial(const TMaterialType& meterial);
	int AddMaterialIndex(unsigned short material);

	int AddMesh(const TSubMeshType& mesh);
	int AddMeshTextureIndex(unsigned short mesh, unsigned short texture);

	void Clear();

	int Output(const std::string& filename) const;

	void Show(std::ostream& os) const;
protected:
	int OutputHeader(std::ofstream& ofile) const;
	int OutputMaterialTemplate(std::ofstream& ofile) const;
	int OutputMeshTemplate(std::ofstream& ofile) const;
protected:
	TVertexVector _vctVertex;
	TTriangleVector _vctTriangle;
	TMeshNormalVecotr _vctMeshNormal;
	TTextureCoordVector _vctTextureCoord;
	TMaterialIndexVector _vctMaterialIndex;
	TMaterialVector _vctMaterial;
	TSubMeshVector _vctSubMesh;
};


#endif
