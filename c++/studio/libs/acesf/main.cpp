
#include "acesf/Service.h"

#if !defined (ACE_LACKS_PRAGMA_ONCE)
#  pragma once
#endif /* ACE_LACKS_PRAGMA_ONCE */

int main(int argc, char* argv[])
{
	if(pACESF_Service == NULL)
		return -1;

	pACESF_Service->run(argc, argv);

	return 0;
}