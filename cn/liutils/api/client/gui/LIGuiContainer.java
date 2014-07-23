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
 * LambdaCraft是完全开源的。它的发布遵从《LambdaCraft开源协议》你允许阅读，修改以及调试运行
 * 源代码， 然而你不允许将源代码以另外任何的方式发布，除非你得到了版权所有者的许可。
 */
package cn.liutils.api.client.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.liutils.api.client.gui.LIGuiButton.ButtonState;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * @author WeAthFolD LambdaCraft的GUI Container，目前具有： 按钮功能 区域Tip功能
 */
public abstract class LIGuiContainer extends GuiContainer {
	
	/**
	 * GUI元素列表。
	 */
	private HashSet<LIGuiPart> elements;

	public LIGuiContainer(Container par1Container) {
		super(par1Container);
		elements = new HashSet<LIGuiPart>();
	}

	
	/**绑定贴图 By Rikka0_0*/
	protected void bindTexture(String texture){
		mc.renderEngine.bindTexture(new ResourceLocation(texture));
	}
	
	protected void bindTexture(ResourceLocation texture){
		mc.renderEngine.bindTexture(texture);
	}
	
	
	/**
	 * 添加一个按钮。
	 * 
	 * @param part
	 */
	public void addElement(LIGuiPart part) {
		elements.add(part);
	}

	public void addElements(LIGuiPart... parts) {
		for (LIGuiPart p : parts)
			addElement(p);
	}

	/**
	 * 处理每个按钮按下时行为的函数，在子类实现它来做些什么。
	 * 
	 * @param button
	 *            被按下的按钮
	 */
	public abstract void onButtonClicked(LIGuiButton button);

	/**
	 * 设置某一个按钮的状态。
	 * 
	 * @param buttonName
	 *            按钮名称
	 * @param state
	 *            状态
	 */
	public void setButtonState(String buttonName, ButtonState state) {
		getButton(buttonName).buttonState = state;
	}

	/**
	 * @param buttonName
	 * @param tip
	 * @return
	 */
	public boolean setElementTip(String buttonName, IGuiTip tip) {
		getElement(buttonName).tip = tip;
		return true;
	}

	/**
	 * 获取某一按钮的状态
	 * 
	 * @param name
	 *            按钮名称
	 * @return 对应按钮状态
	 */
	public ButtonState getButtonState(String name) {
		return getButton(name).buttonState;
	}

	/**
	 * 绘制按钮，请务必在drawGuiBackgroundLayer()中调用。
	 */
	public void drawElements() {
		for (LIGuiPart e : elements) {
			if (!e.doesDraw)
				continue;
			if (e instanceof LIGuiButton) {
				LIGuiButton b = (LIGuiButton) e;
				int x = (width - xSize) / 2;
				int y = (height - ySize) / 2;
				int texU = 0, texV = 0;

				if (b.buttonState == ButtonState.IDLE) {
					texU = b.texU;
					texV = b.texV;
				} else if (b.buttonState == ButtonState.DOWN) {
					texU = b.downTexU;
					texV = b.downTexV;
				} else if (b.buttonState == ButtonState.INVAILD) {
					texU = b.invaildTexU;
					texV = b.invaildTexV;
				}

				drawTexturedModalRect(x + b.posX, y + b.posY, texU, texV,
						b.width, b.height);
			} else {
				int x = (width - xSize) / 2;
				int y = (height - ySize) / 2;
				int texU = e.texU, texV = e.texV;
				drawTexturedModalRect(x + e.posX, y + e.posY, texU, texV,
						e.width, e.height);
			}
		}
	}

	@Override
	/*
	 * 绘制GUI上层图像。请务必在子类中调用它以绘制Tip。
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		IGuiTip currentTip = null;
		for (LIGuiPart b : elements) {
			if (isPointWithin(b, par1, par2) && b.hasToolTip()) {
				currentTip = b.tip;
			}
		}
		if (currentTip != null) {
			boolean drawHead = currentTip.getHeadText() != "";
			List<String> list = new ArrayList();
			if (drawHead) {
				list.add(currentTip.getHeadText());
			}
			int x = (width - xSize) / 2, y = (height - ySize) / 2;
			list.add(currentTip.getTip());
			this.func_102021_a(list, par1 - x, par2 - y);
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		for (LIGuiPart e : elements) {
			if (!(e instanceof LIGuiButton))
				continue;
			LIGuiButton b = (LIGuiButton) e;
			if (isPointWithin(b, par1, par2)) {
				if (b.buttonState != ButtonState.INVAILD) {
					b.setButtonState(ButtonState.DOWN);
					onButtonClicked(b);
				}
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		super.mouseMovedOrUp(par1, par2, par3);

		if (par3 == 0 || par3 == 1) {
			for (LIGuiPart b : elements) {
				if (!(b instanceof LIGuiButton))
					continue;
				if (isPointWithin(b, par1, par2))
					((LIGuiButton) b).setButtonState(ButtonState.IDLE);
			}
		}

	}

	protected boolean isPointWithin(LIGuiPart element, int x, int y) {
		return this.isPointInRegion(element.posX, element.posY, element.width,
				element.height, x, y);
	}

	protected LIGuiButton getButton(String name) {
		LIGuiPart elem = getElement(name);
		if (elem != null && elem instanceof LIGuiButton)
			return (LIGuiButton) elem;
		return null;
	}

	protected LIGuiPart getElement(String name) {
		for (LIGuiPart b : elements) {
			if (b.name == name)
				return b;
		}
		return null;
	}

}
