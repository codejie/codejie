#include <iostream>
#include <string>
#include <vector>

#include "StormLib.h"

using namespace std;

int nError = 0;
HANDLE hMpq;
HANDLE hFile;


int ExtractFile(const string& from, const string& to);


int OpenMPQArchiveFile(const std::string& filename)
{
	if(!SFileOpenArchive(filename.c_str(), 0, 0, &hMpq))
		return -1;

	SFILE_FIND_DATA sf;
	HANDLE hFind;
	DWORD dwDataSize = 0;
	BOOL bFound = TRUE;

	size_t szFileCount = 0;
	hFind = SFileFindFirstFile(hMpq, "*.blp", &sf, NULL);
	while(hFind != NULL && bFound != FALSE)
	{
		cout << "\nFileName = " << sf.cFileName;
		//cout << "\nPlainName = " << hex << (unsigned int)(sf.szPlainName);
		//cout << "\nLocale = " << sf.lcLocale;
		//cout << "\nFileSize = " << sf.dwFileSize;
		//cout << "\nFileFlags = " << sf.dwFileFlags;
		//cout << "\nBlockIndex = " << sf.dwBlockIndex;
		//cout << "\nCompSize = " << sf.dwCompSize;
		//cout << "\n-----------------------" << dec;

		++ szFileCount;

		string str = sf.cFileName;
		for(string::size_type i = 0; i < str.size(); ++ i)
		{
			if(str[i] >=97 && str[i] <= 122)
				str[i] -= 32;
		}
		
		if((str.find("CREATURE\\KELTHUZAD\\KEL'THUZAD.BLP") != string::npos)
			|| (str.find("CREATURE\\ELEMENTALEARTH\\WATERMIST.BLP") != string::npos)
			|| (str.find("CREATURE\\LICH\\ARMORREFLECT3.BLP") != string::npos)
			|| (str.find("ITEM\\OBJECTCOMPONENTS\\WEAPON\\FLARE.BLP") != string::npos)
			|| (str.find("CREATURE\\GHOST\\BLACK32.BLP") != string::npos)
			|| (str.find("CREATURE\\GHOST\\GHOST2.BLP") != string::npos)
			|| (str.find("SPELLS\\SHOCKWAVEWATER1.BLP") != string::npos))
//		if(str.find("CREATURE\\KELTHUZAD\\KELTHUZAD.M2") != string::npos)
		{
			string::size_type pos = str.find_last_of('\\');
			if(pos != string::npos)
				str = str.substr(pos + 1);
			if(ExtractFile(sf.cFileName, str) != 0)
				return -1;
		}

		bFound = SFileFindNextFile(hFind, &sf);
	}

	SFileFindClose(hFind);

	cout << "\nTotal Files : " << szFileCount;


	return 0;
}

int CloseMPQArchiveFile()
{
	SFileCloseArchive(hMpq);
	return 0;
}

int ExtractFile(const string& from, const string& to)
{
	string str = "output\\" + to;
	if(!SFileExtractFile(hMpq, from.c_str(), str.c_str()))
		return -1;
	return 0;
}

int GetMPQFileList(const std::string& path, std::vector<std::string>& vctFile)
{
	WIN32_FIND_DATAA data;
	HANDLE handle  = FindFirstFileA((LPCSTR)path.c_str(), &data);
	if( handle == INVALID_HANDLE_VALUE)
		return -1;
	bool f = true;
	while(f)
	{
		if((data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != FILE_ATTRIBUTE_DIRECTORY)
		{
			std::string str = data.cFileName;
			//if((str.find(".mpq") != std::string::npos) || (str.find(".MPQ") != std::string::npos))
			{
				vctFile.push_back(str);
			}
		}
		//else if((data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) == FILE_ATTRIBUTE_DIRECTORY)
		//{
		//	std::string p = path + "\\" + data.cFileName;
		//	GetMPQFileList(p, vctFile);
		//}
		f = (FindNextFileA(handle, &data) == TRUE ? true : false);
		if(GetLastError() == ERROR_NO_MORE_FILES)
			break;
	}
	FindClose(handle);
	return 0;
}

int main()
{
	std::string path = "n:\\tmp\\";
	std::vector<std::string> vctFile;
	if(GetMPQFileList(path + "*.mpq", vctFile) != 0)
	{
		std::cout << "\nGet MPQ file list failed." << std::endl;
		return -1;
	}
	for(std::vector<std::string>::const_iterator it = vctFile.begin(); it != vctFile.end(); ++ it)
	{
		if(OpenMPQArchiveFile(path + *it) != 0)
			std::cout << "open file '" << *it << "' failed - " << GetLastError() << std::endl;
		CloseMPQArchiveFile();
	}

	char ch;
	std::cin >> ch;

	return 0;
}