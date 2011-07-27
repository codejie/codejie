#ifndef __DSMP3OBJECT_H__
#define __DSMP3OBJECT_H__

#include <string>

//#include "mpg123.h"

#include "DSoundObject.h"
struct mpg123_handle_struct;

class CDSMP3Object : public CDSoundObject
{
public:
	CDSMP3Object();
	virtual ~CDSMP3Object();
	
	virtual int LoadFile(const std::wstring& file);
	virtual double Duration();
	virtual int SetPlayPos(unsigned int pos);
	virtual unsigned int GetPlayPos() const;
protected:
	virtual int SetWaveFormat();

	virtual void Release();

	virtual int InitDSData();
	virtual int LoadDSData(unsigned int start, unsigned int count);
private:
	struct mpg123_handle_struct* _handle;
	unsigned char _buffer[SIZE_DS_BUFFER / 2];
};

#endif
