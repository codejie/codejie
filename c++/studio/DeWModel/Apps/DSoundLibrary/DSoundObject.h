#ifndef __DSOUNDOBJECT_H__
#define __DSOUNDOBJECT_H__

#ifdef DSOUNDLIBRARY_EXPORTS
#define DSOUNDLIBRARY_API __declspec(dllexport)
#else
#define DSOUNDLIBRARY_API __declspec(dllimport)
#endif


#include <windows.h>
#include <Mmreg.h.>
#include <dsound.h>

#include <string>

extern HANDLE _eventNotify[3];

class DSOUNDLIBRARY_API CDSoundObject
{
public:
	enum SoundType { ST_WAVE, ST_MP3 };
	static const DWORD SIZE_DS_BUFFER	=	32 * 1024;
public:
	CDSoundObject(SoundType type);
	virtual ~CDSoundObject();

	virtual int Init(HWND hwnd);
	virtual int LoadFile(const std::wstring& file) = 0;

	virtual bool IsReady() const;
	virtual int Play();
	virtual int Pause();
	virtual int Stop();
	virtual bool IsPlaying() const;
	virtual double Duration() = 0;

	virtual void SetVolume(long vol);
	virtual long GetVolume() const;
	virtual int SetPlayPos(unsigned int offset) = 0;
	virtual unsigned int GetPlayPos() const = 0;
	virtual int GetFormatTag() const;
	virtual int GetChannels() const;
	virtual int GetSamples() const;
	virtual int GetBPS() const;
	virtual int GetBlockAlign() const;
	virtual int GetABPS() const;
protected:
	virtual void Release();

	virtual int CreateDSound();
	virtual void ReleaseDSound();

	virtual int SetWaveFormat() = 0;

	virtual int CreateDSoundBuffer();
	virtual void ReleaseDSoundBuffer();

	virtual int InitDSData() = 0;
	virtual int LoadDSData(unsigned int start, unsigned int count) = 0;
	virtual int PlayOver();
protected:
	HWND _hWnd;
	SoundType _eType;
	IDirectSound8 * _pDS;
	IDirectSoundBuffer8 * _pDSBuffer;
	IDirectSoundNotify8* _pDSNotify;
protected:
	int CreateNotifyThread();
	void ReleaseNotifyThread();
	static DWORD NotifyHandleProc(LPVOID param);
protected:
	DWORD _uiNotifyThreadID;
	HANDLE _hNotifyThread;

	DSBPOSITIONNOTIFY _notifyPos[2];
protected:
	WAVEFORMATEX	_fmtWave;
};

#endif