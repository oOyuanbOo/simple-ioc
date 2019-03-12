package xml.element.loader;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Iterator;


import main.java.xml.document.XmlDocumentHolder;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ElementLoaderImplTest {

	XmlDocumentHolder xmlHolder;

	main.java.xml.element.loader.ElementLoader elementLoader;

	@Before
	public void setUp() throws Exception {
		xmlHolder = new XmlDocumentHolder();
		elementLoader = new main.java.xml.element.loader.ElementLoaderImpl();
		
	}

	@After
	public void tearDown() throws Exception {
		xmlHolder = null;
		elementLoader = null;
	}

	@Test
	public void testAddElements() {
		String filePath = "src/test/resources/xml.element/elementLoaderImpl.xml";
		System.out.println(new File(filePath).exists());
		Document document = xmlHolder.getDocument(filePath);
		assertNotNull(document);
		elementLoader.addBeanElements(document);
		Element e = elementLoader.getBeanElement("test1");
		assertNotNull(e);
		// 穷尽写法，学习下
		for(Iterator iter = elementLoader.getBeanElements().iterator();iter.hasNext();){
			System.out.println(iter.next());
		}
	}
}
