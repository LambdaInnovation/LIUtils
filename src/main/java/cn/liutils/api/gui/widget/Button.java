/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import cn.liutils.api.gui.Widget;

/**
 * Button with click sound
 * 
 * @author mkpoli
 *
 */
public abstract class Button extends Widget {
	private ResourceLocation sound;
	// private boolean needSound;
	private final static ResourceLocation DEFAULT_SOUND = new ResourceLocation(
			"minecraft:gui.button.press");

	/**
	 * 
	 * @param x
	 *            xPos
	 * @param y
	 *            yPos
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	public Button(int x, int y, int w, int h, boolean needSound) {
		super(x, y, w, h);
		sound = needSound ? DEFAULT_SOUND : null;
	}

	/**
	 * 
	 * @param x
	 *            xPos
	 * @param y
	 *            yPos
	 * @param w
	 *            width
	 * @param h
	 *            height
	 * 
	 */
	public Button(int x, int y, int w, int h, String customsound) {
		super(x, y, w, h);
		sound = new ResourceLocation(customsound);
	}

	@Override
	public void onMouseDown(double mx, double my) {
		this.onButtonPressed();
		if (sound != null)
			Minecraft
					.getMinecraft()
					.getSoundHandler()
					.playSound(PositionedSoundRecord.func_147674_a(sound, 1.0F));
	}

	public abstract void onButtonPressed();
}
