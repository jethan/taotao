package cn.itcast.quartz;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
	new ClassPathXmlApplicationContext("classpath:applicationContext-scheduler.xml");
    }

}
