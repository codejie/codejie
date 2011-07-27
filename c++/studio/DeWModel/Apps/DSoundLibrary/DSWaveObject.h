#ifndef __DSWAVEOBJECT_H__
#define __DSWAVEOBJECT_H__

#include <fstream>
#include <string>

#include "DSoundObject.h"

class DSOUNDLIBRARY_API CDSWaveObject : public CDSoundObject
{
protected:
// .WAV file header
	struct WAVE_HEADER
	{
		char    riff_sig[4];            // 'RIFF'
		long    waveform_chunk_size;    // 8
		char    wave_sig[4];            // 'WAVE'
		char    format_sig[4];          // 'fmt ' (notice space after)
		long    format_chunk_size;      // 16;
		short   format_tag;             // WAVE_FORMAT_PCM
		short   channels;               // # of channels
		long    sample_rate;            // sampling rate
		long    bytes_per_sec;          // bytes per second
		short   block_align;            // sample block alignment
		short   bits_per_sample;        // bits per second
		char    data_sig[4];            // 'data'
		long    data_size;              // size of waveform data
	};

public:
	CDSWaveObject();
	virtual ~CDSWaveObject();

	virtual int LoadFile(const std::wstring& file);
	virtual double Duration();
	virtual int SetPlayPos(unsigned int offset);
	virtual unsigned int GetPlayPos() const;
protected:
	virtual int SetWaveFormat();

	virtual void Release();
	virtual int InitDSData();
	virtual int LoadDSData(unsigned int start, unsigned int count);
protected:
	int ReadWaveHeader(WAVE_HEADER& header);
private:
	WAVE_HEADER _headerWave;
//	WAVEFORMATEX _fmtWave;
	std::ifstream _ifStream;
	DWORD _uiReadSize;
};


#endif