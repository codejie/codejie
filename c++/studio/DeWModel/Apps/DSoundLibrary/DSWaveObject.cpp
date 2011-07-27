#include "stdafx.h"

#include "DSWaveObject.h"

CDSWaveObject::CDSWaveObject()
: CDSoundObject(CDSoundObject::ST_WAVE)
, _uiReadSize(0)
{
}

CDSWaveObject::~CDSWaveObject()
{
	Release();
}

void CDSWaveObject::Release()
{
	ReleaseDSoundBuffer();

	if(_ifStream.is_open())
		_ifStream.close();
}

int CDSWaveObject::LoadFile(const std::wstring &file)
{
//	Release();

	_ifStream.open(file.c_str(), std::ios::in | std::ios::binary);
	if(!_ifStream.is_open())
		return -1;

	memset(&_headerWave, 0, sizeof(WAVE_HEADER));

	if(ReadWaveHeader(_headerWave) != 0)
		return -1;

	SetWaveFormat();

	if(CreateDSoundBuffer() != 0)
		return -1;

	return 0;
}

int CDSWaveObject::ReadWaveHeader(CDSWaveObject::WAVE_HEADER &header)
{
	_ifStream.seekg(0, std::ios::beg);
	_ifStream.read((char*)&header, sizeof(WAVE_HEADER));
	if(!_ifStream.good())
		return -1;
	if(memcmp(header.riff_sig, "RIFF", 4) || memcmp(header.wave_sig, "WAVE", 4) ||
       memcmp(header.format_sig, "fmt ", 4) || memcmp(header.data_sig, "data", 4))
    {
        return -1;
    }
	return 0;
}

int CDSWaveObject::SetWaveFormat()
{
//	WAVEFORMATEX wformat;
	memset(&_fmtWave, 0, sizeof(WAVEFORMATEX));
	_fmtWave.wFormatTag = WAVE_FORMAT_PCM;
	_fmtWave.nChannels = _headerWave.channels;
	_fmtWave.nSamplesPerSec = _headerWave.sample_rate;
	_fmtWave.wBitsPerSample = _headerWave.bits_per_sample;
	_fmtWave.nBlockAlign = _headerWave.bits_per_sample / 8 * _headerWave.channels;// header.block_align;
	_fmtWave.nAvgBytesPerSec = _headerWave.sample_rate * _headerWave.block_align;//header.
	//wformat.cbSize = header.data_size;
	return 0;
}

int CDSWaveObject::InitDSData()
{
	_uiReadSize = sizeof(WAVE_HEADER);
	if(LoadDSData(0, CDSoundObject::SIZE_DS_BUFFER) != 0)
		return -1;
	return 0;
}

int CDSWaveObject::LoadDSData(unsigned int start, unsigned int count)
{
	if(_uiReadSize >= _headerWave.data_size)
		return -1;

	_ifStream.seekg(_uiReadSize, std::ios::beg);
	if(!_ifStream.good())
		return -1;

	LPVOID aptr1 = NULL, aptr2 = NULL;
	DWORD abyte1 = NULL, abyte2 = NULL;

	if(count > (_headerWave.data_size - _uiReadSize))
		count = _headerWave.data_size - _uiReadSize;

	HRESULT hr = _pDSBuffer->Lock(start, count, &aptr1, &abyte1, &aptr2, &abyte2, 0);
	if(hr != DS_OK)
		return -1;
	
	_ifStream.read((char*)aptr1, abyte1);
	_uiReadSize += count;

	if(aptr2 != NULL)
	{
		_ifStream.read((char*)aptr2, abyte2);
		_uiReadSize += count;
	}

	_pDSBuffer->Unlock(aptr1, abyte1, aptr2, abyte2);

	return 0;
}

double CDSWaveObject::Duration()
{
	return (_headerWave.data_size / _fmtWave.nAvgBytesPerSec);
}

int CDSWaveObject::SetPlayPos(unsigned int offset)
{
	_pDSBuffer->Stop();
	_uiReadSize = offset / 1000 * _fmtWave.nSamplesPerSec * _fmtWave.wBitsPerSample / 8 + sizeof(WAVE_HEADER);
	if(_uiReadSize <= _headerWave.data_size)
	{
		if(LoadDSData(0, CDSoundObject::SIZE_DS_BUFFER) == 0)
		{
			_pDSBuffer->SetCurrentPosition(0);
			_pDSBuffer->Play(0, 0, DSBPLAY_LOOPING);
			return 0;
		}
	}
	return -1;
}

unsigned int CDSWaveObject::GetPlayPos() const
{
	return _uiReadSize;
}
