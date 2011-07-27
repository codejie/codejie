
#include <iostream>
#include <fstream>

#include "mpg123.h"

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

void InitWaveHeader(WAVE_HEADER& header, int channels, int samples, int bits, int size)
{
	memset(&header, 0, sizeof(header));
	memcpy(&header.riff_sig, "RIFF", 4);
	header.waveform_chunk_size = 8;
	memcpy(&header.wave_sig, "WAVE", 4);
	memcpy(&header.format_sig, "fmt ", 4);
	header.format_chunk_size = 16;
	header.format_tag = 1;
	header.channels = channels;
	header.sample_rate = samples;
	header.bytes_per_sec = samples * channels * (bits / 8);
	header.block_align = 2 * channels;
	header.bits_per_sample = bits;
	memcpy(&header.data_sig, "data", 4);
	header.data_size = size;	
}

std::ofstream ofs;

int OpenWaveFile()
{
	ofs.open(".\\test.wav", std::ios::out | std::ios::binary);
	return ofs.is_open() ? 0 : -1;
}

int UpdateWaveHeader(const WAVE_HEADER& header, bool create)
{
	if(!create)
		ofs.seekp(0, std::ios::beg);
	ofs.write((char*)&header, sizeof(header));
	return ofs.good() ? 0 : -1;
}

int AppendWaveData(const char* data, size_t size)
{
	ofs.write(data, size);
	return ofs.good() ? 0 : -1;
}

void CloseWaveFile()
{
	ofs.close();
}

int main()
{
	int ret = -1;

	OpenWaveFile();

	while(true)
	{
		ret = mpg123_init();
		if(ret != MPG123_OK)
			break;

		char ** ch = mpg123_decoders();		

		mpg123_pars* mp = mpg123_new_pars(&ret);
		if(mp == NULL || ret != MPG123_OK)
			break;
		mpg123_delete_pars(mp);
		mpg123_handle *h = mpg123_new("auto", &ret);
		if(h == NULL)
			break;
		ret = mpg123_open(h, ".\\orgrimmar_intro-moment.mp3");
		if(ret != MPG123_OK)
			break;

		off_t framenum = mpg123_seek_frame(h, 0, SEEK_SET);

		WAVE_HEADER header;
		unsigned char *audio = NULL;
		size_t bytes = 0;
		int mc = mpg123_decode_frame(h, &framenum, &audio, &bytes);
		if(mc == MPG123_NEW_FORMAT)
		{
			long rate = 0;
			int channel = 0, encoding = 0;
			ret = mpg123_getformat(h, &rate, &channel, &encoding);
			if(ret != MPG123_OK)
				break;
			mpg123_frameinfo fi;
			ret = mpg123_info(h, &fi);
			if(ret != MPG123_OK)
				break;

			if((encoding & MPG123_ENC_16) == MPG123_ENC_16)
				encoding = 16;
			else if((encoding & MPG123_ENC_32) == MPG123_ENC_32)
				encoding = 32;
			else
				encoding = 8;

			InitWaveHeader(header, channel, rate, encoding, 0);
		}
		header.data_size = 0;

		UpdateWaveHeader(header, true);

		++ framenum;
		mc = mpg123_decode_frame(h, &framenum, &audio, &bytes);
		while(mc == MPG123_OK && bytes > 0)
		{
			header.data_size += bytes;

			AppendWaveData((const char*)audio, bytes);

			mc = mpg123_decode_frame(h, &framenum, &audio, &bytes);
			++ framenum;
		}

		mpg123_delete(h);
		mpg123_exit();

		header.waveform_chunk_size = header.data_size + 36;

		UpdateWaveHeader(header, false);

		break;
	}

	if(ret != MPG123_OK)
	{
			std::cout << "error : " << mpg123_plain_strerror(ret) << std::endl;
			return -1;
	}

	CloseWaveFile();

	return 0;
}
