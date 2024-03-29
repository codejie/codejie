// -*- C++ -*- generated by wxGlade 0.6.3 on Thu Mar 19 17:25:59 2009

#include <wx/wx.h>
#include <wx/image.h>
// begin wxGlade: ::dependencies
// end wxGlade


#ifndef DSOUND_H
#define DSOUND_H


#include "DSWaveObject.h"
#include "DSMP3Object.h"

// begin wxGlade: ::extracode
// end wxGlade

const int ID_BASE	=	100;
const int IDB_BROWSE	=	ID_BASE + 1;
const int IDB_PLAY		=	ID_BASE + 2;
const int IDB_STOP		=	ID_BASE + 3;
const int IDB_EXIT		=	ID_BASE + 4;
const int IDB_PAUSE		=	ID_BASE + 5;

class CDSoundFrame: public wxFrame {
public:
    // begin wxGlade: CDSoundFrame::ids
    // end wxGlade

    CDSoundFrame(wxWindow* parent, int id, const wxString& title, const wxPoint& pos=wxDefaultPosition, const wxSize& size=wxDefaultSize, long style=wxDEFAULT_FRAME_STYLE);

private:
    // begin wxGlade: CDSoundFrame::methods
    void set_properties();
    void do_layout();
    // end wxGlade

protected:
    // begin wxGlade: CDSoundFrame::attributes
    wxStaticText* label_1;
    wxTextCtrl* m_editFile;
    wxButton* m_btnBrowse;
    wxPanel* panel_1;
    wxButton* m_btnPlay;
	wxButton* m_btnPause;
    wxButton* m_btnStop;
    wxPanel* panel_2;
    wxButton* m_btnExit;
    // end wxGlade

    DECLARE_EVENT_TABLE();

public:
    virtual void OnBtnBrowse(wxCommandEvent &event); // wxGlade: <event_handler>
    virtual void OnBtnPlay(wxCommandEvent &event); // wxGlade: <event_handler>
	virtual void OnBtnPause(wxCommandEvent &event); // wxGlade: <event_handler>
    virtual void OnBtnStop(wxCommandEvent &event); // wxGlade: <event_handler>
    virtual void OnBtnExit(wxCommandEvent &event); // wxGlade: <event_handler>
private:
	int PlayFile(const wxString& file);
private:
	CDSoundObject* obj;
	long vol;
	wxString file;
}; // wxGlade: end class


#endif // DSOUND_H
