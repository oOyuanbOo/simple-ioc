package create;

import exception.BeanCreateException;
import util.IocUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个使用构造方法创建bean对应的实例的类
 * @author rongdi
 *
 */
public class BeanCreatorImpl implements BeanCreator {
	/**
	 * 使用默认的构造方法创建实例，传入的参数为类的全名，可以从bean的class属性的值那里获得
	 * 再通过反射创建实例
	 */
	@Override
	public Object createBeanUseDefaultConstruct(String className) {

		try {
			/**
			 * 获得类的全名对应的Class对象
			 */
			Class<?> clazz = Class.forName(className);
			/**
			 * 使用反射的方式返回一个该类的实例,使用的是无参数的构造方法
			 */
			return clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new BeanCreateException("没有找到"+className+"该类 " + e.getMessage());
		} catch (Exception e) {
			throw new BeanCreateException(e.getMessage());
		}
	}

	@Override
	public Object createBeanUseDefineConstruct(String className,
											   List<Object> args) {
		/**
		 * 将传入的List<Object>类型的参数转换成Class数组的形式
		 */
		Class<?>[] argsClass = this.getArgsClasses(args);
		try {
			/**
			 * 获得传入类的全名的Class对象
			 */
			Class<?> clazz = Class.forName(className);
			/**
			 * 通过反射得到该类的构造方法(Constructor)对象
			 */
			Constructor<?> constructor = getConstructor(clazz, argsClass);
			/**
			 * 根据参数动态创建一个该类的实例
			 */
			return constructor.newInstance(args.toArray());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new BeanCreateException(className+"类没有找到 " + e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new BeanCreateException("没找到"+className+"中对应的构造方法" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanCreateException(e.getMessage());
		}
	}
	/**
	 * 根据类的Class对象和参数的Class对象的列表查找一个类的构造器，注意一般我们定义方法的时候
	 * 由于为了使用多态原理一般我们将方法里的参数定义成我们想接受的参数的一个父类或者是父接口，这样我们
	 * 想通过该方法就获得不到Constructor对象了，所以该方法只是一个初步的方法还需要进行封装才能
	 * 达到我们想要的效果
	 * @param clazz 类型
	 * @param argsClass 构造参数
	 * @return
	 */
	private Constructor<?> getProcessConstructor(Class<?> clazz, Class<?>[] argsClass) {
		try {
			Constructor<?> constructor = clazz.getConstructor(argsClass);
			return constructor;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	/**
	 * 这个方法才是真正或得到构造方法的
	 * @param clazz
	 * @param argsClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Constructor<?> getConstructor(Class<?> clazz, Class<?>[] argsClass)
			throws NoSuchMethodException {
		/**
		 * 首先调用获得直接根据类名和参数的class列表的构造方法，如果该构造方法要传入的类不是构造方法
		 * 中声明的类，一般需要注入的类都是它的子类，这样该方法获得的构造方法对象就是null的，这样
		 * 就需要在获得所有的构造方法，判断传入的是否是构造方法形式参数的实例对象了
		 */
		Constructor<?> constructor = getProcessConstructor(clazz, argsClass);
		/**
		 * 如果获得的构造方法对象为空
		 */
		if (constructor == null) {
			/**
			 * 得到该类的所有的public的构造器对象
			 */
			Constructor<?>[] constructors = clazz.getConstructors();
			/**
			 * 遍历所有的构造器对象
			 */
			for (Constructor<?> c : constructors) {
				/**
				 * 获取到该构造器的所有参数的class对象的Class数组形式
				 */
				Class<?>[] tempClass = c.getParameterTypes();
				/**
				 * 判断该构造器的参数个数是否与argsClass（传进来）的参数个数相同
				 */
				if (tempClass.length == argsClass.length) {
					if (isSameArgs(argsClass, tempClass)) {
						return c;
					}
				}
			}
		} else {
			/**
			 * 如果传入的刚好是构造器中定义的那个类，就会之金额找到该构造器，那么直接返回该构造器
			 */
			return constructor;
		}
		/**
		 * 如果到这里还没返回表示没有找到合适的构造器，直接抛出错误
		 */
		throw new NoSuchMethodException("找不到指定的构造器");
	}
	/**
	 * 判断两个参数数组类型是否匹配
	 * @param argsClass
	 * @return
	 */
	private boolean isSameArgs(Class<?>[] argsClass, Class<?>[] tempClass) {
		/**
		 * for循环比较每一个参数是否都相同（子类和父类看成相同）
		 */
		for (int i = 0; i < argsClass.length; i++) {
			try {
				/**
				 * 将传入参数（前面的参数）与构造器参数，后面的参数进行强制转换，如果转换成功说明前面的参数
				 * 是后面的参数的 子类，那么可以认为类型相同了，若果不是子类就会抛异常
				 */
				argsClass[i].asSubclass(tempClass[i]);
				/**
				 * 循环到最后一个参数都成功转换表示类型相同，该构造器合适了
				 */
				if (i == (argsClass.length - 1)) {
					return true;
				}
			} catch (Exception e) {
				/**
				 * 如果有一个参数类型不符合, 跳出该循环
				 */
				break;
			}
		}
		return false;
	}

	/**
	 * 得到参数集合的class数组
	 * @param args
	 * @return
	 */
	private Class<?>[] getArgsClasses(List<Object> args) {
		/**
		 * 定义一个集合保存所需参数
		 */
		List<Class<?>> result = new ArrayList<Class<?>>();
		/**
		 * 循环所有传入参数
		 */
		for (Object arg : args) {
			/**
			 * 将参数转换成对应的Class对象后加到定义的集合中来
			 */
			result.add(IocUtil.getClass(arg));
		}
		/**
		 * 根据该集合的长度创建一个相同长度的Class数组
		 */
		Class<?>[] a = new Class[result.size()];
		/**
		 * 返回集合对应的Class数组
		 */
		return result.toArray(a);
	}
}