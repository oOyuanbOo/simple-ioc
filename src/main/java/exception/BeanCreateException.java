package main.java.exception;

/**
 * ����һ������beanʱ�����׳����쳣��
 * @author rongdi
 *
 */
@SuppressWarnings("serial")
public class BeanCreateException extends RuntimeException {

	public BeanCreateException(String e) {
		super(e);
	}
}
