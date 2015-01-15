package cn.liutils.util;

/**
 * Debug-related utility functions.
 * @author WeathFolD
 */
public class DebugUtils {

	public static String formatArray(Object... arr) {
		StringBuilder b = new StringBuilder("(");
		for(int i = 0; i < arr.length; ++i) {
			b.append(arr[i]).append(i == arr.length - 1 ? "" : ", "); 
		}
		return b.append(")").toString();
	}
	
	public static String formatArray(boolean[] arr) {
		StringBuilder b = new StringBuilder("(");
		for(int i = 0; i < arr.length; ++i) {
			b.append(arr[i]).append(i == arr.length - 1 ? "" : ", "); 
		}
		return b.append(")").toString();
	}
	
	public static String formatArray(float[] arr) {
		StringBuilder b = new StringBuilder("(");
		for(int i = 0; i < arr.length; ++i) {
			b.append(arr[i]).append(i == arr.length - 1 ? "" : ", "); 
		}
		return b.append(")").toString();
	}
	
	public static String formatArray(int[] arr) {
		StringBuilder b = new StringBuilder("(");
		for(int i = 0; i < arr.length; ++i) {
			b.append(arr[i]).append(i == arr.length - 1 ? "" : ", "); 
		}
		return b.append(")").toString();
	}
	
	public static String formatArray(double[] arr) {
		StringBuilder b = new StringBuilder("(");
		for(int i = 0; i < arr.length; ++i) {
			b.append(arr[i]).append(i == arr.length - 1 ? "" : ", "); 
		}
		return b.append(")").toString();
	}

}
