package create;

import java.util.List;

/**
 * 这是一个创建bean的接口
 * @author rongdi
 *
 */
public interface BeanCreator {
	/**
	 * 使用无参的构造器创建bean实例, 不设置任何属性
	 * @param className
	 * @return
	 */
	public Object createBeanUseDefaultConstruct(String className);

	/**
	 * 使用有参数的构造器创建bean实例, 不设置任何属性
	 * @param className
	 * @param args 参数集合
	 * @return
	 */
	public Object createBeanUseDefineConstruct(String className, List<Object> args);
}