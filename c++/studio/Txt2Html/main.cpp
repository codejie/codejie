
#include <string>
#include <iostream>


#include "TxtTidy.h"


int main()
{
	TxtTidy tidy;
	tidy.Tidy("c.txt", "b.txt");
	tidy.Load("b.txt");
	TxtTidy::TData data;
	while(tidy.GetData(data) == 0)
	{
		std::cout << data.word << "[" << data.symbol << "]:" << data.data << std::endl;
	}

	return 0;
}