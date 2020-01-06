package xml.autowire;
/**
 * @author rongdi
 *
 */
public class NoAutowire implements Autowire {
	
	@SuppressWarnings("unused")
	private String type;
	public NoAutowire(String type) {
		this.type = type;
	}

	public String getType() {
		return "no";
	}

}
