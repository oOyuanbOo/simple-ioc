package context;

/**
 * 这是ioc应用容器的接口
 * @author rongdi
 *
 */
public interface ApplicationContext {
	/**
	 * 根据id找到bean对应的对象的实例
	 * @param id
	 * @return
	 */
	public Object getBeanInstance(String id);
	/**
	 * IoC容器中是否包含id为参数的bean
	 * @param id
	 * @return
	 */
	public boolean beanIsExist(String id);

	/**
	 * 判断一个bean是否为单态
	 * @return
	 */
	public boolean isSingleton(String id);

	/**
	 * 从容器中获得bean对应的实例, 如果从容器中找不到该bean, 返回null
	 * @param id
	 * @return
	 */
	public Object getBeanWithoutCreate(String id);
}