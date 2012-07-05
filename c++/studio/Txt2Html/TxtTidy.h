#ifndef __TXTTIDY_H__
#define __TXTTIDY_H__

class TxtTidy
{
public:
	TxtTidy() {}
	virtual ~TxtTidy() {}

//	int Load(const std::string& file);
	int Tidy(const std::string& file);

private:

};

#endif