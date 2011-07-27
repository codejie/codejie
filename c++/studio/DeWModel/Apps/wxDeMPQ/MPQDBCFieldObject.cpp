#pragma warning(disable:4996)

#include <algorithm>

#include "tinyxml.h"

#include "Toolkit.h"

#include "MPQDBCFieldObject.h"

CMPQDBCFieldManager::CMPQDBCFieldManager()
{
}

CMPQDBCFieldManager::~CMPQDBCFieldManager()
{
	Destory();
}

void CMPQDBCFieldManager::Destory()
{
	TDBCMap::iterator it = _mapDBC.begin();
	while(it != _mapDBC.end())
	{
		TFieldMap::iterator i = it->second.m_mapField.begin();
		while(i != it->second.m_mapField.end())
		{
			delete i->second;
			it->second.m_mapField.erase(i++);
		}
		_mapDBC.erase(it ++);
	}
}

int CMPQDBCFieldManager::Load(const std::string &xml, bool reload)
{
	if(reload)
	{
		Destory();
	}
	return ParseXML(xml);		
}

const CMPQDBCFieldManager::TFieldMap* CMPQDBCFieldManager::FindDBCFields(const std::string& dbc) const
{
	TDBCMap::const_iterator it = _mapDBC.find(dbc);
	if(it == _mapDBC.end())
		return NULL;
	if(it->second.m_bValid == false)
		return NULL;
	return &it->second.m_mapField;
}

const CMPQDBCFieldManager::TFieldMap* CMPQDBCFieldManager::FindDBCFields(const std::string& dbc, bool& valid) const
{
	TDBCMap::const_iterator it = _mapDBC.find(dbc);
	if(it == _mapDBC.end())
		return NULL;
	valid = it->second.m_bValid;
	return &it->second.m_mapField;
}

const CMPQDBCFieldManager::TFieldMap* CMPQDBCFieldManager::FindDBCFields(const std::string& dbc, bool& valid, std::string& notes) const
{
	TDBCMap::const_iterator it = _mapDBC.find(dbc);
	if(it == _mapDBC.end())
		return NULL;
	valid = it->second.m_bValid;
	notes = it->second.m_strNote;
	return &it->second.m_mapField;
}

int CMPQDBCFieldManager::SetValid(const std::string& dbc, bool valid)
{
	TDBCMap::iterator it = _mapDBC.find(dbc);
	if(it == _mapDBC.end())
		return -1;
	it->second.m_bValid = valid;
	return 0;
}

int CMPQDBCFieldManager::ParseXML(const std::string &xml)
{
	TiXmlDocument doc;
	if(!doc.LoadFile(xml.c_str()))
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
			DBCField_t fielddata;
			fielddata.m_bValid = true;
			TDBCMap::iterator it = _mapDBC.find(name);
			if(it != _mapDBC.end())
			{
				TFieldMap::iterator i = it->second.m_mapField.begin();
				while(i != it->second.m_mapField.end())
				{
					delete i->second;
					it->second.m_mapField.erase(i++);
				}
				_mapDBC.erase(it);
			}
			it = _mapDBC.insert(std::make_pair(name, fielddata)).first;
			if(it == _mapDBC.end())
				return -1;

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
					DBCField::FieldAttr_t data;
					data.m_iPos = -1;
					data.m_strType.clear();
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
							data.m_strType = attr->ValueStr();
						else if(attr->NameTStr() == "size")
							data.m_iSize = attr->IntValue();
						//else if(attr->NameTStr() == "refdbc")
						//	data.m_strRefDBC = attr->ValueStr();
						//else if(attr->NameTStr() == "reffield")
						//	data.m_iRefPos = attr->IntValue();
						else if(attr->NameTStr() == "skipbyte")
							data.m_iSkipByte = attr->IntValue();

						attr = attr->Next();
					}
					DBCField::CField* dbcfield = MakeDBCField(data);
					if(dbcfield != NULL)
					{
						TFieldMap::iterator i = it->second.m_mapField.find(data.m_iPos);
						if(i == it->second.m_mapField.end())
						{
							it->second.m_mapField.insert(std::make_pair(dbcfield->m_stAttr.m_iPos, dbcfield));
						}
						else
						{
							delete i->second;
							i->second = dbcfield;
						}
					}

					field = field->NextSiblingElement();
				}
			}
		}
		file = file->NextSiblingElement();
	}

	return 0;
}

DBCField::CField* CMPQDBCFieldManager::MakeDBCField(const DBCField::FieldAttr_t &attr) const
{
	if(attr.m_iPos == -1 || attr.m_strType.empty())
		return NULL;
	if(attr.m_strType == "integer")
		return new DBCField::CIntegerField(attr);
	else if(attr.m_strType == "string")
		return new DBCField::CStringField(attr);
	else if(attr.m_strType == "float")
		return new DBCField::CFloatField(attr);
	else if(attr.m_strType == "bit")
		return new DBCField::CBitField(attr);
	else if(attr.m_strType == "byte")
		return new DBCField::CByteField(attr);
	else if(attr.m_strType == "cstring")
		return new DBCField::CCStringField(attr);
	else if(attr.m_strType == "amount")
		return new DBCField::CAmountField(attr);

	return NULL;
}

//////
namespace DBCField
{

int CField::DefaultData2String(std::string& str, CFileBuffer &fb, int offset)
{
	int data = 0;
//	fb.Seek(offset);
	fb.Read(data);

	Toolkit::StringOf<int>(data, str);

	return fb.Good() ? 0 : -1;
}

int CField::Data2String(std::string& str, CFileBuffer &fb, int offset, int strpos) const
{
	int data = 0;
//	fb.Seek(offset);
	fb.Read(data);

	Toolkit::StringOf<int>(data, str);

	return fb.Good() ? 0 : -1;
}

int CField::GetIntData(int& data, std::string& str, CFileBuffer &fb, int offset, int strpos) const
{
	fb.Read(data);

	Toolkit::StringOf<int>(data, str);

	return fb.Good() ? 0 : -1;
}

//
int CStringField::Data2String(std::string& str, CFileBuffer &fb, int offset, int strpos) const
{
	int data = 0;
//	fb.Seek(offset);
	fb.Read(data);
	
	fb.Seek(data + strpos);
	fb.Read(str);

	fb.Seek(offset + sizeof(data));

	return fb.Good() ? 0 : -1;
}

//
int CFloatField::Data2String(std::string& str, CFileBuffer &fb, int offset, int strpos) const
{
	float data = 0.0f;
//	fb.Seek(offset);
	fb.Read(data);
	
	Toolkit::StringOf<float>(data, str);

	return fb.Good() ? 0 : -1;
}

//
int CBitField::Data2String(std::string &str, CFileBuffer &fb, int offset, int strpos) const
{
	int data = 0;
//	fb.Seek(offset);
	fb.Read(data);

	str = "";
	int i = 0;
	while(data > 0)
	{
		str += ((data % 2 == 1) ? '1' : '0');
		data /= 2;
	}
	while(str.size() < 8)
	{
		str += '0';
	}

	std::reverse(str.begin(), str.end());
	str += 'B';
	return fb.Good() ? 0 : -1;
}

int CByteField::Data2String(std::string& str, CFileBuffer &fb, int offset, int strpos) const
{
	unsigned char data = 0;
//	fb.Seek(offset);
	fb.Read(data);
	
	Toolkit::StringOf<unsigned int>(data, str);

	return fb.Good() ? 0 : -1;
}

int CCStringField::Data2String(std::string& str, CFileBuffer &fb, int offset, int strpos) const
{
	fb.Read(str);
	
	std::replace(str.begin(), str.end(), (char)0x0d, '\\');
	std::replace(str.begin(), str.end(), (char)0x0a, 'n');

//	std::string::size_type pos = str.find(0x0d);
//	while(pos != std::string::npos)
//	{
//		str[pos] = '\\';
////		str[pos + 1] = 'n';
//		pos = str.find(0x0d);
//	}

	return fb.Good() ? 0 : -1;
}

}
