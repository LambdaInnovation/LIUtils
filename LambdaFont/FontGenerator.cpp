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
#include <cairo\cairo.h>

#define PER_LINE 16

typedef std::set<wchar_t> Charset;
Charset charset;

std::wstring_convert<std::codecvt_utf8<wchar_t>> utf_coverter;

std::wifstream* readUTF8File(std::wstring path);
void expandCharset(std::wstring chars);
void parseLang(std::wifstream& langFile);
void genFile(const int size, const std::string&);

inline std::string toutf(std::wstring str) {
	return utf_coverter.to_bytes(str);
}

std::wstring langs[] = {
	L"en_US.lang",
	L"zh_CN.lang"
};

int main() {
	using namespace std;
	
	wcout << "FontGen v0.1 by WeAthFolD poi~" << endl;
	for(wstring path : langs) {
		wifstream *file = readUTF8File(path);
		if(file != NULL) {
			parseLang(*file);
		} else {
			wcout << "Loading lang file \"" << path << "\" failed." << endl;
		}
	}
	wcout << "charset loaded, " << charset.size() << " character(s)." << endl;

	const int font_size = 32;
	const string font_name = "Microsoft Yahei";
	wcout << "Generating font of size " << font_size << " ..." << endl;
	genFile(font_size, font_name);
}

//Some util funcs
int udiv(int a, int b) {
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
				charset.insert(ch);
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

void genFile(const int size, const std::string &font) {
	const std::string png_path("font.png"), desc_path("font.lf");
	int width = size * PER_LINE, height = size * udiv(charset.size(), 16);

	std::ofstream fdesc(desc_path);
	writeProp(fdesc, L"_size", t2ws(size));
	writeProp(fdesc, L"_charset_size", t2ws(charset.size()));

	cairo_t* context;
	cairo_surface_t* surface;
	surface = cairo_image_surface_create(CAIRO_FORMAT_ARGB32, width, height);
	context = cairo_create(surface);

	cairo_select_font_face(context, font.c_str(), CAIRO_FONT_SLANT_NORMAL, CAIRO_FONT_WEIGHT_NORMAL);
	cairo_set_font_size(context, size);

	int i = 0;
	for(wchar_t ch : charset) {
		int x0 = size * (i % PER_LINE);
		int y0 = size * (i / PER_LINE) + size - 4;
		cairo_move_to(context, x0, y0);
		cairo_show_text(context, std::string(utf_coverter.to_bytes(ch)).c_str());
		writeProp(fdesc, t2ws(ch), t2ws(i));
		++i;
	}

	cairo_destroy(context);
	cairo_surface_write_to_png(surface, png_path.c_str());
	cairo_surface_destroy(surface);
	fdesc.close();
}