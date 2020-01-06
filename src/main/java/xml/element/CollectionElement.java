package xml.element;

import xml.element.LeafElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rongdi
 */
public class CollectionElement implements LeafElement {

	private String type;

	private List<LeafElement> list;

	public void setList(List<LeafElement> list) {
		this.list = list;
	}

	public void add(LeafElement leafElement) {
		this.list.add(leafElement);
	}

	@Override
	public String getType() {
		return this.type;
	}

	public CollectionElement(String type) {
		this.type = type;
		
	}
	public List<LeafElement> getList() {
		return list;
	}

	@Override
	public Object[] getValue() {
		List<Object> value = new ArrayList<Object>();
		for(LeafElement le : this.getList()) {
			value.add(le.getValue());
		}
		return value.toArray();
	}

}
