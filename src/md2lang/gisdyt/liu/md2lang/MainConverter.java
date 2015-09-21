package gisdyt.liu.md2lang;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Scanner;

import gisdyt.liu.md2lang.util.Converter;
import gisdyt.liu.md2lang.util.ConverterLoader;
import gisdyt.liu.md2lang.util.Converters;

//functions: 加粗 字体大小 图片及其大小控制
public class MainConverter {

	public static String convertMD2Lang(String md){
		ConverterLoader.load();
		String result=md;
		Converters.converters.sort(new Comparator<Class<?>>() {

			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				// TODO Auto-generated method stub
				int i1=((Converter) o1.getAnnotation(Converter.class)).priority();
				int i2=((Converter) o2.getAnnotation(Converter.class)).priority();
				return i1-i2;
			}
		});
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
		String test="# SPECIFICATION OF THE ENERGY SYSTEM OF ACADEMYCRAFT\n This Specification will introduce you the way you use energy system.";
		System.out.println(test);
		System.out.println("=====================================");
		System.out.println(convertMD2Lang(test));
	}
}
