package gisdyt.liu.md2lang;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import gisdyt.liu.md2lang.util.Converter;
import gisdyt.liu.md2lang.util.ConverterLoader;
import gisdyt.liu.md2lang.util.Converters;

//functions: 加粗 字体大小 图片及其大小控制 finished
//image: ![url](width, height)
//图片把![url](width, height)改成[img src="xxxx" width=123 height=456][/img]
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
		String testing_url="https://raw.githubusercontent.com/LambdaInnovation/AcademyCraft/master/docs_cn/README.md";
		String testing_content=sendGet(testing_url, "");
		System.out.println(convertMD2Lang(testing_content));
	}
	
	 public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line+"\n";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }
}
