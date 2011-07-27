#include <iostream>

#include "tinyxml.h"


void Output(const std::string& str)
{
	std::cout << str << std::endl;
}


int main()
{
	TiXmlDocument doc;
	if(!doc.LoadFile("..\\DBC.xml"))
		return -1;

	std::cout << doc.RootElement()->ValueStr() << std::endl;
	const TiXmlElement* root = doc.RootElement();
	if(root->ValueStr() != "DBCFile")
		return -1;

	const TiXmlElement* file = root->FirstChildElement();
	while(file != NULL)
	{
		Output(file->ValueStr());
		//File Attribute
		const TiXmlAttribute* attr = file->FirstAttribute();
		while(attr != NULL)
		{
			Output(attr->Name());
			Output(attr->ValueStr());
			attr = attr->Next();
		}
		//Fields
		const TiXmlElement* fields = file->FirstChildElement("Fields");// ->FirstChildElement();
		if(fields == NULL)
			return -1;
		//Field
		const TiXmlElement* field = fields->FirstChildElement();
		while(field != NULL)
		{
			Output(field->GetText());
			//Field attribute
			attr = field->FirstAttribute();
			while(attr != NULL)
			{
				Output(attr->Name());
				Output(attr->ValueStr());
				attr = attr->Next();
			}

			field = field->NextSiblingElement();
		}
		//Note
		const TiXmlElement* note = file->FirstChildElement("Note");
		if(note == NULL)
			return -1;
		Output(note->GetText());

		file = file->NextSiblingElement();
	}

	return 0;
}


//
//  using std::string;
//
//  int main()
//{
//  TiXmlDocument* myDocument = new TiXmlDocument();
//  myDocument->LoadFile("..\\ex.xml");
//  TiXmlElement* rootElement = myDocument->RootElement();  //Class
//  TiXmlElement* studentsElement = rootElement->FirstChildElement();  //Students
//  TiXmlElement* studentElement = studentsElement->FirstChildElement();  //Students
//  while ( studentElement ) {
//    TiXmlAttribute* attributeOfStudent = studentElement->FirstAttribute();  //获得student的name属性
//    while ( attributeOfStudent ) {
//      std::cout << attributeOfStudent->Name() << " : " << attributeOfStudent->Value() << std::endl;
//      attributeOfStudent = attributeOfStudent->Next();
//    }
//    TiXmlElement* phoneElement = studentElement->FirstChildElement();//获得student的phone元素
//    std::cout << "phone" << " : " << phoneElement->GetText() << std::endl;
//    TiXmlElement* addressElement = phoneElement->NextSiblingElement();
//    std::cout << "address" << " : " << phoneElement->GetText() << std::endl;
//    studentElement = studentElement->NextSiblingElement();
//  }
//  return 0;
//}
//int GetItem(TiXmlElement* elem);
//
//int GetElement(TiXmlElement* elem)
//{
//	TiXmlElement* e = elem->FirstChildElement();
//	while-(e!= NULL)
//	{
//		std::cout << e->Type() << ":" << e->Value() << std::endl;
//		if(!e->NoChildren())
//		{
////			TiXmlElement* t = e;
//			GetElement(e);
//			e = e->NextSiblingElement();	
//		}
//	}
//	GetItem(elem);
//	return 0;
//}
//
//int GetI0tem(TiXmlElement* elem)
//{
//	TiXmlNode* node = elem->FirstChild();// >FirstChildElement();
//	while(node != NULL)
//	{
//		std::cout << node->Type() << ":" << node->Value() << std::endl;
//		node = node->NextSibling();// = elem->NextSiblingElement();
//	}
//	return 0;
//}
//
//
//int main()
//{
//	TiXmlDocument doc;
//	bool b = doc.LoadFile("..\\DBC.xml");
//	//if(b)
//	//	doc.Print();
//
//	TiXmlElement* elem = doc.FirstChildElement("DBCFile");
//	while(elem != NULL)
//	{
//		std::cout << elem->Type() << ":" << elem->Value() << std::endl;
//		if(!elem->NoChildren())
//		{
////			TiXmlElement* t = elem;
//			GetElement(elem);
//			elem = elem->NextSiblingElement();
//		}
//