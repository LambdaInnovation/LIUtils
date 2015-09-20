package gisdyt.liu.md2lang.converters;

import gisdyt.liu.md2lang.util.Converter;

// TODO Testing
@Converter
public class EscapedCharsConverter {

	private static final String replacable[][]=new String[][]{
			{"&nbsp;", " "}
	}; 
	
	public static String convert(String s){
		for(int i=0;i<replacable.length;++i){
			s=s.replaceAll(replacable[i][0], replacable[i][1]);
		}
		return s;
	}
}
