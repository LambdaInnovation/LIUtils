package cn.liutils.cgui.loader;

public enum EditMethod {
	/**
	 * Default edit method, a plain input box where user input a value.
	 * Supports: Str, numeric type, ResourceLocation.
	 */
	INPUTBOX, 
	/**
	 * Use a drag ball to edit value visually.
	 */
	DRAG
}
