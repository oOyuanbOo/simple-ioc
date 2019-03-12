package xml.document;

import main.java.xml.document.XmlDocumentHolder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class XmlDocumentHolderTest {
	private XmlDocumentHolder xmlHolder;
	@Before
	public void setUp() throws Exception {
		xmlHolder = new XmlDocumentHolder();
	}

	@After
	public void tearDown() throws Exception {
		xmlHolder = null;
	}
	//测试正常情况
	// 这个filePath始终没找对
	@Test
	public void testGetDocument1() {
		String filePath = "src/test/resources/document/xmlDocumentHolder.xml";
		System.out.println(new File(filePath).exists());
		//获得Document对象
		Document doc1 = xmlHolder.getDocument(filePath);
		//看是否为空，为空测试失败
		assertNotNull(doc1);
		//得到xml文档根元素
		Element root = doc1.getRootElement();
		//判断根元素是否为beans，不是beans测试失败
		assertEquals(root.getName(), "beans");
		//再获取一次Document对象，看是否一致
		Document doc2 = xmlHolder.getDocument(filePath);
		System.out.println(doc1);
		System.out.println(doc1);
		assertEquals(doc1, doc2);
	}
	//测试一个读取DTD验证不合格的xml文件看是否抛出异常
	@Test(expected = DocumentException.class)
	public void testGetDocument2(){
        /*
         * 定义一个dtd验证不合格的xml文件，该xml文件的bean元素id和class是必须，
         * 但是少了一个class应该抛出异常
         */
		String filePath = "test/resources/document/xmlDocumentHolder2.xml";
		//获得Document对象
		Document doc = xmlHolder.getDocument(filePath);
	}
	//测试一个读取不到DTD的情况（DTD里面的publicId或systemId写错了无法再本地获取dtd就会
	//尝试到网上下载，但是自定义的网站根本不存在就会报错了）
	@Test(expected = DocumentException.class)
	public void testGetDocument3() throws DocumentException{
        /*
         * 定义一个dtd验证不合格的xml文件，该xml文件的bean元素id和class是必须，
         * 但是少了一个class应该抛出异常
         */
		String filePath = "test/resources/document/xmlDocumentHolder3.xml";
		//获得Document对象
		Document doc = xmlHolder.getDocument(filePath);
	}
}
