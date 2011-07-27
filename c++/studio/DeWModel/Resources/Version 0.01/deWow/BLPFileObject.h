#ifndef __BLPFILEOBJECT_H__
#define __BLPFILEOBJECT_H__

#include <iostream>
#include <string>

class CBLPFileObject
{
public:
	struct Header_t
	{
		char m_achFlag[4];
		unsigned int m_uiVersion;
		unsigned char m_ucCompression;
		unsigned char m_ucAlphaBitDepth;
		unsigned char m_ucAlphaUnknown;
		unsigned char m_ucMIPUnknown;
		unsigned int m_uiXResolution;
		unsigned int m_uiYResolution;
		unsigned int m_auiMIPOffset[16];
		unsigned int m_auiMIPSize[16];
	};
public:
	CBLPFileObject();
	virtual ~CBLPFileObject();

	int Load(const std::string& filename);
	int Convert2PNG(const std::string& output);
protected:
	void Release();
	unsigned char GetPalettizedAlpha(unsigned char* alphaBuffer, int pixelIndex, bool b8Bit);
public:
	Header_t m_stHeader;
	unsigned char* m_pData;
};

#endif
