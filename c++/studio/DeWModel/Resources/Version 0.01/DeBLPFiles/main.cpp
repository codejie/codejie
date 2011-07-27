
#include <iostream>
#include <string>

#include "BLPFileObject.h"

int main()
{
	std::string str = "A.BLP";
	CBLPFileObject blpfile;
	if(blpfile.Load(str) != 0)
		return -1;
	if(blpfile.Convert2PNG("c.png") != 0)
		return -1;
	return 0;
}
