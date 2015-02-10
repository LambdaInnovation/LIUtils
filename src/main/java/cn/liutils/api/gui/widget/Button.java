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
