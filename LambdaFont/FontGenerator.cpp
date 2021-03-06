// Automatic PNG font generator
// Read a set of lang file and automatically create the PNG file contains all of those characters, 
// and create a font description file which can be used in later indexing and drawing.
// Ver: 0.1
// WeAthFolD

#include <locale>
#include <codecvt>
#include <string>
#include <fstream>
#include <iostream>
#include <cstdlib>
#include <cassert>
#include <set>
#include <locale>
#include <codecvt>
#include <sstream>
#include <vector>
#include <Windows.h>
#include <cairo\cairo.h>

#define PER_LINE 8
#define MAXLINES 8

typedef std::vector<wchar_t> Charset;
typedef std::set<wchar_t> BuildSet;
BuildSet buildSet;
Charset charset;

std::wstring_convert<std::codecvt_utf8<wchar_t>> utf_coverter;

std::wifstream* readUTF8File(std::wstring path);
void expandCharset(std::wstring chars);
void parseLang(std::wifstream& langFile);
void genFile(const int size, const std::string&, const std::wstring);

inline std::string toutf(std::wstring str) {
	return utf_coverter.to_bytes(str);
}

void genLangList(std::vector<std::wstring> &vec) {
	WIN32_FIND_DATA fd;
	HANDLE h = FindFirstFile(L"*.lang", &fd);
	do {
		if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
			vec.push_back(fd.cFileName);
	} while(FindNextFile(h, &fd));
}

int main() {
	using namespace std;

	wcout << "FontGen v0.2 by WeAthFolD poi~" << endl;

	vector<wstring> files;
	genLangList(files);
	wcout << "Search finished. " << files.size() << " files found." << endl;

	for(wstring path : files) {
		wifstream *file = readUTF8File(path);
		if(file != NULL) {
			parseLang(*file);
		} else {	
			wcout << "Loading lang file \"" << path << "\" failed." << endl;
		}
	}
	charset.insert(charset.begin(), buildSet.begin(), buildSet.end()); //Change set into vector.
	wcout << "charset loaded, " << charset.size() << " character(s)." << endl;

	const int font_size = 64;
	const string font_name = "Microsoft Yahei";
	wcout << "Generating font of size " << font_size << " ..." << endl;
	genFile(font_size, font_name, L"yahei");
}

//Some util funcs
template<typename T> void t2s(T i) {
	std::ostringstream s;
	s << i;
	return s.str();
}

inline int udiv(int a, int b) {
	int ret = a / b;
	return ret * b == a ? ret : ret + 1;
}

inline void writeProp(std::ofstream& file, const std::wstring &key, const std::wstring &value) {
	file << toutf(key) << toutf(L"=") << toutf(value) << std::endl;
}

template<typename T> inline std::wstring t2ws(T i) {
	std::wostringstream sout;
	sout << i;
	return sout.str();
}

void parseLang(std::wifstream& fin) {
	std::wstring curLine;

	while(fin.good()) {
		std::getline(fin, curLine);
		size_t ind = curLine.find(L'=');
		if(ind > 0 && ind < curLine.length() - 1) {
			curLine = curLine.substr(ind + 1);
			for(wchar_t ch : curLine) {
				buildSet.insert(ch);
			}
		}
	}
}

std::wifstream* readUTF8File(std::wstring path) {
	const std::locale empty_locale = std::locale::empty();
    typedef std::codecvt_utf8<wchar_t> converter_type;
    const converter_type* converter = new converter_type;
    const std::locale utf8_locale = std::locale(empty_locale, converter);

    std::wifstream *ptr = new std::wifstream(path);
	ptr->imbue(utf8_locale);
	return ptr->good() ? ptr : NULL;
}

void genFile(const int size, const std::string &font, const std::wstring out_name) {
	const int spacing = 22;
	int lines = udiv(charset.size(), PER_LINE);
	int width = size * PER_LINE, height = (size + spacing) * MAXLINES;

	std::ofstream fdesc(toutf(out_name) + ".lf");
	writeProp(fdesc, L"_size", t2ws(size));
	writeProp(fdesc, L"_charset_size", t2ws(charset.size()));
	writeProp(fdesc, L"_spacing", t2ws(spacing));
	writeProp(fdesc, L"_maxlines", t2ws(MAXLINES));
	writeProp(fdesc, L"_per_line", t2ws(PER_LINE));

	cairo_t* context = 0;
	cairo_surface_t* surface = 0;

	size_t ind = 0; //Current drawing char index
	size_t cur = MAXLINES * PER_LINE; //Current page drawed chars
	int pid = -1; //New page id to write to
	do {
		if(cur == MAXLINES * PER_LINE || ind == charset.size()) { //Time to create new context
			cur = 0;
			//flush the data?
			if(context) {
				std::wstring path = out_name;
				wchar_t buf[20] = {};
				wsprintf(buf, L"%d.png", pid);
				path += buf;
				writeProp(fdesc, L"_file_" + t2ws(pid), path);

				cairo_destroy(context);
				cairo_surface_write_to_png(surface, toutf(path).c_str());
				cairo_surface_destroy(surface);
			}
			++pid;

			if(ind != charset.size()) {
				//create new
				surface = cairo_image_surface_create(CAIRO_FORMAT_ARGB32, width, height);
				context = cairo_create(surface);

				//select font
				cairo_select_font_face(context, font.c_str(), CAIRO_FONT_SLANT_NORMAL, CAIRO_FONT_WEIGHT_NORMAL);
				cairo_set_font_size(context, size);
				cairo_set_source_rgb(context, 1.0, 1.0, 1.0);
			}
		}
		if(ind == charset.size()) break;

		//Continue drawing.
		wchar_t ch = charset[ind];
		int x0 = size * (cur % PER_LINE);
		int y0 = (size + spacing) * (cur / PER_LINE) + size;
		cairo_move_to(context, x0, y0);
		cairo_text_extents_t extents;
		std::string utf = utf_coverter.to_bytes(ch);
		cairo_show_text(context, utf.c_str());
		cairo_text_extents(context, utf.c_str(), &extents);
		writeProp(fdesc, t2ws(ch), t2ws(pid) + L"," + t2ws(cur) + L"," + t2ws(extents.x_advance));
		
		++ind;
		++cur;
	} while(ind <= charset.size());

	std::cout << "generated font with " << pid << "pages" << std::endl;
	fdesc.close();
}