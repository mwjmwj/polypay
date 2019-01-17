package com.polypay.platform.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Lists;

public class DemoClass {

	public static void main(String[] args) {

		// 构建任务
		List<Task> tasks = Lists.newArrayList();
	
		tasks.add(()->metho1());
		tasks.add(()->metho2());
		
		tasks.add(new Task() {
			@Override
			public void executor() {
				metho1();
			}
		});
		
		
		// 执行
		executorTask(tasks);
		
	}
	
	/**
	 *  业务方法1
	 */
	public static void metho1()
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("method1");
	}
	
	/**
	 *  业务方法2
	 */
	public static void metho2()
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("method2");
	}

	/**
	 *  传入任务
	 * @param tasks
	 */
	private static void executorTask(List<Task> tasks) {

		// 开启线程数 保持跟任务持平
		ExecutorService es = Executors.newFixedThreadPool(tasks.size());
		try {

			// 执行
			tasks.forEach(t -> {
				es.execute(() -> {
					t.executor();
				});
			});

			// 销毁
			es.shutdown();
		} catch (Exception e) {
			if (null != es) {
				es.shutdown();
			}
		} finally {
			if (null != es) {
				es.shutdown();
			}
		}
	}

	

}
