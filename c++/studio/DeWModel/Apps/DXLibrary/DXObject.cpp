#include "stdafx.h"

#include "DXObject.h"


CDXObject::CDXObject()
: _hThread(NULL), _dwThreadID(0)
{
	for(int i = 0; i < MAX_EVENT; ++ i)
		_haEvent[i] = NULL;
}

CDXObject::~CDXObject()
{
	ReleaseThread();
	ReleaseD3DObject();
}

int CDXObject::Init(HWND hwnd)
{
	if(InitD3DObject(hwnd) != 0)
		return -1;

	return 0;
}

int CDXObject::InitThread()
{
	ReleaseThread();

	//Event
	_haEvent[0] = CreateEvent(NULL, FALSE, FALSE, L"STOP");
	_haEvent[1] = CreateEvent(NULL, FALSE, FALSE, L"PAUSE");
		
	return 0;
}

int CDXObject::Run()
{
	_hThread = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)ThreadProc, NULL, 0, &_dwThreadID);
	if(_hThread == NULL)
		return -1;
	return 0;
}

DWORD CDXObject::ThreadProc(LPVOID param)
{
	bool stop = false;
	while(!stop)
	{
		DWORD ret = MsgWaitForMultipleObjects(MAX_EVENT, _haEvent, FALSE, 0, QS_ALLEVENTS);
		DWORD evt = ret - WAIT_OBJECT_0;
		switch(evt)
		{
		case 0:
			stop = true;
			break;
		case 1:
			continue;
			break;
		default:

		}
	}
	return 0;
}

void CDXObject::ReleaseD3DObject()
{
}

void CDXObject::ReleaseThread()
{
	if(_haEvent[0] != NULL)
	{
		SetEvent(_haEvent[0]);

	}
	for(int i = 0; i < MAX_EVENT; ++ i)
	{
		if(_haEvent[i] != NULL)
		{
			CloseHandle(_haEvent[i]);
			_haEvent[i] = NULL;
		}
	}
}