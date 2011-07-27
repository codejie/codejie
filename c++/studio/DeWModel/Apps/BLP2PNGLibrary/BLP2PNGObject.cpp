#include "stdafx.h"

#include "BLPConverter\MemImage.h"

#include "BLP2PNGObject.h"

CBLP2PNGObject::CBLP2PNGObject()
{
}

CBLP2PNGObject::~CBLP2PNGObject()
{
}

int CBLP2PNGObject::Convert(const std::string &blp, const std::string &png) const
{
	MemImage image;
	if(!image.LoadFromBLP(blp.c_str()))
		return -1;
	return image.SaveToPNG(png.c_str()) ? 0 : -1;
}

int CBLP2PNGObject::Convert(const char* buffer, size_t size, const std::string& png) const
{
	MemImage image;
	if(!image.LoadBLPFromBuffer(buffer, size))
		return -1;
	return image.SaveToPNG(png.c_str()) ? 0 : -1;
}