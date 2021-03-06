/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under  
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.cgui.loader.xml;

import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cn.liutils.cgui.gui.LIGui;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.WidgetContainer;
import cn.liutils.cgui.gui.component.Component;

/**
 * @author WeAthFolD
 */
public class CGUIDocWriter {
	
	public static CGUIDocWriter instance = new CGUIDocWriter();
	
	DocumentBuilderFactory dbf;
	
	DocumentBuilder db;
	
	WidgetContainer container = new WidgetContainer();

	public CGUIDocWriter() {
		dbf = DocumentBuilderFactory.newInstance();
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void clearContent() {
		container.clear();
	}
	
	public void feed(LIGui gui) {
		for(Map.Entry<String, Widget> entry : gui.getEntries()) {
			container.addWidget(entry.getKey(), entry.getValue());
		}
	}
	
	public boolean saveToXml(File dest) {
		try {
			Document doc = db.newDocument();
			
			Element root = doc.createElement("Widgets");
			doc.appendChild(root);
			
			for(Map.Entry<String, Widget> entry : container.getEntries()) {
				root.appendChild(createWidgetNode(doc, entry.getKey(), entry.getValue()));
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result =  new StreamResult(dest);
			transformer.transform(source, result);
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Element createWidgetNode(Document doc, String name, Widget w) {
		Element root = doc.createElement("Widget");
		
		root.setAttribute("name", name);
		
		//Write components
		Element element = doc.createElement("Components");
		for(Component c : w.getComponentList()) {
			if(!c.canStore()) continue;
			
			Element e = doc.createElement("Component");
			e.setAttribute("class", c.getClass().getName());
			
			//Add properties
			Map<String, String> props = c.getPropertyMap();
			for(Map.Entry<String, String> ent : props.entrySet()) {
				Element a = doc.createElement(ent.getKey());
				a.appendChild(doc.createTextNode(ent.getValue()));
				e.appendChild(a);
			}
			
			element.appendChild(e);
		}
		root.appendChild(element);
		
		//Write sub widgets
		for(Widget child : w.getDrawList()) {
			root.appendChild(createWidgetNode(doc, child.getName(), child));
		}
		
		return root;
	}
	
	public static boolean save(LIGui gui, File file) {
		instance.clearContent();
		instance.feed(gui);
		return instance.saveToXml(file);
	}
	
}
