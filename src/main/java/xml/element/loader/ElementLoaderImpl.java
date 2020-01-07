package xml.element.loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xml.element.loader.ElementLoader;
import org.dom4j.Document;
import org.dom4j.Element;

public class ElementLoaderImpl implements ElementLoader {
	/**
	 * beanElements，id是key
	 */
	Map<String,Element> beanElements = new HashMap<String,Element>();
	/**
	 * 将element放入beanElements
	 * @param document
	 */
	@Override
	public void addBeanElements(Document document) {
		/**
		 * 从根节点开始获取所有的element
		 */
		@SuppressWarnings("unchecked")
		List<Element> elementList = document.getRootElement().elements();
		/**
		 * 遍历
		 */
		for(Element e:elementList) {
			/**
			 * 获取id
			 */
			String id = e.attributeValue("id");
			/**
			 * 存入map
			 */
			this.beanElements.put(id, e);
		}
	}

	@Override
	public Element getBeanElement(String id) {
		/**
		 * 通过id获取element 这个map就相当于bean的容器
		 */
		return beanElements.get(id);
	}

	@Override
	public Collection<Element> getBeanElements() {
		/**
		 * 获取所有的Elements
		 */
		return beanElements.values();
	}

}
