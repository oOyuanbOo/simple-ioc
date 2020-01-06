package xml.element.parser;

import java.util.ArrayList;
import java.util.List;

import util.IocUtil;
import xml.autowire.Autowire;
import xml.autowire.ByNameAutowire;
import xml.autowire.NoAutowire;
import xml.element.*;
import org.dom4j.Element;

public class BeanElementParserImpl implements BeanElementParser {
	/**
	 * 获取lazy-init属性值
	 */
	public boolean isLazy(Element beanElement) {

		String elementLazy = this.getAttribute(beanElement, "lazy-init");

		Element parentElement = beanElement.getParent();
		// default-lazy-init 和 bean上的lazy-init的区别
		// default-lazy-init 是<beans>上的属性
		// lazy-init 是<bean>上的属性, 优先级高于 default-lazy-init
		Boolean parentElementLazy = new Boolean(this.getAttribute(parentElement, "default-lazy-init"));
		if (parentElementLazy) {
			if ("false".equals(elementLazy)) {
				return false;
			}
			return true;
		} else {
			if ("true".equals(elementLazy)) {
				return true;
			}
			return false;
		}
		
	}
	/**
	 * 获取构造器参数集合，都是以element作为元素
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Element> getConstructorArgsElements(Element element) {

		List<Element> children = element.elements();

		List<Element> result = new ArrayList<Element>();
		/**
		 * 获取构造器参数
		 */
		for (Element e : children) {
			if ("constructor-arg".equals(e.getName())) {
				result.add(e);
			}
		}
		return result;
	}

	@Override
	public String getAttribute(Element element, String name) {
		String value = element.attributeValue(name);
		return value;
	}

	@Override
	public boolean isSingleton(Element element) {
		Boolean singleton = new Boolean(this.getAttribute(element, "singleton"));
		return singleton;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Element> getPropertyElements(Element element) {

		List<Element> children = element.elements();

		List<Element> result = new ArrayList<Element>();

		for (Element e : children) {
			if("property".equals(e.getName())) {
				result.add(e);
			}
		}

		return result;
	}

	@Override
	public Autowire getAutowire(Element element) {

		String type = this.getAttribute(element, "xml/autowire");

		String parentType = this.getAttribute(element.getParent(), "default-xml.autowire");

		// 要区分beans上的参数default-xml.autowire，和autowire要区分开，后者优先级更高
		if ("no".equals(parentType)) {
			if ("byName".equals(type)) {
				return new ByNameAutowire(type);
			}
			return new NoAutowire(type);
		} else if ("byName".equals(parentType)) {
			if ("no".equals(type)) {
				return new NoAutowire(type);
			}
			return new ByNameAutowire(type);
		}
		return new NoAutowire(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LeafElement> getConstructorValue(Element element) {

		List<Element> cons = this.getConstructorArgsElements(element);

		List<LeafElement> result = new ArrayList<LeafElement>();

		for (Element e : cons) {

			List<Element> eles = e.elements();

			LeafElement leafElement = this.getLeafElement(eles.get(0));

			result.add(leafElement);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PropertyElement> getPropertyValue(Element element) {

		List<Element> properties = this.getPropertyElements(element);

		List<PropertyElement> result = new ArrayList<PropertyElement>();

		for (Element e : properties) {

			List<Element> eles = e.elements();

			LeafElement leafElement = getLeafElement(eles.get(0));

			String propertyNameAtt = this.getAttribute(e, "name");

			PropertyElement pe = new PropertyElement(propertyNameAtt, leafElement);

			result.add(pe);
		}

		return result;
	}

	private LeafElement getLeafElement(Element leafElement) {

		String name = leafElement.getName();

		if ("value".equals(name)) {			// 对象名称字符串

			return new ValueElement(this.getValueOfValueElement(leafElement));
		}

		else if("ref".equals(name)) {		// 对象的引用

			return new RefElement(this.getAttribute(leafElement, "bean"));
		}
		// 每个键值对构成一个对象
		else if("collection".equals(name)) {

			return this.getCollectionElement(leafElement);
		}

		return null;
	}

	private Object getValueOfValueElement(Element leafElement) {

		String typeName = leafElement.attributeValue("type");

		String data = leafElement.getText();

		return IocUtil.getValue(typeName, data);
	}

	@SuppressWarnings("unchecked")
	private CollectionElement getCollectionElement(Element leafElement) {

		List<LeafElement> temp = new ArrayList<LeafElement>();

		String typeName = leafElement.attributeValue("type");

		CollectionElement ce = new CollectionElement(typeName);

		List<Element> elements = leafElement.elements();

		for(Element e:elements) {

			String tempName = e.getName();

			if("value".equals(tempName)) {
				temp.add(new ValueElement(this.getValueOfValueElement(e)));
			}

			else if("ref".equals(tempName)) {
				temp.add(new RefElement(this.getAttribute(e, "bean")));
			}
		}

		ce.setList(temp);

		return ce;
	}
	

}
