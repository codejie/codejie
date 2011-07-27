#include "stdafx.h"

#include "tinyxml.h"

#include "Toolkit.h"
#include "FieldManager.h"

CDBFieldManager::CDBFieldManager()
{
}

CDBFieldManager::~CDBFieldManager()
{
}

int CDBFieldManager::Load(const std::wstring &xml, bool reload)
{
	return ParseConfig(xml);		
}

int CDBFieldManager::ParseConfig(const std::wstring &xml)
{
	TiXmlDocument doc;
	if(!doc.LoadFile((Toolkit::WString2String(xml)).c_str()))
		return -1;

	std::string name;

	const TiXmlElement* root = doc.RootElement();
	if((root->ValueStr() != "DBCFile") && (root->ValueStr() != "WDBFile"))
		return -1;
	const TiXmlElement* file = root->FirstChildElement();
	while(file != NULL)
	{
		//File Attribute
		name.clear();
		const TiXmlAttribute* attr = file->FirstAttribute();
		while(attr != NULL)
		{
			if(attr->NameTStr() == "name")
				name = attr->ValueStr();
			attr = attr->Next();
		}
		if(!name.empty())
		{
			DBFile_t fielddata;
			TDBMap::iterator it = _mapDB.find(name);
			if(it != _mapDB.end())
			{
				_mapDB.erase(it);
			}
			it = _mapDB.insert(std::make_pair(name, fielddata)).first;

			const TiXmlElement* notes = file->FirstChildElement("Note");
			if(notes != NULL && notes->GetText() != NULL)
				it->second.m_strNote = notes->GetText();
			else
				it->second.m_strNote = "[NONE]";

			const TiXmlElement* fields = file->FirstChildElement("Fields");
			if(fields != NULL)
			{
				const TiXmlElement * field = fields->FirstChildElement("Field");
				while(field != NULL)
				{
					FieldAttr_t data;
					data.m_iPos = -1;
					data.m_eType = FT_UNKNOWN;
					if(field->GetText() != NULL)
						data.m_strTitle = field->GetText();
					else
						data.m_strTitle = "";
					data.m_iSize = 0;
					attr = field->FirstAttribute();
					while(attr != NULL)
					{
						if(attr->NameTStr() == "position")
						{
							data.m_iPos = attr->IntValue();
							if(data.m_strTitle.empty())
								data.m_strTitle = "Undef_" + attr->ValueStr();
						}
						else if(attr->NameTStr() == "type")
						{
							if(attr->ValueStr() == "integer")
								data.m_eType = FT_INTEGER;
							else if(attr->ValueStr() == "string")
								data.m_eType = FT_STRING;
							else if(attr->ValueStr() == "float")
								data.m_eType = FT_FLOAT;
							else if(attr->ValueStr() == "bit")
								data.m_eType = FT_BIT;
							else if(attr->ValueStr() == "byte")
								data.m_eType = FT_BYTE;
							else if(attr->ValueStr() == "cstring")
								data.m_eType = FT_CSTRING;
							else if(attr->ValueStr() == "amount")
								data.m_eType = FT_AMOUNT;
							else
								data.m_eType = FT_UNKNOWN;
						}
						else if(attr->NameTStr() == "size")
							data.m_iSize = attr->IntValue();
						else if(attr->NameTStr() == "skipbyte")
							data.m_iSkipByte = attr->IntValue();

						attr = attr->Next();
					}
					TFieldMap::iterator i = it->second.m_mapField.find(data.m_iPos);
					if(i != it->second.m_mapField.end())
						it->second.m_mapField.erase(i);
					it->second.m_mapField.insert(std::make_pair(data.m_iPos, data));

					field = field->NextSiblingElement();
				}
			}
		}
		file = file->NextSiblingElement();
	}

	return 0;
}

const CDBFieldManager::TFieldMap* CDBFieldManager::FindFieldMap(const std::string &dbc) const
{
	TDBMap::const_iterator it = _mapDB.find(dbc);
	if(it == _mapDB.end())
		return NULL;
	return &it->second.m_mapField;
}
