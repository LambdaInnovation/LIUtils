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
package cn.liutils.core.client.register;

import java.util.HashSet;

import cn.liutils.core.LIUtilsMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

/**
 * 统一的声音注册类，请使用addSoundPath加入声音（详见方法说明）
 * 
 * @author WeAthFolD, Rikka
 */
@SideOnly(Side.CLIENT)
public class LISoundRegistry {

	private static HashSet<String> pathSounds = new HashSet<String>();

	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
		try {
			SoundPoolEntry snd;
			for (String path : pathSounds) {
				event.manager.soundPoolSounds.addSound(path);
			}
 		} catch (Exception e) {
			e.printStackTrace();
		}
		LIUtilsMod.log.fine("LIUtils successfully loaded " + pathSounds.size() + " sound files");
	}

	/**
	 * 请在preInit中使用这个函数 || 两个参数的末尾都自动被加上了 .wav 后缀。
	 * 
	 * @param name
	 *            : 声音名字. i.e. "weapons/rocket"
	 */
	public static void addSoundPath(String name) {
		pathSounds.add("lambdacraft:" + name + ".wav");
	}
	
	/**
	 * @see addSoundPath
	 * @param name
	 * @param absPath
	 * @param cnt
	 */
	public static void addSoundWithVariety(String name, int cnt) {
		for(int i = 0; i < cnt; i ++) {
			char ch = (char) ('a' + i);
			addSoundPath(name + ch);
		}
	}

}
