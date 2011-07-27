
#include "wxDeMPQ.h"

IMPLEMENT_APP(CwxDeMPQApp)

bool CwxDeMPQApp::OnInit()
{
	CMainFrame* MainFrame = new CMainFrame(NULL, wxID_ANY, wxEmptyString);
    MainFrame->Show();
	wxInitAllImageHandlers(); 

    SetTopWindow(MainFrame);
	return true;
}