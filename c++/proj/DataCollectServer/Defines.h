#ifndef __DEFINES_H__
#define __DEFINES_H__


#define APP_TITLE   "DataCollectServer"
#define APP_VERSION "1.8.03"
#define SYS_PROMPT	"DCServer>"

const int TASK_APP     			=   1;
const int TASK_TIMER    		=   2;
const int TASK_COLLECT_SERVER   =   3;
const int TASK_COMMAND_SERVER   =   4;
const int TASK_CONTROLLER_SERVER=	5;


const int FPARAM_SHUTDOWN			=   0;
const int FPARAM_PACKET				=   1;
const int FPARAM_DATALOADER			=   2;
const int FPARAM_SOCKET_CONNECT		=	3;
const int FPARAM_SOCKET_DISCONNECT	=	4;

#endif
