#include "mpg123.h"

#include "DSMP3Object.h"

CDSMP3Object::CDSMP3Object()
: CDSoundObject(CDSoundObject::ST_MP3)
, _handle(NULL)
{
}

CDSMP3Object::~CDSMP3Object()
{
	Release();
}

void CDSMP3Object::Release()
{
	ReleaseDSoundBuffer();

	if(_handle != NULL)
	{
		mpg123_close(_handle);
		mpg123_delete(_handle);
		mpg123_exit();
		_handle = NULL;
	}
}

int CDSMP3Object::LoadFile(const std::wstring &file)
{
//	Release();

	if(mpg123_init() != MPG123_OK)
		return -1;
	int ret = -1;
	_handle = mpg123_new(NULL, &ret);
	if(_handle == NULL || ret != MPG123_OK)
		return -1;

	if(mpg123_topen(_handle, file.c_str()) != MPG123_OK)
		return -1;

	SetWaveFormat();

	if(CreateDSoundBuffer() != 0)
		return -1;

	return 0;
}

int CDSMP3Object::SetWaveFormat()
{
	long rate = 0;
	int channel = 0;
	int encoding = 0;

	if(mpg123_getformat(_handle, &rate, &channel, &encoding) != MPG123_OK)
		return -1;
	if((encoding & MPG123_ENC_16) == MPG123_ENC_16)
		encoding = 16;
	else if((encoding & MPG123_ENC_32) == MPG123_ENC_32)
		encoding = 32;
	else
		encoding = 8;

	memset(&_fmtWave, 0, sizeof(WAVEFORMATEX));
	_fmtWave.wFormatTag = WAVE_FORMAT_PCM;
	_fmtWave.nChannels = channel;
	_fmtWave.nSamplesPerSec = rate;
	_fmtWave.wBitsPerSample = encoding;
	_fmtWave.nBlockAlign = encoding / 8 * channel;
	_fmtWave.nAvgBytesPerSec = rate * (encoding / 8) * channel;

	return 0;
}

int CDSMP3Object::InitDSData()
{
	if(LoadDSData(0, CDSoundObject::SIZE_DS_BUFFER) != 0)
		return -1;

	return 0;
}

int CDSMP3Object::LoadDSData(unsigned int start, unsigned int count)
{
	LPVOID aptr1 = NULL, aptr2 = NULL;
	DWORD abyte1 = NULL, abyte2 = NULL;

	HRESULT hr = _pDSBuffer->Lock(start, count, &aptr1, &abyte1, &aptr2, &abyte2, 0);
	if(hr != DS_OK)
		return -1;

	size_t outsize = 0;
	int ret = mpg123_read(_handle, _buffer, SIZE_DS_BUFFER / 2, &outsize);
	if(ret != MPG123_OK)
		return -1;

	memcpy(aptr1, _buffer, outsize);
	if(aptr2 != 0)
	{
		if(mpg123_read(_handle, _buffer, SIZE_DS_BUFFER / 2, &outsize) != MPG123_OK)
			return -1;
		memcpy(aptr2, _buffer, outsize);
	}

	_pDSBuffer->Unlock(aptr1, abyte1, aptr2, abyte2);

	return 0;
}

double CDSMP3Object::Duration()
{
	off_t len = mpg123_length(_handle);
	return (len / _fmtWave.nSamplesPerSec);
}

int CDSMP3Object::SetPlayPos(unsigned int pos)
{
	if(mpg123_seek(_handle, ((pos * 8) / _fmtWave.wBitsPerSample), SEEK_SET) < 0)
		return -1;

	if(LoadDSData(0, CDSoundObject::SIZE_DS_BUFFER) != 0)
		return -1;
	_pDSBuffer->SetCurrentPosition(0);
	return 0;
}

unsigned int CDSMP3Object::GetPlayPos() const
{
	return mpg123_tell(_handle) * _fmtWave.wBitsPerSample / 8;
}


