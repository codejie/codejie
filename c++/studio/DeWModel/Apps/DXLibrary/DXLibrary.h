// The following ifdef block is the standard way of creating macros which make exporting 
// from a DLL simpler. All files within this DLL are compiled with the DXLIBRARY_EXPORTS
// symbol defined on the command line. this symbol should not be defined on any project
// that uses this DLL. This way any other project whose source files include this file see 
// DXLIBRARY_API functions as being imported from a DLL, whereas this DLL sees symbols
// defined with this macro as being exported.
#ifdef DXLIBRARY_EXPORTS
#define DXLIBRARY_API __declspec(dllexport)
#else
#define DXLIBRARY_API __declspec(dllimport)
#endif

// This class is exported from the DXLibrary.dll
class DXLIBRARY_API CDXLibrary {
public:
	CDXLibrary(void);
	// TODO: add your methods here.
};

extern DXLIBRARY_API int nDXLibrary;

DXLIBRARY_API int fnDXLibrary(void);
