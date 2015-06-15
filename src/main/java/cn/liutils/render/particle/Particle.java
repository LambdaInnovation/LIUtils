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
package cn.liutils.render.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import cn.annoreg.core.Registrant;
import cn.annoreg.mc.RegEntity;
import cn.liutils.entityx.EntityAdvanced;
import cn.liutils.entityx.handlers.Rigidbody;
import cn.liutils.util.helper.Color;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
@Registrant
@RegEntity(clientOnly = true)
@RegEntity.HasRender
public final class Particle extends EntityAdvanced implements ISpriteEntity {
	
	@RegEntity.Render
	public static RenderIcon render;
	
	public ResourceLocation texture = null;
	public Color color = Color.WHITE();
	public float size = 1.0f;
	public boolean hasLight = false;

	long creationTime;
	
	int fadeTime;
	int life = 10000000;
	double startAlpha;
	
	public Particle() {
		super(null);
	}
	
	public void fadeAfter(int life, int fadeTime) {
		this.life = life;
		this.fadeTime = fadeTime;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(ticksExisted > life) {
			int dt = ticksExisted - life;
			double alpha = 1 - (double) dt / fadeTime;
			if(alpha < 0) {
				setDead();
				alpha = 0;
			}
			color.a = alpha * startAlpha;
		}
	}
	
	public void fromTemplate(Particle template) {
		this.texture = template.texture;
		this.color = template.color.copy();
		this.size = template.size;
		this.hasLight = template.hasLight;
		this.fadeTime = template.fadeTime;
		this.life = template.life;
	}
	
	@Override
	protected void onFirstUpdate() {
		this.addMotionHandler(new Rigidbody());
		creationTime = Minecraft.getSystemTime();
		startAlpha = color.a;
	}
	
	public long getParticleLife() {
		return Minecraft.getSystemTime() - creationTime;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		setDead();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {}

	@Override
	public void updateSprite(Sprite s) {
		s.cullFace = false;
		s.height = s.width = size;
		s.texture = texture;
		s.color = color;
		s.hasLight = hasLight;
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	public boolean needViewOptimize() {
		return false;
	}
	
	

}
