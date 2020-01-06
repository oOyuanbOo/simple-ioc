package xml.element.parser;

import java.util.List;

import xml.autowire.Autowire;
import xml.element.LeafElement;
import xml.element.PropertyElement;
import org.dom4j.Element;

/**
 * 获取Element的属性
 * @author rongdi
 *
 */
public interface BeanElementParser {
	/**
	 * 懒加载
	 * @return
	 */
	public boolean isLazy(Element beanElement);
	
	/**
	 * 构造函数参数
	 * @return
	 */
	public List<Element> getConstructorArgsElements(Element bean);
	
	/**
	 * 通过属性name获取属性值
	 * @param element
	 * @param name
	 * @return
	 */
	public String getAttribute(Element element, String name);
	
	/**
	 * 是否单例
	 * @return
	 */
	public boolean isSingleton(Element bean);
	
	/**
	 * 获取bean的属性值
	 * @return
	 */
	public List<Element> getPropertyElements(Element bean);
	
	/**
	 * 获取Autowire属性，有三种  no byName  byType
	 * @return
	 */
	public Autowire getAutowire(Element bean);
	
	/**
	 *
	 * @return
	 */
	public List<LeafElement> getConstructorValue(Element bean);
	
	/**
	 * @return
	 */
	List<PropertyElement> getPropertyValue(Element bean);
}
