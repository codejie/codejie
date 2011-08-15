
#include "DataAccess.h"
#include "DataLoader.h"


void DataLoader::Final()
{
	if(_taskMsg != NULL)
	{
		_taskMsg->clear_timer();
	}
}