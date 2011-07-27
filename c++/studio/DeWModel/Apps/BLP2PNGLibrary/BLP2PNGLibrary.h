// The following ifdef block is the standard way of creating macros which make exporting 
// from a DLL simpler. All files within this DLL are compiled with the BLP2PNGLIBRARY_EXPORTS
// symbol defined on the command line. this symbol should not be defined on any project
// that uses this DLL. This way any other project whose source files include this file see 
// BLP2PNGLIBRARY_API functions as being imported from a DLL, whereas this DLL sees symbols
// defined with this macro as being exported.

#ifndef __BLP2PNGLIBRARY_H__
#define __BLP2PNGLIBRARY_H__

#include <iostream>
#include <string>

#ifdef BLP2PNGLIBRARY_EXPORTS
#define BLP2PNGLIBRARY_API __declspec(dllexport)
#else
#define BLP2PNGLIBRARY_API __declspec(dllimport)
#endif

// This class is exported from the BLP2PNGLibrary.dll
class BLP2PNGLIBRARY_API CBLP2PNGLibrary {
public:
	CBLP2PNGLibrary(void);
	// TODO: add your methods here.

	int Convert(const std::string& input, const std::string& output) const;

	void Show(std::ostream& os) const;
};

//extern BLP2PNGLIBRARY_API int nBLP2PNGLibrary;
//
//BLP2PNGLIBRARY_API int fnBLP2PNGLibrary(void);

#endif
