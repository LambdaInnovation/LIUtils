package cn.liutils.cgui.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import cn.liutils.cgui.gui.LIGui;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.component.Draggable;
import cn.liutils.cgui.gui.component.DrawTexture;
import cn.liutils.cgui.loader.xml.CGUIDocLoader;
import cn.liutils.cgui.loader.xml.CGUIDocWriter;

public class Test {
	
	public static void main(String[] args) {
		LIGui gui = new LIGui();
		Widget w1 = new Widget();
		w1.addComponent(new DrawTexture());
		w1.addComponent(new Draggable());
		
		Widget w2 = new Widget();
		w1.addWidget(w2);
		
		Widget w3 = new Widget();
		
		gui.addWidget(w1);
		gui.addWidget(w3);
		
		CGUIDocWriter.save(gui, new File("/text.xml"));
		
		try {
			String xml = IOUtils.toString(new FileInputStream(new File("/text.xml")));
			
			LIGui restore = CGUIDocLoader.load(xml);
			System.out.println(restore.getEntries());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
