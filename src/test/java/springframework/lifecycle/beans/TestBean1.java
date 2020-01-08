package springframework.lifecycle.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.SmartLifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class TestBean1 implements ApplicationContextAware , InitializingBean, BeanNameAware, DisposableBean, TestBean1Inteface
, SmartLifecycle {

    private String prop1;

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public void init(){
        System.out.println("init-method");
    }

    @PostConstruct
    private void PostConstruct(){
        System.out.println("PostConstruct");
    }

    private void destroyMethod() {
        System.out.println("destroy-method");
    }

    @PreDestroy
    private void preDestroy() {
        System.out.println("preDestroy");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("setBeanName");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("setApplicationContext");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void start() {
        System.out.println("LifecycleProcessor start");
    }

    @Override
    public void stop() {
        System.out.println("LifecycleProcessor stop");
    }

    @Override
    public boolean isRunning() {
        System.out.println("LifecycleProcessor isRunning");
        return false;
    }

    @Override
    public boolean isAutoStartup() {
        return false;
    }

    @Override
    public void stop(Runnable runnable) {

    }

    @Override
    public int getPhase() {
        return 0;
    }
}
