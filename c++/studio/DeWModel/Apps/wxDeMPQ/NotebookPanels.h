#ifndef __NOTEBOOKPANELS_H__
#define __NOTEBOOKPANELS_H__

#include <map>

#include "wx/timer.h"
//#include "wx/grid.h"


#include "DSObject.h"
#include "ModelObject.h"

#include "MPQDataObject.h"
#include "MPQDBCFieldObject.h"

class wxToolbook;
class wxPanel;
class wxStaticBox;
class wxStaticText;
class wxTextCtrl;
class wxButton;
class wxString;
class wxListCtrl;
class wxNotebook;
class wxGrid;

class CNotebookPanel;

enum PanelType { PT_INFO = 0, PT_DATA, PT_DBC, PT_BLP, PT_WAVE, PT_M2, PT_WDB };

class CNotebookPanelManager
{
private:
	struct PanelData_t
	{
		PanelData_t(size_t pos, CNotebookPanel* panel)
			: m_szPos(pos), m_objPanel(panel)
		{
		}
		size_t m_szPos;
		CNotebookPanel* m_objPanel;
	};
	typedef std::map<PanelType, PanelData_t> TPanelMap;
public:
	CNotebookPanelManager();
	virtual ~CNotebookPanelManager();

	int InitPanels(wxToolbook* notebook);
	
	CNotebookPanel* ShowPanel(PanelType type, bool show, bool selected = true);

	CNotebookPanel* GetPanel(PanelType type);
protected:
	void DestoryPanels();
private:
	wxToolbook* _ctrlNotebook;
	TPanelMap _mapPanel;
};

class CNotebookPanel : public wxPanel
{
public:
	CNotebookPanel(wxWindow* parent, PanelType type, const wxString& title, int image = 0, int id = wxID_ANY);
	virtual ~CNotebookPanel();

	int InitControls();
	
	virtual void SetFileName(const wxString& str) {}
protected:
	virtual int CreatePanelControls() = 0;
	virtual int SetPanelControls() = 0;
public:
	PanelType m_eType;
	wxString m_strTitle;
	int m_iImageID;
};

class CNotebookInfoPanel : public CNotebookPanel
{
public:
	CNotebookInfoPanel(wxWindow* parent);
	virtual ~CNotebookInfoPanel();

	void DisplayInfo(const MPQData::TFileData *data);
protected:
	virtual int CreatePanelControls();
	virtual int SetPanelControls();
private:
	wxStaticBox* sizer_16_staticbox;
	wxStaticBox* sizer_6_staticbox;
	wxStaticText* m_labelFileName;
	wxStaticText* label_8;
    wxTextCtrl* m_editInfoFileName;
    wxStaticText* label_9;
    wxTextCtrl* m_editInfoOffset;
    wxStaticText* label_10;
    wxTextCtrl* m_editInfoVersion;
    wxStaticText* label_11;
    wxTextCtrl* m_editInfoSize;
    wxStaticText* label_12;
    wxTextCtrl* m_editInfoFlag;
    wxStaticText* label_13;
    wxTextCtrl* m_editInfoIndex;
    wxStaticText* label_14;
    wxTextCtrl* m_editInfoCompSize;
    wxButton* m_btnInfoExport;
    wxPanel* panel_1;
};

class CNotebookDataPanel : public CNotebookPanel
{
public:
	CNotebookDataPanel(wxWindow* parent);
	virtual ~CNotebookDataPanel();

	virtual void SetFileName(const wxString& str);
	
	bool CheckDataFileName(const wxString& file) const;

	void Refresh(const wxString& str);
	void AppendEnd();
	void AppendData(const wxString& str);
protected:
	virtual int CreatePanelControls();
	virtual int SetPanelControls();
private:
	wxStaticBox* sizer_15_staticbox;
    wxStaticText* m_labelDataFileName;
    wxTextCtrl* m_ctrlDataText;
};


class CNotebookDBCPanel : public CNotebookPanel
{
//protected:
//	class CMyGrid : public wxGrid
//	{
//	public:
//		CMyGrid(wxWindow* parent, wxWindowID id)
//			:wxGrid(parent, id)
//		{
//		}
//		void SetFileName(const wxString& str) { _strFileName = str; }
//	public:
//		virtual void OnContextMenu(wxContextMenuEvent& event);
//		virtual void OnMenuGridSave(wxCommandEvent& event);
//	private:
//		DECLARE_EVENT_TABLE()
//	private:
//		wxString _strFileName;
//	};
public:
	CNotebookDBCPanel(wxWindow* parent);
	virtual ~CNotebookDBCPanel();

	virtual void SetFileName(const wxString& str);
	void SetHeaderInfo(const MPQData::CDBCHeader& header);
	void InitGrid(int fields, const CMPQDBCFieldManager::TFieldMap* mapField);
	void AppendRow();
	void SetGridData(int row, int col, const wxString& data);
protected:
	virtual int CreatePanelControls();
	virtual int SetPanelControls();
private:
    wxStaticBox* sizer_6_staticbox;
    wxStaticBox* sizer_3_staticbox;
    wxStaticText* m_labelFileName;
    wxStaticText* label_6;
    wxTextCtrl* m_editSignature;
    wxStaticText* label_8;
    wxTextCtrl* m_editRecords;
    wxStaticText* label_9;
    wxTextCtrl* m_editFields;
    wxStaticText* label_7;
    wxTextCtrl* m_editRecordSize;
    wxStaticText* label_10;
    wxTextCtrl* m_editStringSize;
    wxPanel* panel_2;
    //wxPanel* panel_3;
	wxButton* m_btnGridExport;
    wxGrid* m_ctrlGrid;
private:
	virtual void OnButtonGridSave(wxCommandEvent& event);

	DECLARE_EVENT_TABLE()
};

class CNotebookBLPPanel : public CNotebookPanel
{
protected:
	class CScrolledBitmapCanvas : public wxScrolledWindow
	{
	public:
		CScrolledBitmapCanvas(wxWindow *parent, wxWindowID id, const wxPoint &pos, const wxSize &size);
		virtual ~CScrolledBitmapCanvas();

		void OnPaint(wxPaintEvent &event);
		//void OnRButtonUp(wxMouseEvent& event);
		void OnContextMenu(wxContextMenuEvent& event);
		void OnMenuBitmapSave(wxCommandEvent& event);

		int SetBitmap(const wxString& file, wxSize& size);

		int InitPNGHandler();
	protected:
		wxString _strFileName;
		wxBitmap _bitmapPNG;
	private:
		DECLARE_EVENT_TABLE()
	};
public:
	CNotebookBLPPanel(wxWindow* parent);
	virtual ~CNotebookBLPPanel();

	virtual void SetFileName(const wxString& str);
	int ShowImage(const wxString& file);
protected:
	virtual int CreatePanelControls();
	virtual int SetPanelControls();
private:
    wxStaticBox* sizer_3_staticbox;
    wxStaticText* m_labelFileName;
    CScrolledBitmapCanvas* m_canvasBitmap;
};


class CNotebookWavePanel : public CNotebookPanel
{
public:
	CNotebookWavePanel(wxWindow* parent);
	virtual ~CNotebookWavePanel();

	virtual void SetFileName(const wxString& str);
	
	int PlayFile(const wxString& file, bool remove);
private:
	int LoadWaveFile(const wxString& file);
	int LoadMP3File(const wxString& file);
	int RemoveFile(const wxString& file);
protected:
	virtual int CreatePanelControls();
	virtual int SetPanelControls();
protected:
	int ShowInfo();
	int Play();
	int Pause();
	int Stop();

	void Release();
private:
	CDSoundObject* _objDSound;
	wxString _strFileName;
	bool _bRemove;
	wxTimer _timerPlay;
private:
    wxStaticBox* sizer_6_staticbox;
    wxStaticBox* sizer_1_staticbox;
    wxStaticBox* sizer_23_staticbox;
    wxStaticText* m_labelFileName;
    wxStaticText* m_labelFormat;
    wxTextCtrl* m_editFormat;
    wxStaticText* m_labelChannels;
    wxTextCtrl* m_editChannels;
    wxStaticText* m_labelSamples;
    wxTextCtrl* m_editSamples;
    wxStaticText* m_labelBPS;
    wxTextCtrl* m_editBPS;
    wxStaticText* m_labelBlockAlign;
    wxTextCtrl* m_editBAlign;
    wxStaticText* m_labelABPS;
    wxTextCtrl* m_editABPS;
    wxSlider* m_sliderPlay;
    wxPanel* panel_5;
    wxPanel* panel_2;
    wxButton* m_btnPlay;
    wxButton* m_btnPause;
    wxButton* m_btnStop;
    wxPanel* panel_4;
    wxPanel* panel_3;
    wxStaticText* label_7;
    wxSlider* m_sliderVolume;
    wxPanel* panel_1;

    DECLARE_EVENT_TABLE();
public:
    virtual void OnSliderPlayRelease(wxScrollEvent &event); // wxGlade: <event_handler>
    virtual void OnBtnPlay(wxCommandEvent &event); // wxGlade: <event_handler>
    virtual void OnBtnPause(wxCommandEvent &event); // wxGlade: <event_handler>
    virtual void OnBtnStop(wxCommandEvent &event); // wxGlade: <event_handler>
    virtual void OnSliderVolumeRelease(wxCommandEvent &event); // wxGlade: <event_handler>
	virtual void OnTimer(wxTimerEvent& event);
};

///
class CNotebookM2Panel : public CNotebookPanel
{
public:
	CNotebookM2Panel(wxWindow* parent);
	virtual ~CNotebookM2Panel();

	virtual void SetFileName(const wxString& str);

	int ShowModel(const std::string& mpq, const std::string& m2, const std::string& mpqpath, bool findmpq, bool remove);
protected:
	virtual int CreatePanelControls();
	virtual int SetPanelControls();
private:
	int LoadModel(const std::string& mpq, const std::string& m2, const std::string& mpqpath, bool findmpq);
	int ShowM2Header();
	int ShowSkinHeader();
	int ShowTextureInfo();
	int ToX(const std::string& file);
private:
	CModelObject _objModel;
	bool _bRemove;
private:
    wxStaticBox* sizer_10_staticbox;
    wxStaticBox* sizer_4_staticbox;
    wxStaticText* m_labelFileName;
    wxListCtrl* m_listM2Header;
    wxTextCtrl* m_editM2Info;
    wxPanel* notebook_2_pane_1;
    wxListCtrl* m_listSkinHeader;
    wxTextCtrl* m_editSkinInfo;
    wxPanel* notebook_2_pane_2;
    wxPanel* notebook_2_pane_3;
    wxListCtrl* m_listTexInfo;
    wxPanel* notebook_2_pane_4;
    wxNotebook* notebook_2;
    wxPanel* panel_1;
    wxButton* m_btnToX;

    DECLARE_EVENT_TABLE();
public:
    virtual void OnBtnToX(wxCommandEvent &event); // wxGlade: <event_handler>
};

//

class CNotebookWDBPanel : public CNotebookPanel
{
public:
	CNotebookWDBPanel(wxWindow* parent);
	virtual ~CNotebookWDBPanel();

	virtual void SetFileName(const wxString& str);
	void SetHeaderInfo(const MPQData::CWDBHeader& header);
	void InitGrid(const CMPQDBCFieldManager::TFieldMap* mapField);
	void AppendRow();
	void SetGridData(int row, int col, const wxString& data);
protected:
	virtual int CreatePanelControls();
	virtual int SetPanelControls();
private:
    wxStaticBox* sizer_6_staticbox;
    wxStaticBox* sizer_3_staticbox;
    wxStaticText* m_labelFileName;
    wxStaticText* label_6;
    wxTextCtrl* m_editSignature;
    wxStaticText* label_8;
    wxTextCtrl* m_editClientVersion;
    wxStaticText* label_9;
    wxTextCtrl* m_editLanguage;
    wxStaticText* label_7;
    wxTextCtrl* m_editRowSize;
    wxStaticText* label_10;
    wxTextCtrl* m_editVersion;
    wxPanel* panel_2;
    //wxPanel* panel_3;
	wxButton* m_btnGridExport;
    wxGrid* m_ctrlGrid;
private:
	virtual void OnButtonGridSave(wxCommandEvent& event);

	DECLARE_EVENT_TABLE()
};

#endif
