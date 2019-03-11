package main.java.create;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * �������ԵĽӿ�
 * @author rongdi
 *
 */
public interface PropertyHandler {
	/**
	 * Ϊ����obj�������ԣ���һ����������Ҫ����ֵ�Ķ��󣬵ڶ��������Ǹ�Object����ı���
	 * ��ʲôֵ��Map��key����propertyԪ�ص�name���Զ�Ӧ�ڶ���ĳ�Ա��������Map��value
	 * ���Ƕ�Ӧ��ֵ
	 * @param obj
	 * @param properties ���Լ���
	 * @return
	 */
	public Object setProperties(Object obj, Map<String, Object> properties);
	/**
	 * ����һ�������������е�setter����, ��װ��map, keyΪsetter������ȥ��set����ַ���
	 * ����Ϊʲô�������ģ�����ԭ����ʵ�����е�˽�з���getMethodNameWithOutSet�Ѿ�����
	 * ��ϸ�Ľ���
	 * @param obj
	 * @return
	 */
	public Map<String, Method> getSetterMethodsMap(Object obj);
	
	/**
	 * ʹ�÷���ִ��һ����������Ҫ������ɵ���һ�ξ�Ϊ��������һ������
	 * @param object ��Ҫִ�з����Ķ���
	 * @param argBean ������bean
	 * @param method setXX��������
	 */
	public void executeMethod(Object object, Object argBean, Method method);

}
