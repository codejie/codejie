#ifndef __BLP2PNGOBJECT_H__
#define __BLP2PNGOBJECT_H__

#include <string>

#ifdef BLP2PNGLIBRARY_EXPORTS
#define BLP2PNGLIBRARY_API __declspec(dllexport)
#else
#define BLP2PNGLIBRARY_API __declspec(dllimport)
#endif

class BLP2PNGLIBRARY_API CBLP2PNGObject
{
public:
	CBLP2PNGObject();
	virtual ~CBLP2PNGObject();

	int Convert(const std::string& blp, const std::string& png) const;
	int Convert(const char* buffer, size_t size, const std::string& png) const;
};


#endif
