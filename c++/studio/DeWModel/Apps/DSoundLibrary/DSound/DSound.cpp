// -*- C++ -*- generated by wxGlade 0.6.3 on Thu Mar 19 17:25:59 2009

#include <string>

#include "DSound.h"

//#include "DSWaveObject.h"

// begin wxGlade: ::extracode
// end wxGlade



CDSoundFrame::CDSoundFrame(wxWindow* parent, int id, const wxString& title, const wxPoint& pos, const wxSize& size, long style):
    wxFrame(parent, id, title, pos, size, wxCAPTION|wxCLOSE_BOX|wxSYSTEM_MENU)
{
    // begin wxGlade: CDSoundFrame::CDSoundFrame
    label_1 = new wxStaticText(this, wxID_ANY, wxT("WAVE File : "));
    m_editFile = new wxTextCtrl(this, wxID_ANY, wxEmptyString);
    m_btnBrowse = new wxButton(this, IDB_BROWSE, wxT("&Browse.."));
    panel_1 = new wxPanel(this, wxID_ANY);
    m_btnPlay = new wxButton(this, IDB_PLAY, wxT("&Play"));
	m_btnPause = new wxButton(this, IDB_PAUSE, wxT("P&ause/Cont."));
    m_btnStop = new wxButton(this, IDB_STOP, wxT("&Stop"));
    panel_2 = new wxPanel(this, wxID_ANY);
    m_btnExit = new wxButton(this, IDB_EXIT, wxT("E&xit"));

    set_properties();
    do_layout();
    // end wxGlade
	obj = NULL;
}


BEGIN_EVENT_TABLE(CDSoundFrame, wxFrame)
    // begin wxGlade: CDSoundFrame::event_table
    EVT_BUTTON(IDB_BROWSE, CDSoundFrame::OnBtnBrowse)
    EVT_BUTTON(IDB_PLAY, CDSoundFrame::OnBtnPlay)
    EVT_BUTTON(IDB_STOP, CDSoundFrame::OnBtnStop)
	EVT_BUTTON(IDB_EXIT, CDSoundFrame::OnBtnExit)
	EVT_BUTTON(IDB_PAUSE, CDSoundFrame::OnBtnPause)
    // end wxGlade
END_EVENT_TABLE();


void CDSoundFrame::OnBtnBrowse(wxCommandEvent &event)
{
	wxFileDialog dlg(this,
						wxT("Open Wav File.."),
						wxEmptyString,
						_T("*.wav;*mp3"),
						_T("Wave Files (*.wav;*.mp3)|*.wav;*.mp3|All Files (*.*)|*.*"),
						wxFD_OPEN|wxFD_FILE_MUST_EXIST);
	if (dlg.ShowModal() == wxID_OK)
	{
		file = dlg.GetPath();
		m_editFile->SetValue(file);

//		PlayFile(file);
	}

    event.Skip();
}


void CDSoundFrame::OnBtnPlay(wxCommandEvent &event)
{
	PlayFile(file);
	obj->SetVolume(-2000);
	obj->Play();

    event.Skip();
}

void CDSoundFrame::OnBtnPause(wxCommandEvent &event)
{
	if(obj->IsReady())
	{
		if(obj->IsPlaying())
			obj->Pause();
		else
			obj->Play();
	}
    event.Skip();
}

void CDSoundFrame::OnBtnStop(wxCommandEvent &event)
{
	obj->Stop();
    event.Skip();
}


void CDSoundFrame::OnBtnExit(wxCommandEvent &event)
{
	Close();
    event.Skip();
}


// wxGlade: add CDSoundFrame event handlers


void CDSoundFrame::set_properties()
{
    // begin wxGlade: CDSoundFrame::set_properties
    SetTitle(wxT("DSound - WAVE/MP3"));
    SetSize(wxSize(374, 122));
    SetBackgroundColour(wxSystemSettings::GetColour(wxSYS_COLOUR_3DFACE));
    // end wxGlade
}


void CDSoundFrame::do_layout()
{
    // begin wxGlade: CDSoundFrame::do_layout
    wxBoxSizer* sizer_1 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_2 = new wxBoxSizer(wxVERTICAL);
    wxBoxSizer* sizer_4 = new wxBoxSizer(wxHORIZONTAL);
    wxBoxSizer* sizer_3 = new wxBoxSizer(wxHORIZONTAL);
    sizer_3->Add(label_1, 0, wxALIGN_CENTER_VERTICAL, 0);
    sizer_3->Add(m_editFile, 1, wxLEFT|wxRIGHT|wxEXPAND|wxALIGN_CENTER_VERTICAL, 4);
    sizer_3->Add(m_btnBrowse, 0, wxALIGN_CENTER_VERTICAL, 0);
    sizer_2->Add(sizer_3, 0, wxALL|wxEXPAND, 8);
    sizer_4->Add(panel_1, 1, wxEXPAND, 0);
	sizer_4->Add(m_btnPlay, 0, 0, 0);
	sizer_4->Add(m_btnPause, 0, 0, 0);
    sizer_4->Add(m_btnStop, 0, 0, 0);
    sizer_4->Add(panel_2, 1, wxEXPAND, 0);
    sizer_4->Add(m_btnExit, 0, 0, 0);
    sizer_2->Add(sizer_4, 0, wxALL|wxEXPAND, 8);
    sizer_1->Add(sizer_2, 0, wxEXPAND, 0);
    SetSizer(sizer_1);
    Layout();
    Centre();
    // end wxGlade
}

int CDSoundFrame::PlayFile(const wxString &file)
{
	wchar_t ch[1024];
//		strcpy(ch, (const char*)m_editFile->GetValue().mb_str(wxConvUTF8));
	wcscpy(ch, file.wchar_str());
	std::wstring wstr(ch);

	if(obj != NULL)
		delete obj, obj = NULL;

	//std::string str = std::string(ch);
	std::wstring::size_type pos = wstr.find_last_of(L".");
	if(pos == std::string::npos)
		return -1;
	wstr = wstr.substr(pos);
	if(wstr == L".wav")
		obj = new CDSWaveObject();
	else if(wstr == L".mp3")
		obj = new CDSMP3Object();
	else
		return -1;
	obj->Init((HWND)this->GetHandle());
	if(obj->LoadFile(std::wstring(ch)) != 0)
	{
		wxMessageBox(wxT("Load file failed."));
		return -1;
	}
	return 0;
}



class CDSoundApp: public wxApp {
public:
    bool OnInit();
};

IMPLEMENT_APP(CDSoundApp)

bool CDSoundApp::OnInit()
{
    wxInitAllImageHandlers();
    CDSoundFrame* DSound = new CDSoundFrame(NULL, wxID_ANY, wxEmptyString);
    SetTopWindow(DSound);
    DSound->Show();
    return true;
}
