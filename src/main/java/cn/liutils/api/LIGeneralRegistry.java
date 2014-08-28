package cn.liutils.api;

import java.lang.reflect.Field;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cn.liutils.api.register.Configurable;

/**
 * 各种Registry类的统一接口，方便各方面注册。
 * @author WeAthFolD
 *
 */
public class LIGeneralRegistry {
	
	/**
	 * 加载一个含有可设置参数的类。
	 * 
	 * @param conf
	 *            公用设置
	 * @param cl
	 *            类，要注册的参数必须为Static
	 */
	public static void loadConfigurableClass(Configuration conf, Class<?> cl) {
		Property prop;
		for (Field f : cl.getFields()) {
			Configurable c = f.getAnnotation(Configurable.class);
			if (c != null) {
				try {
					prop = conf.get(c.category(), c.key(), c.defValue());
					prop.comment = c.comment();
					Class<?> type = f.getType();
					if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
						f.setInt(null, prop.getInt(Integer.parseInt(c.defValue())));
					} else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
						f.setBoolean(null, prop.getBoolean(Boolean.parseBoolean(c.defValue())));
					} else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
						f.setDouble(null, prop.getDouble(Double.parseDouble(c.defValue())));
					} else if (type.equals(String.class)) {
						f.set(null, prop.getString());
					} else {
						throw new UnsupportedOperationException();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
