package main.java.xml.element;


import main.java.xml.element.LeafElement;

/**
 * @author rongdi
 *
 */
public class PropertyElement {

	private String name;

	private LeafElement leafElement;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LeafElement getLeafElement() {
		return leafElement;
	}

	public void setLeafElement(LeafElement leafElement) {
		this.leafElement = leafElement;
	}

	public PropertyElement(String name, LeafElement leafElement) {
		this.name = name;
		this.leafElement = leafElement;
	}
}
