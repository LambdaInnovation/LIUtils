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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Property;
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
	public static void loadConfigurableClass(Config conf, Class<?> cl) {
		Property prop;
		for (Field f : cl.getFields()) {
			Configurable c = f.getAnnotation(Configurable.class);
			if (c != null) {
				try {
					prop = conf
							.getProperty(c.category(), c.key(), c.defValue());
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

	/**
	 * 获得一个空的物品ID。（调用Config配置）
	 * 
	 * @param name
	 *            物品名字
	 * @param cat
	 *            物品分类
	 * @see cn.lambdacraft.core.proxy.GeneralProps
	 * @return 获取的ID
	 */
	public static int getItemId(Config config, String name, int cat) {
		try {
			return config.getItemID(name, getEmptyItemId(cat)) - 256;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获得一个空的方块ID。（调用Config配置）
	 * 
	 * @param name
	 *            方块名字
	 * @param cat
	 *            方块分类
	 * @see cn.lambdacraft.core.proxy.GeneralProps
	 * @return 获取的ID
	 */
	public static int getBlockId(Config config, String name, int cat) {
		try {
			return config.GetBlockID(name, getEmptyBlockId(cat));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static int getFixedBlockId(Config config, String name, int def) {
		try {
			return config.GetBlockID(name, def);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static int getFixedBlockId(Config config, String name, int def, int max) {
		try {
			int id =  config.getSpecialBlockID(name, def);
			if(id >= max)
				throw new IllegalArgumentException("Block id has been set as a value too large : " + name + "as id + " + id + " , it must be below the value of " + max);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private static int getEmptyItemId(int cat) {
		int begin = 5200;
		begin += cat * 50;
		int theId = 0;
		for (int i = 0; i < 50; i++) {
			theId = begin + i;
			if (Item.itemsList[theId] == null)
				return theId;
		}
		return -1;
	}

	private static int getEmptyBlockId(int cat) {
		int begin = 400;
		begin += cat * 50;
		int theId = 0;
		for (int i = 0; i < 50; i++) {
			theId = begin + i;
			if (Block.blocksList[theId] == null)
				return theId;
		}
		return -1;
	}

}
