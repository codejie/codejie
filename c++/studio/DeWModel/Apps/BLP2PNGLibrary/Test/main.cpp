//#include "BLP2PNGLibrary.h"

#include "BLP2PNGObject.h"

#include <iostream>

int main()
{
	//CBLP2PNGLibrary obj;
	//obj.Show(std::cout);
	//if(obj.Convert("UI-Glyph-Rune-2.blp", "UI-Glyph-Rune-2.blp.png") != 0)
	//	std::cout << "failed." << std::endl;

	CBLP2PNGObject obj;

	if(obj.Convert("xyz.blp", "xyz.blp.png") != 0)
		std::cout << "failed." << std::endl;

	return 0;
}