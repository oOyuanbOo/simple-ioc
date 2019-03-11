package main.java.xml.document;

import org.dom4j.Document;

public interface DocumentHolder {
	/**
	 * 根据xml文件的路径得到dom4j里面的Document对象
	 * @param filePath
	 * @return
	 */
	Document getDocument(String filePath);
}
