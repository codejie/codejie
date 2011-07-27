#include "stdafx.h"



#include "D3DObject.h"

CD3DObject::CD3DObject()
: _hWnd(NULL)
, _pD3D(NULL), _pDevice(NULL)
{

}

CD3DObject::~CD3DObject()
{
}

int CD3DObject::Init(HWND hwnd)
{
	_hWnd = hwnd;

	_pD3D = Direct3DCreate9(D3D_SDK_VERSION);
	if(_pD3D == NULL)
		return -1;

	D3DPRESENT_PARAMETERS d3dpp;
	ZeroMemory(&d3dpp, sizeof(d3dpp));
	d3dpp.Windowed = TRUE;
	d3dpp.SwapEffect = D3DSWAPEFFECT_DISCARD;
	d3dpp.BackBufferFormat = D3DFMT_UNKNOWN;
	d3dpp.EnableAutoDepthStencil = TRUE;
	d3dpp.AutoDepthStencilFormat = D3DFMT_D16;

	if(FAILED(_pD3D->CreateDevice(D3DADAPTER_DEFAULT, D3DDEVTYPE_HAL, _hWnd, D3DCREATE_SOFTWARE_VERTEXPROCESSING, &d3dpp, &_pDevice)))
		return -1;
	_pDevice->SetRenderState(D3DRS_FILLMODE, D3DFILL_WIREFRAME);
	_pDevice->SetRenderState(D3DRS_ZENABLE, TRUE);
	_pDevice->SetRenderState(D3DRS_AMBIENT, 0xFFFFFFFF);

	return 0;
}

int CD3DObject::AddModel(const CModelObject *model)
{
	if(model == NULL)
		return -1;
	_vctModel.push_back(model);

	return 0;
}

int CD3DObject::SetupWorldMatrix(const D3DXMATRIXA16 &matrix)
{
	_pDevice->SetTransform(D3DTS_WORLD, &matrix);
	return 0;
}

int CD3DObject::SetupViewMatrix(const D3DXMATRIXA16 &matrix)
{
	_pDevice->SetTransform(D3DTS_VIEW, &matrix);
	return 0;
}

int CD3DObject::Render()
{
	_pDevice->Clear(0, NULL, D3DCLEAR_TARGET | D3DCLEAR_ZBUFFER, D3DCOLOR_XRGB( 0, 0, 255 ), 1.0f, 0);

	if(FAILED(_pDevice->BeginScene()))
		return -1;
	for(TModelObjectVector::const_iterator it = _vctModel.begin(); it != _vctModel.end(); ++ it)
		it->Render();
	_pDevice->EndScene();

	_pDevice->Present(NULL, NULL, NULL, NULL);
	return 0;
}

void CD3DObject::ReleaseModels()
{
	TModelObjectVector::iterator it = _vctModel.begin();
	while(it != _vctModel().end())
	{
	}
}




