#pragma once

#include <d3dx9.h>
#include <mmsystem.h>

#include <vector>

class CModelObject;

class CD3DObject
{
protected:
	typedef std::vector<const CModelObject*> TModelObjectVector;
public:
	CD3DObject();
	virtual ~CD3DObject();

	int Init(HWND hwnd);

	int SetupWorldMatrix(const D3DXMATRIXA16& matrix);
	int SetupViewMatrix(const D3DXMATRIXA16& matrix);
	int AddModel(const CModelObject* model);

	int Render();
protected:
	void ReleaseModels();
private:
	HWND _hWnd;
	LPDIRECT3D9 _pD3D;
	LPDIRECT3DDEVICE9 _pDevice;
private:
	TModelObjectVector _vctModel;
};