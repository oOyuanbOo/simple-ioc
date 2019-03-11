package main.java.xml.element;

import main.java.xml.element.LeafElement;

/**
 * ���Ǵ���value��ǩ�Ľڵ�Ԫ�أ�ʵ����LeafElement�ӿ�
 * @author rongdi
 */
public class ValueElement implements LeafElement {

	private Object value;

	public ValueElement(Object value) {
		this.value = value;
	}

	@Override
	public String getType() {
		
		return "value";
	}

	@Override
	public Object getValue() {
	
		return this.value;
	}


}
