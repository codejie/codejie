#include <windows.h>
#include <fstream>

#include "ATI_Compress.h"

#include "png.h"

#include "BLPFileObject.h"

CBLPFileObject::CBLPFileObject()
: m_pData(NULL)
{
}

CBLPFileObject::~CBLPFileObject()
{
	Release();
}

void CBLPFileObject::Release()
{
	if(m_pData != NULL)
		delete [] m_pData, m_pData = NULL;
}

int CBLPFileObject::Load(const std::string& filename)
{
	Release();

	std::ifstream ifile;

	ifile.open(filename.c_str(), std::ios_base::in | std::ios_base::binary);
	if(!ifile.good())
		return -1;

	ifile.read(m_stHeader.m_achFlag, 4);
	ifile.read((char*)&m_stHeader.m_uiVersion, sizeof(m_stHeader.m_uiVersion));
	ifile.read((char*)&m_stHeader.m_ucCompression, sizeof(m_stHeader.m_ucCompression));
	ifile.read((char*)&m_stHeader.m_ucAlphaBitDepth, sizeof(m_stHeader.m_ucAlphaBitDepth));
	ifile.read((char*)&m_stHeader.m_ucAlphaUnknown, sizeof(m_stHeader.m_ucAlphaUnknown));
	ifile.read((char*)&m_stHeader.m_ucMIPUnknown, sizeof(m_stHeader.m_ucMIPUnknown));
	ifile.read((char*)&m_stHeader.m_uiXResolution, sizeof(m_stHeader.m_uiXResolution));
	ifile.read((char*)&m_stHeader.m_uiYResolution, sizeof(m_stHeader.m_uiYResolution));
	ifile.read((char*)m_stHeader.m_auiMIPOffset, sizeof(unsigned int) * 16);
	ifile.read((char*)m_stHeader.m_auiMIPSize, sizeof(unsigned int) * 16);

	ifile.seekg(-1, std::ios_base::end);

	size_t sz = ifile.tellg();

	m_pData = new unsigned char[sz];
	ifile.seekg(0, std::ios_base::beg);
	ifile.read((char*)m_pData, sz);

	if(ifile.is_open())
		ifile.close();

	return 0;
}

unsigned char CBLPFileObject::GetPalettizedAlpha(unsigned char *alphaBuffer, int pixelIndex, bool b8Bit)
{
	if (b8Bit)
	{
		return alphaBuffer[pixelIndex];
	}
	else
	{
		int byteIndex = pixelIndex / 8;
		int bitIndex = pixelIndex % 8;
		unsigned char mask = 0x0001 << bitIndex;
		return (alphaBuffer[byteIndex] & mask) ? 255 : 0;
	}
}

int CBLPFileObject::Convert2PNG(const std::string &output)
{
	if(m_pData == NULL)
		return -1;

	FILE* ofile = fopen(output.c_str(), "wb");
	if(ofile == NULL)
		return -1;

	png_structp pngptr = NULL;
	png_infop pnginfoptr = NULL;

	try
	{
		pngptr = png_create_write_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
		if(pngptr == NULL)
			throw std::exception("png create failed.");
		pnginfoptr = png_create_info_struct(pngptr);
		if(pnginfoptr == NULL)
		{
			png_destroy_write_struct(&pngptr, png_infopp_NULL);
			throw std::exception("pnp create info failed.");
		}

		if(setjmp(png_jmpbuf(pngptr)) != NULL)
		{
			png_destroy_read_struct(&pngptr, &pnginfoptr, png_infopp_NULL);
			throw std::exception("setjmp is not NULL.");
		}

		png_init_io(pngptr, ofile);

		bool bNeedDelete = false;
		int transform = PNG_TRANSFORM_IDENTITY;
		unsigned char** pRowPointers = NULL;
		if(m_stHeader.m_ucCompression == 1)//Uncompressed
		{
			//PNG palette
			png_color pngPalette[256];
			for(int i = 0; i < 256; ++ i)
			{
				pngPalette[i].red = m_pData[i * 4 + 2];
				pngPalette[i].green = m_pData[i * 4 + 1];
				pngPalette[i].blue = m_pData[i * 4 + 0];
			}
			//PNG image type
			png_set_IHDR(pngptr, pnginfoptr, m_stHeader.m_uiXResolution, m_stHeader.m_uiYResolution, 8/* bit depth */, PNG_COLOR_TYPE_PALETTE, PNG_INTERLACE_NONE, PNG_COMPRESSION_TYPE_DEFAULT, PNG_FILTER_TYPE_DEFAULT);
			//PNG create row pointers
			pRowPointers = new unsigned char*[m_stHeader.m_uiYResolution];
			unsigned int bufOffset = m_stHeader.m_auiMIPOffset[0];
			for(unsigned int i = 0; i < m_stHeader.m_uiYResolution; ++ i)
			{
				pRowPointers[i] = &m_pData[bufOffset];
				bufOffset += m_stHeader.m_uiXResolution;
			}
			if(m_stHeader.m_ucAlphaBitDepth == 8)
			{
				bufOffset = m_stHeader.m_auiMIPOffset[0];
				unsigned char* imgBuff = &m_pData[bufOffset];
				unsigned int alphaOffset = bufOffset + (m_stHeader.m_uiXResolution* m_stHeader.m_uiYResolution);
				bool trans = false;
				unsigned char alphaTransIndex = 0;
				unsigned int pixelCount = m_stHeader.m_uiXResolution * m_stHeader.m_uiYResolution;
				for(unsigned int i = 0; i < pixelCount; ++ i)
				{
					unsigned char alpha = GetPalettizedAlpha(&m_pData[alphaOffset], i, (m_stHeader.m_ucAlphaBitDepth == 8));
					if(alpha < 0x80)//alpha default value
					{
						if(trans == false)
						{
							alphaTransIndex = m_pData[bufOffset + i];
							pngPalette[alphaTransIndex].red = pngPalette[0].red;
							pngPalette[alphaTransIndex].green = pngPalette[0].green;
							pngPalette[alphaTransIndex].blue = pngPalette[0].blue;
							trans = true;
							break;
						}
					}

				}
				if(trans)
				{
					for(unsigned int i = 0; i < pixelCount; ++ i)
					{
						if(m_pData[bufOffset + i] == 0)
							m_pData[bufOffset + i] = alphaTransIndex;
						unsigned char alpha = GetPalettizedAlpha(&m_pData[bufOffset], i, (m_stHeader.m_ucAlphaBitDepth == 8));
						if(alpha < 0x80)
							m_pData[bufOffset + i] = 0;
					}
					png_byte t[1];
					t[0] = 0;
					png_set_tRNS(pngptr, pnginfoptr, t, 1, NULL);
				}
			}
			png_set_PLTE(pngptr, pnginfoptr, pngPalette, 256);
		}
		else if(m_stHeader.m_ucCompression == 2)//compress
		{
			int textureFormat;
			int blpFormat;
			unsigned int colorType;
			if(m_stHeader.m_ucAlphaBitDepth == 8)
			{
				blpFormat = 5;//FMT_DXT3
				textureFormat = 3;//FMT_A8R8G8B8
				colorType = PNG_COLOR_TYPE_RGB_ALPHA;
			}
			else if(m_stHeader.m_ucAlphaBitDepth == 1)
			{
				blpFormat = 4;//FMT_DXT1;
				textureFormat = 3;//FMT_A8R8G8B8
				colorType = PNG_COLOR_TYPE_RGB_ALPHA;
			}
			else
			{
				blpFormat = 4;//FMT_DXT1
				textureFormat = 2;//FMT_X8R8G8B8
				colorType = PNG_COLOR_TYPE_RGB;
			}
			transform = PNG_TRANSFORM_BGR;
			//conver to ARGB
			unsigned int srcPitch = 2 * m_stHeader.m_uiXResolution;
			if(blpFormat == 5)
				srcPitch *= 2;
			ATI_TC_Texture atiSource;
			atiSource.dwSize = sizeof(ATI_TC_Texture);
			atiSource.dwWidth = m_stHeader.m_uiXResolution;
			atiSource.dwHeight = m_stHeader.m_uiYResolution;
			atiSource.dwDataSize = m_stHeader.m_auiMIPSize[0];
			atiSource.dwPitch = srcPitch;
			atiSource.format = (blpFormat == 4) ? ATI_TC_FORMAT_DXT1 : ATI_TC_FORMAT_DXT3;
			atiSource.pData = &(m_pData[m_stHeader.m_auiMIPOffset[0]]);

			ATI_TC_Texture atiDest;
			atiDest.dwSize = sizeof(ATI_TC_Texture);
			atiDest.dwWidth = m_stHeader.m_uiXResolution;
			atiDest.dwHeight = m_stHeader.m_uiYResolution;
			atiDest.format = ATI_TC_FORMAT_ARGB_8888;
			atiDest.dwPitch = atiDest.dwWidth * 4;
			atiDest.dwDataSize = ATI_TC_CalculateBufferSize(&atiDest);
			atiDest.pData = new ATI_TC_BYTE[atiDest.dwDataSize];

			ATI_TC_ERROR error = ATI_TC_ConvertTexture(&atiSource, &atiDest, NULL, NULL, NULL, NULL);
			if(error != ATI_TC_OK)
				throw std::exception("ATI_TC_ConvertTexture failed.");
			
			pRowPointers = new unsigned char*[m_stHeader.m_uiYResolution];
			int row = atiDest.dwPitch;
			if(textureFormat == 2)
			{
				bNeedDelete = true;
				for(unsigned int i = 0; i < m_stHeader.m_uiYResolution; ++ i)
				{
					pRowPointers[i] = new unsigned char[3 * m_stHeader.m_uiYResolution];
					for(unsigned int j = 0; j < m_stHeader.m_uiYResolution; ++ j)
					{
						pRowPointers[i][3 * j + 0] = atiDest.pData[i * row + 4 * j + 0];
						pRowPointers[i][3 * j + 1] = atiDest.pData[i * row + 4 * j + 1];
						pRowPointers[i][3 * j + 2] = atiDest.pData[i * row + 4 * j + 2];
					}
				}
			}
			else
			{
				for(unsigned int i = 0; i < m_stHeader.m_uiYResolution; ++ i)
				{
					pRowPointers[i] = &((unsigned char*)atiDest.pData)[i * row];
				}
			}
			png_set_IHDR(pngptr, pnginfoptr, m_stHeader.m_uiXResolution, m_stHeader.m_uiYResolution, 8, colorType, PNG_INTERLACE_NONE, PNG_COMPRESSION_TYPE_DEFAULT, PNG_FILTER_TYPE_DEFAULT);
		}
		png_set_rows(pngptr, pnginfoptr, pRowPointers);
		png_write_png(pngptr, pnginfoptr, transform, NULL);

		if(bNeedDelete)
		{
			for(unsigned int i = 0; i < m_stHeader.m_uiYResolution; ++ i)
			{
				delete [] pRowPointers[i];
			}
		}
		delete [] pRowPointers;
	}
	catch(const std::exception& e)
	{
		fclose(ofile);
		return -1;
	}
	fclose(ofile);

	return 0;
}