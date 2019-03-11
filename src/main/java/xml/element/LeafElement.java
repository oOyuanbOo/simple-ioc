package main.java.xml.element;
/**
 * 叶子节点 和 element区分开，只有 type 和 value两个属性
 * @author rongdi
 *
 */
public interface LeafElement {
	/**
	 * @return
	 */
	public String getType();
	/**
	 * @return
	 */
	public Object getValue();
}
