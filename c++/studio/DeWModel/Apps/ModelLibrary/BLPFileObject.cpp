#include "stdafx.h"

#include "BLP2PNGObject.h"
#include "MPQFileWrapper.h"

#include "BLPFileObject.h"

CBLPFileObject::CBLPFileObject()
{
}

CBLPFileObject::~CBLPFileObject()
{
}

int CBLPFileObject::Extract2PNG(const std::string &mpq, const std::string &blp, const std::string &png)
{
	CMPQFileWrapper mf;
	if(mf.Attach(mpq, blp) != 0)
		return -1;
	size_t sz = 0;
	if(mf.GetSize(sz) != 0)
		return -1;
	char* buffer = new char[sz];
	
	if(mf.Read(buffer, sz) != 0)
	{
		delete [] buffer;
		return -1;
	}

	CBLP2PNGObject obj;
	if(obj.Convert(buffer, sz, png) != 0)
	{
		delete [] buffer;
		return -1;
	}
	delete [] buffer;
	return 0;
}
