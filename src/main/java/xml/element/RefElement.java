package main.java.xml.element;

/**
 * @author rongdi
 */
public class RefElement implements LeafElement {

	private Object value;

	public RefElement(Object value) {
		this.value = value;
	}

	@Override
	public String getType() {
		return "ref";
	}

	@Override
	public Object getValue() {
		
		return this.value;
	}

}
