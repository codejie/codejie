// DXLibrary.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "DXLibrary.h"


// This is an example of an exported variable
DXLIBRARY_API int nDXLibrary=0;

// This is an example of an exported function.
DXLIBRARY_API int fnDXLibrary(void)
{
	return 42;
}

// This is the constructor of a class that has been exported.
// see DXLibrary.h for the class definition
CDXLibrary::CDXLibrary()
{
	return;
}
