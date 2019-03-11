package main.java.xml.autowire;

/**
 * @author rongdi
 *
 */
public class ByNameAutowire implements Autowire {

	private String type;

	public ByNameAutowire(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
