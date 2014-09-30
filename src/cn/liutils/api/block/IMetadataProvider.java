package cn.liutils.api.block;

/**
 * 本不必如此麻烦的，要是有多重继承的话……
 * @author WeathFolD
 */
public interface IMetadataProvider {
	public int getMetadata();
	public void setMetadata(int meta);
}
