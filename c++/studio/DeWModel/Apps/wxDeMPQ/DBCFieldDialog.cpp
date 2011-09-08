// -*- C++ -*- generated by wxGlade 0.6.3 on Thu Apr 23 21:11:46 2009

#include "Consts.h"
#include "Toolkit.h"
#include "MPQDBCFieldObject.h"

#include "DBCFieldDialog.h"

// begin wxGlade: ::extracode
// end wxGlade


BEGIN_EVENT_TABLE(CDBCFieldDialog, wxDialog)
	EVT_CLOSE(CDBCFieldDialog::OnClose)
	EVT_LISTBOX(IDC_DBCCONFIG_CHECKLIST, CDBCFieldDialog::OnListDBCSelected)
	EVT_CHECKLISTBOX(IDC_DBCCONFIG_CHECKLIST, CDBCFieldDialog::OnListDBCChecked)
END_EVENT_TABLE();

CDBCFieldDialog::CDBCFieldDialog(wxWindow* parent, int id, const wxString& title, const wxPoint& pos, const wxSize& size, long style):
    wxDialog(parent, id, title, pos, size, wxDEFAULT_DIALOG_STYLE|wxRESIZE_BORDER|wxTHICK_FRAME)
, _dbcManager(NULL)
{
    // begin wxGlade: CDBCFieldDialog::CDBCFieldDialog
    window_1 = new wxSplitterWindow(this, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxSP_3DSASH);
    window_1_pane_2 = new wxPanel(window_1, wxID_ANY);
    sizer_1_staticbox = new wxStaticBox(this, -1, wxEmptyString);
    window_1_pane_1 = new wxPanel(window_1, wxID_ANY);
    const wxString *m_listDBC_choices = NULL;
    m_listDBC = new wxCheckListBox(window_1_pane_1, IDC_DBCCONFIG_CHECKLIST, wxDefaultPosition, wxDefaultSize, 0, m_listDBC_choices, 0);
    m_listDetail = new wxListCtrl(window_1_pane_2, wxID_ANY, wxDefaultPosition, wxDefaultSize, wxLC_REPORT|wxSUNKEN_BORDER);
    m_editNote = new wxTextCtrl(window_1_pane_2, wxID_ANY, wxEmptyString, wxDefaultPosition, wxDefaultSize, wxTE_READONLY);

    set_properties();
    do_layout();
    // end wxGlade
}


void CDBCFieldDialog::set_properties()
{
    // begin wxGlade: CDBCFieldDialog::set_properties
    SetTitle(wxT("DBC Field Info.."));	
	m_listDetail->InsertColumn(1, wxT("Position"));
//	m_listDetail->SetColumnWidth(1, -1);
	m_listDetail->InsertColumn(2, wxT("Field Title"));
//	m_listDetail->SetColumnWidth(2, -1);
	m_listDetail->InsertColumn(3, wxT("Data Type"));
//	m_listDetail->SetColumnWidth(3, -1);
	m_listDetail->InsertColumn(4, wxT("Field Size"));
//	m_listDetail->SetColumnWidth(4, -1);
	m_listDetail->InsertColumn(4, wxT("SkipByte"));

    // end wxGlade
}


void CDBCFieldDialog::do_layout()
{
    // begin wxGlade: CDBCFieldDialog::do_layout
    wxStaticBoxSizer* sizer_1 = new wxStaticBoxSizer(sizer_1_staticbox, wxHORIZONTAL);
    wxBoxSizer* sizer_3 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_2 = new wxBoxSizer(wxHORIZONTAL);
    sizer_2->Add(m_listDBC, 1, wxEXPAND, 0);
    window_1_pane_1->SetSizer(sizer_2);
    sizer_3->Add(m_listDetail, 1, wxEXPAND, 0);
    sizer_3->Add(m_editNote, 0, wxEXPAND|wxALIGN_CENTER_VERTICAL, 0);
    window_1_pane_2->SetSizer(sizer_3);
    window_1->SplitVertically(window_1_pane_1, window_1_pane_2, 135);
	window_1->SetMinimumPaneSize(100);
    sizer_1->Add(window_1, 1, wxALL|wxEXPAND, 2);
    SetSizer(sizer_1);
    sizer_1->Fit(this);
	SetSize(wxSize(485, 347));

    Layout();
    // end wxGlade
}

void CDBCFieldDialog::OnClose(wxCloseEvent& event)
{
	Hide();
	event.Skip();
}

int CDBCFieldDialog::ShowDBCInfo(CMPQDBCFieldManager &manager)
{
	m_listDBC->Clear();
	m_listDetail->DeleteAllItems();
	m_editNote->Clear();

	for(CMPQDBCFieldManager::TDBCMap::const_iterator it = manager._mapDBC.begin(); it != manager._mapDBC.end(); ++ it)
	{
		m_listDBC->Append(Toolkit::String2wxString(it->first));
		m_listDBC->Check(m_listDBC->GetCount() - 1, it->second.m_bValid);
	}

	_dbcManager = &manager;

	return 0;
}

int CDBCFieldDialog::ShowDBCFieldInfo(const wxString& dbc)
{
	if(_dbcManager == NULL)
		return -1;
	m_listDetail->DeleteAllItems();
	m_editNote->Clear();

	bool valid = false;
	std::string notes;
	const CMPQDBCFieldManager::TFieldMap* mapfield = _dbcManager->FindDBCFields(Toolkit::wxString2String(dbc), valid, notes);
	if(mapfield == NULL)
		return -1;
	long i = 0;
	wxString str;
	for(CMPQDBCFieldManager::TFieldMap::const_iterator it = mapfield->begin(); it != mapfield->end(); ++ it)
	{
		long l = m_listDetail->InsertItem(i ++, str.Format(wxT("%d"), it->first));
		m_listDetail->SetItem(l, 1, Toolkit::String2wxString(it->second->m_stAttr.m_strTitle));
		//switch(it->second->m_eType)
		//{
		//case DBCField::FT_INTEGER:
		//	m_listDetail->SetItem(l, 2, wxT("Integer"));
		//	break;
		//case DBCField::FT_STRING:
		//	m_listDetail->SetItem(l, 2, wxT("String"));
		//	break;
		//case DBCField::FT_FLOAT:
		//	m_listDetail->SetItem(l, 2, wxT("Float"));
		//	break;
		//case DBCField::FT_BIT:
		//	m_listDetail->SetItem(l, 2, wxT("Bit"));
		//	break;
		//case DBCField::FT_BYTE:
		//	m_listDetail->SetItem(l, 2, wxT("Byte"));
		//	break;
		//case DBCField::FT_CSTRING:
		//	m_listDetail->SetItem(l, 2, wxT("CString"));
		//	break;
		//default:
		//	m_listDetail->SetItem(l, 2, wxT("Unknown"));
		//}
		m_listDetail->SetItem(l, 2, Toolkit::String2wxString(it->second->m_stAttr.m_strType));
		m_listDetail->SetItem(l, 3, str.Format(wxT("%d"), it->second->m_stAttr.m_iSize));
		m_listDetail->SetItem(l, 4, str.Format(wxT("%d"), it->second->m_stAttr.m_iSkipByte));
	}
	m_editNote->SetValue(Toolkit::String2wxString(notes));
	return 0;
}

void CDBCFieldDialog::OnListDBCSelected(wxCommandEvent &event)
{
	ShowDBCFieldInfo(m_listDBC->GetStringSelection());
	event.Skip();
}

void CDBCFieldDialog::OnListDBCChecked(wxCommandEvent &event)
{
	if(_dbcManager != NULL)
	{
		unsigned int i = event.GetInt();
		_dbcManager->SetValid(Toolkit::wxString2String(m_listDBC->GetString(i)), m_listDBC->IsChecked(i));
	}
	event.Skip();
}

