package create;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 处理属性的接口
 * @author rongdi
 *
 */
public interface PropertyHandler {
	/**
	 * 为对象obj设置属性，第一个参数是需要设置值的对象，第二个参数是给Object里面的变量
	 * 设什么值，Map的key就是property元素的name属性对应于对象的成员变量名，Map的value
	 * 就是对应的值
	 * @param obj
	 * @param properties 属性集合
	 * @return
	 */
	public Object setProperties(Object obj, Map<String, Object> properties);
	/**
	 * 返回一个对象里面所有的setter方法, 封装成map, key为setter方法名去掉set后的字符串
	 * 至于为什么是这样的，具体原因在实现类中的私有方法getMethodNameWithOutSet已经做了
	 * 详细的解释
	 * @param obj
	 * @return
	 */
	public Map<String, Method> getSetterMethodsMap(Object obj);

	/**
	 * 使用反射执行一个方法，主要是来完成调用一次就为对象设置一个属性
	 * @param object 需要执行方法的对象
	 * @param argBean 参数的bean
	 * @param method setXX方法对象
	 */
	public void executeMethod(Object object, Object argBean, Method method);

}