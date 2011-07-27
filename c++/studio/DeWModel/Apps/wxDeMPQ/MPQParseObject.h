#ifndef __MPQPARSEOBJECT_H__
#define __MPQPARSEOBJECT_H__

#include <map>
#include <fstream>
#include <string>

#include "wx/treebase.h"

#include "FileBuffer.h"
#include "MPQDataObject.h"

class wxTreeCtrl;

class CMPQTreeObject
{
public:
	enum TreeStyle { TS_CATEGORY, TS_DIRECTORY } ;
	enum FileType { FT_WORLD, FT_GRAPHIC, FT_DATABASE, FT_ADDONS, FT_MUSIC, FT_UNDEF, FT_UNKNOWN };
	enum SuffixType {
						ST_WDT, ST_WDL, ST_ADT,
						ST_BLP, ST_M2, ST_WMO, ST_BLS, ST_TRS, ST_LIT, ST_ANIM, ST_SKIN, ST_JPEG, ST_GIF, 
						ST_WDB,	ST_DBC, ST_DB,
						ST_XSD, ST_XML, ST_TOC, ST_WTF, ST_LUA, ST_HTML, ST_CSS, ST_PDF, ST_JS, ST_URL,
						ST_WAV, ST_MP3,
						ST_ZMP, ST_WLM, ST_WLQ, ST_WLW,	ST_DLL, ST_EXE,
						ST_UNKNOWN
					};

	struct ItemData_t
	{
		wxTreeItemId m_id;
		wxString m_caption;
		int m_image;
		size_t m_count;
	};

	typedef std::map<FileType, ItemData_t> TFileItemIDMap;
	typedef std::map<SuffixType, ItemData_t> TSuffixItemIDMap;
	typedef std::map<unsigned int, MPQData::TFileData> TItemIDDataMap;
public:
	CMPQTreeObject();
	virtual ~CMPQTreeObject();

	void AttachTreeCtrl(wxTreeCtrl* tree);

	bool SetTreeStyle(TreeStyle type);
	
	void Refresh(const wxString& root);
	void Append(const MPQData::TFileData& data, bool show = false);
	void AppendOver(unsigned int count);

	const MPQData::TFileData* GetData(unsigned int id) const;

	SuffixType GetSuffixType(const wxString& suffix) const;
	int GetSuffixTypeImage(SuffixType type) const;
private:
	void DataClear();
	void DataAppend(const wxTreeItemId& id, const MPQData::TFileData& data);

	void TreeClear();
	void TreeDataInit(const wxString& root);
	wxTreeItemId TreeAppend(const wxTreeItemId& parent, SuffixType type, const wxString& caption, bool show = false);

	wxTreeItemId GetTypeItemID(SuffixType suffix);
	wxTreeItemId GetSuffixItemID(const wxString& caption, SuffixType& type);
	const wxString GetItemCaption(const ItemData_t& data) const;

	wxTreeItemId GetPathItemID(const wxString& caption, wxString& file);
//	wxTreeItemId TreeAppend(const wxTreeItemId& parent, const wxString& file, bool show = false);
private:
	wxTreeCtrl* _ctrlTree;
	TFileItemIDMap _mapFileItemID;
	TSuffixItemIDMap _mapSuffixItemID;
	TItemIDDataMap _mapItemIDData;
private:
	TreeStyle _eTreeStyle;
};

class CNotebookDataPanel;

class CMPQDataObject
{
public:
	CMPQDataObject();
	virtual ~CMPQDataObject();

	void AttachDataPanel(CNotebookDataPanel* panel);
	
	void ReadBegin(const wxString& file);
	void ReadEnd();
	void Read(const char *data, size_t size);
private:
	CNotebookDataPanel* _objPanel;
	unsigned int _uiLineCount;
};
/////////
enum MPQFileType { MFT_DBC = 0, MFT_BLP, MFT_WAVE, MFT_M2, MFT_WDB };

class CNotebookPanel;
class CMPQFileObject
{
public:
	CMPQFileObject(MPQFileType type);
	virtual ~CMPQFileObject();

	virtual void AttachPanel(CNotebookPanel* panel);
	virtual int LoadFile(const std::string& file, bool remove = true);

	const std::string& FileName() const;
protected:
	virtual void RemoveCache();
public:
	MPQFileType m_eType;
protected:
	CNotebookPanel* _objPanel;
	CFileBuffer _objBuffer;
	std::string _strFileName;
	bool _bFileRemove;
};

class CNotebookDBCPanel;
class CMPQDBCFieldManager;
class CMPQDBCObject : public CMPQFileObject
{
public:
	CMPQDBCObject();
	virtual ~CMPQDBCObject();

	virtual void AttachPanel(CNotebookPanel* panel);

	int ParseInfo();
	int ParseData(const CMPQDBCFieldManager& manager);
private:
	CNotebookDBCPanel* _objDBCPanel;
	MPQData::CDBCHeader _stHeader;
};
///
class CNotebookBLPPanel;
class CMPQBLPObject : public CMPQFileObject
{
public:
	CMPQBLPObject();
	virtual ~CMPQBLPObject();

	virtual void AttachPanel(CNotebookPanel* panel);
	virtual int LoadFile(const std::string& file, bool remove = true);

	int ShowImage();protected:
	virtual void RemoveCache();
private:
	CNotebookBLPPanel* _objBLPPanel;
	std::string _strPNGFileName;
};

////
class CNotebookWavePanel;
class CMPQWaveObject : public CMPQFileObject
{
public:
	CMPQWaveObject();
	virtual ~CMPQWaveObject();

	virtual void AttachPanel(CNotebookPanel* panel);
	virtual int LoadFile(const std::string& file, bool remove = true);

	int PlayFile();
protected:
	virtual void RemoveCache() {}
private:
	CNotebookWavePanel * _objWavePanel;
};

////
class CModelObject;
class CNotebookM2Panel;
class CMPQM2Object : public CMPQFileObject
{
public:
	CMPQM2Object();
	virtual ~CMPQM2Object();

	virtual void AttachPanel(CNotebookPanel* panel);
	virtual int ShowModel(const std::string& mpq, const std::string& m2, const std::string& mpqpath, bool findmpq, bool remove = true);
protected:
private:
	CNotebookM2Panel* _objM2Panel;
};

/////
class CNotebookWDBPanel;
class CMPQWDBObject : public CMPQFileObject
{
public:
	CMPQWDBObject();
	virtual ~CMPQWDBObject();

	virtual void AttachPanel(CNotebookPanel* panel);
	int ParseInfo();
	int ParseData(const CMPQDBCFieldManager& manager);
private:
	CNotebookWDBPanel* _objWDBPanel;
	MPQData::CWDBHeader _stHeader;
};

#endif
