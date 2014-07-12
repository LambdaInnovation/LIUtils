package cn.liutils.api;

import net.minecraftforge.common.config.Configuration;
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
	public static void loadConfigurableClass(Configuration conf, Class<?> cl) {
		ConfigHandler.loadConfigurableClass(conf, cl);
	}

}
