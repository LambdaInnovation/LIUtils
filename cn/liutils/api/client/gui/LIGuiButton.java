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
 * GUI按钮类，和CBCGuiContainer配合使用
 * 
 * @author WeAthFolD
 * 
 */
public class LIGuiButton extends LIGuiPart {

	public enum ButtonState {
		INVAILD, IDLE, DOWN;
	}

	/**
	 * 两种状态对应贴图的U、V。
	 */
	public int downTexU, downTexV, invaildTexU, invaildTexV;

	/*
	 * 当前的按钮状态。
	 */
	public ButtonState buttonState;

	/**
	 * GUI按钮的构造类。
	 * 
	 * @param name
	 *            按钮名字，用于获取按钮信息。
	 * @param x
	 *            渲染位置X坐标
	 * @param y
	 *            渲染位置Y坐标
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 */
	public LIGuiButton(String name, int x, int y, int w, int h) {
		super(name, x, y, w, h);
		buttonState = ButtonState.IDLE;
	}

	/**
	 * 设置按钮按下时的U、V位置（像素，左上角）
	 * 
	 * @param u
	 * @param v
	 * @return 当前按钮
	 */
	public LIGuiButton setDownCoords(int u, int v) {
		downTexU = u;
		downTexV = v;
		doesDraw = true;
		return this;
	}

	/**
	 * 设置按钮无法使用时的U、V位置（像素，左上角）
	 * 
	 * @param u
	 * @param v
	 * @return 当前按钮
	 */
	public LIGuiButton setInvaildCoords(int u, int v) {
		invaildTexU = u;
		invaildTexV = v;
		doesDraw = true;
		return this;
	}

	/**
	 * 设置当前的按钮状态。
	 * 
	 * @param a
	 */
	public void setButtonState(ButtonState a) {
		if (this.buttonState != ButtonState.INVAILD)
			this.buttonState = a;
	}

	/**
	 * 强制设置按钮状态。
	 * 
	 * @param a
	 *            按钮状态
	 */
	public void setButtonStateForce(ButtonState a) {
		this.buttonState = a;
	}

}
