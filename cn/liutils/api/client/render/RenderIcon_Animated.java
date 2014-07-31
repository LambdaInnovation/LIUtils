/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.client.render;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * 渲染时会循环播放ResourceLocation数组所指向的贴图内容。可以手动设置帧率。
 * @author WeAthFolD
 *
 */
public class RenderIcon_Animated extends RenderIcon {

	private ResourceLocation[] icons;
	private int frameSpeed = 1; //帧速度：n tick/frame
	
	/**
	 * @param ic
	 */
	public RenderIcon_Animated(ResourceLocation[] ics) {
		super(ics[0]);
		icons = ics;
	}
	
	public RenderIcon_Animated(ResourceLocation[] ics, int fs) {
		this(ics);
		this.frameSpeed = fs;
	}
	
	@Override
	public void doRender(Entity entity, double par2, double par4,
			double par6, float par8, float par9) {
		int delta = (entity.ticksExisted * frameSpeed) % icons.length;
		delta /= frameSpeed;
		this.icon = icons[delta];
		super.doRender(entity, par2, par4, par6, par8, par9);
	}

}
