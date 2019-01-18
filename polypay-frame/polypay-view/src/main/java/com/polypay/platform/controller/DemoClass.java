package com.polypay.platform.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class DemoClass {

	public static void main(String[] args) {

		// 构建任务
		List<Runnable> tasks = Lists.newArrayList();

		tasks.add(() -> metho1());
		tasks.add(() -> metho2());

		tasks.add(new Runnable() {
			@Override
			public void run() {
				metho1();
			}
		});

		// 执行
		executorTask(tasks);

	}

	/**
	 * 业务方法1
	 */
	public static void metho1() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("method1");
	}

	/**
	 * 业务方法2
	 */
	public static void metho2() {
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("method2");
	}

	/**
	 * 传入任务
	 * 
	 * @param tasks
	 */
	private static void executorTask(List<Runnable> tasks) {

		// 开启线程数 保持跟任务持平
		ThreadFactory tFactory = new ThreadFactoryBuilder().build();
		ExecutorService pool = new ThreadPoolExecutor(8, 20, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(1024),
				tFactory, new ThreadPoolExecutor.AbortPolicy());

		try {

			// 执行
			tasks.forEach(t -> {
				pool.execute(() -> {
					
					t.run();
				});
			});

			// 销毁
			pool.shutdown();
		} catch (Exception e) {
			if (null != pool) {
				pool.shutdown();
			}
		} finally {
			if (null != pool) {
				pool.shutdown();
			}
		}
	}

}
