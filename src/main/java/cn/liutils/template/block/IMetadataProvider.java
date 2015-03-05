/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.block;

/**
 * Use this interface to mark a TileEntity so it has the capability to represent metadata.
 * It is currently used by BlockDirectionedMulti, you can use this abstract interface
 * for any other operations.<br/>
 * P.S>本不必如此麻烦的，要是有多重继承的话……
 * @author WeathFolD
 */
public interface IMetadataProvider {
	public int getMetadata();
	public void setMetadata(int meta);
}
