package main.java.create;

import java.util.List;

/**
 * 创建bean
 * @author rongdi
 *
 */
public interface BeanCreator {
	/**
	 * @param className
	 * @return
	 */
	public Object createBeanUseDefaultConstruct(String className);
	
	/**
	 * @param className
	 * @param args ��������
	 * @return
	 */
	public Object createBeanUseDefineConstruct(String className, List<Object> args);
}
