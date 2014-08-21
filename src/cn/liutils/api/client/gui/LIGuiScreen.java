/**
 * Copyright (C) Lambda-Innovation, 2013-2014
 * This code is open-source. Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 */
package cn.liutils.api.client.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.liutils.api.client.gui.LIGuiButton.ButtonState;
import cn.liutils.api.client.util.HudUtils;
import cn.liutils.api.client.util.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;

/**
 * @author WeAthFolD
 *
 */
public abstract class LIGuiScreen extends GuiScreen {
	
	/**
	 * List of all GUI elements
	 */
	private HashSet<LIGuiPart> elements;
	
	int xSize, ySize;

	public LIGuiScreen(int xSize, int ySize) {
		elements = new HashSet<LIGuiPart>();
		this.xSize = xSize;
		this.ySize = ySize;
	}
	
	/**
	 * Add a GUI part.
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
	 * A process for each button's pressing movement.
	 * @param button
	 */
	public abstract void onButtonClicked(LIGuiButton button, boolean is);

	/**
	 * Set a state for a given button.
	 * @param buttonName
	 * @param state
	 */
	public void setButtonState(String buttonName, ButtonState state) {
		getButton(buttonName).state = state;
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
		LIGuiButton button = getButton(name);
		return button == null ? null : button.state;
	}

	/**
	 * 绘制按钮，请务必在drawGuiBackgroundLayer()中调用。
	 */
	public void drawElements() {
		for (LIGuiPart e : elements) {
			drawElement(e);
		}
		for(Set<LIGuiPart> st : getAdditionalButtons()) {
			for(LIGuiPart e : st)
				drawElement(e);
		}
	}
	
	private void drawElement(LIGuiPart e) {
		if (!e.doesDraw)
			return;
		if (e instanceof LIGuiButton) {
			LIGuiButton b = (LIGuiButton) e;
			int x = (width - xSize) / 2;
			int y = (height - ySize) / 2;
			int texU = 0, texV = 0;

			if (b.state == ButtonState.IDLE) {
				texU = b.texU;
				texV = b.texV;
			} else if (b.state == ButtonState.DOWN) {
				texU = b.downTexU;
				texV = b.downTexV;
			} else if (b.state == ButtonState.INVAILD) {
				texU = b.invaildTexU;
				texV = b.invaildTexV;
			}

			if(b.hasTexOverride()) RenderUtils.loadTexture(b.texOverride);
			HudUtils.drawTexturedModalRect(x + b.posX, y + b.posY, texU, texV,
					b.width, b.height, b.texWidth, b.texHeight);
		} else {
			int x = (width - xSize) / 2;
			int y = (height - ySize) / 2;
			int texU = e.texU, texV = e.texV;
			if(e.hasTexOverride()) RenderUtils.loadTexture(e.texOverride);
			HudUtils.drawTexturedModalRect(x + e.posX, y + e.posY, texU, texV,
					e.width, e.height, e.texWidth, e.texHeight);
		}
	}

	@Override
	/*
	 * 绘制GUI上层图像。请务必在子类中调用它以绘制Tip。
	 */
	public void drawScreen(int par1, int par2, float par3)
    {
		super.drawScreen(par1, par2, par3);
		IGuiTip currentTip = null;
		for (LIGuiPart b : elements) {
			if (isPointWithin(b, par1, par2) && b.hasToolTip()) {
				currentTip = b.tip;
			}
		}
		for(Set<LIGuiPart> st : getAdditionalButtons()) {
			for(LIGuiPart b : st) {
				if (isPointWithin(b, par1, par2) && b.hasToolTip()) {
					currentTip = b.tip;
				}
			}
		}
		
		if (currentTip != null) {
			boolean drawHead = currentTip.getHeader() != "";
			List<String> list = new ArrayList();
			if (drawHead) {
				list.add(currentTip.getHeader());
			}
			int x = (width - xSize) / 2, y = (height - ySize) / 2;
			list.add(currentTip.getText());
			this.func_146283_a(list, par1 - x, par2 - y);
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		for (LIGuiPart e : elements) {
			checkElement(e, par1, par2, par3, true);
		}
		for(Set<LIGuiPart> st : getAdditionalButtons()) {
			System.out.println("set" + st);
			for(LIGuiPart e : st) {
				System.out.println(e.name);
				checkElement(e, par1, par2, par3, false);
			}
		}
	}
	
	private void checkElement(LIGuiPart e, int par1, int par2, int par3, boolean is) {
		if (!(e instanceof LIGuiButton))
			return;
		LIGuiButton b = (LIGuiButton) e;
		if (isPointWithin(b, par1, par2)) {
			if (b.state != ButtonState.INVAILD) {
				b.setButtonState(ButtonState.DOWN);
				onButtonClicked(b, is);
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		super.mouseMovedOrUp(par1, par2, par3);
		/*
		if (par3 == 0 || par3 == 1) {
			for (LIGuiPart b : elements)
				elementMoved(b, par1, par2, par3);
			for(Set<LIGuiPart> st : getAdditionalButtons()) {
				for(LIGuiPart b : st) {
					elementMoved(b, par1, par2, par3);
				}
			}
		}*/
	}
	
	private void elementMoved(LIGuiPart b, int par1, int par2, int par3) {
		if (!(b instanceof LIGuiButton))
			return;
		if (isPointWithin(b, par1, par2))
			((LIGuiButton) b).setButtonState(ButtonState.IDLE);
	}

	protected boolean isPointWithin(LIGuiPart element, int x, int y) {
		float x0 = (this.width - this.xSize) / 2F,
			y0 = (this.height - this.ySize) / 2F;
		float epx = element.posX + x0, epy = element.posY + y0;
		//System.out.println(epx + ", " + epy + " - " + element.name);
		return epx <= x && epy <= y && x <= epx + element.width && y <= epy + element.height;
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
	
	public Set<LIGuiPart>[] getAdditionalButtons() {
		return new Set[] {};
	}
}
