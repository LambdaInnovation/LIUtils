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
 * LambdaCraft是完全开源的。它的发布遵从《LambdaCraft开源协议》。你允许阅读，修改以及调试运行
 * 源代码， 然而你不允许将源代码以另外任何的方式发布，除非你得到了版权所有者的许可。
 */
package cn.liutils.api.client.gui;

/**
 * LambdaCraft GUI元素。
 * 
 * @author WeAthFolD
 * 
 */
public class LIGuiPart {

	/**
	 * 该GUI元素的名称。
	 */
	public String name;

	/**
	 * GUI元素位置。
	 */
	public int posX, posY;

	/**
	 * GUI元素的大小。
	 */
	public int width, height;

	public int texU, texV;

	public boolean doesDraw;

	public IGuiTip tip;

	/**
	 * 
	 */
	public LIGuiPart(String n, int x, int y, int w, int h) {
		name = n;
		posX = x;
		posY = y;
		width = w;
		height = h;
		doesDraw = false;
	}

	/**
	 * 设置按钮是否被渲染。
	 * 
	 * @param b
	 * @return
	 */
	public LIGuiPart setDraw(boolean b) {
		this.doesDraw = b;
		return this;
	}

	/**
	 * 设置GUI元素空闲时的U、V位置（像素，左上角）
	 * 
	 * @param u
	 * @param v
	 * @return 当前按钮
	 */
	public LIGuiPart setTextureCoords(int u, int v) {
		texU = u;
		texV = v;
		doesDraw = true;
		return this;
	}

	/**
	 * 判断是否拥有鼠标移上去时提示。
	 * 
	 * @return
	 */
	public boolean hasToolTip() {
		return this.tip != null;
	}

}
