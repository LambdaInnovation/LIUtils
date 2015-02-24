/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.api.client.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.prop.Transform;

/**
 * @author WeathFolD
 *
 */
public abstract class WeaponRenderBase extends DrawObject implements IItemRenderer {
	
	public Transform 
		transModel = new Transform(),
		transFirst = new Transform(),
		transThird = new Transform();
	
	public boolean renderInv = false;

	public WeaponRenderBase() {}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch(type) {
		case EQUIPPED_FIRST_PERSON:
		case EQUIPPED:
		case ENTITY:
			return true;
		case INVENTORY:
			return renderInv;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public final void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		switch(type) {
		case EQUIPPED_FIRST_PERSON:
			transFirst.perform();
			transModel.perform();
			renderFirstPerson((EntityPlayer) data[1], item);
			break;
		case EQUIPPED:
			transFirst.perform();
			transModel.perform();
			renderEquipped((EntityLivingBase) data[1], item);
			break;
		case ENTITY:
			transModel.perform();
			renderEntityItem(item);
			break;
		case INVENTORY:
			transModel.perform();
			renderInventory(item);
			break;
		default:
		}
		GL11.glPopMatrix();
	}
	
	public abstract void renderEquipped(EntityLivingBase holder, ItemStack stack);
	
	public abstract void renderFirstPerson(EntityPlayer holder, ItemStack stack);
	
	public abstract void renderEntityItem(ItemStack stack);
	
	public void renderInventory(ItemStack stack) {}

}
