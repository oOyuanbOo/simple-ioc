package main.java.objects;

public class BeanCreatorObject1 {
	private String name;
	private String value;
	public BeanCreatorObject1(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public BeanCreatorObject1() {
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
