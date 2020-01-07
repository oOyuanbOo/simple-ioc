package util;

/**
 * 这是一个帮助类
 */
public class IocUtil {
	/**
	 * 如果该类型是java中的几个基本数据类型那么返回它的类型，注意Integer.type就是获得他的class对象
	 * 如果不是基础类型则使用getClass（）返回它的Class对象
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
	}/**
	 * 判断className的类型是否为基础类型。如java.lang.Integer, 是的话将数据进行转换
	 * 成对应的类型该方法是供本类中的方法调用的，作用是根据type类型的值将对应的value数据转换
	 * 成对应的type类型的值
	 * @param className
	 * @param data
	 * @return
	 */
	public static Object getValue(String className, String data) {
		/**
		 * 下面的所有if和else if都是判断是否是java的8中基本数据类型的包装类型
		 */
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
			 * 如果是Character类型则取第一个字符
			 */
			return data.charAt(0);
		} else if (isType(className, "Byte")) {
			return Byte.valueOf(data);
		} else {
			/**
			 * 如果不是8种基本数据类型的包装类那么就是自定义的类了，直接返回该值
			 */
			return data;
		}
	}
	/**
	 * 该方法是判断类名中是否含有对应的type字符串的方法，如判断className:java.lang.Integer中
	 * 是否包含Integer这样就返回true，不包含则返回false，该方法是供上面的方法调用的
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