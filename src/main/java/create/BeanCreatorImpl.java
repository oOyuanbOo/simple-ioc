package main.java.create;

import main.java.exception.BeanCreateException;
import main.java.util.IocUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
/**
 * @author rongdi
 *
 */
public class BeanCreatorImpl implements BeanCreator {
	@Override
	public Object createBeanUseDefaultConstruct(String className) {

		try {

			Class<?> clazz = Class.forName(className);

			return clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new BeanCreateException("û���ҵ�"+className+"���� " + e.getMessage());
		} catch (Exception e) {
			throw new BeanCreateException(e.getMessage());
		}
	}

	@Override
	public Object createBeanUseDefineConstruct(String className,
			List<Object> args) {
		/**
		 * 有参数的
		 */
		Class<?>[] argsClass = this.getArgsClasses(args);
		try {
			/**
			 *
			 */
			Class<?> clazz = Class.forName(className);
			/**
			 * ͨ获取构造器对象
			 */
			Constructor<?> constructor = getConstructor(clazz, argsClass);

			return constructor.newInstance(args.toArray());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new BeanCreateException(className+"��û���ҵ� " + e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new BeanCreateException("û�ҵ�"+className+"�ж�Ӧ�Ĺ��췽��" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanCreateException(e.getMessage());
		}
	}
	/**
	 * �������Class����Ͳ�����Class������б����һ����Ĺ�������ע��һ�����Ƕ��巽����ʱ��
	 * ����Ϊ��ʹ�ö�̬ԭ��һ�����ǽ�������Ĳ����������������ܵĲ�����һ����������Ǹ��ӿڣ���������
	 * ��ͨ���÷����ͻ�ò���Constructor�����ˣ����Ը÷���ֻ��һ�������ķ�������Ҫ���з�װ����
	 * �ﵽ������Ҫ��Ч��
	 * @param clazz ����
	 * @param argsClass �������
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
	 * �����������������õ����췽����
	 * @param clazz
	 * @param argsClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Constructor<?> getConstructor(Class<?> clazz, Class<?>[] argsClass)
		throws NoSuchMethodException {
		/**
		 * ���ȵ��û��ֱ�Ӹ��������Ͳ�����class�б�Ĺ��췽��������ù��췽��Ҫ������಻�ǹ��췽��
		 * ���������࣬һ����Ҫע����඼���������࣬�����÷�����õĹ��췽���������null�ģ�����
		 * ����Ҫ�ڻ�����еĹ��췽�����жϴ�����Ƿ��ǹ��췽����ʽ������ʵ��������
		 */
		Constructor<?> constructor = getProcessConstructor(clazz, argsClass);
		/**
		 * todo: 为什么为获取为空, 可能是因为没有显式的构造方法
		 */
		if (constructor == null) {
			/**
			 * �õ���������е�public�Ĺ���������
			 */
			Constructor<?>[] constructors = clazz.getConstructors();
			/**
			 * �������еĹ���������
			 */
			for (Constructor<?> c : constructors) {
				/**
				 * ��ȡ���ù����������в�����class�����Class������ʽ
				 */
				Class<?>[] tempClass = c.getParameterTypes();
				/**
				 * �жϸù������Ĳ��������Ƿ���argsClass�����������Ĳ���������ͬ
				 */
				if (tempClass.length == argsClass.length) {
					if (isSameArgs(argsClass, tempClass)) {
						return c;
					}
				}
			}
		} else {
			/**
			 * �������ĸպ��ǹ������ж�����Ǹ��࣬�ͻ�֮����ҵ��ù���������ôֱ�ӷ��ظù�����
			 */
			return constructor;
		}
		/**
		 * ��������ﻹû���ر�ʾû���ҵ����ʵĹ�������ֱ���׳�����
		 */
		throw new NoSuchMethodException("�Ҳ���ָ���Ĺ�����");
	}
		/**
		 * �ж������������������Ƿ�ƥ��
		 * @param argsClass
		 * @return
		 */
		private boolean isSameArgs(Class<?>[] argsClass, Class<?>[] tempClass) {
			/**
			 * forѭ���Ƚ�ÿһ�������Ƿ���ͬ������͸��࿴����ͬ��
			 */
			for (int i = 0; i < argsClass.length; i++) {
				try {
					/**
					 * �����������ǰ��Ĳ������빹��������������Ĳ�������ǿ��ת�������ת���ɹ�˵��ǰ��Ĳ���
					 * �Ǻ���Ĳ����� ���࣬��ô������Ϊ������ͬ�ˣ�������������ͻ����쳣
					 */
					argsClass[i].asSubclass(tempClass[i]);
					/**
					 * ѭ�������һ���������ɹ�ת����ʾ������ͬ���ù�����������
					 */
					if (i == (argsClass.length - 1)) {
						return true;
					}
				} catch (Exception e) {
					/**
					 * �����һ���������Ͳ�����, ������ѭ��
					 */
					break;
				}
			}
			return false;
		}

		/**
		 * �õ��������ϵ�class����
		 * @param args
		 * @return
		 */
		private Class<?>[] getArgsClasses(List<Object> args) {
			/**
			 * ����һ�����ϱ����������
			 */
			List<Class<?>> result = new ArrayList<Class<?>>();
			/**
			 * ѭ�����д������
			 */
			for (Object arg : args) {
				/**
				 * ������ת���ɶ�Ӧ��Class�����ӵ�����ļ�������
				 */
				result.add(IocUtil.getClass(arg));
			}
			/**
			 * ���ݸü��ϵĳ��ȴ���һ����ͬ���ȵ�Class����
			 */
			Class<?>[] a = new Class[result.size()];
			/**
			 * ���ؼ��϶�Ӧ��Class����
			 */
			return result.toArray(a);
		}
}
