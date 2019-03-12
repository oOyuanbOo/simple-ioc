package main.java.context;

import main.java.create.BeanCreator;
import main.java.create.BeanCreatorImpl;
import main.java.create.PropertyHandler;
import main.java.create.PropertyHandlerImpl;
import main.java.exception.BeanCreateException;
import main.java.xml.autowire.Autowire;
import main.java.xml.autowire.ByNameAutowire;
import main.java.xml.autowire.NoAutowire;
import main.java.xml.document.DocumentHolder;
import main.java.xml.document.XmlDocumentHolder;
import main.java.xml.element.*;
import main.java.xml.element.loader.ElementLoader;
import main.java.xml.element.loader.ElementLoaderImpl;
import main.java.xml.element.parser.BeanElementParser;
import main.java.xml.element.parser.BeanElementParserImpl;
import org.dom4j.Document;
import org.dom4j.Element;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 该类继承ApplicationContext接口，定义成抽象类是因为本类中定义的方法还不够完善
 * 不想本类被直接实例化来使用希望使用它扩展功能后的子类
 * @author rongdi
 *
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

	/**
	 * 定义一个文档持有对象
	 */
	protected DocumentHolder documentHolder = new XmlDocumentHolder();
	/**
	 * 定义一个元素加载对象
	 */
	protected ElementLoader elementLoader = new ElementLoaderImpl();
	/**
	 * 定义一个Element元素读取类
	 */
	protected BeanElementParser elementParser = new BeanElementParserImpl();
	/**
	 * 定义一个创建bean对象的类
	 */
	protected BeanCreator beanCreator = new BeanCreatorImpl();
	/**
	 * 定义一个属性处理类
	 */
	protected PropertyHandler propertyHandler = new PropertyHandlerImpl();
	/**
	 * 定义一个Map用来保存bean元素的id和生成的对应的实例，主要是单实例的bean的对象需要保存起来
	 */
	protected Map<String, Object> beanInstances = new HashMap<String, Object>();

	/**
	 * 初始化Elements将对应的document元素中的Element调用elementLoader的方法缓存起来
	 * 可以读取多个xnl文件的路径，参数为一个字符串数组
	 * @param xmlPaths
	 */
	protected void initElements(String[] xmlPaths) {
		try {
			/**
			 * 获取当前项目的根路径
			 */
			URL classPathUrl = AbstractApplicationContext.class.getClassLoader().getResource(".");
			/**
			 * 为防止路径出现汉字乱码的情况使用utf-8进行解码
			 */
			String classPath = java.net.URLDecoder.decode(classPathUrl.getPath(),"utf-8");
			/**
			 * 遍历所有的路径
			 */
			for (String path : xmlPaths) {
				/**
				 * 由根路径加传入的相对路径获取document元素
				 */
				Document doc = documentHolder.getDocument(classPath + path);
				/**
				 * 将所有的路径对应的xml文件里的bean元素都缓存到elementLoader对象中
				 */
				elementLoader.addBeanElements(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 创建一个bean实例, 如果找不到该bean对应的配置文件的Element对象, 抛出异常
	 * @param id
	 * @return
	 */
	protected Object createBeanInstance(String id) {
		/**
		 * 首先直接在elementLoader对象中找id对应的元素
		 */
		Element e = elementLoader.getBeanElement(id);
		/**
		 * 如果没找到抛出异常
		 */
		if (e == null)
			throw new BeanCreateException("没找到 " + id+"对应的bean元素");
		/**
		 * 调用本类定义的instance方法实例化该元素对应的类
		 */
		Object result = this.instanceBeanElement(e);
		System.out.println("创建bean: " + id);
		System.out.println("该bean的对象是: " + result);
		/**
		 * 设值注入, 先判断是否自动装配
		 */
		Autowire autowire = elementParser.getAutowire(e);
		if (autowire instanceof ByNameAutowire) {
			/**
			 * 使用名称自动装配
			 */
			autowireByName(result);
		} else if (autowire instanceof NoAutowire) {
			/**
			 * 调用设置注入，给根据e元素生成的对象result设置e元素里配置的property属性值
			 */
			this.setterInject(result, e);
		}
		/**
		 * 返回创建的实例result
		 */
		return result;
	}
	/**
	 * 实例化一个bean, 如果该bean的配置有constructor-arg元素, 那么使用带参数的构造器
	 * @param e
	 * @return
	 */
	protected Object instanceBeanElement(Element e) {
		/**
		 * 得到该bean元素的class属性的值
		 */
		String className = elementParser.getAttribute(e, "class");
		/**
		 * 得到bean节点下面的constructor-arg元素
		 */
		List<Element> constructorElements = elementParser.getConstructorArgsElements(e);
		/**
		 * 判断使用什么构造器进行创建(判断标准为bean元素下是否有constructor-arg子元素)
		 * 如果没有constructor-arg子元素调用无参构造器
		 */
		if (constructorElements.size() == 0) {
			/**
			 * 没有constructor-arg子元素, 使用无参构造器
			 */
			return beanCreator.createBeanUseDefaultConstruct(className);
		} else {
			/**
			 * 有constructor-arg子元素,得到所有的构造参数 使用有参数构造器, 构造注入参数
			 */
			List<Object> args = getConstructArgs(e);
			return beanCreator.createBeanUseDefineConstruct(className, args);
		}
	}
	/**
	 * 创建所有的bean的实例, 延迟加载的不创建
	 */
	protected void createBeanInstances() {
		/**
		 * 获取保存到elementLoader对象中的Bean元素
		 */
		Collection<Element> elements = elementLoader.getBeanElements();
		/**
		 * 遍历所有的bean元素
		 */
		for (Element e : elements) {
			/**
			 * 得到bean元素的lazy属性值
			 */
			boolean lazy = elementParser.isLazy(e);
			/**
			 * 如果不是延迟加载
			 */
			if (!lazy) {
				/**
				 * 得到该元素的id属性值
				 */
				String id = e.attributeValue("id");
				/**
				 * 创建这个id所对应的bean的实例
				 */
				Object bean = this.getBeanInstance(id);
				/**
				 * 如果bean实例进入处理bean的方法中
				 */
				if (bean == null) {
					/**
					 * 处里bean的方法分是否是单例两种情况进行考虑
					 */
					handleBean(id);
				}
			}
		}
	}

	/**
	 * 处理bean, 如果是单态的, 则加到map中, 非单态, 则创建返回
	 * @param id
	 * @return
	 */
	protected Object handleBean(String id) {
		/**
		 * 首先根据传入的id属性值找到该bean创建一个对应的bean的实例
		 */
		Object beanInstance = createBeanInstance(id);;
		/**
		 * 如果是单例的则保存到Map中方便需要的时候取出
		 */
		if (isSingleton(id)) {
			/**
			 * 单态的话, 放到map中
			 */
			this.beanInstances.put(id, beanInstance);
		}
		/**
		 * 返回创建的bean的实例
		 */
		return beanInstance ;
	}

	/**
	 * 判断id值对应的bean是否为单态的
	 */
	public boolean isSingleton(String id) {
		/**
		 * 使用ElementLoader方法获得对应的Element
		 */
		Element e = elementLoader.getBeanElement(id);
		/**
		 * 使用ElementReader判断是否为单态
		 */
		return elementParser.isSingleton(e);
	}


	/**
	 * 通过property元素为参数obj设置属性
	 * @param obj
	 */
	protected void setterInject(Object obj, Element beanElement) {
		/**
		 * 返回bean元素的所有的property标签对应的元素
		 */
		List<PropertyElement> properties = elementParser.getPropertyValue(beanElement);
		/**
		 * 调用本类定义的方法得到所需要的属性名与所要设置的值的对信息
		 */
		Map<String, Object> propertiesMap = this.getPropertyArgs(properties);
		/**
		 * 将对应的值设置到obj对象中
		 */
		propertyHandler.setProperties(obj, propertiesMap);
	}

	/**
	 * 以map的形式得到需要注入的参数对象, key为setter方法对应的属性名, value为参数对象
	 * @param properties
	 * @return
	 */
	protected Map<String, Object> getPropertyArgs(List<PropertyElement> properties) {
		/**
		 * 定义一个结果映射保存所需要的属性名与所要设置的值的对信息
		 */
		Map<String, Object> result = new HashMap<String, Object>();
		/**
		 * 遍历所有的property元素
		 */
		for (PropertyElement p : properties) {
			/**
			 * 得到prperty元素中的子元素
			 */
			LeafElement le = p.getLeafElement();
			/**
			 * 判断如果是RefElement元素
			 */
			if (le instanceof RefElement) {
				/**
				 * 将对应的属性名和需要设置进去的实例对象保存在map中
				 */
				result.put(p.getName(), this.getBeanInstance((String)le.getValue()));
			} else if (le instanceof ValueElement) {
				/**
				 * 如果是ValueElement，将对应的属性名和需要设置的值保存到Map中
				 */
				result.put(p.getName(), le.getValue());
			} else if(le instanceof CollectionElement) {
				/**
				 * 先判断是否是CollectionElement如果是再判断Collection标签里面放的是value标签还是ref标签
				 * 可以直接取出Collection里面的List判断还要判断类型是list还是set，根据不同情况调用不同的方法
				 * 将值放入result的map中
				 */
				if(this.childIsValueElement((CollectionElement)le)) {
					if("list".equals(le.getType()))
						result.put(p.getName(),this.arrayToArrayList((Object[])le.getValue()));
					else {
						result.put(p.getName(),this.arrayToHashSet((Object[])le.getValue()));
					}
				}
				else {
					if("list".equals(le.getType())){
						result.put(p.getName(),this.arrayToArrayList(this.getValuesIfChildIsRefElement(le)));
					}
					else {
						result.put(p.getName(),this.arrayToHashSet(this.getValuesIfChildIsRefElement(le)));
					}

				}
			}
		}
		/**
		 * 返回该结果信息
		 */
		return result;
	}
	/**
	 * 如果collectionElement下是ref元素那么调用该方法将集合标签中所有ref标签下对应的实例都生成好后
	 * 返回这些实例的一个Object数组形式
	 * @param le
	 * @return
	 */
	protected Object[] getValuesIfChildIsRefElement(LeafElement le) {
		/**
		 * 定义一个临时存放的ArrayList
		 */
		List<Object> tempList = new ArrayList<Object>();
		/**
		 * 遍历在CollectionElement里面取出的Object数组，因为这里都是ref标签，根据对应的值得到实例对象
		 */
		for(Object o:(Object[])le.getValue()) {
			tempList.add(this.getBeanInstance((String)o));
		}
		return tempList.toArray();
	}

	/**
	 * 将数组转换为ArrayList的方法
	 * @param obj
	 * @return
	 */
	protected List<Object> arrayToArrayList(Object[] obj) {
		List<Object> temp = new ArrayList<Object>();
		for(Object o:obj) {
			temp.add(o);
		}
		return temp;
	}
	/**
	 * 将数组转换成HashSet的方法
	 */
	protected Set<Object> arrayToHashSet(Object[] obj) {
		Set<Object> temp = new HashSet<Object>();
		for(Object o:obj) {
			temp.add(o);
		}
		return temp;
	}
	/**
	 * 判断CollectionElement中配置的是ValueElement元素，如果是则返回true
	 */
	protected boolean childIsValueElement(CollectionElement ce) {
		/**
		 * 在CollectionElement元素中得到保存子元素的list判断该list中是否是ValueElement元素
		 * 如果是返回true
		 */
		if(ce.getList().get(0) instanceof ValueElement) {
			return true;
		}
		return false;
	}
	/**
	 * 得到一个bean里面配置的构造参数
	 * @param e
	 * @return
	 */
	protected List<Object> getConstructArgs(Element beanElment) {
		/**
		 * 得到该bean元素所有的构造参数元素，该参数可能使RefElement和ValueElement
		 */
		List<LeafElement> datas = elementParser.getConstructorValue(beanElment);
		/**
		 * 定义一个结果信息保存得到的的参数集合
		 */
		List<Object> result = new ArrayList<Object>();
		/**
		 * 遍历所有的构造参数元素
		 */
		for (LeafElement d : datas) {
			/**
			 * 如果是ValueElement元素那么直接将该元素的值保存到结果中
			 */
			if (d instanceof ValueElement) {
				d = (ValueElement)d;
				result.add(d.getValue());
			} else if (d instanceof RefElement) {
				d = (RefElement)d;
				String refId = (String)d.getValue();
				/**
				 * 如果是引用元素, 则直接调getBean去获取(获取不到则创建)，本方法本来就间接的被
				 * getgetBeanInstance方法调用了，现在在这个方法里在调用getBeanInstance
				 * 这个方法相当于形成了一个递归调用，递归的出口在引用所对一个的bean中再没有引用
				 */
				result.add(this.getBeanInstance(refId));
			}
		}
		return result;
	}

	/**
	 * 自动装配一个对象, 得到该bean的所有setter方法, 再从容器中查找对应的bean
	 * 例如, 如果bean中有一个setSchool(School)方法, 那么就去查名字为school的bean,
	 * 再调用setSchool方法设入对象中
	 * @param obj
	 */
	protected void autowireByName(Object obj) {
		/**
		 * 得到该对象所有的setXXX方法和对应的属性（bean的id值）
		 */
		Map<String, Method> methods = propertyHandler.getSetterMethodsMap(obj);
		/**
		 * 遍历所有的方法
		 */
		for (String s : methods.keySet()) {
			/**
			 * 得到对应的bean元素
			 */
			Element e = elementLoader.getBeanElement(s);
			/**
			 * 没有对应的元素配置, 继续循环
			 */
			if (e == null) continue;
			/**
			 * 调用getBeanInstance方法返回beanInstance
			 */
			Object beanInstance = this.getBeanInstance(s);
			/**
			 * 得到该对象的setter方法
			 */
			Method method = methods.get(s);
			/**
			 * 将产生的实例调用executeMethod使用反射的方式设值到obj对象中，实现按名字的自动装配
			 */
			propertyHandler.executeMethod(obj, beanInstance, method);
			System.out.println("执行"+method.getName()+"方法给对象:"+obj+"注入"+beanInstance);
		}
	}
	/**
	 * 得到对应id的bean的实例
	 */
	public Object getBeanInstance(String id) {
		Object beanInstance = this.beanInstances.get(id);
		/**
		 * 如果获取不到该bean, 则调用handleBean处理
		 */
		if (beanInstance == null) {
			/**
			 * 判断处理单态或者非单态的bean
			 */
			beanInstance = handleBean(id);
		}
		/**
		 * 返回得到的bean的实例
		 */
		return beanInstance;
	}
	/**
	 * 判断对应id的bean元素是否存在（直接到配置文件中找不要到elementLoader对象的缓存中找）
	 */
	public boolean beanIsExist(String id) {
		/**
		 * 调用ElementLoader对象, 根据id得到对应的Element对象
		 */
		Element e = elementLoader.getBeanElement(id);
		return (e == null) ? false : true;
	}

	/**
	 * 在本类的缓存中获得id对应的实例，若果该id对应的bean是单态的就能获取到
	 */
	public Object getBeanWithoutCreate(String id) {
		return this.beanInstances.get(id);
	}
}