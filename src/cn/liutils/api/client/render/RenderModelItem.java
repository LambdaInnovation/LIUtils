/** 
 * Copyright (c) Lambda Innovation Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.cn/
 * 
 * The mod is open-source. It is distributed under the terms of the
 * Lambda Innovation Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * 本Mod是完全开源的，你允许参考、使用、引用其中的任何代码段，但不允许将其用于商业用途，在引用的时候，必须注明原作者。
 */
package cn.liutils.api.client.render;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import cn.liutils.api.client.model.IItemModel;
import cn.liutils.api.client.util.RenderUtils;
import cn.liutils.api.debug.IItemRenderInfProvider;

/**
 * 物品模型的渲染器。提供了大量的设置功能以辅助位置调整。
 * 说明：建模位置最好在Techne中的中心点（0, 0, 0），不是地面，否则需要设置Offset。
 * 如果在物品栏中显示过大，需要调整invOffset。
 * @author WeAthFolD
 *
 */
public class RenderModelItem implements IItemRenderer, IItemRenderInfProvider {

	protected Tessellator t = Tessellator.instance;
	IItemModel model;
	ResourceLocation texturePath;
	Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * 模型处于标准位置的旋转角度。
	 */
	public Vec3 stdRotation = initVec();
	
	/**
	 * 模型处于标准位置的位移量。
	 */
	public Vec3 stdOffset = initVec();
	
	/**
	 * 模型的标准缩放度。
	 */
	public float scale = 1.0F;
	
	/**
	 * 装备时模型向前移动的量。
	 */
	public Vec3 equipOffset = initVec();
	
	/**
	 * 装备时的特殊旋转角度。
	 */
	public Vec3 equipRotation = initVec();
	
	/**
	 * 物品栏渲染的大小缩放比例。
	 */
	public float invScale = 1.0F;
	
	/**
	 * 物品栏渲染的位移。
	 */
	public Vector2f invOffset = new Vector2f();
	
	public Vec3 entityItemRotation = initVec();
	
	public Vec3 invRotation = initVec();
	
	/**
	 * 是否渲染物品栏。
	 */
	public boolean renderInventory = true;
	
	/**
	 * 是否渲染物品实体。
	 */
	public boolean renderEntityItem = true;
	
	/**
	 * 是否让物品栏中的模型一直旋转。
	 */
	public boolean inventorySpin = true;
	
	protected static Random RNG = new Random();
	
	/**
	 * 喜闻乐见的构造器。
	 * @param mdl 模型类
	 * @param texture 对应的贴图文件
	 */
	public RenderModelItem(IItemModel mdl, ResourceLocation texture) {
		model = mdl;
		texturePath = texture;
	}
	
	public RenderModelItem setRenderInventory(boolean b) {
		renderInventory = b;
		return this;
	}
	
	public RenderModelItem setRenderEntityItem(boolean b) {
		renderEntityItem = b;
		return this;
	}
	
	public RenderModelItem setInventorySpin(boolean b) {
		inventorySpin = b;
		return this;
	}
	
	public RenderModelItem setEntityItemRotation(float b0, float b1, float b2) {
		initVec(entityItemRotation, b0, b1, b2);
		return this;
	}
	
	public RenderModelItem setEquipOffset(float b0, float b1, float b2) {
		initVec(equipOffset, b0, b1, b2);
		return this;
	}
	
	public RenderModelItem setRotation(float x, float y, float z) {
		initVec(stdRotation, x, y, z);
		return this;
	}
	
	public RenderModelItem setInvRotation(float x, float y, float z) {
		initVec(invRotation, x, y, z);
		return this;
	}
	
	public RenderModelItem setScale(float s) {
		scale = s;
		return this;
	}
	
	public RenderModelItem setEquipRotation(float x, float y, float z) {
		initVec(equipRotation, x, y, z);
		return this;
	}
	
	public RenderModelItem setInvScale(float s) {
		invScale = s;
		return this;
	}
	
	public RenderModelItem setOffset(float offsetX, float offsetY, float offsetZ) {
		initVec(stdOffset, offsetX, offsetY, offsetZ);
		return this;
	}
	
	public RenderModelItem setInvOffset(float offsetX, float offsetY) {
		this.invOffset.set(offsetX, offsetY);
		return this;
	}
	
	public RenderModelItem setInformationFrom(RenderModelItem a) {
		
		this.renderInventory = a.renderInventory;
		this.invOffset = a.invOffset;
		this.setInvScale(a.invScale);
		
		this.stdOffset = a.stdOffset;
		this.stdRotation = a.stdRotation;
		
		this.scale = a.scale;
		return this;
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
			return true;
		case ENTITY:
			return renderEntityItem;
		case INVENTORY:
			return renderInventory;

		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		switch (helper) {
		case ENTITY_ROTATION:
		case ENTITY_BOBBING:
			return true;

		default:
			return false;

		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
			renderEquipped(item, (RenderBlocks) data[0], (EntityLivingBase) data[1], type);
			break;
		case ENTITY:
			renderEntityItem((RenderBlocks)data[0], (EntityItem) data[1]);
			break;
		case INVENTORY:
			renderInventory();
			break;
		default:
			break;

		}
	}
	
	public void renderInventory() {
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		
		RenderUtils.loadTexture(texturePath);
		GL11.glTranslatef(8.0F + invOffset.x, 8.0F + invOffset.y, 0.0F);
		GL11.glScalef(16F * invScale, 16F * invScale, 16F * invScale);
		float rotation = 145F;
		if(inventorySpin) {
			rotation = Minecraft.getSystemTime() / 100F;
		}
		GL11.glRotatef(rotation, 0, 1, 0);
		this.doRotation(invRotation);
		GL11.glScalef(-1F, -1F, 1F);
		
		renderAtStdPosition();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
	
	public void renderEntityItem(RenderBlocks render, EntityItem ent) {
		
		GL11.glPushMatrix();
		RenderUtils.loadTexture(texturePath);
		
		//GL11.glTranslatef(0F , 0.5F , 0F);
		this.doRotation(entityItemRotation);
		renderAtStdPosition();
		
		GL11.glPopMatrix();
	}
	
	public void renderEquipped(ItemStack item, RenderBlocks render,
			EntityLivingBase entity, ItemRenderType type) {

		if (item.stackTagCompound == null)
			item.stackTagCompound = new NBTTagCompound();
		
		GL11.glPushMatrix();
		RenderUtils.loadTexture(texturePath);
		float sc2 = 0.0625F;
		GL11.glRotatef(40F, 0, 0, 1);
		this.doTransformation(equipOffset);
		this.doRotation(equipRotation);
		GL11.glRotatef(90, 0, -1, 0);
		renderAtStdPosition(getModelAttribute(item, entity));
		GL11.glPopMatrix();
		
	}
	
	/**
	 * 如果参数被设置正确，模型应该被渲染在（0, 0, 0）为中心的四周。
	 */
	protected final void renderAtStdPosition() {
		renderAtStdPosition(0.0F);
	}
	
	protected final void renderAtStdPosition(float i) {
		GL11.glScalef(scale, scale, scale);
		this.doTransformation(stdOffset);
		GL11.glScalef(-1F , -1F , 1F );
		
		GL11.glRotated(stdRotation.yCoord + 180, 0.0F, 1.0F, 0.0F); //历史遗留问题，没什么必要改啦=w=
		GL11.glRotated(stdRotation.zCoord, 0.0F, 0.0F, 1.0F);
		GL11.glRotated(stdRotation.xCoord, 1.0F, 0.0F, 0.0F);
		
		model.render(null, 0.0625F, i);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	protected float getModelAttribute(ItemStack item, EntityLivingBase entity) {
		return 0.1F;
	}

	@Override
	public String getFullInformation() {
		StringBuilder sb =  new StringBuilder("[STD OFFSET: " + stdOffset + "]\n");
		return sb.toString();
	}

	private <T> String formatNumber(T... nums) {
		StringBuilder sb = new StringBuilder("(");
		for(T n : nums) {
			sb.append(n).append(", ");
		}
		return sb.append(")").toString();
	}
	
	protected void doTransformation(Vec3 vec3) {
		if(vec3 != null)
			GL11.glTranslated(vec3.xCoord, vec3.yCoord, vec3.zCoord);
	}
	
	protected void doRotation(Vec3 vec3) {
		if(vec3 != null) {
			GL11.glRotated(vec3.yCoord, 0.0F, 1.0F, 0.0F);
			GL11.glRotated(vec3.zCoord, 0.0F, 0.0F, 1.0F);
			GL11.glRotated(vec3.xCoord, 1.0F, 0.0F, 0.0F);
		}
	}
	
	private static void initVec(Vec3 vec) {
		vec = vec == null ?  Vec3.createVectorHelper(0D, 0D, 0D) : vec;
	}
	
	private static Vec3 initVec() {
		return Vec3.createVectorHelper(0D, 0D, 0D);
	}
	
	private static void initVec(Vec3 vec, double x, double y, double z) {
		if(vec == null)
			vec = Vec3.createVectorHelper(x, y, z);
		else {
			vec.xCoord = x;
			vec.yCoord = y;
			vec.zCoord = z;
		}
	}

}
