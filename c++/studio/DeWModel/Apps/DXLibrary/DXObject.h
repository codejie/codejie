#pragma once

#include <windows.h>

#ifdef DXLIBRARY_EXPORTS
#define DXLIBRARY_API __declspec(dllexport)
#else
#define DXLIBRARY_API __declspec(dllimport)
#endif

class DXLIBRARY_API CDXObject
{
protected:
	static const int MAX_EVENT	=	2;
public:
	CDXObject();
	virtual ~CDXObject();

	int Init(HWND hwnd);

	int Run();
	int Pause();
	int Stop();
private:
	int InitD3DObject(HWND hwnd);
	int InitThread();
	DWORD ThreadProc(LPVOID param);
	void ReleaseD3DObject();
	void ReleaseThread();
private:
	HANDLE _haEvent[MAX_EVENT];
	HANDLE _hThread;
	DWORD _dwThreadID;
	CD3DObject _objD3D;
};