/** 
 * Copyright (c) LambdaCraft Modding Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.half-life.cn/
 * 
 * LambdaCraft is open-source. It is distributed under the terms of the
 * LambdaCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * LambdaCraft是完全开源的。它的发布遵从《LambdaCraft开源协议》。你允许阅读，修改以及调试运行
 * 源代码， 然而你不允许将源代码以另外任何的方式发布，除非你得到了版权所有者的许可。
 */
package cn.liutils.core.register;

import java.lang.reflect.Field;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cn.liutils.api.register.Configurable;

/**
 * 
 * 通用的注册函数。目前所有函数中被标记的域必须都为静态public域。
 * 
 * @author WeAthFolD
 * 
 */
public class ConfigHandler {

	private static final int BLOCK_BEGIN = 400;

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
						f.setInt(null,
								prop.getInt(Integer.parseInt(c.defValue())));
					} else if (type.equals(Boolean.TYPE)
							|| type.equals(Boolean.class)) {
						f.setBoolean(null, prop.getBoolean(Boolean
								.parseBoolean(c.defValue())));
					} else if (type.equals(Double.TYPE)
							|| type.equals(Double.class)) {
						f.setDouble(null, prop.getDouble(Double.parseDouble(c
								.defValue())));
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
