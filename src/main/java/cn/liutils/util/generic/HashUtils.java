package cn.liutils.util.generic;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.liutils.core.LIUtils;

/**
 * Provides some hash functions
 * @author EAirPeter
 */
public enum HashUtils {
	MD5("MD5"),
	SHA1("SHA1");
	
	private final MessageDigest MD;
	
	private HashUtils(String alg) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(alg);
		}
		catch (NoSuchAlgorithmException e) {
			LIUtils.log.error("Failed to initialize HashUtils." + alg, e);
		}
		MD = md;
	}
	
	public byte[] hash(byte[] src) {
		return MD.digest(src);
	}
	
	public byte[] hash(InputStream src) {
		return hash(readBytes(src));
	}
	
	public static byte[] readBytes(InputStream src) {
		byte[] bytes = null;
		try {
				int count = src.available();
			bytes = new byte[count];
			if (src.read(bytes) != count)
				throw new RuntimeException("Java sucks");
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to read all bytes from InputStream", e);
		}
		return bytes;
	}

}
