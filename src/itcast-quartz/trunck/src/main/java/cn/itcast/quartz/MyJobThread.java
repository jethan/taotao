package cn.itcast.quartz;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MyJobThread {
    public void service() {
	ThreadPoolTaskExecutor jobPropertyConfigurer = (ThreadPoolTaskExecutor) ContextUtil.getBean("executor");
	jobPropertyConfigurer.execute(new TestThread()); // 单个线程加入到线程池中执行，可加入多个线程到线程池
    }
}
