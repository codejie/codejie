//////////////////////////////////////////////////////////////////////////////
//
//  ATI Technologies Inc.
//  1 Commerce Valley Drive East
//  Markham, Ontario
//  CANADA  L3T 7X6
//
//  File Name:   ATI2N_Compress.cpp
//  Description: Demonstrates uses of the ATI_Compress library
//               Error checking & other such niceties have been avoided in 
//               the interest of code clarity (& laziness)
//
//  Copyright (c) 2004 ATI Technologies Inc.
//
//  Developer:  Seth Sowerby
//  Email:		devtools@ati.com
//
//////////////////////////////////////////////////////////////////////////////

#ifdef WIN64
#	define POINTER_64 __ptr64
#endif

#include <windows.h>
#include <stdio.h>
#include <ddraw.h>
#include "ATI_Compress.h"


#ifdef WIN64
#pragma pack(4)

typedef struct _DDSURFACEDESC2_64
{
    DWORD               dwSize;                 // size of the DDSURFACEDESC structure
    DWORD               dwFlags;                // determines what fields are valid
    DWORD               dwHeight;               // height of surface to be created
    DWORD               dwWidth;                // width of input surface
    union
    {
        LONG            lPitch;                 // distance to start of next line (return value only)
        DWORD           dwLinearSize;           // Formless late-allocated optimized surface size
    } DUMMYUNIONNAMEN(1);
    union
    {
        DWORD           dwBackBufferCount;      // number of back buffers requested
        DWORD           dwDepth;                // the depth if this is a volume texture 
    } DUMMYUNIONNAMEN(5);
    union
    {
        DWORD           dwMipMapCount;          // number of mip-map levels requestde
                                                // dwZBufferBitDepth removed, use ddpfPixelFormat one instead
        DWORD           dwRefreshRate;          // refresh rate (used when display mode is described)
        DWORD           dwSrcVBHandle;          // The source used in VB::Optimize
    } DUMMYUNIONNAMEN(2);
    DWORD               dwAlphaBitDepth;        // depth of alpha buffer requested
    DWORD               dwReserved;             // reserved
    void* __ptr32       lpSurface;              // pointer to the associated surface memory
    union
    {
        DDCOLORKEY      ddckCKDestOverlay;      // color key for destination overlay use
        DWORD           dwEmptyFaceColor;       // Physical color for empty cubemap faces
    } DUMMYUNIONNAMEN(3);
    DDCOLORKEY          ddckCKDestBlt;          // color key for destination blt use
    DDCOLORKEY          ddckCKSrcOverlay;       // color key for source overlay use
    DDCOLORKEY          ddckCKSrcBlt;           // color key for source blt use
    union
    {
        DDPIXELFORMAT   ddpfPixelFormat;        // pixel format description of the surface
        DWORD           dwFVF;                  // vertex format description of vertex buffers
    } DUMMYUNIONNAMEN(4);
    DDSCAPS2            ddsCaps;                // direct draw surface capabilities
    DWORD               dwTextureStage;         // stage in multitexture cascade
} DDSURFACEDESC2_64;

#define DDSD2 DDSURFACEDESC2_64
#else
#define DDSD2 DDSURFACEDESC2
#endif

static const DWORD DDS_HEADER = MAKEFOURCC('D', 'D', 'S', ' ');

#define FOURCC_ATI2N MAKEFOURCC('A', 'T', 'I', '2')

int main(int argc, char* argv[])
{
	if(argc < 2)
	{
		printf("ATI2N_Compress sourcefile destfile\n");
		return 0;
	}

	char* pszSourceFile = argv[1];
	char* pszDestFile = argv[2];

	char szDestFile[MAX_PATH];
	if(pszDestFile == NULL)
	{
		pszDestFile = szDestFile;
		int nLen = strlen(pszSourceFile)-4;
		strncpy(pszDestFile, pszSourceFile, nLen);
		strcpy(pszDestFile+nLen, "_Out.dds");
	}

	printf("ATI2N_Compress %s %s\n", pszSourceFile, pszDestFile);


	// Load the source texture
	FILE* pSourceFile = fopen(pszSourceFile, "rb");

	DWORD dwFileHeader;
	fread(&dwFileHeader ,sizeof(DWORD), 1, pSourceFile);

	DDSD2 ddsdSource;
	fread(&ddsdSource, sizeof(DDSD2), 1, pSourceFile);

	if(ddsdSource.ddpfPixelFormat.dwRGBBitCount==32)
	{
// Init source texture
		ATI_TC_Texture srcTexture;
		srcTexture.dwSize = sizeof(srcTexture);
		srcTexture.dwWidth = ddsdSource.dwWidth;
		srcTexture.dwHeight = ddsdSource.dwHeight;
		srcTexture.dwPitch = ddsdSource.lPitch;
		srcTexture.format = ATI_TC_FORMAT_ARGB_8888;
		srcTexture.dwDataSize = ATI_TC_CalculateBufferSize(&srcTexture);
		srcTexture.pData = (ATI_TC_BYTE*) malloc(srcTexture.dwDataSize);

		fread(srcTexture.pData, srcTexture.dwDataSize, 1, pSourceFile);
		fclose(pSourceFile);

// Init dest texture
		ATI_TC_Texture destTexture;
		destTexture.dwSize = sizeof(destTexture);
		destTexture.dwWidth = ddsdSource.dwWidth;
		destTexture.dwHeight = ddsdSource.dwHeight;
		destTexture.dwPitch = 0;
		destTexture.format = ATI_TC_FORMAT_ATI2N;
		destTexture.dwDataSize = ATI_TC_CalculateBufferSize(&destTexture);
		destTexture.pData = (ATI_TC_BYTE*) malloc(destTexture.dwDataSize);

// Compress
		printf("Compressing\n");
		ATI_TC_ConvertTexture(&srcTexture, &destTexture, NULL, NULL, NULL, NULL);
		printf("Done compress\n");

// Save the compressed texture
		FILE* pDestFile = fopen(pszDestFile, "wb");
		fwrite(&DDS_HEADER, sizeof(DWORD), 1, pDestFile);

		DDSD2 ddsdDest;
		memset(&ddsdDest, 0, sizeof(DDSD2));
		ddsdDest.dwSize = sizeof(DDSD2);
		ddsdDest.dwFlags = DDSD_CAPS|DDSD_WIDTH|DDSD_HEIGHT|DDSD_PIXELFORMAT|DDSD_MIPMAPCOUNT|DDSD_LINEARSIZE;
		ddsdDest.dwWidth = destTexture.dwWidth;
		ddsdDest.dwHeight = destTexture.dwHeight;
		ddsdDest.dwMipMapCount = 1;
		ddsdDest.dwLinearSize = destTexture.dwDataSize;

		ddsdDest.ddpfPixelFormat.dwSize = sizeof(DDPIXELFORMAT);
		ddsdDest.ddpfPixelFormat.dwFlags=DDPF_FOURCC;
		ddsdDest.ddpfPixelFormat.dwFourCC = FOURCC_ATI2N;
		ddsdDest.ddsCaps.dwCaps = DDSCAPS_TEXTURE|DDSCAPS_COMPLEX|DDSCAPS_MIPMAP;

		fwrite(&ddsdDest, sizeof(DDSD2), 1, pDestFile);
		fwrite(destTexture.pData , destTexture.dwDataSize, 1, pDestFile);
		fclose(pDestFile);

		free(srcTexture.pData);
		free(destTexture.pData);
	}
	else if(ddsdSource.ddpfPixelFormat.dwFourCC == FOURCC_ATI2N)
	{
// Init source texture
		ATI_TC_Texture srcTexture;
		srcTexture.dwSize = sizeof(srcTexture);
		srcTexture.dwWidth = ddsdSource.dwWidth;
		srcTexture.dwHeight = ddsdSource.dwHeight;
		srcTexture.dwPitch = 0;
		srcTexture.format = ATI_TC_FORMAT_ATI2N;
		srcTexture.dwDataSize = ddsdSource.dwLinearSize;
		srcTexture.pData = (ATI_TC_BYTE*) malloc(srcTexture.dwDataSize);

		fread(srcTexture.pData, srcTexture.dwDataSize, 1, pSourceFile);
		fclose(pSourceFile);

// Init dest texture
		ATI_TC_Texture destTexture;
		destTexture.dwSize = sizeof(destTexture);
		destTexture.dwWidth = ddsdSource.dwWidth;
		destTexture.dwHeight = ddsdSource.dwHeight;
		destTexture.dwPitch = 0;
		destTexture.format = ATI_TC_FORMAT_ARGB_8888;
		destTexture.dwDataSize = ATI_TC_CalculateBufferSize(&destTexture);
		destTexture.pData = (ATI_TC_BYTE*) malloc(destTexture.dwDataSize);

// Decompress
		printf("Decompressing\n");
		ATI_TC_ConvertTexture(&srcTexture, &destTexture, NULL, NULL, NULL, NULL);
		printf("Done decompress\n");

// Save the decompressed texture
		FILE* pDestFile = fopen(pszDestFile, "wb");
		fwrite(&DDS_HEADER, sizeof(DWORD), 1, pDestFile);

		DDSD2 ddsdDest;
		memset(&ddsdDest, 0, sizeof(DDSD2));
		ddsdDest.dwSize = sizeof(DDSD2);
		ddsdDest.dwFlags = DDSD_CAPS|DDSD_WIDTH|DDSD_HEIGHT|DDSD_PIXELFORMAT|DDSD_MIPMAPCOUNT|DDSD_LINEARSIZE;
		ddsdDest.dwWidth = destTexture.dwWidth;
		ddsdDest.dwHeight = destTexture.dwHeight;
		ddsdDest.dwMipMapCount = 1;
		ddsdDest.lPitch = destTexture.dwWidth * 4;

		ddsdDest.ddpfPixelFormat.dwSize = sizeof(DDPIXELFORMAT);
		ddsdDest.ddpfPixelFormat.dwRBitMask = 0x00ff0000;
		ddsdDest.ddpfPixelFormat.dwGBitMask = 0x0000ff00;
		ddsdDest.ddpfPixelFormat.dwBBitMask = 0x000000ff;
		ddsdDest.ddpfPixelFormat.dwRGBBitCount = 32;
		ddsdDest.ddpfPixelFormat.dwFlags=DDPF_ALPHAPIXELS|DDPF_RGB;
		ddsdDest.ddpfPixelFormat.dwRGBAlphaBitMask = 0xff000000;
		ddsdDest.ddsCaps.dwCaps = DDSCAPS_TEXTURE|DDSCAPS_COMPLEX|DDSCAPS_MIPMAP;

		fwrite(&ddsdDest, sizeof(DDSD2), 1, pDestFile);
		fwrite(destTexture.pData, destTexture.dwDataSize, 1, pDestFile);
		fclose(pDestFile);

		free(srcTexture.pData);
		free(destTexture.pData);
	}
	else
		printf("Invalid source texture - bit count = %i, FourCC = %08x\n", ddsdSource.ddpfPixelFormat.dwFourCC);

	return 0;
}
