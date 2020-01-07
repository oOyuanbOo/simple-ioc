package create;

import exception.BeanCreateException;
import exception.PropertyException;
import util.IocUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这是处理属性的类
 * @author rongdi
 *
 */
public class PropertyHandlerImpl implements PropertyHandler {

	/**
	 *    为对象obj设置属性，第一个参数是需要设置值的对象，第二个参数是给Object里面的变量
	 * 设什么值，Map的key就是property元素的name属性对应于对象的成员变量名，Map的value
	 * 就是对应的值
	 */
	@Override
	public Object setProperties(Object obj, Map<String, Object> properties) {
		/**
		 * 得到需要设置的对象obj的Class对象
		 */
		Class<?> clazz = obj.getClass();
		try {
			/**
			 * 遍历Map中所有的key值,该key值就是对象中需要使用setXXX方法设值的成员变量
			 */
			for (String key : properties.keySet()) {
				/**
				 * 调用本类中定义的getSetterMethodName方法获得一个属性的成员变量
				 * 对应的set方法
				 */
				String setterName = this.getSetterMethodName(key);
				/**
				 * 获得要给该成员变量设置的值的Class对象
				 */
				Class<?> argClass = IocUtil.getClass(properties.get(key));
				/**
				 * 通过反射找到obj对象对应的setXXX方法的Method对象
				 */
				Method setterMethod = getSetterMethod(clazz, setterName, argClass);
				/**
				 * 通过反射调用该setXXX方法，传入Map中保存的对应的值
				 */
				setterMethod.invoke(obj, properties.get(key));
			}
			return obj;
		} catch (NoSuchMethodException e) {
			throw new PropertyException("对应的setter方法没找到" + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new PropertyException("wrong argument " + e.getMessage());
		} catch (Exception e) {
			throw new PropertyException(e.getMessage());
		}
	}
	/**
	 * 返回一个属性的setter方法
	 * @param propertyName
	 * @return
	 */
	private String getSetterMethodName(String propertyName) {
		return "set" + this.firstWordToUpperCase(propertyName);
	}
	/**
	 * 将参数s的首字母变为大写
	 * @return
	 */
	private String firstWordToUpperCase(String s) {
		String firstWord = s.substring(0, 1);
		String upperCaseWord = firstWord.toUpperCase();
		return s.replaceFirst(firstWord, upperCaseWord);
	}
	/**
	 * 通过反射得到methodName对应的Method对象，第一个参数为操作对象的Class对象
	 * 第二个参数为需要操作的方法名，第三个是需要操作的方法的参数的Class列表
	 * @param objClass
	 * @param methodName
	 * @param argClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Method getSetterMethod(Class<?> objClass, String methodName,
								   Class<?> argClass) throws NoSuchMethodException {
		/**
		 * 使用原类型获得方法,也就是不算它的父类或者是父接口。 如果没有找到该方法, 则得到null
		 */
		Method argClassMethod = this.getMethod(objClass, methodName, argClass);
		/**
		 * 如果找不到原类型的方法, 则找该类型所实现的接口
		 */
		if (argClassMethod == null) {
			/**
			 * 调用本类定义的getMethods方法得到所有名字为methodName的并且只有一个参数的方法
			 */
			List<Method> methods = this.getMethods(objClass, methodName);
			/**
			 * 调用本类定义的findMethod方法找到所需要的Method对象
			 */
			Method method = this.findMethod(argClass, methods);
			if (method == null) {
				/**
				 * 找不到任何方法直接抛异常
				 */
				throw new NoSuchMethodException(methodName);
			}
			/**
			 * 方法不为空说明找到方法，返回该方法对象
			 */
			return method;
		} else {
			/**
			 * 找到了原参数类型的方法直接返回
			 */
			return argClassMethod;
		}
	}
	/**
	 * 根据方法名和参数类型得到方法, 如果没有该方法返回null
	 * @param objClass
	 * @param methodName
	 * @param argClass
	 * @return
	 */
	private Method getMethod(Class<?> objClass, String methodName, Class<?> argClass) {
		try {
			Method method = objClass.getMethod(methodName, argClass);
			return method;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	/**
	 * 得到所有名字为methodName并且只有一个参数的方法
	 * @param objClass
	 * @param methodName
	 * @return
	 */
	private List<Method> getMethods(Class<?> objClass, String methodName) {
		/**
		 * 创建一个ArrayList集合用来保存所需要的Method对象
		 */
		List<Method> result = new ArrayList<Method>();
		/**
		 * 通过反射得到所有的方法后遍历所有的方法
		 */
		for (Method m : objClass.getMethods()) {
			/**
			 * 如果方法名相同
			 */
			if (m.getName().equals(methodName)) {
				/**
				 * 得到方法的所有参数, 如果只有一个参数, 则添加到集合中
				 */
				Class<?>[] c = m.getParameterTypes();
				/**
				 * 如果只有一个参数就加到ArrayList中
				 */
				if (c.length == 1) {
					result.add(m);
				}
			}
		}
		/**
		 * 返回所需要的集合
		 */
		return result;
	}

	/**
	 * 方法集合中寻找参数类型是interfaces其中一个的方法
	 * @param argClass 参数类型
	 * @param methods 方法集合
	 * @return
	 */
	private Method findMethod(Class<?> argClass, List<Method> methods) {
		/**
		 * 遍历所有找到的方法
		 */
		for (Method m : methods) {
			/**
			 * 判断参数类型与方法的参数类型是否一致，如果一致说明找到了对应的方法。返回该方法
			 */
			if (this.isMethodArgs(m, argClass)) {
				return m;
			}
		}
		/**
		 * 没找到该方法返回null
		 */
		return null;
	}

	/**
	 * 得到obj对象中的所有setXXX方法的Map映射，Map的key值为对应的属性名，value为对应的set
	 * 方法的Method对象
	 */
	public Map<String, Method> getSetterMethodsMap(Object obj) {
		/**
		 * 调用本类中所定义的getSetterMethodsList方法得到所有的setXXX方法
		 */
		List<Method> methods = this.getSetterMethodsList(obj);
		/**
		 * 定义一个结果的映射用来存放方法的属性名（对应到bean里面就是bean的id属性的值）
		 * 与对应的set方法
		 */
		Map<String, Method> result = new HashMap<String, Method>();
		/**
		 * 遍历所有的Method对象，调用本类的getMethodNameWithoutSet得到该方法
		 * 对应的属性名（也就是去掉set后的值）
		 */
		for (Method m : methods) {
			String propertyName = this.getMethodNameWithOutSet(m.getName());
			/**
			 * 将所需的属性名和方法对信息放入map中
			 */
			result.put(propertyName, m);
		}
		/**
		 * 返回所需的map
		 */
		return result;
	}
	/**
	 * 将setter方法还原, setName作为参数, 得到name
	 * @param methodName
	 * @return
	 */
	private String getMethodNameWithOutSet(String methodName) {
		/**
		 * 得到方法名中去掉set之后的名字，为什么是这样的，我们不得不说下在设值注入的时候实际上
		 * 到底注入什么参数实际上是看setXxx方法去掉set然后再把后面的第一个字符变成小写之后的xxx
		 * 作为依据如一个类中有属性
		 * private String yy;
		 * public void setXx(String aa) {
		 *         this.yy = aa;
		 * }
		 * <bean id="test3" class="com.tear.Test3">
		 *      <property name="xx">
		 *         <value type="java.lang.String">123456</value>
		 *      </property>
		 * </bean>
		 * 实际上这里的property标签的name值要注入的地方的依据并不是去找类中属性名为xx的去设置
		 * 而是去找xx的第一个字符大写前面加上set即setXx方法来完成设值，所以看到xx属性实际上会
		 * 注入到了yy成员变量中，所以这里的配置文件的属性的值的注入始终是找该属性变成相应的set方法
		 * 去设值的，这一点不管在struts2的action层还是在spring的Ioc都有很明显的表现，不相信的
		 * 小伙伴可以自己去试试。当然作为自己的实现你可以自己定义设值的规则
		 */
		String propertyName = methodName.substring(3);
		/**
		 * 得到该属性名的第一个大写字母
		 */
		String firstWord = propertyName.substring(0, 1);
		/**
		 * 将大写字母换成小写的
		 */
		String lowerFirstWord = firstWord.toLowerCase();
		/**
		 * 返回该setXXX方法对应的正确的属性名
		 */
		return propertyName.replaceFirst(firstWord, lowerFirstWord);
	}
	/**
	 * 通过反射得到obj对象中的所有setXXX方法对应的Method对象的集合形式
	 * @param obj
	 * @return
	 */
	private List<Method> getSetterMethodsList(Object obj) {
		/**
		 * 反射的入口，首先得到obj对象的Class对象
		 */
		Class<?> clazz = obj.getClass();
		/**
		 * 由该对象的Class对象得打所有的方法
		 */
		Method[] methods = clazz.getMethods();
		/**
		 * 声明一个结果集合，准备用来方法所需要的Method对象
		 */
		List<Method> result = new ArrayList<Method>();
		/**
		 * 遍历所有得到的Method对象找到set开头的方法将其放到结果集合中
		 */
		for (Method m : methods) {
			if (m.getName().startsWith("set")) {
				result.add(m);
			}
		}
		/**
		 * 返回所需要的Method对象的结果集合
		 */
		return result;
	}
	/**
	 * 执行某一个方法，该方法用来被自动装配的时候调用对应对象中的setter方法把产生的argBean对象set进去的方法
	 * 其中object为带设值的对象，argBean就是要设进去的参数，第三个参数就是setXX对象本身的Method对象，主要是
	 * 方便使用反射
	 */
	public void executeMethod(Object object, Object argBean, Method method) {
		try {
			/**
			 * 获取需要调用的方法的参数类型
			 */
			Class<?>[] parameterTypes = method.getParameterTypes();
			/**
			 * 如果参数数量为1，则执行该方法，因为作为setXX方法参数个数肯定
			 * 是1
			 */
			if (parameterTypes.length == 1) {
				/**
				 * 如果参数类型一样, 才执行方法
				 */
				if (isMethodArgs(method, parameterTypes[0])) {
					method.invoke(object, argBean);
				}
			}
		} catch (Exception e) {
			/**
			 * 因为如该方法主要是被自动装备的方法调用，所以如果遇到问题抛出自动装配异常的信息
			 */
			throw new BeanCreateException("自动装配异常 " + e.getMessage());
		}
	}

	/**
	 * 判断参数类型(argClass)是否是该方法(m)的参数类型
	 * @param m
	 * @param argClass
	 * @return
	 */
	private boolean isMethodArgs(Method m, Class<?> argClass) {
		/**
		 * 得到方法的参数类型
		 */
		Class<?>[] c = m.getParameterTypes();
		/**
		 * 如果只有一个参数才符合要求
		 */
		if (c.length == 1) {
			try {
				/**
				 * 将参数类型(argClass)与方法中的参数类型进行强制转换, 不抛异常说明
				 * 传入的参数是方法参数的子类的类型，或者就是方法参数的类型。
				 */
				argClass.asSubclass(c[0]);
				/**
				 * 没抛异常返回true，其他情返回false
				 */
				return true;
			} catch (ClassCastException e) {
				return false;
			}
		}
		return false;
	}

}