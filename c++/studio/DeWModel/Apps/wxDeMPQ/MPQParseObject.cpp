
#include <sstream>
#include <set>

#include "wx/treectrl.h"
#include "wx/textctrl.h"

#include "BLP2PNGObject.h"

#include "Toolkit.h"
#include "NotebookPanels.h"
#include "MPQDBCFieldObject.h"

#include "MPQParseObject.h"

CMPQTreeObject::CMPQTreeObject()
: _ctrlTree(NULL)
, _eTreeStyle(TS_DIRECTORY)
{
}

CMPQTreeObject::~CMPQTreeObject()
{
}

void CMPQTreeObject::AttachTreeCtrl(wxTreeCtrl* tree)
{
	_ctrlTree = tree;
}

bool CMPQTreeObject::SetTreeStyle(CMPQTreeObject::TreeStyle type)
{
	if(_eTreeStyle != type)
	{
		_eTreeStyle = type;
		return true;
	}
	return false;
}

void CMPQTreeObject::Refresh(const wxString& root)
{
	TreeClear();
	DataClear();
	TreeDataInit(root);
}

void CMPQTreeObject::Append(const MPQData::TFileData &data, bool show)
{
	wxString caption = Toolkit::String2wxString(data.m_strName);
	SuffixType type = ST_UNKNOWN;
	
	wxTreeItemId id;
	if(_eTreeStyle == TS_CATEGORY)
	{
		wxTreeItemId parent = GetSuffixItemID(caption, type);
		id = TreeAppend(parent, type, caption, show);
	}
	else
	{
		wxString file;
		wxTreeItemId parent = GetPathItemID(caption, file);
		id = TreeAppend(parent, type, file, show);
	}
	DataAppend(id, data);
}

void CMPQTreeObject::AppendOver(unsigned int count)
{
	wxTreeItemId root = _ctrlTree->GetRootItem();
	wxString str;
	str.sprintf(wxT("%s(%d)"), _ctrlTree->GetItemText(root), _mapItemIDData.size());
	_ctrlTree->SetItemText(root, str);
	_ctrlTree->Expand(root);
}

void CMPQTreeObject::DataClear()
{
	if(_eTreeStyle == TS_CATEGORY)
	{
		_mapFileItemID.clear();
		_mapSuffixItemID.clear();
	}
	else
	{
	}
	_mapItemIDData.clear();
}

void CMPQTreeObject::DataAppend(const wxTreeItemId &id, const MPQData::TFileData &data)
{
	_mapItemIDData.insert(std::make_pair(unsigned int(id.m_pItem), data));
}

void CMPQTreeObject::TreeClear()
{
	_ctrlTree->DeleteAllItems();
}

void CMPQTreeObject::TreeDataInit(const wxString &root)
{
	int image = -1;
	wxTreeItemId rt = _ctrlTree->AddRoot(root, image);
	
	if(_eTreeStyle == TS_CATEGORY)
	{
		//Add File Type
		ItemData_t data;
		data.m_caption = wxT("World");
		data.m_count = 0;
		data.m_image = -1;	
		data.m_id = _ctrlTree->AppendItem(rt, GetItemCaption(data), image);
		_mapFileItemID.insert(std::make_pair(FT_WORLD, data));

		data.m_caption = wxT("Graphics/Models");
		data.m_count = 0;
		data.m_image = -1;
		data.m_id = _ctrlTree->AppendItem(rt, GetItemCaption(data), image);
		_mapFileItemID.insert(std::make_pair(FT_GRAPHIC, data));

		data.m_caption = wxT("Database/Cache");
		data.m_count = 0;
		data.m_image = -1;
		data.m_id = _ctrlTree->AppendItem(rt, GetItemCaption(data), image);
		_mapFileItemID.insert(std::make_pair(FT_DATABASE, data));

		data.m_caption = wxT("Addons/Scripting");
		data.m_count = 0;
		data.m_image = -1;
		data.m_id = _ctrlTree->AppendItem(rt, GetItemCaption(data), image);
		_mapFileItemID.insert(std::make_pair(FT_ADDONS, data));

		data.m_caption = wxT("Music");
		data.m_count = 0;
		data.m_image = -1;
		data.m_id = _ctrlTree->AppendItem(rt, GetItemCaption(data), image);
		_mapFileItemID.insert(std::make_pair(FT_MUSIC, data));

		data.m_caption = wxT("Undefine");
		data.m_count = 0;
		data.m_image = -1;
		data.m_id = _ctrlTree->AppendItem(rt, GetItemCaption(data), image);
		_mapFileItemID.insert(std::make_pair(FT_UNDEF, data));

		data.m_caption = wxT("Unknown");
		data.m_count = 0;
		data.m_image = -1;
		data.m_id = _ctrlTree->AppendItem(rt, GetItemCaption(data), image);
		_mapFileItemID.insert(std::make_pair(FT_UNKNOWN, data));

		_ctrlTree->Expand(rt);
//		_ctrlTree->Update();
	}
	else
	{
	}
}

wxTreeItemId CMPQTreeObject::TreeAppend(const wxTreeItemId& parent, SuffixType type, const wxString &caption, bool show)
{
	int image = -1;//type
	wxTreeItemId id = _ctrlTree->AppendItem(parent, caption, image);
	if(show)
	{
		_ctrlTree->Expand(parent);
		_ctrlTree->Update();
	}
	return id;
}

wxTreeItemId CMPQTreeObject::GetTypeItemID(CMPQTreeObject::SuffixType suffix)
{
	FileType type = FT_UNKNOWN;
	switch(suffix)
	{
	case ST_WDT:
	case ST_WDL:
	case ST_ADT:
		type = FT_WORLD;
		break;	
	case ST_BLP:
	case ST_M2:
	case ST_WMO:
	case ST_BLS:
	case ST_TRS:
	case ST_LIT:
	case ST_JPEG:
	case ST_GIF:
	case ST_ANIM:
	case ST_SKIN:
		type = FT_GRAPHIC;
		break;
	case ST_DB:
	case ST_DBC:
	case ST_WDB:
		type = FT_DATABASE;
		break;
	case ST_XSD:
	case ST_XML:
	case ST_TOC:
	case ST_WTF:
	case ST_LUA:
	case ST_HTML:
	case ST_CSS:
	case ST_PDF:
	case ST_JS:
	case ST_URL:
		type = FT_ADDONS;
		break;
	case ST_WAV:
	case ST_MP3:
		type = FT_MUSIC;
		break;
	case ST_DLL:
	case ST_EXE:
	case ST_ZMP:
	case ST_WLM:
	case ST_WLQ:
	case ST_WLW:
		type = FT_UNDEF;
		break;
	default:
		type = FT_UNKNOWN;
	}

	TFileItemIDMap::iterator it = _mapFileItemID.find(type);
	++ it->second.m_count;
	if(_ctrlTree != NULL)
		_ctrlTree->SetItemText(it->second.m_id, GetItemCaption(it->second));
	return it->second.m_id;
}

wxTreeItemId CMPQTreeObject::GetSuffixItemID(const wxString &caption, CMPQTreeObject::SuffixType &type)
{
	size_t pos = caption.find_last_of(wxT("."));
	wxString suffix = caption.substr(pos + 1);
	if(pos == 0)
	{
		suffix = wxT("");
	}
	type = GetSuffixType(suffix);
	TSuffixItemIDMap::iterator it = _mapSuffixItemID.find(type);
	if(it == _mapSuffixItemID.end())
	{
		wxTreeItemId parent = GetTypeItemID(type);

		if(type != ST_UNKNOWN)
		{

			ItemData_t data;
			data.m_caption = suffix;
			data.m_count = 0;
			data.m_image = GetSuffixTypeImage(type);
			data.m_id = _ctrlTree->AppendItem(parent, GetItemCaption(data), data.m_image); 
			it = _mapSuffixItemID.insert(std::make_pair(type, data)).first;
		}
		else
		{
			return parent;
		}
	}

	++ it->second.m_count;

	if(_ctrlTree != NULL)
	{
		_ctrlTree->SetItemText(it->second.m_id, GetItemCaption(it->second));
		_ctrlTree->Update();
	}

	return it->second.m_id;
}

CMPQTreeObject::SuffixType CMPQTreeObject::GetSuffixType(const wxString &suffix) const
{
	SuffixType type = ST_UNKNOWN;

	wxString sf = suffix.Lower();

	if(sf == wxT("xsd"))
		return ST_XSD;
	else if(sf == wxT("xml"))
		return ST_XML;
	else if(sf == wxT("toc"))
		return ST_TOC;
	else if(sf == wxT("wtf"))
		return ST_WTF;
	else if(sf == wxT("lua"))
		return ST_LUA;
	else if(sf == wxT("dbc"))
		return ST_DBC;
	else if(sf == wxT("wdt"))
		return ST_WDT;
	else if(sf == wxT("wdl"))
		return ST_WDL;
	else if(sf == wxT("adt"))
		return ST_ADT;
	else if(sf == wxT("blp"))
		return ST_BLP;
	else if(sf == wxT("m2"))
		return ST_M2;
	else if(sf == wxT("lit"))
		return ST_LIT;
	else if(sf == wxT("bls"))
		return ST_BLS;
	else if(sf == wxT("trs"))
		return ST_TRS;
	else if(sf == wxT("zmp"))
		return ST_ZMP;
	else if(sf == wxT("wlm"))
		return ST_WLM;
	else if(sf == wxT("wlq"))
		return ST_WLQ;
	else if(sf == wxT("wlw"))
		return ST_WLW;
	else if(sf == wxT("skin"))
		return ST_SKIN;
	else if(sf == wxT("jpg"))
		return ST_JPEG;
	else if(sf == wxT("gif"))
		return ST_GIF;
	else if(sf == wxT("anim"))
		return ST_ANIM;
	else if(sf == wxT("html"))
		return ST_HTML;
	else if(sf == wxT("dll"))
		return ST_DLL;
	else if(sf == wxT("exe"))
		return ST_EXE;
	else if(sf == wxT("wmo"))
		return ST_WMO;
	else if(sf == wxT("css"))
		return ST_CSS;
	else if(sf == wxT("pdf"))
		return ST_PDF;
	else if(sf == wxT("js"))
		return ST_JS;
	else if(sf == wxT("url"))
		return ST_URL;
	else if(sf == wxT("db"))
		return ST_DB;
	else if(sf == wxT("wav"))
		return ST_WAV;
	else if(sf == wxT("mp3"))
		return ST_MP3;
	else
		return ST_UNKNOWN;
}

int CMPQTreeObject::GetSuffixTypeImage(CMPQTreeObject::SuffixType type) const
{
	return -1;
}

const wxString CMPQTreeObject::GetItemCaption(const CMPQTreeObject::ItemData_t &data) const
{
	wxString str;
	str.sprintf(wxT("%s(%d)"),data.m_caption, data.m_count);
	return str;
}

const MPQData::TFileData* CMPQTreeObject::GetData(unsigned int id) const
{
	TItemIDDataMap::const_iterator it = _mapItemIDData.find(id);
	if(it == _mapItemIDData.end())
		return NULL;
	return &it->second;
}

wxTreeItemId CMPQTreeObject::GetPathItemID(const wxString &caption, wxString& file)
{
	file = caption;
	wxTreeItemId id = _ctrlTree->GetRootItem(), next;

	int image = -1;
	int pos = file.find(wxT("\\"));
	while(pos != wxNOT_FOUND)
	{
		wxString path = file.substr(0, pos);

		wxTreeItemIdValue cookie;
		next = _ctrlTree->GetFirstChild(id, cookie);
		do 
		{
			if(next.IsOk())
			{
				if(_ctrlTree->GetItemText(next) != path)
				{
					next = _ctrlTree->GetNextChild(id, cookie);
					continue;
				}
			}
			else
			{
				next = _ctrlTree->AppendItem(id, path, image);
			}
			id = next;
			break;
		}while(1);

		file = file.substr(pos + 1);
		pos = file.find(wxT("\\"));
	}
	
	return id;
}

////
CMPQDataObject::CMPQDataObject()
: _objPanel(NULL)
, _uiLineCount(0)
{
}

CMPQDataObject::~CMPQDataObject()
{
}

void CMPQDataObject::AttachDataPanel(CNotebookDataPanel* panel)
{
	_objPanel = panel;
}

void CMPQDataObject::ReadBegin(const wxString &file)
{
	if(_objPanel == NULL)
		return;
	_objPanel->Refresh(file);

	_uiLineCount = 0;
}

void CMPQDataObject::ReadEnd()
{
	if(_objPanel == NULL)
		return;
	_objPanel->AppendEnd();
}

void CMPQDataObject::Read(const char *data, size_t size)
{
	if(data == NULL || size == 0)
		return;

	std::ostringstream ostr;

	std::ios::fmtflags lflag = ostr.flags();
	char cfill = ostr.fill();

	ostr.flags(std::ios::hex | std::ios::uppercase);
	ostr.fill('0');

	const std::string strSep1 = "   ";
	const std::string strSep2 = " ";
	const std::string strSep3 = " ";
	const std::string strSep4 = "   ";

	for(size_t i = 0; i < (size / 16); ++ i)
	{
		ostr.width(6);
		ostr << 16 * _uiLineCount << strSep1;
		for(size_t j = 0; j < 16; ++ j)
		{
			if(j == 8)
				ostr << strSep2;
			ostr.width(2);
			ostr << (unsigned int)(unsigned char(data[i * 16 + j])) << strSep3;

		}
		ostr << strSep1;
		for(size_t j = 0; j < 16; ++ j)
		{
			if(j == 8)
				ostr << strSep2;
			
			if(data[i * 16 + j] >= 0x20)
				ostr << data[i * 16 + j];
			else
				ostr << ".";
		}
		ostr << std::endl;
		_uiLineCount ++;
		//if(_ctrlText != NULL)
		//	_ctrlText->AppendText(Toolkit::String2wxString(ostr.str()));
//		ostr.str("");
	}

	size_t doneline = (size / 16);
	size_t rest = (size - doneline * 16);
	if(rest > 0)
	{
		ostr.width(6);
		ostr << 16 * _uiLineCount << strSep1;
		for(size_t j = 0; j < rest; ++ j)
		{
			if(j == 8)
				ostr << strSep2;
			ostr.width(2);
			ostr << (unsigned int)(unsigned char(data[doneline * 16 + j])) << strSep3;
		}
		for(size_t j = rest; j < 16; ++ j)
		{
			if(j == 8)
				ostr << strSep2;
			ostr << strSep4;
		}

		ostr << strSep1;
		for(size_t j = 0; j < rest; ++ j)
		{
			if(j == 8)
				ostr << strSep2;

			if(data[doneline * 16 + j] >= 0x20)
				ostr << data[doneline * 16 + j];
			else
				ostr << ".";
		}
		_uiLineCount ++;
	}

	if(_objPanel != NULL)
		_objPanel->AppendData(Toolkit::String2wxString(ostr.str()));
}

//////////
CMPQFileObject::CMPQFileObject(MPQFileType type)
: m_eType(type)
, _objPanel(NULL)
, _bFileRemove(false)
{
}

CMPQFileObject::~CMPQFileObject()
{
	RemoveCache();
}

void CMPQFileObject::AttachPanel(CNotebookPanel* panel)
{
	_objPanel = panel;
}

int CMPQFileObject::LoadFile(const std::string& file, bool remove)
{
	if(_objBuffer.Attach(file) != 0)
		return -1;

	_strFileName = file;
	_bFileRemove = remove;

	return 0;
}

const std::string& CMPQFileObject::FileName() const
{
	return _strFileName;
}

void CMPQFileObject::RemoveCache()
{
	if(_strFileName.empty())
		return;

	_objBuffer.Detach();

	if(_bFileRemove)
		_unlink(_strFileName.c_str());
}

////
CMPQDBCObject::CMPQDBCObject()
: CMPQFileObject(MFT_DBC)
, _objDBCPanel(NULL)
{
}

CMPQDBCObject::~CMPQDBCObject()
{
}

void CMPQDBCObject::AttachPanel(CNotebookPanel *panel)
{
	CMPQFileObject::AttachPanel(panel);

	_objDBCPanel = (CNotebookDBCPanel*)_objPanel;
}

int CMPQDBCObject::ParseInfo()
{
	if(_objDBCPanel == NULL)
		return 0;

//	_objDBCPanel->SetFileName(Toolkit::String2wxString(_strFileName));
	if(_stHeader.Read(_objBuffer) != 0)
		return -1;
	_objDBCPanel->SetHeaderInfo(_stHeader);

	return 0;
}

int CMPQDBCObject::ParseData(const CMPQDBCFieldManager& manager)
{
	if(_objDBCPanel == NULL)
		return 0;

	int fsize = _stHeader.m_iRecordSize / _stHeader.m_iFields;

	//if(fsize != 4)
	//{
	//	wxMessageBox(wxT("DBC FieldSize != 4."));
	//	return 0;
	//}

	std::string str = CMPQFileObject::FileName();
	size_t pos = str.find_last_of("\\");
	if(pos != 0)
	{
		str = str.substr(pos + 1);
	}
	
	const CMPQDBCFieldManager::TFieldMap* mapField = manager.FindDBCFields(str);
	if(mapField == NULL && fsize != 4)
	{
		wxMessageBox(wxT("DBC Field NOT defined && FieldSize != 4."));
		return 0;
	}
	_objDBCPanel->InitGrid(_stHeader.m_iFields, mapField);

	int strpos = _stHeader.Size() + _stHeader.m_iRecords * _stHeader.m_iRecordSize;
	int offset = _stHeader.Size();
	for(int rec = 0; rec < _stHeader.m_iRecords; ++ rec)
	{
		_objDBCPanel->AppendRow();
		for(int field = 0; field < _stHeader.m_iFields; ++ field)
		{
			if(mapField != NULL)
			{
				const CMPQDBCFieldManager::TFieldMap::const_iterator it = mapField->find(field);
				if(it != mapField->end())
				{
					if(it->second->Data2String(str, _objBuffer, offset, strpos) != 0)
						str = "#ERROR";
					_objDBCPanel->SetGridData(rec, field, Toolkit::String2wxString(str));

					offset += it->second->m_stAttr.m_iSize;

					continue;
				}
			}
			DBCField::CField::DefaultData2String(str, _objBuffer, offset);
			_objDBCPanel->SetGridData(rec, field, Toolkit::String2wxString(str));

			offset += fsize;
		}
	}

	return 0;
}
//////
CMPQBLPObject::CMPQBLPObject()
: CMPQFileObject(MFT_BLP)
, _objBLPPanel(NULL)
{
}

CMPQBLPObject::~CMPQBLPObject()
{
	RemoveCache();
}

void CMPQBLPObject::AttachPanel(CNotebookPanel *panel)
{
	CMPQFileObject::AttachPanel(panel);

	_objBLPPanel = (CNotebookBLPPanel*)_objPanel;
}

int CMPQBLPObject::LoadFile(const std::string &file, bool remove)
{
	_strFileName = file;
	_bFileRemove = remove;

	return 0;
}

void CMPQBLPObject::RemoveCache()
{
	if(_bFileRemove)
		_unlink(_strPNGFileName.c_str());
}

int CMPQBLPObject::ShowImage()
{
	_strPNGFileName = _strFileName + ".png";

	CBLP2PNGObject converter;
	if(converter.Convert(_strFileName.c_str(), _strPNGFileName.c_str()) != 0)
		return -1;

	if(_objBLPPanel != NULL)
	{
		if(_objBLPPanel->ShowImage(Toolkit::String2wxString(_strPNGFileName)) != 0)
			return -1;
	}

	return 0;
}

////////////////////
CMPQWaveObject::CMPQWaveObject()
: CMPQFileObject(MFT_WAVE)
, _objWavePanel(NULL)
{
}

CMPQWaveObject::~CMPQWaveObject()
{
}

void CMPQWaveObject::AttachPanel(CNotebookPanel *panel)
{
	CMPQFileObject::AttachPanel(panel);
	_objWavePanel = (CNotebookWavePanel*)panel;
}

int CMPQWaveObject::LoadFile(const std::string &file, bool remove)
{
	_strFileName = file;
	_bFileRemove = remove;

	return 0;
}

int CMPQWaveObject::PlayFile()
{
	if(_objWavePanel == NULL)
		return -1;

	return _objWavePanel->PlayFile(Toolkit::String2wxString(_strFileName), _bFileRemove);
}

/////////////////////
CMPQM2Object::CMPQM2Object()
: CMPQFileObject(MFT_M2)
, _objM2Panel(NULL)
{
}

CMPQM2Object::~CMPQM2Object()
{
}

void CMPQM2Object::AttachPanel(CNotebookPanel *panel)
{
	CMPQFileObject::AttachPanel(panel);
	_objM2Panel = (CNotebookM2Panel*)panel;
}

int CMPQM2Object::ShowModel(const std::string& mpq, const std::string& m2, const std::string& mpqpath, bool findmpq, bool remove)
{
	if(_objM2Panel == NULL)
		return -1;
	return _objM2Panel->ShowModel(mpq, m2, mpqpath, findmpq, remove);
}

////
CMPQWDBObject::CMPQWDBObject()
: CMPQFileObject(MFT_WDB)
, _objWDBPanel(NULL)
{
}

CMPQWDBObject::~CMPQWDBObject()
{
}

void CMPQWDBObject::AttachPanel(CNotebookPanel *panel)
{
	CMPQFileObject::AttachPanel(panel);

	_objWDBPanel = (CNotebookWDBPanel*)_objPanel;
}

int CMPQWDBObject::ParseInfo()
{
	if(_objWDBPanel == NULL)
		return 0;

//	_objDBCPanel->SetFileName(Toolkit::String2wxString(_strFileName));
	if(_stHeader.Read(_objBuffer) != 0)
		return -1;
	_objWDBPanel->SetHeaderInfo(_stHeader);

	return 0;
}

int CMPQWDBObject::ParseData(const CMPQDBCFieldManager& manager)
{
	if(_objWDBPanel == NULL)
		return 0;

	std::string str = CMPQFileObject::FileName();
	size_t pos = str.find_last_of("\\");
	if(pos != 0)
	{
		str = str.substr(pos + 1);
	}
	
	const CMPQDBCFieldManager::TFieldMap* mapField = manager.FindDBCFields(str);
	if(mapField == NULL)
	{
		wxMessageBox(wxT("WDB field is NOT defined."));
		return 0;
	}

	_objWDBPanel->InitGrid(mapField);

	int offset = _stHeader.Size();

	_objBuffer.Seek(offset);

	int rec = 0;

	while(_objBuffer.Good())
	{
		_objWDBPanel->AppendRow();
		for(int field = 0; field < mapField->size(); ++ field)
		{
			const CMPQDBCFieldManager::TFieldMap::const_iterator it = mapField->find(field);
			if(it == mapField->end())
			{
				wxMessageBox(wxT("Field is NOT defined."));
				return 0;
			}
			if(it->second->m_eType != DBCField::FT_AMOUNT)
			{
				if(it->second->Data2String(str, _objBuffer, offset, 0) != 0)
					str = "#ERROR";
			}
			else
			{
				int data = -1;
				if(it->second->GetIntData(data, str, _objBuffer, offset, 0) != 0)
					str = "#ERROR";
				else
					_objBuffer.Skip((data + 1) * it->second->m_stAttr.m_iSkipByte);
			}
			_objWDBPanel->SetGridData(rec , field, Toolkit::String2wxString(str));
		}

		++ rec;
	}

	return 0;
}

