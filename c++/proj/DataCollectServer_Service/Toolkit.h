/*
 * Toolkit.h
 *
 *  Created on: Jun 15, 2011
 *      Author: codejie
 */

#ifndef __TOOLKIT_H__
#define __TOOLKIT_H__

#include <string>

#include "ace/OS_NS_time.h"
#include "ace/OS_NS_sys_time.h"
#include "ace/OS_NS_stdio.h"
//#include "ace/Time_Value.h"


namespace Toolkit
{

extern const unsigned short CRC16_TABLE[256];

extern const unsigned short CRC_BASE;

extern unsigned short CRC16(const unsigned char* data, size_t size);
extern const std::string GetTimeOfDay();

}


#endif /* __TOOLKIT_H__ */
