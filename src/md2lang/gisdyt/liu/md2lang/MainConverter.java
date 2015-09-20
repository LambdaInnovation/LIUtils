package gisdyt.liu.md2lang;

import java.lang.reflect.InvocationTargetException;

import gisdyt.liu.md2lang.util.ConverterLoader;
import gisdyt.liu.md2lang.util.Converters;

public class MainConverter {

	public static String convertMD2Lang(String md){
		ConverterLoader.load();
		String result=md;
		for(int i=0;i<Converters.converters.size();++i){
			try {
				result=(String) Converters.converters.get(i).getMethod("convert", String.class).invoke(null, result);
				System.out.println("Using converter ["+Converters.converters.get(i).getName()+"]");
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		convertMD2Lang("abcd");
	}
}
