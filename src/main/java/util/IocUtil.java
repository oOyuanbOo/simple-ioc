package main.java.util;
/**
 * ����һ��������
 */
public class IocUtil {
	/**
	 * �����������java�еļ�����������������ô�����������ͣ�ע��Integer.type���ǻ������class����
	 * ������ǻ���������ʹ��getClass������������Class����
	 * @param obj
	 * @return
	 */
	public static Class<?> getClass(Object obj) {
		if (obj instanceof Integer) {
			return Integer.TYPE;
		} else if (obj instanceof Boolean) {
			return Boolean.TYPE;
		} else if (obj instanceof Long) {
			return Long.TYPE;
		} else if (obj instanceof Short) {
			return Short.TYPE;
		} else if (obj instanceof Double) {
			return Double.TYPE;
		} else if (obj instanceof Float) {
			return Float.TYPE;
		} else if (obj instanceof Character) {
			return Character.TYPE;
		} else if (obj instanceof Byte) {
			return Byte.TYPE;
		}
		return obj.getClass();
	}
	/**
	 * �ж�className�������Ƿ�Ϊ�������͡���java.lang.Integer, �ǵĻ������ݽ���ת��
	 * �ɶ�Ӧ�����͸÷����ǹ������еķ������õģ������Ǹ���type���͵�ֵ����Ӧ��value����ת��
	 * �ɶ�Ӧ��type���͵�ֵ
	 * @param className
	 * @param data
	 * @return
	 */
	public static Object getValue(String className, String data) {

		if (isType(className, "Integer")) {
			return Integer.parseInt(data);
		} else if (isType(className, "Boolean")) {
			return Boolean.valueOf(data);
		} else if (isType(className, "Long")) {
			return Long.valueOf(data);
		} else if (isType(className, "Short")) {
			return Short.valueOf(data);
		} else if (isType(className, "Double")) {
			return Double.valueOf(data);
		} else if (isType(className, "Float")) {
			return Float.valueOf(data);
		} else if (isType(className, "Character")) {
			/**
			 * �����Character������ȡ��һ���ַ�
			 */
			return data.charAt(0);
		} else if (isType(className, "Byte")) {
			return Byte.valueOf(data);
		} else {

			return data;
		}
	}
	/**
	 * �÷������ж��������Ƿ��ж�Ӧ��type�ַ����ķ��������ж�className:java.lang.Integer��
	 * �Ƿ����Integer�����ͷ���true���������򷵻�false���÷����ǹ�����ķ������õ�
	 * @param className
	 * @param type
	 * @return
	 */
	private static boolean isType(String className, String type) {
		if (className.lastIndexOf(type) != -1)
			return true;
		return false;
	}
}
