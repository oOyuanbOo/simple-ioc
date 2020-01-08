package springframework.lifecycle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springframework.lifecycle.beans.ApplicationContextAwareUtil;
import springframework.lifecycle.beans.TestBean1;
import springframework.lifecycle.beans.TestBean1Inteface;
import springframework.lifecycle.proxy.TestBean1Proxy;

import java.lang.reflect.Proxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springxml/spring-context-test.xml")
public class LifecycleTest {

    @Autowired
    private TestBean1 testBean1;

    @Test
    public void testLifecycleOfBean (){
        testBean1.setProp1("1");
        System.out.println(testBean1.getProp1());
    }

    @Test
    public void testBeanInitMethodInLifecycleAndProxy(){
        TestBean1Proxy proxy = new TestBean1Proxy();
        TestBean1Inteface tb = (TestBean1Inteface) Proxy.newProxyInstance(TestBean1.class.getClassLoader(), TestBean1.class.getInterfaces(), proxy);
        tb.init();
    }

    @Test
    public void testApplicationContextAware(){
        new ApplicationContextAwareUtil().getBean(TestBean1.class);
    }

    @Test
    public void testBeanNameAware(){
        new ApplicationContextAwareUtil().getBean(TestBean1.class);
    }

}
