// BLP2PNGLibrary.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "BLPConverter\MemImage.h"
#include "BLP2PNGLibrary.h"


//// This is an example of an exported variable
//BLP2PNGLIBRARY_API int nBLP2PNGLibrary=0;
//
//// This is an example of an exported function.
//BLP2PNGLIBRARY_API int fnBLP2PNGLibrary(void)
//{
//	return 42;
//}

// This is the constructor of a class that has been exported.
// see BLP2PNGLibrary.h for the class definition
CBLP2PNGLibrary::CBLP2PNGLibrary()
{
}


void CBLP2PNGLibrary::Show(std::ostream &os) const
{
	os << "Hello world." << std::endl;
}

int CBLP2PNGLibrary::Convert(const std::string &input, const std::string &output) const
{
	MemImage image;
	FORMATID informat;
	if(!image.LoadFromBLP(input.c_str(), &informat))
		return -1;
	FORMATID outformat = MemImage::s_ruleTable[informat];
	return (image.Save(output.c_str(), outformat) ? 0 : -1);
}