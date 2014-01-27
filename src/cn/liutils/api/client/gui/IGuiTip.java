/** 
 */
package cn.liutils.api.client.gui;

/**
 * 在GUI中显示提示文字，与CBCGuiPart和CBCGuiContainer配合使用。
 * 
 * @author WeAthFolD
 */
public interface IGuiTip {

	/**
	 * 获取头行文字。(请自己指定颜色）
	 */
	String getHeadText();

	/**
	 * 获取说明信息。
	 */
	String getTip();
}
