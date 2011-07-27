
#pragma warning(disable:4996)

#include <fstream>
#include <string>

#include "wx/wx.h"
#include "wx/panel.h"
#include "wx/notebook.h"
#include "wx/toolbook.h"
#include "wx/grid.h"

#include "wx/ListCtrl.h"

#include "Consts.h"
#include "Toolkit.h"

#include "NotebookPanels.h"

CNotebookPanelManager::CNotebookPanelManager()
: _ctrlNotebook(NULL)
{
}

CNotebookPanelManager::~CNotebookPanelManager()
{
	DestoryPanels();
}

void CNotebookPanelManager::DestoryPanels()
{
}

int CNotebookPanelManager::InitPanels(wxToolbook *notebook)
{
	_ctrlNotebook = notebook;

	return 0;
}

CNotebookPanel* CNotebookPanelManager::ShowPanel(PanelType type, bool show, bool selected)
{
	if(_ctrlNotebook == NULL)
		return NULL;

	TPanelMap::iterator it = _mapPanel.find(type);
	if(show)
	{
		CNotebookPanel* panel = NULL;
		if(it == _mapPanel.end())
		{
			switch(type)
			{
			case PT_INFO:
				panel = new CNotebookInfoPanel(_ctrlNotebook);
				break;
			case PT_DATA:
				panel = new CNotebookDataPanel(_ctrlNotebook);
				break;
			case PT_DBC:
				panel = new CNotebookDBCPanel(_ctrlNotebook);
				break;
			case PT_BLP:
				panel = new CNotebookBLPPanel(_ctrlNotebook);
				break;
			case PT_WAVE:
				panel = new CNotebookWavePanel(_ctrlNotebook);
				break;
			case PT_M2:
				panel = new CNotebookM2Panel(_ctrlNotebook);
				break;
			case PT_WDB:
				panel = new CNotebookWDBPanel(_ctrlNotebook);
				break;
			default:
				return NULL;
			}
			panel->InitControls();
			_ctrlNotebook->AddPage(panel, panel->m_strTitle, false, panel->m_iImageID);
			PanelData_t data(_ctrlNotebook->GetPageCount() - 1, panel);
			it = _mapPanel.insert(std::make_pair(type, data)).first;
		}
		if(selected)
			_ctrlNotebook->SetSelection(it->second.m_szPos);
		return it->second.m_objPanel;
	}
	else
	{
		if(it != _mapPanel.end())
		{
			_ctrlNotebook->DeletePage(it->second.m_szPos);
			//delete it->second.m_objPanel;
			_mapPanel.erase(it);
		}
	}
	return NULL;
}

CNotebookPanel* CNotebookPanelManager::GetPanel(PanelType type)
{
	if(_ctrlNotebook == NULL)
		return NULL;

	TPanelMap::iterator it = _mapPanel.find(type);
	if(it == _mapPanel.end())
		return NULL;

	return it->second.m_objPanel;
}

////

CNotebookPanel::CNotebookPanel(wxWindow* parent, PanelType type, const wxString& title, int image, int id)
: wxPanel(parent, id)
, m_eType(type)
, m_strTitle(title)
, m_iImageID(image)
{
}

CNotebookPanel::~CNotebookPanel()
{
}

int CNotebookPanel::InitControls()
{
	if(CreatePanelControls() == 0 && SetPanelControls() == 0)
		return 0;

	return -1;
}

////
CNotebookInfoPanel::CNotebookInfoPanel(wxWindow* parent)
: CNotebookPanel(parent, PT_INFO, wxT("Information"), 0)
{
}

CNotebookInfoPanel::~CNotebookInfoPanel()
{
}

int CNotebookInfoPanel::CreatePanelControls()
{
    sizer_16_staticbox = new wxStaticBox(this, -1, wxEmptyString);
	sizer_6_staticbox = new wxStaticBox(this, -1, wxEmptyString);

    m_labelFileName = new wxStaticText(this, wxID_ANY, wxEmptyString);

	label_8 = new wxStaticText(this, wxID_ANY, wxT("FileName : "), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editInfoFileName = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_9 = new wxStaticText(this, wxID_ANY, wxT("Offset : "), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editInfoOffset = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_10 = new wxStaticText(this, wxID_ANY, wxT("Version : "), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editInfoVersion = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_11 = new wxStaticText(this, wxID_ANY, wxT("Size : "), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editInfoSize = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_12 = new wxStaticText(this, wxID_ANY, wxT("Flag : "), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editInfoFlag = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_13 = new wxStaticText(this, wxID_ANY, wxT("Index : "), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editInfoIndex = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_14 = new wxStaticText(this, wxID_ANY, wxT("CompSize : "), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editInfoCompSize = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    m_btnInfoExport = new wxButton(this, IDB_DATA_EXPORT, wxT("Export ..."));
    panel_1 = new wxPanel(this, wxID_ANY);

	return 0;
}

int CNotebookInfoPanel::SetPanelControls()
{
    wxBoxSizer* sizer_5 = new wxBoxSizer(wxVERTICAL);
    wxStaticBoxSizer* sizer_6 = new wxStaticBoxSizer(sizer_6_staticbox, wxHORIZONTAL);
    wxBoxSizer* sizer_7 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_14 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_13 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_12 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_11 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_10 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_9 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_8 = new wxBoxSizer(wxHORIZONTAL);

    wxStaticBoxSizer* sizer_16 = new wxStaticBoxSizer(sizer_16_staticbox, wxHORIZONTAL);


    sizer_16->Add(m_labelFileName, 0, wxEXPAND|wxALIGN_CENTER_VERTICAL, 0);
    sizer_5->Add(sizer_16, 0, wxEXPAND, 0);

	sizer_8->Add(label_8, 2, wxLEFT|wxALIGN_CENTER_VERTICAL, 4);
    sizer_8->Add(m_editInfoFileName, 8, wxRIGHT, 16);
    sizer_7->Add(sizer_8, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_9->Add(label_9, 2, wxLEFT|wxALIGN_CENTER_VERTICAL, 4);
    sizer_9->Add(m_editInfoOffset, 8, wxRIGHT, 16);
    sizer_7->Add(sizer_9, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_10->Add(label_10, 2, wxLEFT|wxALIGN_CENTER_VERTICAL, 4);
    sizer_10->Add(m_editInfoVersion, 8, wxRIGHT, 16);
    sizer_7->Add(sizer_10, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_11->Add(label_11, 2, wxLEFT|wxALIGN_CENTER_VERTICAL, 4);
    sizer_11->Add(m_editInfoSize, 8, wxRIGHT, 16);
    sizer_7->Add(sizer_11, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_12->Add(label_12, 2, wxLEFT|wxALIGN_CENTER_VERTICAL, 4);
    sizer_12->Add(m_editInfoFlag, 8, wxRIGHT, 16);
    sizer_7->Add(sizer_12, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_13->Add(label_13, 2, wxLEFT|wxALIGN_CENTER_VERTICAL, 4);
    sizer_13->Add(m_editInfoIndex, 8, wxRIGHT, 16);
    sizer_7->Add(sizer_13, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_14->Add(label_14, 2, wxLEFT|wxALIGN_CENTER_VERTICAL, 4);
    sizer_14->Add(m_editInfoCompSize, 8, wxRIGHT, 16);
    sizer_7->Add(sizer_14, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_6->Add(sizer_7, 1, wxEXPAND, 0);
    sizer_5->Add(sizer_6, 0, wxEXPAND, 0);
    sizer_5->Add(m_btnInfoExport, 0, wxTOP|wxBOTTOM|wxEXPAND, 4);
    sizer_5->Add(panel_1, 1, wxEXPAND, 0);
    this->SetSizer(sizer_5);

	this->Layout();

	return 0;
}

void CNotebookInfoPanel::DisplayInfo(const MPQData::TFileData *data)
{
	if(data == NULL)
	{
		m_btnInfoExport->Disable();
	}
	else
	{
		m_labelFileName->SetLabel(Toolkit::String2wxString(data->m_strName));
		m_editInfoFileName->SetValue(Toolkit::String2wxString(data->m_strName));
		wxString str;
		str.sprintf(wxT("0x%X"), data->m_pPointer);
		m_editInfoOffset->SetValue(str);
		str.sprintf(wxT("0x%X"), data->m_uiVersion); 
		m_editInfoVersion->SetValue(str);
		str.sprintf(wxT("%d Bytes"), data->m_uiSize);
		m_editInfoSize->SetValue(str);
		str.sprintf(wxT("0x%X"), data->m_uiFlag);
		m_editInfoFlag->SetValue(str);
		str.sprintf(wxT("0x%X"), data->m_uiIndex);
		m_editInfoIndex->SetValue(str);
		str.sprintf(wxT("%d Bytes"), data->m_uiCompSize);
		m_editInfoCompSize->SetValue(str);

		m_btnInfoExport->Enable();
	}
}

////

CNotebookDataPanel::CNotebookDataPanel(wxWindow* parent)
: CNotebookPanel(parent, PT_DATA, wxT("Data Dump"), 0)
{
}

CNotebookDataPanel::~CNotebookDataPanel()
{
}

int CNotebookDataPanel::CreatePanelControls()
{
	sizer_15_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    m_labelDataFileName = new wxStaticText(this, wxID_ANY, wxEmptyString);
    m_ctrlDataText = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_MULTILINE|wxTE_READONLY|wxHSCROLL);

	return 0;
}

int CNotebookDataPanel::SetPanelControls()
{
    wxBoxSizer* sizer_4 = new wxBoxSizer(wxVERTICAL);
    wxStaticBoxSizer* sizer_15 = new wxStaticBoxSizer(sizer_15_staticbox, wxHORIZONTAL);
 
	sizer_15->Add(m_labelDataFileName, 0, wxEXPAND|wxALIGN_RIGHT|wxALIGN_CENTER_VERTICAL, 0);
    sizer_4->Add(sizer_15, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_4->Add(m_ctrlDataText, 1, wxEXPAND, 0);
    this->SetSizer(sizer_4);

	m_labelDataFileName->SetLabel(wxT(""));
    m_ctrlDataText->SetFont(wxFont(10, wxMODERN, wxNORMAL, wxNORMAL, 0, wxT("Courier New")));

	this->Layout();

	return 0;
}

bool CNotebookDataPanel::CheckDataFileName(const wxString& file) const
{
	return file == m_labelDataFileName->GetLabel();
}

void CNotebookDataPanel::SetFileName(const wxString& str)
{
	m_labelDataFileName->SetLabel(str);
}

void CNotebookDataPanel::Refresh(const wxString& str)
{
	m_labelDataFileName->SetLabel(str);
	m_ctrlDataText->Clear();
}

void CNotebookDataPanel::AppendEnd()
{
	m_ctrlDataText->ShowPosition(0);
}

void CNotebookDataPanel::AppendData(const wxString& str)
{
	m_ctrlDataText->AppendText(str);
}

////

//BEGIN_EVENT_TABLE(CNotebookDBCPanel::CMyGrid, wxGrid)
//	EVT_CONTEXT_MENU(CNotebookDBCPanel::CMyGrid::OnContextMenu)
//	EVT_MENU(IDM_NB_DBC_GRID_SAVE, CNotebookDBCPanel::CMyGrid::OnMenuGridSave)
//END_EVENT_TABLE()
//
//void CNotebookDBCPanel::CMyGrid::OnContextMenu(wxContextMenuEvent &event)
//{
//	wxPoint point = event.GetPosition();
//	point = ScreenToClient(point);
//
//    wxMenu menu;
//
//    menu.Append(IDM_NB_DBC_GRID_SAVE, _T("&Save To.."));
//
//    PopupMenu(&menu, point.x, point.y);
//}
//
//void CNotebookDBCPanel::CMyGrid::OnMenuGridSave(wxCommandEvent &event)
//{
//	wxString file = _strFileName;
//	size_t pos = file.find_last_of(wxT("\\"));
//	if(pos != 0)
//	{
//		file = file.substr(pos + 1);
//	}
//
//	wxFileDialog dlg(this,
//						wxT("Save DBC file..."),
//						wxEmptyString,
//						file + wxT(".txt"),
//						wxT("Text Files (*.txt)|*.txt"),
//						wxFD_SAVE|wxFD_OVERWRITE_PROMPT);
//	if (dlg.ShowModal() == wxID_OK)
//	{
//		wxMessageBox(wxT("Save DBC file failed."));
//	}
//	event.Skip();
//}

BEGIN_EVENT_TABLE(CNotebookDBCPanel, wxPanel)
	EVT_BUTTON(IDB_NB_DBC_GRID_SAVE, CNotebookDBCPanel::OnButtonGridSave)
END_EVENT_TABLE()

CNotebookDBCPanel::CNotebookDBCPanel(wxWindow* parent)
: CNotebookPanel(parent, PT_DBC, wxT("DBC"), 0)
{
}

CNotebookDBCPanel::~CNotebookDBCPanel()
{
}

int CNotebookDBCPanel::CreatePanelControls()
{
    sizer_6_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    sizer_3_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    m_labelFileName = new wxStaticText(this, wxID_ANY, wxEmptyString);
    label_6 = new wxStaticText(this, wxID_ANY, wxT("Signature:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editSignature = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_8 = new wxStaticText(this, wxID_ANY, wxT("Records:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editRecords = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_9 = new wxStaticText(this, wxID_ANY, wxT("Fields:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editFields = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_7 = new wxStaticText(this, wxID_ANY, wxT("Record Size:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editRecordSize = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_10 = new wxStaticText(this, wxID_ANY, wxT("String Size:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editStringSize = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    panel_2 = new wxPanel(this, wxID_ANY);
    //panel_3 = new wxPanel(this, wxID_ANY);
	m_btnGridExport = new wxButton(this, IDB_NB_DBC_GRID_SAVE, wxT("Grid Save ..."));
    m_ctrlGrid = new wxGrid(this, wxID_ANY);

	return 0;
}

int CNotebookDBCPanel::SetPanelControls()
{
	wxBoxSizer* sizer_2 = new wxBoxSizer(wxVERTICAL);
    wxStaticBoxSizer* sizer_6 = new wxStaticBoxSizer(sizer_6_staticbox, wxVERTICAL);
    wxBoxSizer* sizer_8 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_7 = new wxBoxSizer(wxHORIZONTAL);
    wxStaticBoxSizer* sizer_3 = new wxStaticBoxSizer(sizer_3_staticbox, wxHORIZONTAL);
    sizer_3->Add(m_labelFileName, 0, wxEXPAND|wxALIGN_CENTER_VERTICAL, 0);
    sizer_2->Add(sizer_3, 0, wxEXPAND, 0);
    sizer_7->Add(label_6, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_7->Add(m_editSignature, 5, wxEXPAND, 0);
    sizer_7->Add(label_8, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_7->Add(m_editRecords, 5, wxEXPAND, 0);
    sizer_7->Add(label_9, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_7->Add(m_editFields, 5, wxEXPAND, 0);
    sizer_6->Add(sizer_7, 0, wxTOP|wxBOTTOM|wxEXPAND, 2);
    sizer_8->Add(label_7, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_8->Add(m_editRecordSize, 5, wxEXPAND, 0);
    sizer_8->Add(label_10, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_8->Add(m_editStringSize, 5, wxEXPAND, 0);
    sizer_8->Add(panel_2, 4, wxEXPAND, 0);
    sizer_8->Add(m_btnGridExport, 5, wxEXPAND, 0);// panel_3, 5, wxEXPAND, 0);
    sizer_6->Add(sizer_8, 0, wxEXPAND, 2);
    sizer_2->Add(sizer_6, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_2->Add(m_ctrlGrid, 1, wxEXPAND, 0);
    this->SetSizer(sizer_2);

    m_ctrlGrid->CreateGrid(0, 0,wxGrid::wxGridSelectRows);
    m_ctrlGrid->EnableEditing(false);
    m_ctrlGrid->SetGridLineColour(wxColour(0, 0, 255));
//    m_ctrlGrid->SetColLabelValue(0, wxEmptyString);

	this->Layout();

	return 0;
}

void CNotebookDBCPanel::OnButtonGridSave(wxCommandEvent &event)
{
	wxString file = m_labelFileName->GetLabel();
	size_t pos = file.find_last_of(wxT("\\"));
	if(pos != 0)
	{
		file = file.substr(pos + 1);
	}

	wxFileDialog dlg(this,
						wxT("Save DBC file..."),
						wxEmptyString,
						file + wxT(".txt"),
						wxT("Text Files (*.txt)|*.txt"),
						wxFD_SAVE|wxFD_OVERWRITE_PROMPT);
	if (dlg.ShowModal() == wxID_OK)
	{
		std::ofstream ofs(Toolkit::wxString2String(dlg.GetPath()).c_str(), std::ios::out | std::ios::trunc);
		if(ofs.is_open())
		{
			for(int j = 0; j < m_ctrlGrid->GetNumberCols();)
			{
				ofs << Toolkit::wxString2String(m_ctrlGrid->GetColLabelValue(j));
				if(++ j != m_ctrlGrid->GetNumberCols())
					ofs << ",";
				else
					ofs << ";" << std::endl;
			}

			for(int i = 0; i < m_ctrlGrid->GetNumberRows(); ++ i)
			{
				for(int j = 0; j < m_ctrlGrid->GetNumberCols();)
				{
					ofs << Toolkit::wxString2String(m_ctrlGrid->GetCellValue(i, j));
					if(++ j != m_ctrlGrid->GetNumberCols())
						ofs << ",";
					else
						ofs << ";" << std::endl;
				}
			}
			ofs.close();
		}
		else
		{
			wxMessageBox(wxT("Open output file failed."));
		}
	}
	event.Skip();
}

void CNotebookDBCPanel::SetFileName(const wxString &str)
{
	m_labelFileName->SetLabel(str);
//	m_ctrlGrid->SetFileName(str);
}

void CNotebookDBCPanel::SetHeaderInfo(const MPQData::CDBCHeader &header)
{
	wxString str;
	str.sprintf(wxT("%c%c%c%c"), header.m_acSignature[0], header.m_acSignature[1], header.m_acSignature[2], header.m_acSignature[3]);
	m_editSignature->SetValue(str);
	str.sprintf(wxT("%d"), header.m_iRecords);
	m_editRecords->SetValue(str);
	str.sprintf(wxT("%d"), header.m_iFields);
	m_editFields->SetValue(str);
	str.sprintf(wxT("%d"), header.m_iRecordSize);
	m_editRecordSize->SetValue(str);
	str.sprintf(wxT("%d"), header.m_iBlockSize);
	m_editStringSize->SetValue(str);
}

void CNotebookDBCPanel::InitGrid(int fields, const CMPQDBCFieldManager::TFieldMap* mapField)
{
	m_ctrlGrid->ClearGrid();
	if(m_ctrlGrid->GetNumberCols() > 0)
		m_ctrlGrid->DeleteCols(0, m_ctrlGrid->GetNumberCols());
	if(m_ctrlGrid->GetNumberRows() > 0)
		m_ctrlGrid->DeleteRows(0, m_ctrlGrid->GetNumberRows());
	m_ctrlGrid->AppendCols(fields);

	for(int i = 0; i < m_ctrlGrid->GetNumberCols(); ++ i)
	{
		m_ctrlGrid->SetColLabelValue(i, wxString::Format(wxT("Field_%d"), i));
	}
	if(mapField != NULL)
	{
		for(CMPQDBCFieldManager::TFieldMap::const_iterator it = mapField->begin(); it != mapField->end(); ++ it)
		{
			if(it->first < fields)
			{
				m_ctrlGrid->SetColLabelValue(it->first, Toolkit::String2wxString(it->second->m_stAttr.m_strTitle));
			}
		}
	}
}

void CNotebookDBCPanel::AppendRow()
{
	m_ctrlGrid->AppendRows(1);
}

void CNotebookDBCPanel::SetGridData(int row, int col, const wxString& data)
{
	m_ctrlGrid->SetCellValue(row, col, data);
}
////

BEGIN_EVENT_TABLE(CNotebookBLPPanel::CScrolledBitmapCanvas, wxScrolledWindow)
    EVT_PAINT(CNotebookBLPPanel::CScrolledBitmapCanvas::OnPaint)
	//EVT_RIGHT_UP(CNotebookBLPPanel::CScrolledBitmapCanvas::OnRButtonUp)
	EVT_CONTEXT_MENU(CNotebookBLPPanel::CScrolledBitmapCanvas::OnContextMenu)
	EVT_MENU(IDM_NB_BLP_BITMAPCANVAS_SAVE, CNotebookBLPPanel::CScrolledBitmapCanvas::OnMenuBitmapSave)
END_EVENT_TABLE()

CNotebookBLPPanel::CScrolledBitmapCanvas::CScrolledBitmapCanvas(wxWindow *parent, wxWindowID id, const wxPoint &pos, const wxSize &size)
: wxScrolledWindow(parent, id, pos, size)
{
}

CNotebookBLPPanel::CScrolledBitmapCanvas::~CScrolledBitmapCanvas()
{
}

int CNotebookBLPPanel::CScrolledBitmapCanvas::InitPNGHandler()
{
	//wxPNGHandler handler;
	//wxImage image;
	//image.AddHandler(&handler);
	return 0;
}

void CNotebookBLPPanel::CScrolledBitmapCanvas::OnPaint(wxPaintEvent &event)
{
	if(_bitmapPNG.Ok())
	{
		wxPaintDC dc(this);
		PrepareDC(dc);

		dc.DrawBitmap(_bitmapPNG, 0, 0);
	}
	event.Skip();
}

void CNotebookBLPPanel::CScrolledBitmapCanvas::OnContextMenu(wxContextMenuEvent& event)
{
	wxPoint point = event.GetPosition();
	point = ScreenToClient(point);

    wxMenu menu;

    menu.Append(IDM_NB_BLP_BITMAPCANVAS_SAVE, _T("&Save To.."));

    PopupMenu(&menu, point.x, point.y);

//	event.Skip();
}

void CNotebookBLPPanel::CScrolledBitmapCanvas::OnMenuBitmapSave(wxCommandEvent& event)
{
	wxString file = _strFileName;
	size_t pos = file.find_last_of(wxT("\\"));
	if(pos != 0)
	{
		file = file.substr(pos + 1);
	}

	wxFileDialog dlg(this,
						wxT("Save PNG file..."),
						wxEmptyString,
						file,
						wxT("PNG Files (*.png)|*.png"),
						wxFD_SAVE|wxFD_OVERWRITE_PROMPT);
	if (dlg.ShowModal() == wxID_OK)
	{
		if(!_bitmapPNG.SaveFile(dlg.GetPath(), wxBITMAP_TYPE_PNG))
		{
			wxMessageBox(wxT("Save PNG file failed."));
		}
	}
	event.Skip();
}

int CNotebookBLPPanel::CScrolledBitmapCanvas::SetBitmap(const wxString& file, wxSize& size)
{
	_strFileName = file;
	if(!_bitmapPNG.LoadFile(_strFileName, wxBITMAP_TYPE_PNG))
		return -1;
	size.Set(_bitmapPNG.GetWidth(), _bitmapPNG.GetHeight());
	return 0;
}

CNotebookBLPPanel::CNotebookBLPPanel(wxWindow* parent)
: CNotebookPanel(parent, PT_BLP, wxT("BLP"), 0)
{
}

CNotebookBLPPanel::~CNotebookBLPPanel()
{
}

int CNotebookBLPPanel::CreatePanelControls()
{
    sizer_3_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    m_labelFileName = new wxStaticText(this, wxID_ANY, wxEmptyString);
    m_canvasBitmap = new CScrolledBitmapCanvas(this, IDC_NB_BLP_BITMAPCANVAS, wxPoint(0, 0), wxSize(-1, -1));
	m_canvasBitmap->InitPNGHandler();

	return 0;
}

int CNotebookBLPPanel::SetPanelControls()
{
	wxBoxSizer* sizer_1 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_2 = new wxBoxSizer(wxVERTICAL);
    wxStaticBoxSizer* sizer_3 = new wxStaticBoxSizer(sizer_3_staticbox, wxHORIZONTAL);
    sizer_3->Add(m_labelFileName, 0, wxEXPAND|wxALIGN_CENTER_VERTICAL, 0);
	sizer_2->Add(sizer_3, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_2->Add(m_canvasBitmap, 1, wxEXPAND, 0);
    this->SetSizer(sizer_2);

	this->Layout();

	return 0;
}

void CNotebookBLPPanel::SetFileName(const wxString &str)
{
	m_labelFileName->SetLabel(str);
}

int CNotebookBLPPanel::ShowImage(const wxString& file)
{
	wxSize size(0, 0);
	if(m_canvasBitmap->SetBitmap(file, size) != 0)
		return -1;

	m_canvasBitmap->SetScrollbars(1, 1, size.GetWidth(), size.GetHeight());

	return 0;
}
//////

BEGIN_EVENT_TABLE(CNotebookWavePanel, wxPanel)
    // begin wxGlade: CNotebookWavePanel::event_table
    EVT_BUTTON(IDC_NB_WAVE_BUTTON_PLAY, CNotebookWavePanel::OnBtnPlay)
    EVT_BUTTON(IDC_NB_WAVE_BUTTON_PAUSE, CNotebookWavePanel::OnBtnPause)
    EVT_BUTTON(IDC_NB_WAVE_BUTTON_STOP, CNotebookWavePanel::OnBtnStop)
//    EVT_COMMAND_SCROLL_THUMBRELEASE(IDC_NB_WAVE_SLIDER_PLAY, CNotebookWavePanel::OnSliderPlayRelease)
//    EVT_COMMAND_SCROLL_THUMBRELEASE(IDC_NB_WAVE_SLIDER_VOLUME, CNotebookWavePanel::OnSliderVolumeRelease)
	//EVT_COMMAND_SCROLL_CHANGED(IDC_NB_WAVE_SLIDER_VOLUME, CNotebookWavePanel::OnSliderVolumeRelease)
	EVT_COMMAND_SCROLL_CHANGED(IDC_NB_WAVE_SLIDER_PLAY, CNotebookWavePanel::OnSliderPlayRelease)
	EVT_SLIDER(IDC_NB_WAVE_SLIDER_VOLUME, CNotebookWavePanel::OnSliderVolumeRelease)	

	EVT_TIMER(IDT_NB_WAVE_PLAYTIMER, CNotebookWavePanel::OnTimer)
    // end wxGlade
END_EVENT_TABLE();

CNotebookWavePanel::CNotebookWavePanel(wxWindow *parent)
: CNotebookPanel(parent, PT_WAVE, wxT("Wave"), 0)
, _objDSound(NULL)
, _bRemove(true)
, _timerPlay(this, IDT_NB_WAVE_PLAYTIMER)
{
}

CNotebookWavePanel::~CNotebookWavePanel()
{
	Release();
	RemoveFile(wxT(""));
}

int CNotebookWavePanel::CreatePanelControls()
{
    sizer_1_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    sizer_6_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    sizer_23_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    m_labelFileName = new wxStaticText(this, wxID_ANY, wxEmptyString);
    m_labelFormat = new wxStaticText(this, wxID_ANY, wxT("WaveFormatTag:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editFormat = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    m_labelChannels = new wxStaticText(this, wxID_ANY, wxT("Channels:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editChannels = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    m_labelSamples = new wxStaticText(this, wxID_ANY, wxT("SamplesPerSec:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editSamples = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    m_labelBPS = new wxStaticText(this, wxID_ANY, wxT("BitsPerSample:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editBPS = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    m_labelBlockAlign = new wxStaticText(this, wxID_ANY, wxT("BlockAlign:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editBAlign = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    m_labelABPS = new wxStaticText(this, wxID_ANY, wxT("AvgBytesPerSec:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editABPS = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    m_sliderPlay = new wxSlider(this, IDC_NB_WAVE_SLIDER_PLAY, 0, 0, 10, wxDefaultPosition, wxDefaultSize, wxSL_HORIZONTAL|wxSL_AUTOTICKS|wxSL_LABELS);
    panel_5 = new wxPanel(this, wxID_ANY);
    panel_2 = new wxPanel(this, wxID_ANY);
    m_btnPlay = new wxButton(this, IDC_NB_WAVE_BUTTON_PLAY, wxT("&Play"));
    m_btnPause = new wxButton(this, IDC_NB_WAVE_BUTTON_PAUSE, wxT("P&ause"));
    m_btnStop = new wxButton(this, IDC_NB_WAVE_BUTTON_STOP, wxT("&Stop"));
    panel_4 = new wxPanel(this, wxID_ANY);
    panel_3 = new wxPanel(this, wxID_ANY);
    label_7 = new wxStaticText(this, wxID_ANY, wxT("Volume"));
    m_sliderVolume = new wxSlider(this, IDC_NB_WAVE_SLIDER_VOLUME, 0, 0, 10000, wxDefaultPosition, wxDefaultSize, wxSL_VERTICAL|wxSL_BOTH);
    panel_1 = new wxPanel(this, wxID_ANY);

	return 0;
}

int CNotebookWavePanel::SetPanelControls()
{
    wxBoxSizer* sizer_21 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_22 = new wxBoxSizer(wxVERTICAL);
    wxStaticBoxSizer* sizer_6 = new wxStaticBoxSizer(sizer_6_staticbox, wxHORIZONTAL);
    wxBoxSizer* sizer_8 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_7 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_9 = new wxBoxSizer(wxHORIZONTAL);
    wxStaticBoxSizer* sizer_1 = new wxStaticBoxSizer(sizer_1_staticbox, wxVERTICAL);
    wxBoxSizer* sizer_4 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_3 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_2 = new wxBoxSizer(wxHORIZONTAL);
    wxStaticBoxSizer* sizer_23 = new wxStaticBoxSizer(sizer_23_staticbox, wxHORIZONTAL);
    sizer_23->Add(m_labelFileName, 0, wxEXPAND|wxALIGN_CENTER_VERTICAL, 0);
    sizer_22->Add(sizer_23, 0, wxEXPAND, 0);
    sizer_2->Add(m_labelFormat, 4, wxALIGN_RIGHT|wxALIGN_CENTER_VERTICAL, 0);
    sizer_2->Add(m_editFormat, 3, wxALIGN_CENTER_VERTICAL, 0);
    sizer_2->Add(m_labelChannels, 4, wxALIGN_RIGHT|wxALIGN_CENTER_VERTICAL, 0);
    sizer_2->Add(m_editChannels, 3, wxALIGN_CENTER_VERTICAL, 0);
    sizer_1->Add(sizer_2, 0, wxTOP|wxBOTTOM|wxEXPAND, 4);
    sizer_3->Add(m_labelSamples, 4, wxALIGN_RIGHT|wxALIGN_CENTER_VERTICAL, 0);
    sizer_3->Add(m_editSamples, 3, wxALIGN_CENTER_VERTICAL, 0);
    sizer_3->Add(m_labelBPS, 4, wxALIGN_RIGHT|wxALIGN_CENTER_VERTICAL, 0);
    sizer_3->Add(m_editBPS, 3, wxALIGN_CENTER_VERTICAL, 0);
    sizer_1->Add(sizer_3, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_4->Add(m_labelBlockAlign, 4, wxALIGN_RIGHT|wxALIGN_CENTER_VERTICAL, 0);
    sizer_4->Add(m_editBAlign, 3, wxALIGN_CENTER_VERTICAL, 0);
    sizer_4->Add(m_labelABPS, 4, wxALIGN_RIGHT|wxALIGN_CENTER_VERTICAL, 0);
    sizer_4->Add(m_editABPS, 3, wxALIGN_CENTER_VERTICAL, 0);
    sizer_1->Add(sizer_4, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_22->Add(sizer_1, 0, wxEXPAND, 0);
    sizer_7->Add(m_sliderPlay, 3, wxEXPAND, 0);
    sizer_7->Add(panel_5, 2, wxEXPAND, 0);
    sizer_9->Add(panel_2, 1, wxEXPAND, 0);
    sizer_9->Add(m_btnPlay, 0, 0, 0);
    sizer_9->Add(m_btnPause, 0, 0, 0);
    sizer_9->Add(m_btnStop, 0, 0, 0);
    sizer_9->Add(panel_4, 1, wxEXPAND, 0);
    sizer_7->Add(sizer_9, 3, wxEXPAND, 0);
    sizer_6->Add(sizer_7, 1, wxEXPAND, 0);
    sizer_6->Add(panel_3, 0, wxEXPAND, 0);
    sizer_8->Add(label_7, 0, wxALIGN_CENTER_HORIZONTAL, 0);
    sizer_8->Add(m_sliderVolume, 0, wxEXPAND, 0);
    sizer_6->Add(sizer_8, 0, wxEXPAND, 0);
    sizer_22->Add(sizer_6, 0, wxEXPAND, 0);
    sizer_22->Add(panel_1, 1, wxEXPAND, 0);
    this->SetSizer(sizer_22);
    this->Layout();
	this->Update();

	return 0;
}

void CNotebookWavePanel::Release()
{
	_timerPlay.Stop();
	if(_objDSound != NULL)
		delete _objDSound, _objDSound = NULL;
}

void CNotebookWavePanel::OnBtnPlay(wxCommandEvent &event)
{
	Play();
    event.Skip();
}

void CNotebookWavePanel::OnBtnPause(wxCommandEvent &event)
{
	Pause();
    event.Skip();
}

void CNotebookWavePanel::OnBtnStop(wxCommandEvent &event)
{
	Stop();
    event.Skip();
}

void CNotebookWavePanel::OnSliderPlayRelease(wxScrollEvent& event)
{
	if(_objDSound->IsReady())
		_objDSound->SetPlayPos(event.GetPosition());

	event.Skip();
}

void CNotebookWavePanel::OnSliderVolumeRelease(wxCommandEvent& event)
{
	if(_objDSound->IsReady())
		_objDSound->SetVolume(-m_sliderVolume->GetValue());

	event.Skip();
}

void CNotebookWavePanel::OnTimer(wxTimerEvent& event)
{
	int pos = m_sliderPlay->GetValue();
	if(pos < m_sliderPlay->GetMax())
	{
		m_sliderPlay->SetValue(pos + 500);
	}
	else
	{
		Stop();
	}

	event.Skip();
}

void CNotebookWavePanel::SetFileName(const wxString &str)
{
	m_labelFileName->SetLabel(str);
}

int CNotebookWavePanel::PlayFile(const wxString &file, bool remove)
{
	Release();
	RemoveFile(file);

	_strFileName = file;
	_bRemove = remove;

	wxString str = file;
	size_t pos = str.find_last_of(wxT("."));
	if(pos == wxString::npos)
		return -1;

	str = str.substr(pos);
	if(str == wxT(".wav"))
		return LoadWaveFile(file);
	else if(str == wxT(".mp3"))
		return LoadMP3File(file);
	return -1;
}

int CNotebookWavePanel::LoadWaveFile(const wxString& file)
{
	_objDSound = new CDSWaveObject();
	_objDSound->Init((HWND)this->GetHandle());
	_objDSound->LoadFile(file.wc_str());

	_objDSound->SetVolume(- m_sliderVolume->GetValue());

	ShowInfo();
	
	_timerPlay.Start(500, false);

	return _objDSound->Play();
}

int CNotebookWavePanel::LoadMP3File(const wxString& file)
{
	_objDSound = new CDSMP3Object();
	_objDSound->Init((HWND)this->GetHandle());
	_objDSound->LoadFile(file.wc_str());

	_objDSound->SetVolume(- m_sliderVolume->GetValue());

	ShowInfo();

	_timerPlay.Start(500, false);

	return _objDSound->Play();
}

int CNotebookWavePanel::RemoveFile(const wxString& file)
{
	if(!_bRemove)
		return -1;
	if(_strFileName.empty())
		return -1;
	if(_strFileName == file)
		return -1;
	std::string str = Toolkit::wxString2String(_strFileName);
	_unlink(str.c_str());
	return 0;
}

int CNotebookWavePanel::Play()
{
	return PlayFile(_strFileName, _bRemove);
}

int CNotebookWavePanel::Pause()
{
	if(_objDSound->IsReady())
	{
		if(_objDSound->IsPlaying())
		{
			_timerPlay.Stop();
			_objDSound->Pause();
		}
		else
		{
			_timerPlay.Start(500, false);
			_objDSound->Play();
		}
	}
	return 0;
}

int CNotebookWavePanel::Stop()
{
	if(_objDSound->IsReady())
	{
		_objDSound->Stop();
	}
	_timerPlay.Stop();
	m_sliderPlay->SetValue(0);

	return 0;
}

int CNotebookWavePanel::ShowInfo()
{
	m_editFormat->SetValue(wxString::Format(wxT("%s"), (_objDSound->GetFormatTag() == 1 ? wxT("PCM") : wxT("Unknown"))));
	m_editChannels->SetValue(wxString::Format(wxT("%d"), _objDSound->GetChannels()));
	m_editSamples->SetValue(wxString::Format(wxT("%d"), _objDSound->GetSamples()));
	m_editBPS->SetValue(wxString::Format(wxT("%d"), _objDSound->GetBPS()));
	m_editBAlign->SetValue(wxString::Format(wxT("%d"), _objDSound->GetBlockAlign()));
	m_editABPS->SetValue(wxString::Format(wxT("%d"), _objDSound->GetABPS()));

	double duration = _objDSound->Duration();
	m_sliderPlay->SetMin(0);
	m_sliderPlay->SetMax(duration * 1000);
	m_sliderPlay->SetTickFreq(1000, 0);
	m_sliderPlay->SetValue(0);
	m_sliderPlay->Layout();

	return 0;
}

//////////////////////////////////
BEGIN_EVENT_TABLE(CNotebookM2Panel, wxPanel)
    EVT_BUTTON(IDB_NB_M2_TOX, CNotebookM2Panel::OnBtnToX)
END_EVENT_TABLE();

CNotebookM2Panel::CNotebookM2Panel(wxWindow *parent)
: CNotebookPanel(parent, PT_M2, wxT("Model"), 0)
, _bRemove(true)
{
}

CNotebookM2Panel::~CNotebookM2Panel()
{
}

int CNotebookM2Panel::CreatePanelControls()
{
    notebook_2 = new wxNotebook(this, wxID_ANY, wxDefaultPosition, wxDefaultSize, 0);
    notebook_2_pane_4 = new wxPanel(notebook_2, wxID_ANY);
    notebook_2_pane_2 = new wxPanel(notebook_2, wxID_ANY);
    notebook_2_pane_1 = new wxPanel(notebook_2, wxID_ANY);
    sizer_10_staticbox = new wxStaticBox(this, -1, wxT("Info"));
    sizer_4_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    m_labelFileName = new wxStaticText(this, wxID_ANY, wxEmptyString);
    m_listM2Header = new wxListCtrl(notebook_2_pane_1, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxLC_REPORT|wxSUNKEN_BORDER);
    m_editM2Info = new wxTextCtrl(notebook_2_pane_1, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_MULTILINE|wxTE_READONLY);
    m_listSkinHeader = new wxListCtrl(notebook_2_pane_2, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxLC_REPORT|wxSUNKEN_BORDER);
    m_editSkinInfo = new wxTextCtrl(notebook_2_pane_2, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_MULTILINE|wxTE_READONLY);
    notebook_2_pane_3 = new wxPanel(notebook_2, wxID_ANY);
    m_listTexInfo = new wxListCtrl(notebook_2_pane_4, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxLC_REPORT|wxSUNKEN_BORDER);
    panel_1 = new wxPanel(this, wxID_ANY);
    m_btnToX = new wxButton(this, IDB_NB_M2_TOX, wxT("To &X .."));

	return 0;
}

int CNotebookM2Panel::SetPanelControls()
{
    wxBoxSizer* sizer_2 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_3 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_9 = new wxBoxSizer(wxHORIZONTAL);
    wxStaticBoxSizer* sizer_10 = new wxStaticBoxSizer(sizer_10_staticbox, wxHORIZONTAL);
    wxBoxSizer* sizer_13 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_12 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_11 = new wxBoxSizer(wxHORIZONTAL);
    wxStaticBoxSizer* sizer_4 = new wxStaticBoxSizer(sizer_4_staticbox, wxHORIZONTAL);
    sizer_4->Add(m_labelFileName, 1, wxEXPAND|wxALIGN_CENTER_VERTICAL, 0);
    sizer_3->Add(sizer_4, 0, wxEXPAND, 0);
    sizer_11->Add(m_listM2Header, 1, wxEXPAND, 0);
    sizer_11->Add(m_editM2Info, 1, wxEXPAND, 0);
    notebook_2_pane_1->SetSizer(sizer_11);
    sizer_12->Add(m_listSkinHeader, 1, wxEXPAND, 0);
    sizer_12->Add(m_editSkinInfo, 1, wxEXPAND, 0);
    notebook_2_pane_2->SetSizer(sizer_12);
    sizer_13->Add(m_listTexInfo, 1, wxEXPAND, 0);
    notebook_2_pane_4->SetSizer(sizer_13);
    notebook_2->AddPage(notebook_2_pane_1, wxT("M2 Header"));
    notebook_2->AddPage(notebook_2_pane_2, wxT("Skin Header"));
    notebook_2->AddPage(notebook_2_pane_3, wxT("Anim Header"));
    notebook_2->AddPage(notebook_2_pane_4, wxT("Texture Info"));
    sizer_10->Add(notebook_2, 1, wxEXPAND, 0);
    sizer_3->Add(sizer_10, 1, wxEXPAND, 0);
    sizer_9->Add(panel_1, 1, wxEXPAND, 0);
    sizer_9->Add(m_btnToX, 0, 0, 0);
    sizer_3->Add(sizer_9, 0, wxEXPAND, 0);

	m_listM2Header->InsertColumn(0, wxT("Property"));
	m_listM2Header->InsertColumn(1, wxT("Value"));

	m_listSkinHeader->InsertColumn(0, wxT("Property"));
	m_listSkinHeader->InsertColumn(1, wxT("Value"));

	m_listTexInfo->InsertColumn(0, wxT("Index"));
	m_listTexInfo->InsertColumn(1, wxT("Type"));
	m_listTexInfo->InsertColumn(2, wxT("BLP"));
	m_listTexInfo->InsertColumn(4, wxT("DBC"));
	m_listTexInfo->InsertColumn(5, wxT("MPQ"));

    this->SetSizer(sizer_3);
    this->Layout();
	this->Update();

	return 0;
}

void CNotebookM2Panel::SetFileName(const wxString &str)
{
	m_labelFileName->SetLabel(str);
}

int CNotebookM2Panel::ShowModel(const std::string &mpq, const std::string &m2, const std::string &mpqpath, bool findmpq, bool remove)
{
	if(LoadModel(mpq, m2, mpqpath, findmpq) != 0)
	{
		wxMessageBox(wxT("Model load failed."));
		return -1;
	}

	ShowM2Header();
	ShowSkinHeader();
	ShowTextureInfo();

	return 0;
}

int CNotebookM2Panel::LoadModel(const std::string &mpq, const std::string &m2, const std::string &mpqpath, bool findmpq)
{
	std::wstring m = Toolkit::String2WString(mpq);
	std::wstring mp = Toolkit::String2WString(mpqpath);
	std::string wm2 = m2;
	
	if(_objModel.Init(mp, Toolkit::String2WString(CSTR_DBC_CONFIGUREFILE), Toolkit::String2WString(CSTR_WDB_CONFIGUREFILE)) != 0)// (mp) != 0)
		return -1;

	if(_objModel.LoadModel(m, wm2) != 0)
		return -1;

	return 0;
}

int CNotebookM2Panel::ShowM2Header()
{
	std::vector<std::pair<std::string, std::string> > vct;

	_objModel.OutputM2HeaderInfo(vct);

	m_listM2Header->DeleteAllItems();

	long index = 0;
	for(std::vector<std::pair<std::string, std::string> >::const_iterator it = vct.begin(); it != vct.end(); ++ it, ++ index)
	{
		long l = m_listM2Header->InsertItem(index, Toolkit::String2wxString(it->first));
		m_listM2Header->SetItem(l, 1, Toolkit::String2wxString(it->second));
	}

	return 0;
}

int CNotebookM2Panel::ShowSkinHeader()
{
	std::vector<std::pair<std::string, std::string> > vct;

	_objModel.OutputSkinHeaderInfo(vct);

	m_listSkinHeader->DeleteAllItems();

	long index = 0;
	for(std::vector<std::pair<std::string, std::string> >::const_iterator it = vct.begin(); it != vct.end(); ++ it, ++ index)
	{
		long l = m_listSkinHeader->InsertItem(index, Toolkit::String2wxString(it->first));
		m_listSkinHeader->SetItem(l, 1, Toolkit::String2wxString(it->second));
	}

	return 0;
}

int CNotebookM2Panel::ShowTextureInfo()
{
	std::vector<CTextureObject::Texture_t> vct;
	_objModel .OutputTextureInfo(vct);

	m_listTexInfo->DeleteAllItems();

	long index = 0;
	for(std::vector<CTextureObject::Texture_t>::const_iterator it = vct.begin(); it != vct.end(); ++ it)
	{
		long l = m_listTexInfo->InsertItem(index, wxString::Format(wxT("%d"), index));
		m_listTexInfo->SetItem(l, 1, wxString::Format(wxT("%d"), it->m_uiType));
		m_listTexInfo->SetItem(l, 2, Toolkit::String2wxString(it->m_strPath));
		m_listTexInfo->SetItem(l ,3, Toolkit::String2wxString(it->m_strDBC));
		m_listTexInfo->SetItem(l, 4, Toolkit::String2wxString(Toolkit::WString2String(it->m_strMPQ)));
		++ index;
	}

	return 0;
}

int CNotebookM2Panel::ToX(const std::string &file)
{
	std::wstring f = Toolkit::String2WString(file);
	if(_objModel.ToXFile(f) != 0)
		return -1;
	ShowTextureInfo();
	return 0;
}

void CNotebookM2Panel::OnBtnToX(wxCommandEvent &event)
{
	wxString xfile = m_labelFileName->GetLabel();
	size_t pos = xfile.find_last_of(wxT("\\"));
	if(pos != wxString::npos)
		xfile = xfile.substr(pos + 1);
	xfile += wxT(".x");

	wxFileDialog dlg(this,
						wxT("Save .x file..."),
						wxEmptyString,
						xfile,
						wxT(".X Files (*.x)|*.x"),
						wxFD_SAVE|wxFD_OVERWRITE_PROMPT);
	if (dlg.ShowModal() == wxID_OK)
	{
		if(ToX(Toolkit::wxString2String(dlg.GetPath())) != 0)
			wxMessageBox(wxT("Extract model failed."));
	}

	event.Skip();
}

///////////////////////////////////////////////
BEGIN_EVENT_TABLE(CNotebookWDBPanel, wxPanel)
	EVT_BUTTON(IDB_NB_WDB_GRID_SAVE, CNotebookWDBPanel::OnButtonGridSave)
END_EVENT_TABLE()

CNotebookWDBPanel::CNotebookWDBPanel(wxWindow* parent)
: CNotebookPanel(parent, PT_WDB, wxT("WDB"), 0)
{
}

CNotebookWDBPanel::~CNotebookWDBPanel()
{
}

int CNotebookWDBPanel::CreatePanelControls()
{
    sizer_6_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    sizer_3_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    m_labelFileName = new wxStaticText(this, wxID_ANY, wxEmptyString);
    label_6 = new wxStaticText(this, wxID_ANY, wxT("Signature:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editSignature = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_8 = new wxStaticText(this, wxID_ANY, wxT("ClientVersion:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editClientVersion = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_9 = new wxStaticText(this, wxID_ANY, wxT("Language:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editLanguage = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_7 = new wxStaticText(this, wxID_ANY, wxT("RowSize:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editRowSize = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    label_10 = new wxStaticText(this, wxID_ANY, wxT("Version:"), wxDefaultPosition, wxDefaultSize, wxALIGN_RIGHT);
    m_editVersion = new wxTextCtrl(this, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);
    panel_2 = new wxPanel(this, wxID_ANY);
    //panel_3 = new wxPanel(this, wxID_ANY);
	m_btnGridExport = new wxButton(this, IDB_NB_WDB_GRID_SAVE, wxT("Grid Save ..."));
    m_ctrlGrid = new wxGrid(this, wxID_ANY);

	return 0;
}

int CNotebookWDBPanel::SetPanelControls()
{
	wxBoxSizer* sizer_2 = new wxBoxSizer(wxVERTICAL);
    wxStaticBoxSizer* sizer_6 = new wxStaticBoxSizer(sizer_6_staticbox, wxVERTICAL);
    wxBoxSizer* sizer_8 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_7 = new wxBoxSizer(wxHORIZONTAL);
    wxStaticBoxSizer* sizer_3 = new wxStaticBoxSizer(sizer_3_staticbox, wxHORIZONTAL);
    sizer_3->Add(m_labelFileName, 0, wxEXPAND|wxALIGN_CENTER_VERTICAL, 0);
    sizer_2->Add(sizer_3, 0, wxEXPAND, 0);
    sizer_7->Add(label_6, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_7->Add(m_editSignature, 5, wxEXPAND, 0);
    sizer_7->Add(label_8, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_7->Add(m_editClientVersion, 5, wxEXPAND, 0);
    sizer_7->Add(label_9, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_7->Add(m_editLanguage, 5, wxEXPAND, 0);
    sizer_6->Add(sizer_7, 0, wxTOP|wxBOTTOM|wxEXPAND, 2);
    sizer_8->Add(label_7, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_8->Add(m_editRowSize, 5, wxEXPAND, 0);
    sizer_8->Add(label_10, 4, wxALIGN_CENTER_VERTICAL, 0);
    sizer_8->Add(m_editVersion, 5, wxEXPAND, 0);
    sizer_8->Add(panel_2, 4, wxEXPAND, 0);
    sizer_8->Add(m_btnGridExport, 5, wxEXPAND, 0);// panel_3, 5, wxEXPAND, 0);
    sizer_6->Add(sizer_8, 0, wxEXPAND, 2);
    sizer_2->Add(sizer_6, 0, wxBOTTOM|wxEXPAND, 4);
    sizer_2->Add(m_ctrlGrid, 1, wxEXPAND, 0);
    this->SetSizer(sizer_2);

	label_10->Disable();
	m_editVersion->Disable();

    m_ctrlGrid->CreateGrid(0, 0,wxGrid::wxGridSelectRows);
    m_ctrlGrid->EnableEditing(false);
    m_ctrlGrid->SetGridLineColour(wxColour(0, 0, 255));
//    m_ctrlGrid->SetColLabelValue(0, wxEmptyString);

	this->Layout();

	return 0;
}

void CNotebookWDBPanel::OnButtonGridSave(wxCommandEvent &event)
{
	wxString file = m_labelFileName->GetLabel();
	size_t pos = file.find_last_of(wxT("\\"));
	if(pos != 0)
	{
		file = file.substr(pos + 1);
	}

	wxFileDialog dlg(this,
						wxT("Save WDB file..."),
						wxEmptyString,
						file + wxT(".txt"),
						wxT("Text Files (*.txt)|*.txt"),
						wxFD_SAVE|wxFD_OVERWRITE_PROMPT);
	if (dlg.ShowModal() == wxID_OK)
	{
		std::ofstream ofs(Toolkit::wxString2String(dlg.GetPath()).c_str(), std::ios::out | std::ios::trunc);
		if(ofs.is_open())
		{
			for(int j = 0; j < m_ctrlGrid->GetNumberCols();)
			{
				ofs << Toolkit::wxString2String(m_ctrlGrid->GetColLabelValue(j));
				if(++ j != m_ctrlGrid->GetNumberCols())
					ofs << ",";
				else
					ofs << ";" << std::endl;
			}

			for(int i = 0; i < m_ctrlGrid->GetNumberRows(); ++ i)
			{
				for(int j = 0; j < m_ctrlGrid->GetNumberCols();)
				{
					ofs << Toolkit::wxString2String(m_ctrlGrid->GetCellValue(i, j));
					if(++ j != m_ctrlGrid->GetNumberCols())
						ofs << ",";
					else
						ofs << ";" << std::endl;
				}
			}
			ofs.close();
		}
		else
		{
			wxMessageBox(wxT("Open output file failed."));
		}
	}
	event.Skip();
}

void CNotebookWDBPanel::SetFileName(const wxString &str)
{
	m_labelFileName->SetLabel(str);
//	m_ctrlGrid->SetFileName(str);
}

void CNotebookWDBPanel::SetHeaderInfo(const MPQData::CWDBHeader &header)
{
	wxString str;
	str.sprintf(wxT("%c%c%c%c"), header.m_acSignature[3], header.m_acSignature[2], header.m_acSignature[1], header.m_acSignature[0]);
	m_editSignature->SetValue(str);
	str.sprintf(wxT("%d"), header.m_iClientVersion);
	m_editClientVersion->SetValue(str);
	str.sprintf(wxT("%c%c%c%c"), header.m_acLanguage[3], header.m_acLanguage[2], header.m_acLanguage[1], header.m_acLanguage[0]);
	m_editLanguage->SetValue(str);
	str.sprintf(wxT("%d"), header.m_iRowSize);
//	m_editRowSize->SetValue(str);
//	str.sprintf(wxT("%d"), header.m_iVersion);
	m_editVersion->SetValue(str);
}

void CNotebookWDBPanel::InitGrid(const CMPQDBCFieldManager::TFieldMap* mapField)
{
	m_ctrlGrid->ClearGrid();
	if(m_ctrlGrid->GetNumberCols() > 0)
		m_ctrlGrid->DeleteCols(0, m_ctrlGrid->GetNumberCols());
	if(m_ctrlGrid->GetNumberRows() > 0)
		m_ctrlGrid->DeleteRows(0, m_ctrlGrid->GetNumberRows());
	m_ctrlGrid->AppendCols(mapField->size());

	for(CMPQDBCFieldManager::TFieldMap::const_iterator it = mapField->begin(); it != mapField->end(); ++ it)
	{
		m_ctrlGrid->SetColLabelValue(it->first, Toolkit::String2wxString(it->second->m_stAttr.m_strTitle));
	}
}

void CNotebookWDBPanel::AppendRow()
{
	m_ctrlGrid->AppendRows(1);
}

void CNotebookWDBPanel::SetGridData(int row, int col, const wxString& data)
{
	m_ctrlGrid->SetCellValue(row, col, data);
}