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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.client.gui.part.LIGuiPart;

/**
 * @author WeAthFolD
 *
 */
public abstract class LIGuiScreen extends GuiScreen {
	
	/**
	 * List of all GUI elements
	 */
	private HashSet<LIGuiPage> activatePages = new HashSet<LIGuiPage>();
	
	int xSize, ySize;

	public LIGuiScreen(int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
	}

	/**
	 * 绘制按钮，请务必在drawGui时调用。
	 */
	public void drawElements(int mx, int my) {
		update();
		for(LIGuiPage pg : activatePages) {
			Iterator<LIGuiPart> parts = pg.getParts();
			float x0 = (width - xSize) / 2F;
			float y0 = (height - ySize) / 2F;
			
			
			GL11.glPushMatrix(); {
				GL11.glTranslatef(x0 + pg.originX, y0 + pg.originY, 0F);
				pg.drawPage();
			} GL11.glPopMatrix();
			
			while(parts.hasNext()) {
				LIGuiPart pt = parts.next();
				if(pt != null && pt.doesDraw) {
					drawElement(pg, pt, mx, my, x0, y0);
				}
			}
		}
	}
	
	private void drawElement(LIGuiPage page, LIGuiPart e, int x, int y, float x0, float y0) {
		GL11.glPushMatrix(); {
			GL11.glTranslatef(x0 + e.posX + page.originX, y0 + e.posY + page.originY, 0F);
			e.drawAtOrigin(isPointWithin(e, page, x, y));
		} GL11.glPopMatrix();
	}

	@Override
	/*
	 * 绘制GUI上层图像。请务必在子类中调用它以绘制Tip。
	 */
	public void drawScreen(int par1, int par2, float par3) {}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		update();
		for(LIGuiPage pg : activatePages) {
			Iterator<LIGuiPart> parts = pg.getParts();
			while(parts.hasNext()) {
				LIGuiPart pt = parts.next();
				checkElement(pt, pg, par1, par2, par3);
			}
		}
	}
	
	private void checkElement(LIGuiPart b, LIGuiPage pg, int par1, int par2, int par3) {
		float x0 = (this.width - this.xSize) / 2F,
				y0 = (this.height - this.ySize) / 2F;
		float hitX = par1 - x0 - pg.originX, hitY = par2 - y0 - pg.originY;
		if (isPointWithin(b, pg, par1, par2)) {
			if(b.onPartClicked(hitX - b.posX, hitY - b.posY))
				pg.onPartClicked(b, hitX, hitY);
		}
	}

	protected boolean isPointWithin(LIGuiPart element, LIGuiPage page, int x, int y) {
		float x0 = (this.width - this.xSize) / 2F,
			y0 = (this.height - this.ySize) / 2F;
		float epx = element.posX + x0 + page.originX, epy = element.posY + y0 + page.originY;
		return epx <= x && epy <= y && x <= epx + element.width && y <= epy + element.height;
	}
	
	private final void update() {
		activatePages.clear();
		updateActivatedPages(activatePages);
	}
	
	public abstract void updateActivatedPages(Set<LIGuiPage> pages);
}
