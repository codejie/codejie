#ifndef __CONSTS_H__
#define __CONSTS_H__

#include <string>

const int ID_BASE	=	100;

const int IDM_FILE_EXIT		=	ID_BASE + 1;
const int IDM_HELP_ABOUT	=	ID_BASE + 2;
const int IDM_PARSER_DBC	=	ID_BASE + 3;
const int IDM_HELP_TEST		=	ID_BASE + 4;
const int IDM_VIEW_TREESTYLE_CATEGORY	=	ID_BASE + 5;
const int IDM_VIEW_TREESTYLE_DIRECTORY	=	ID_BASE + 6;
const int IDM_SETTING_LOADDBCCONFIG		= ID_BASE + 7;
const int IDM_PARSER_BLP	=	ID_BASE + 8;
const int IDM_PARSER_WAVE	=	ID_BASE + 9;
const int IDM_SETTING_VIEWDBCCONFIG		= ID_BASE + 10;
const int IDM_PARSER_WDB	=	ID_BASE + 11;
const int IDM_PARSER_DUMP	=	ID_BASE + 12;

const int IDT_BUTTON_OPEN	=	ID_BASE + 101;
const int IDT_BUTTON_RELOAD	=	ID_BASE + 102;

const int IDB_DATA_EXPORT	=	ID_BASE + 201;

const int IDC_TREE			=	ID_BASE + 1001;
const int IDC_NOTEBOOK		=	ID_BASE + 1002;
const int IDC_COMBO			=	ID_BASE + 1003;
const int IDC_NB_BLP_BITMAPCANVAS	=	ID_BASE + 1004;
const int IDC_NB_WAVE_BUTTON_PLAY	=	ID_BASE + 1005;
const int IDC_NB_WAVE_BUTTON_PAUSE	=	ID_BASE + 1006;
const int IDC_NB_WAVE_BUTTON_STOP	=	ID_BASE + 1007;
const int IDC_NB_WAVE_SLIDER_PLAY	=	ID_BASE + 1008;
const int IDC_NB_WAVE_SLIDER_VOLUME	=	ID_BASE + 1009;
const int IDT_NB_WAVE_PLAYTIMER		=	ID_BASE + 1010;
const int IDB_NB_M2_TOX				=	ID_BASE + 1011;
const int IDM_NB_BLP_BITMAPCANVAS_SAVE	=	ID_BASE + 1012;
const int IDB_NB_DBC_GRID_SAVE		=	ID_BASE + 1013;
const int IDC_DBCCONFIG_CHECKLIST	=	ID_BASE + 1015;
const int IDB_NB_WDB_GRID_SAVE		=	ID_BASE + 1014;


const std::string CSTR_MPQFILE_CACHE		=	".\\cache\\";
const std::string CSTR_DBC_CONFIGUREFILE	=	".\\DBCFields.xml";
const std::string CSTR_WDB_CONFIGUREFILE	=	".\\WDBFields.xml";
const std::string CSTR_MPQFILE_PATH			=	"e:\\data";

#endif

