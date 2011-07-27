#ifndef __SKINSTRUCTURE_H__
#define __SKINSTRUCTURE_H__

#include <vector>

#include "DataTypes.h"
#include "MPQFileWrapper.h"

namespace Skin
{

enum StructType { ST_BASE, ST_HEADER, ST_INDEX, ST_TRIANGLE, ST_PROPERTY,
					ST_SUBMESH, ST_TEXTURE};

class CBase
{
public:
	CBase(StructType type) : m_eType(type) {}
	virtual ~CBase() {}
public:
	StructType m_eType;
};

class CHeader : public CBase
{
public:
	CHeader() : CBase(ST_HEADER) {}
	virtual ~CHeader() {}

	int Read(CMPQFileWrapper& mf)
	{
		return m_stHeader.Read(mf);
	}

	void Clear() { memset(&m_stHeader, 0, sizeof(Header_t)); }
public:
	Header_t m_stHeader;
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

typedef CHeader THeader;
typedef CSubStruct<ST_INDEX, UInt16_t> TIndex;
typedef CSubStruct<ST_TRIANGLE, UInt16_t> TTriangle;
typedef CSubStruct<ST_PROPERTY, UInt32_t> TProperty;
typedef CSubStruct<ST_SUBMESH, SubMesh_t> TSubMesh;
typedef CSubStruct<ST_TEXTURE, Texture_t> TTexture;


}//skin

#endif

