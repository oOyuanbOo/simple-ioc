package main.java.xml.element.loader;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * 加载Element
 * @author rongdi
 */
public interface ElementLoader {
	/**
	 * 将所有的element放入map中，key是id
	 * 
	 * @param document
	 */
	public void addBeanElements(Document document);

	/**
	 * 通过id获取Element
	 * 
	 * @param id
	 * @return
	 */
	public Element getBeanElement(String id);

	/**
	 * 获取Elements
	 * 
	 * @return
	 */
	public Collection<Element> getBeanElements();
}
