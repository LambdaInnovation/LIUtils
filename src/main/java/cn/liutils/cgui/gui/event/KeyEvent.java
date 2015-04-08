/**
 * 
 */
package cn.liutils.cgui.gui.event;

/**
 * @author WeAthFolD
 */
public class KeyEvent implements GuiEvent {
	char ch;
	int key;
	
	public KeyEvent(char _ch, int _key) {
		ch = _ch;
		key = _key;
	}
}
