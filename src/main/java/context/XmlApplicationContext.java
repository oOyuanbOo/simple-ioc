package main.java.context;

/**
 * 这是真正使用的IoC的应用框架类，可以创建所有配置好的类的实例
 * @author Administrator
 *
 */
public class XmlApplicationContext extends AbstractApplicationContext {


	public XmlApplicationContext(String[] xmlPaths) {
		/**
		 * 初始化文档和元素
		 */
		initElements(xmlPaths);
		/**
		 * 创建所有bean的实例
		 */
		createBeanInstances();
	}
}