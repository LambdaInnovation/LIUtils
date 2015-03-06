/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.client.render.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * A RenderIcon which repeatly plays what's inside icons array. the framerate can be set.
 * @author WeAthFolD
 */
public class RenderIconAnimated extends RenderIcon {

	private ResourceLocation[] icons;
	private int frameRate = 1; //tick/frame
	
	public RenderIconAnimated(ResourceLocation[] ics) {
		super(ics[0]);
		icons = ics;
	}
	
	public RenderIconAnimated(ResourceLocation[] ics, int fs) {
		this(ics);
		this.frameRate = fs;
	}
	
	@Override
	public void doRender(Entity entity, double par2, double par4,
			double par6, float par8, float par9) {
		int delta = (entity.ticksExisted * frameRate) % icons.length;
		delta /= frameRate;
		this.icon = icons[delta];
		super.doRender(entity, par2, par4, par6, par8, par9);
	}

}
