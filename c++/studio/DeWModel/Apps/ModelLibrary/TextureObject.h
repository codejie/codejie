#ifndef __TEXTUREOBJECT_H__
#define __TEXTUREOBJECT_H__

#include <string>
#include <vector>
#include <map>

#include "M2Structure.h"

class CTextureObject
{
protected:
	static const std::string STR_MPQ_LOCAL_ZHCN;
	static const std::string STR_DBC_ITEMDISPLAYINFO;
	static const std::string STR_DBC_CREATUREDISPLAYINFO;
	static const std::string STR_DBC_CHARACTERSECTION;
public:
	struct Texture_t
	{
		unsigned int m_uiType;
		std::string m_strName;
		std::string m_strDBC;
		std::wstring m_strMPQ;
		std::string m_strPath;
		int m_iStatus;//-1:notfound;0:normal;1:locate;
	};
	typedef std::vector<Texture_t> TTextureVector;
	typedef std::map<std::string, std::wstring> TMPQMap;
public:
	CTextureObject();
	virtual ~CTextureObject();//¡°£¿¡®":''''' Add by Xiaobao

	int Init(const std::vector<std::wstring>& vctmpq, const std::wstring& dbcfield, const std::wstring& wdbfield);
	int LoadTexture(const M2::TTexture &texture, const std::string& m2, const std::string &m2name);
	int Extract(const std::wstring& path);

	const TTextureVector& TextureVector() const { return _vctTexture; }
	int OutputInfo(std::vector<CTextureObject::Texture_t>& vct) const;
private:
	int SearchTexture(const std::string& m2, const std::string& m2name, std::string& dbc, std::string& tex);

	int SearchItemTexture(const std::string& dbc, const std::string& m2name, std::string& tex);
	int SearchCreatureTexture(const std::string& dbc, const std::string& m2name, std::string& tex);
	int SearchCharacterTexture(const std::string& dbc, const std::string& m2name, std::string& tex);

	int LocateTexture();
private:
	TMPQMap _mapMPQ;
	std::wstring _strDBCField;
	std::wstring _strWDBField;
	TTextureVector _vctTexture;
/* Add by Xiaobao
£«£­£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤£¤¡Á	;
*/
};
/* Add by Xiaobao
"?///////;.;
k,
*/

#endif
