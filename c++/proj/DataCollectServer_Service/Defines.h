#ifndef __DEFINES_H__
#define __DEFINES_H__


#define APP_TITLE   "DataCollectServer"
#define APP_DESC    "HuanTai Data Collect Service"
#define APP_VERSION "1.8.22"
#define SYS_PROMPT	"DCServer>"

const int TASK_APP     			=   1;
const int TASK_TIMER    		=   2;
const int TASK_COLLECT_SERVER   =   3;
const int TASK_COMMAND_SERVER   =   4;
const int TASK_CONTROLLER_SERVER=	5;
const int OBJ_DATALOADER        =   6;


const int FPARAM_SHUTDOWN			=   0;
const int FPARAM_PACKET				=   1;
const int FPARAM_DATALOADER			=   2;
const int FPARAM_SOCKET_CONNECT		=	3;
const int FPARAM_SOCKET_DISCONNECT	=	4;
const int FPARAM_DATALOADER_RTTIMER   =   5;
const int FPARAM_DATALOADER_PDTIMER   =   6;
//const int FPARAM_DATA_VALVECONTROL  =   7;
//const int FPARAM_DATA_FEEADD        =   8;
//const int FPARAM_DATA_REAL          =   9;

const int FPARAM_STATEDATA          =   10;

const int CLIENTTYPE_TERMINAL		=	1;
const int CLIENTTYPE_CONTROLLER		=	2;

#endif
