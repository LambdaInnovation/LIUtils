package cn.liutils.api;

import cn.liutils.core.register.Config;
import cn.liutils.core.register.ConfigHandler;

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
	public static void loadConfigurableClass(Config conf, Class<?> cl) {
		ConfigHandler.loadConfigurableClass(conf, cl);
	}
	
	public static int getItemId(Config conf, String name, int cat) {
		return ConfigHandler.getItemId(conf, name, cat);
	}
	
	public static int getBlockId(Config conf, String name, int cat) {
		return ConfigHandler.getBlockId(conf, name, cat);
	}
	
	public static int getFixedBlockId(Config conf, String name, int def) {
		return ConfigHandler.getFixedBlockId(conf, name, def);
	}
	
	public static int getFixedBlockId(Config conf, String name, int def, int max) {
		return ConfigHandler.getFixedBlockId(conf, name, def, max);
	}

}
