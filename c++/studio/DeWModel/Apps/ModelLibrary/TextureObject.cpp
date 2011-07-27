#include "stdafx.h"

#include "DBQueryObject.h"

#include "Toolkit.h"
#include "BLPFileObject.h"
#include "TextureObject.h"


const std::string CTextureObject::STR_MPQ_LOCAL_ZHCN			=	"locale-zhCN.MPQ";
const std::string CTextureObject::STR_DBC_ITEMDISPLAYINFO		=	"DBFilesClient\\ItemDisplayInfo.dbc";
const std::string CTextureObject::STR_DBC_CREATUREDISPLAYINFO	=	"DBFilesClient\\CreatureDisplayInfo.dbc";
const std::string CTextureObject::STR_DBC_CHARACTERSECTION		=	"DBFilesClient\\CharSections.dbc";

CTextureObject::CTextureObject()
{
}

CTextureObject::~CTextureObject()
{
}

int CTextureObject::Init(const std::vector<std::wstring> &vctmpq, const std::wstring& dbcfield, const std::wstring& wdbfield)
{
	for(std::vector<std::wstring>::const_iterator it = vctmpq.begin(); it != vctmpq.end(); ++ it)
	{
		std::string str = Toolkit::WString2String(*it);
		std::string::size_type pos = str.find_last_of("\\");
		if(pos != std::string::npos)
			str = str.substr(pos + 1);
		_mapMPQ.insert(std::make_pair(str, *it));
	}

	_strDBCField = dbcfield;
	_strWDBField = wdbfield;	

	return 0;
}

int CTextureObject::LoadTexture(const M2::TTexture &texture, const std::string& m2, const std::string &m2name)
{
	_vctTexture.clear();

	for(M2::TTexture::TVector::const_iterator it = texture.m_vct.begin(); it != texture.m_vct.end(); ++ it)
	{
		Texture_t tex;
		tex.m_uiType = it->m_uiType;
		tex.m_iStatus = 0;
		if(tex.m_uiType == 0)
		{
			tex.m_strPath = it->m_strName;

			tex.m_strDBC = "NONE";
		}
		else
		{
			if(SearchTexture(m2, m2name, tex.m_strDBC, tex.m_strPath) != 0)
			{
				tex.m_strName = "NotFound";
				tex.m_strPath = "NotFound";
				tex.m_strDBC = "NONE";
				tex.m_iStatus = -1;
				continue;
			}
		}
		tex.m_strName = tex.m_strPath;
		std::string::size_type pos = tex.m_strName.find_last_of("\\");
		if(pos != std::string::npos)
			tex.m_strName = tex.m_strName.substr(pos + 1);

		_vctTexture.push_back(tex);
	}
	return 0;
}

int CTextureObject::Extract(const std::wstring &path)
{
	if(LocateTexture() != 0)
		return -1;

	CBLPFileObject obj;
	for(TTextureVector::const_iterator it = _vctTexture.begin(); it != _vctTexture.end(); ++ it)
	{
		if(it->m_iStatus == 1)
		{
			std::string mpq = Toolkit::WString2String(it->m_strMPQ);
			std::wstring png = Toolkit::String2WString(it->m_strName) + L".png";
			png = path + L"\\" + png;
			if(obj.Extract2PNG(mpq, it->m_strPath, Toolkit::WString2String(png)) != 0)
				return -1;		
		}
	}
	return 0;
}

int CTextureObject::SearchTexture(const std::string &m2, const std::string &m2name, std::string &dbc, std::string &tex)
{
	std::string str = m2;
	std::string::size_type pos = m2.find_first_of("\\");
	if(pos != std::string::npos)
		str = str.substr(0, pos);
	if(str == "ITEM")
	{
		dbc = STR_DBC_ITEMDISPLAYINFO;
		return SearchItemTexture(dbc, m2name, tex);
	}
	else if(str == "Creature")
	{
		dbc = STR_DBC_CREATUREDISPLAYINFO;
		return SearchCreatureTexture(dbc, m2name, tex);
	}
	else if(str == "Character")
	{
		dbc = STR_DBC_CHARACTERSECTION;
		return SearchCharacterTexture(dbc, m2name, tex);
	}
	else
	{
		return -1;
	}

	return 0;
}

int CTextureObject::SearchItemTexture(const std::string& dbc, const std::string &m2name, std::string & tex)
{
	TMPQMap::const_iterator it = _mapMPQ.find(STR_MPQ_LOCAL_ZHCN);
	if(it == _mapMPQ.end())
		return -1;
	std::wstring mpq = 	it->second;

	std::string m2 = m2name + ".mdx";

	CDBQueryObject obj;
	if(obj.InitConfig(_strDBCField) != 0)
		return -1;

	TQueryData query;
	query.m_iPos = 1;
	query.m_strValue = m2;

	TResultData result;
	result.m_iPos = 3;
	
	if(obj.QueryDBC(mpq, dbc, query, result) == 0)
	{
		tex = result.m_strValue + ".blp";
		return 0;
	}

	//right model
	query.m_iPos = 2;
	result.m_iPos = 4;

	if(obj.QueryDBC(mpq, dbc, query, result) == 0)
	{
		tex = result.m_strValue + ".blp";
		return 0;
	}

	return -1;
}

int CTextureObject::SearchCreatureTexture(const std::string& dbc, const std::string &m2name, std::string &tex)
{
	return -1;
}

int CTextureObject::SearchCharacterTexture(const std::string& dbc, const std::string &m2name, std::string &tex)
{
	return -1;
}

int CTextureObject::LocateTexture()
{
	CMPQFileWrapper wrapper;
	size_t size = 0;
	int flag = 0;
	for(TMPQMap::const_iterator it = _mapMPQ.begin(); it != _mapMPQ.end(); ++ it)
	{
		std::string mpq = Toolkit::WString2String(it->second);
		size_t count = _vctTexture.size();
		for(TTextureVector::iterator i = _vctTexture.begin(); i != _vctTexture.end(); ++ i)
		{
			if(i->m_iStatus == 0)
			{
				std::string blp;
				if(i->m_uiType != 0)
				{
					blp = "*\\" + i->m_strName;
				}
				else
				{
					blp = i->m_strPath;
				}
				if(wrapper.Locate(mpq, blp, i->m_strPath, size, flag) == 0)
				{
					i->m_strMPQ = it->second;
					i->m_iStatus = 1;
					-- count;
				}
			}
			else
			{
				-- count;
			}
		}
		if(count == 0)
			break;
	}
	return 0;
}

int CTextureObject::OutputInfo(std::vector<CTextureObject::Texture_t>& vct) const
{
	vct = _vctTexture;
	return 0;
}


