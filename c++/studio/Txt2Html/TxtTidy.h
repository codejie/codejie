#ifndef __TXTTIDY_H__
#define __TXTTIDY_H__

#include <string>

class TxtTidy
{
public:
	TxtTidy() {}
	virtual ~TxtTidy() {}

//	int Load(const std::string& file);
	int Tidy(const std::string& input, const std::string& output);

private:

};

#endif