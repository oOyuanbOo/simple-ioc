package main.java.create;

import main.java.exception.BeanCreateException;
import main.java.exception.PropertyException;
import main.java.util.IocUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ���Ǵ������Ե���
 * @author rongdi
 *
 */
public class PropertyHandlerImpl implements PropertyHandler {

	/**
	 *	Ϊ����obj�������ԣ���һ����������Ҫ����ֵ�Ķ��󣬵ڶ��������Ǹ�Object����ı���
	 * ��ʲôֵ��Map��key����propertyԪ�ص�name���Զ�Ӧ�ڶ���ĳ�Ա��������Map��value
	 * ���Ƕ�Ӧ��ֵ
	 */
	@Override
	public Object setProperties(Object obj, Map<String, Object> properties) {
		/**
		 * �õ���Ҫ���õĶ���obj��Class����
		 */
		Class<?> clazz = obj.getClass();
		try {
			/**
			 * ����Map�����е�keyֵ,��keyֵ���Ƕ�������Ҫʹ��setXXX������ֵ�ĳ�Ա����
			 */
			for (String key : properties.keySet()) {
				/**
				 * ���ñ����ж����getSetterMethodName�������һ�����Եĳ�Ա����
				 * ��Ӧ��set����
				 */
				String setterName = this.getSetterMethodName(key);
				/**
				 * ���Ҫ���ó�Ա�������õ�ֵ��Class����
				 */
				Class<?> argClass = IocUtil.getClass(properties.get(key));
				/**
				 * ͨ�������ҵ�obj�����Ӧ��setXXX������Method����
				 */
				Method setterMethod = getSetterMethod(clazz, setterName, argClass);
				/**
				 * ͨ��������ø�setXXX����������Map�б���Ķ�Ӧ��ֵ
				 */
				setterMethod.invoke(obj, properties.get(key));
			}
			return obj;
		} catch (NoSuchMethodException e) {
			throw new PropertyException("��Ӧ��setter����û�ҵ�" + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new PropertyException("wrong argument " + e.getMessage());
		} catch (Exception e) {
			throw new PropertyException(e.getMessage());
		}
	}
	/**
	 * ����һ�����Ե�setter����
	 * @param propertyName
	 * @return
	 */
	private String getSetterMethodName(String propertyName) {
		return "set" + this.firstWordToUpperCase(propertyName);
	}
	/**
	 * ������s������ĸ��Ϊ��д
	 * @param key
	 * @return
	 */
	private String firstWordToUpperCase(String s) {
		String firstWord = s.substring(0, 1);
		String upperCaseWord = firstWord.toUpperCase();
		return s.replaceFirst(firstWord, upperCaseWord);
	}
	/**
	 * ͨ������õ�methodName��Ӧ��Method���󣬵�һ������Ϊ���������Class����
	 * �ڶ�������Ϊ��Ҫ�����ķ�����������������Ҫ�����ķ����Ĳ�����Class�б�
	 * @param objClass
	 * @param methodName
	 * @param argClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private Method getSetterMethod(Class<?> objClass, String methodName, 
			Class<?> argClass) throws NoSuchMethodException {
		/**
		 * ʹ��ԭ���ͻ�÷���,Ҳ���ǲ������ĸ�������Ǹ��ӿڡ� ���û���ҵ��÷���, ��õ�null
		 */
		Method argClassMethod = this.getMethod(objClass, methodName, argClass);
		/**
		 * ����Ҳ���ԭ���͵ķ���, ���Ҹ�������ʵ�ֵĽӿ�
		 */
		if (argClassMethod == null) {
			/**
			 * ���ñ��ඨ���getMethods�����õ���������ΪmethodName�Ĳ���ֻ��һ�������ķ���
			 */
			List<Method> methods = this.getMethods(objClass, methodName);
			/**
			 * ���ñ��ඨ���findMethod�����ҵ�����Ҫ��Method����
			 */
			Method method = this.findMethod(argClass, methods);
			if (method == null) {
				/**
				 * �Ҳ����κη���ֱ�����쳣
				 */
				throw new NoSuchMethodException(methodName);
			}
			/**
			 * ������Ϊ��˵���ҵ����������ظ÷�������
			 */
			return method;
		} else {
			/**
			 * �ҵ���ԭ�������͵ķ���ֱ�ӷ���
			 */
			return argClassMethod;
		}
	}
	/**
	 * ���ݷ������Ͳ������͵õ�����, ���û�и÷�������null
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
	 * �õ���������ΪmethodName����ֻ��һ�������ķ���
	 * @param objClass
	 * @param methodName
	 * @return
	 */
	private List<Method> getMethods(Class<?> objClass, String methodName) {
		/**
		 * ����һ��ArrayList����������������Ҫ��Method����
		 */
		List<Method> result = new ArrayList<Method>();
		/**
		 * ͨ������õ����еķ�����������еķ���
		 */
		for (Method m : objClass.getMethods()) {
			/**
			 * �����������ͬ
			 */
			if (m.getName().equals(methodName)) {
				/**
				 * �õ����������в���, ���ֻ��һ������, ����ӵ�������
				 */
				Class<?>[] c = m.getParameterTypes();
				/**
				 * ���ֻ��һ�������ͼӵ�ArrayList��
				 */
				if (c.length == 1) {
					result.add(m);
				}
			}
		}
		/**
		 * ��������Ҫ�ļ���
		 */
		return result;
	}

	/**
	 * ����������Ѱ�Ҳ���������interfaces����һ���ķ���
	 * @param argClass ��������
	 * @param methods ��������
	 * @return
	 */
	private Method findMethod(Class<?> argClass, List<Method> methods) {
		/**
		 * ���������ҵ��ķ���
		 */
		for (Method m : methods) {
			/**
			 * �жϲ��������뷽���Ĳ��������Ƿ�һ�£����һ��˵���ҵ��˶�Ӧ�ķ��������ظ÷���
			 */
			if (this.isMethodArgs(m, argClass)) {
				return m;
			}
		}
		/**
		 * û�ҵ��÷�������null
		 */
		return null;
	}
	
	/**
	 * �õ�obj�����е�����setXXX������Mapӳ�䣬Map��keyֵΪ��Ӧ����������valueΪ��Ӧ��set
	 * ������Method����
	 */
	public Map<String, Method> getSetterMethodsMap(Object obj) {
		/**
		 * ���ñ������������getSetterMethodsList�����õ����е�setXXX����
		 */
		List<Method> methods = this.getSetterMethodsList(obj);
		/**
		 * ����һ�������ӳ��������ŷ���������������Ӧ��bean�������bean��id���Ե�ֵ��
		 * ���Ӧ��set����
		 */
		Map<String, Method> result = new HashMap<String, Method>();
		/**
		 * �������е�Method���󣬵��ñ����getMethodNameWithoutSet�õ��÷���
		 * ��Ӧ����������Ҳ����ȥ��set���ֵ��
		 */
		for (Method m : methods) {
			String propertyName = this.getMethodNameWithOutSet(m.getName());
			/**
			 * ��������������ͷ�������Ϣ����map��
			 */
			result.put(propertyName, m);
		}
		/**
		 * ���������map
		 */
		return result;
	}
	/**
	 * ��setter������ԭ, setName��Ϊ����, �õ�name
	 * @param methodName
	 * @return
	 */
	private String getMethodNameWithOutSet(String methodName) {
		/**
		 * �õ���������ȥ��set֮������֣�Ϊʲô�������ģ����ǲ��ò�˵������ֵע���ʱ��ʵ����
		 * ����ע��ʲô����ʵ�����ǿ�setXxx����ȥ��setȻ���ٰѺ���ĵ�һ���ַ����Сд֮���xxx
		 * ��Ϊ������һ������������
		 * private String yy;
		 * public void setXx(String aa) {
		 * 		this.yy = aa;
		 * }
		 * <bean id="test3" class="com.tear.Test3">
    	 *	  <property name="xx">
         *		 <value type="java.lang.String">123456</value>
    	 *	  </property>
		 * </bean>
		 * ʵ���������property��ǩ��nameֵҪע��ĵط������ݲ�����ȥ������������Ϊxx��ȥ����
		 * ����ȥ��xx�ĵ�һ���ַ���дǰ�����set��setXx�����������ֵ�����Կ���xx����ʵ���ϻ�
		 * ע�뵽��yy��Ա�����У���������������ļ������Ե�ֵ��ע��ʼ�����Ҹ����Ա����Ӧ��set����
		 * ȥ��ֵ�ģ���һ�㲻����struts2��action�㻹����spring��Ioc���к����Եı��֣������ŵ�
		 * С�������Լ�ȥ���ԡ���Ȼ��Ϊ�Լ���ʵ��������Լ�������ֵ�Ĺ���
		 */
		String propertyName = methodName.substring(3);
		/**
		 * �õ����������ĵ�һ����д��ĸ
		 */
		String firstWord = propertyName.substring(0, 1);
		/**
		 * ����д��ĸ����Сд��
		 */
		String lowerFirstWord = firstWord.toLowerCase();
		/**
		 * ���ظ�setXXX������Ӧ����ȷ��������
		 */
		return propertyName.replaceFirst(firstWord, lowerFirstWord);
	}
	/**
	 * ͨ������õ�obj�����е�����setXXX������Ӧ��Method����ļ�����ʽ
	 * @param obj
	 * @return
	 */
	private List<Method> getSetterMethodsList(Object obj) {
		/**
		 * �������ڣ����ȵõ�obj�����Class����
		 */
		Class<?> clazz = obj.getClass();
		/**
		 * �ɸö����Class����ô����еķ���
		 */
		Method[] methods = clazz.getMethods();
		/**
		 * ����һ��������ϣ�׼��������������Ҫ��Method����
		 */
		List<Method> result = new ArrayList<Method>();
		/**
		 * �������еõ���Method�����ҵ�set��ͷ�ķ�������ŵ����������
		 */
		for (Method m : methods) {
			if (m.getName().startsWith("set")) {
				result.add(m);
			}
		}
		/**
		 * ��������Ҫ��Method����Ľ������
		 */
		return result;
	}
	/**
	 * ִ��ĳһ���������÷����������Զ�װ���ʱ����ö�Ӧ�����е�setter�����Ѳ�����argBean����set��ȥ�ķ���
	 * ����objectΪ����ֵ�Ķ���argBean����Ҫ���ȥ�Ĳ�������������������setXX�������Method������Ҫ��
	 * ����ʹ�÷���
	 */
	public void executeMethod(Object object, Object argBean, Method method) {
		try {
			/**
			 * ��ȡ��Ҫ���õķ����Ĳ�������
			 */
			Class<?>[] parameterTypes = method.getParameterTypes();
			/**
			 * �����������Ϊ1����ִ�и÷�������Ϊ��ΪsetXX�������������϶�
			 * ��1
			 */
			if (parameterTypes.length == 1) {
				/**
				 * �����������һ��, ��ִ�з���
				 */
				if (isMethodArgs(method, parameterTypes[0])) {
					method.invoke(object, argBean);
				}
			}
		} catch (Exception e) {
			/**
			 * ��Ϊ��÷�����Ҫ�Ǳ��Զ�װ���ķ������ã�����������������׳��Զ�װ���쳣����Ϣ
			 */
			throw new BeanCreateException("�Զ�װ���쳣 " + e.getMessage());
		}
	}

	/**
	 * �жϲ�������(argClass)�Ƿ��Ǹ÷���(m)�Ĳ�������
	 * @param m
	 * @param argClass
	 * @return
	 */
	private boolean isMethodArgs(Method m, Class<?> argClass) {
		/**
		 * �õ������Ĳ�������
		 */
		Class<?>[] c = m.getParameterTypes();
		/**
		 * ���ֻ��һ�������ŷ���Ҫ��
		 */
		if (c.length == 1) {
			try {
				/**
				 * ����������(argClass)�뷽���еĲ������ͽ���ǿ��ת��, �����쳣˵��
				 * ����Ĳ����Ƿ�����������������ͣ����߾��Ƿ������������͡�
				 */
				argClass.asSubclass(c[0]);
				/**
				 * û���쳣����true�������鷵��false
				 */
				return true;
			} catch (ClassCastException e) {
				return false;
			}
		}
		return false;
	}

}
