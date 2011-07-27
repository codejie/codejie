#include "stdafx.h"

#include "DSoundObject.h"

HANDLE _eventNotify[3];

CDSoundObject::CDSoundObject(CDSoundObject::SoundType type)
: _eType(type)
, _pDS(NULL), _pDSBuffer(NULL), _pDSNotify(NULL)
, _uiNotifyThreadID(0), _hNotifyThread(NULL)
{
}

CDSoundObject::~CDSoundObject()
{
	Release();
}

void CDSoundObject::Release()
{
	ReleaseDSound();
}

int CDSoundObject::Init(HWND hwnd)
{
	_hWnd = hwnd;

	return CreateDSound();
}

bool CDSoundObject::IsReady() const
{
	return (_pDS != NULL && _pDSBuffer != NULL);
}

int CDSoundObject::CreateDSound()
{
	HRESULT hr = DirectSoundCreate8(NULL, &_pDS, NULL);
	if(hr != DS_OK)
		return -1;
	_pDS->SetCooperativeLevel(_hWnd, DSSCL_NORMAL);
	return 0;
}


void CDSoundObject::ReleaseDSound()
{
	if(_pDS != NULL)
		_pDS->Release(), _pDS = NULL;
}

int CDSoundObject::CreateDSoundBuffer()
{
	IDirectSoundBuffer* psbuffer = NULL;

	DSBUFFERDESC desc;
	memset(&desc, 0, sizeof(DSBUFFERDESC));
	desc.dwSize = sizeof(DSBUFFERDESC);
	desc.dwFlags = DSBCAPS_GLOBALFOCUS | DSBCAPS_CTRLVOLUME | DSBCAPS_CTRLPOSITIONNOTIFY | DSBCAPS_LOCSOFTWARE;
	desc.dwBufferBytes = CDSoundObject::SIZE_DS_BUFFER;//header.data_size;
	desc.lpwfxFormat = &_fmtWave;

	if(_pDSBuffer != NULL)
		_pDSBuffer->Release();

	HRESULT hr = _pDS->CreateSoundBuffer(&desc, &psbuffer, NULL);
	if(hr != DS_OK)
		return -1;
	hr = psbuffer->QueryInterface(IID_IDirectSoundBuffer8, (void**)&_pDSBuffer);
	psbuffer->Release();
	if(hr != DS_OK)
		return -1;

	if(CreateNotifyThread() != 0)
		return -1;

	if(InitDSData() != 0)
		return -1;
	_pDSBuffer->SetCurrentPosition(0);

	return 0;
}

void CDSoundObject::ReleaseDSoundBuffer()
{
	ReleaseNotifyThread();	

	if(_pDSBuffer != NULL)
	{
		_pDSBuffer->Stop();
		_pDSBuffer->Release();
		_pDSBuffer = NULL;
	}
}

int CDSoundObject::CreateNotifyThread()
{
	//event
	_eventNotify[0] = CreateEvent(NULL, FALSE, FALSE, NULL);
	_eventNotify[1] = CreateEvent(NULL, FALSE, FALSE, NULL);
	_eventNotify[2] = CreateEvent(NULL, FALSE, FALSE, NULL);

	_hNotifyThread = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)NotifyHandleProc, (LPVOID)this, 0, &_uiNotifyThreadID);
	if(_hNotifyThread == NULL)
		return -1;

	HRESULT hr = _pDSBuffer->QueryInterface(IID_IDirectSoundNotify8, (void**)&_pDSNotify);
	if(hr != DS_OK)
		return -1;

	_notifyPos[0].dwOffset = (SIZE_DS_BUFFER / 2) - 1;
	_notifyPos[0].hEventNotify = _eventNotify[0];
	_notifyPos[1].dwOffset = SIZE_DS_BUFFER - 1;
	_notifyPos[1].hEventNotify = _eventNotify[1];

	hr = _pDSNotify->SetNotificationPositions(2, _notifyPos);
	if(hr != DS_OK)
		return -1;

	return 0;
}

void CDSoundObject::ReleaseNotifyThread()
{
	if(_hNotifyThread != NULL)
	{
		SetEvent(_eventNotify[2]);
		Sleep(10);
		TerminateThread(_hNotifyThread, 0);
		CloseHandle(_hNotifyThread);
		_hNotifyThread = NULL;
	}
	for(int i = 0; i < 3; ++ i)
	{
		if(_eventNotify[i] != NULL)
		{
			CloseHandle(_eventNotify[i]);
			_eventNotify[i] = NULL;
		}
	}

	if(_pDSNotify != NULL)
	{
		_pDSNotify->Release();
		_pDSNotify = NULL;
	}
}

DWORD CDSoundObject::NotifyHandleProc(LPVOID param)
{
	CDSoundObject* obj = (CDSoundObject*)(param);
	if(obj == NULL)
		return -1;

	while(true)
	{
		DWORD ret = MsgWaitForMultipleObjects(3, _eventNotify, FALSE, INFINITE, QS_ALLEVENTS);
		if(ret == WAIT_FAILED)
			return -1;
		
		DWORD notify = ret - WAIT_OBJECT_0;
		if(notify == 0)
		{
			if(obj->LoadDSData(0, SIZE_DS_BUFFER / 2) != 0)
				break;
		}
		else if(notify == 1)
		{
			if(obj->LoadDSData(SIZE_DS_BUFFER / 2 , SIZE_DS_BUFFER / 2) != 0)
				break;
		}
		else if(notify == 2)
		{
			break;
		}
		else
		{
			continue;
		}
	}

	obj->PlayOver();

	return 0;
}

int CDSoundObject::Play()
{
	_pDSBuffer->Play(0, 0, DSBPLAY_LOOPING);

	return 0;
}

int CDSoundObject::Pause()
{
	_pDSBuffer->Stop();

	return 0;
}

int CDSoundObject::Stop()
{
	if(_pDSBuffer == NULL)
		return -1;
	_pDSBuffer->Stop();

	Release();

	return 0;
}

bool CDSoundObject::IsPlaying() const
{
	if(_pDSBuffer == NULL)
		return false;

	DWORD status = 0;
	HRESULT hr = _pDSBuffer->GetStatus(&status);
	if(hr != DS_OK)
		return false;
	return ((status & DSBSTATUS_PLAYING) == DSBSTATUS_PLAYING ? true : false);
}

int CDSoundObject::PlayOver()
{
	return Stop();
}

////
void CDSoundObject::SetVolume(long vol)
{
	if(vol > DSBVOLUME_MAX)
		vol = DSBVOLUME_MAX;
	if(vol < DSBVOLUME_MIN)
		vol = DSBVOLUME_MIN;
	if(_pDSBuffer != NULL)
	{
		_pDSBuffer->SetVolume(vol);
	}
}

long CDSoundObject::GetVolume() const
{
	long vol = 0;
	_pDSBuffer->GetVolume(&vol);
	return vol;
}

int CDSoundObject::GetFormatTag() const
{
	return _fmtWave.wFormatTag;
}

int CDSoundObject::GetChannels() const
{
	return _fmtWave.nChannels;
}
int CDSoundObject::GetSamples() const
{
	return _fmtWave.nSamplesPerSec;
}

int CDSoundObject::GetBPS() const
{
	return _fmtWave.wBitsPerSample;
}

int CDSoundObject::GetBlockAlign() const
{
	return _fmtWave.nBlockAlign;
}

int CDSoundObject::GetABPS() const
{
	return _fmtWave.nAvgBytesPerSec;
}


