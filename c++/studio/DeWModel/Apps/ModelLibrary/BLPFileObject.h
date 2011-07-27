#ifndef __BLPFILEOBJECT_H__
#define __BLPFILEOBJECT_H__

class CBLPFileObject
{
public:
	CBLPFileObject();
	virtual ~CBLPFileObject();

	int Extract2PNG(const std::string& mpq, const std::string& blp, const std::string& png);
};

#endif
