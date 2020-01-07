package xml.document;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * 自定义的XmlEntityResolver实现dom4j里的EntityResolver接口并实现里面的
 * resolveEntity方法，来获得一个dtd的输入源
 * @author rongdi
 */
public class XmlEntityResolver implements EntityResolver {

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		/**
		 * 如果自己写的xml配置文件中引入dtd的时候publicId与"-//RONGDI//DTD BEAN//CN"相同
		 * 并且systemId与"http://www.cnblogs.com/rongdi/beans.dtd"相同，就从本地的相对项目的路径
		 * 寻找dtd,返回一个dtd的输入源，若果找不到该dtd就会尝试到对应的网址上寻找
		 */
		if ("-//RONGDI//DTD BEAN//CN".equals(publicId)&& "dtd/beans.dtd".equals(systemId)) {
			InputStream stream = this.getClass().
			getResourceAsStream("/src/main/java/dtd/beans.dtd");
			return new InputSource(stream);
		} 
		return null;
	}
}
