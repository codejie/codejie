// -*- C++ -*- generated by wxGlade 0.6.3 on Thu Apr 23 21:11:46 2009

#include <wx/wx.h>
#include <wx/image.h>
// begin wxGlade: ::dependencies
#include <wx/splitter.h>
#include <wx/checklst.h>
#include <wx/listctrl.h>
// end wxGlade


#ifndef DBCFIELDDIALOG_H
#define DBCFIELDDIALOG_H


// begin wxGlade: ::extracode
// end wxGlade

class CMPQDBCFieldManager;

class CDBCFieldDialog: public wxDialog {
public:
    // begin wxGlade: CDBCFieldDialog::ids
    // end wxGlade

    CDBCFieldDialog(wxWindow* parent, int id, const wxString& title = wxEmptyString, const wxPoint& pos=wxDefaultPosition, const wxSize& size=wxDefaultSize, long style=wxDEFAULT_DIALOG_STYLE);

	int ShowDBCInfo(CMPQDBCFieldManager& manager);
private:
    // begin wxGlade: CDBCFieldDialog::methods
    void set_properties();
    void do_layout();
    // end wxGlade

	int ShowDBCFieldInfo(const wxString& dbc);
protected:
    // begin wxGlade: CDBCFieldDialog::attributes
    wxStaticBox* sizer_1_staticbox;
    wxCheckListBox* m_listDBC;
    wxPanel* window_1_pane_1;
    wxListCtrl* m_listDetail;
    wxTextCtrl* m_editNote;
    wxPanel* window_1_pane_2;
    wxSplitterWindow* window_1;
    // end wxGlade

private:
	virtual void OnClose(wxCloseEvent& event);
    virtual void OnListDBCSelected(wxCommandEvent &event); // wxGlade: <event_handler>
	virtual void OnListDBCChecked(wxCommandEvent& event);

    DECLARE_EVENT_TABLE();
private:
	CMPQDBCFieldManager* _dbcManager;
}; // wxGlade: end class


#endif // DBCFIELDDIALOG_H