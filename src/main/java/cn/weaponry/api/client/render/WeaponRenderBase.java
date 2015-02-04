/**
 * 
 */
package cn.weaponry.api.client.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.tess.Transform;

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
