package xmltest.context;

import java.util.ArrayList;
import java.util.HashSet;

import main.java.context.ApplicationContext;
import main.java.context.XmlApplicationContext;
import main.java.objects.XmlApplicationContextObject1;
import main.java.objects.XmlApplicationContextObject2;
import main.java.objects.XmlApplicationContextObject3;
import main.java.objects.XmlApplicationContextObject4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class XmlApplicationContextTest {
	ApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new XmlApplicationContext(
				new String[] { "/resources/xml/context/XmlApplicationContext1.xml" });
	}

	@After
	public void tearDown() throws Exception {
		ctx = null;
	}

	@Test
	public void testGetBean() {

		// 拿到第一个, 使用无参构造器创建
		XmlApplicationContextObject1 obj1 = (XmlApplicationContextObject1) ctx
				.getBeanInstance("test1");
		assertNotNull(obj1);
	}

	@Test
	public void testSingleton() {
		// test1是单态bean
		XmlApplicationContextObject1 obj1 = (XmlApplicationContextObject1) ctx
				.getBeanInstance("test1");
		XmlApplicationContextObject1 obj2 = (XmlApplicationContextObject1) ctx
				.getBeanInstance("test1");
		assertEquals(obj1, obj2);
		// test3不是单态bean
		XmlApplicationContextObject1 obj3 = (XmlApplicationContextObject1) ctx
				.getBeanInstance("test3");
		XmlApplicationContextObject1 obj4 = (XmlApplicationContextObject1) ctx
				.getBeanInstance("test3");
		assertFalse(obj3.equals(obj4));
	}

	@Test
	public void testConstructInjection() {
		XmlApplicationContextObject1 obj1 = (XmlApplicationContextObject1) ctx
				.getBeanInstance("test1");
		// 拿到第二个, 使用多参数构造器创建
		XmlApplicationContextObject2 obj2 = (XmlApplicationContextObject2) ctx
				.getBeanInstance("test2");
		assertNotNull(obj2);
		assertEquals(obj2.getName(), "rongdi");
		assertEquals(obj2.getAge(), 22);
		assertEquals(obj2.getObject1(), obj1);
	}

	/*
     * 测试自动装配
     */
	@Test
	public void testAutowire() {

		XmlApplicationContextObject3 obj1 = (XmlApplicationContextObject3) ctx
				.getBeanInstance("test4");
		assertNotNull(obj1);
		XmlApplicationContextObject1 obj2 = obj1.getObject1();
		System.out.println(obj2);
		assertNotNull(obj2);
		XmlApplicationContextObject1 obj3 = (XmlApplicationContextObject1) ctx
				.getBeanInstance("object1");
		assertEquals(obj2, obj3);
	}

	/*
     * 测试是否包含该bean
     */
	@Test
	public void testContainsBean() {
		boolean result = ctx.beanIsExist("test1");
		assertTrue(result);
		result = ctx.beanIsExist("test5");
		assertTrue(result);
		result = ctx.beanIsExist("No exists");
		assertFalse(result);
	}

	/*
     * 测试延迟加载
     */
	@Test
	public void testLazyInit() {
		// test5是延迟加载的, 没有调用过getBean方法, 那么容器中就不会创建这个bean
		Object obj = ctx.getBeanWithoutCreate("test5");
		assertNull(obj);
		// System.out.println(obj);
		obj = ctx.getBeanInstance("test5");
		assertNotNull(obj);
		System.out.println(obj);
	}

	/*
     * 测试设值注入
     */
	@Test
	public void testSetProperties() {
		XmlApplicationContextObject3 obj1 = (XmlApplicationContextObject3) ctx
				.getBeanInstance("test6");
		XmlApplicationContextObject1 obj2 = (XmlApplicationContextObject1) ctx
				.getBeanInstance("object1");
		assertEquals(obj1.getName(), "rongdi");
		assertEquals(obj1.getAge(), 22);
		assertEquals(obj1.getObject1(), obj2);
		XmlApplicationContextObject4 obj4 = (XmlApplicationContextObject4) ctx
				.getBeanInstance("test7");
		System.out.println((ArrayList<Object>) obj4.getList());
		System.out.println((HashSet<Object>) obj4.getSet());
		System.out.println((ArrayList<Object>) obj4.getRefTest());
	}

}