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
package cn.liutils.api.register;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.liutils.core.register.ConfigHandler;

/**
 * **Inspired by HyperX，将一个变量标记为可以Config设置。 需要在GeneralRegistry中加载标记过的类。
 * 当前只支持static域。
 * 
 * @see ConfigHandler#loadConfigurableClass
 * @author WeAthFolD
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configurable {

	/**
	 * 配置的分类。
	 */
	String category() default "general";

	/**
	 * 该配置在Config中显示名称。
	 */
	String key();

	/**
	 * 为该配置规定一个默认值。
	 */
	String defValue();

	/**
	 * 该comment的注释。
	 */
	String comment() default "";
}
