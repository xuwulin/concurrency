package com.xwl.concurrent;

/**
 * @Auther: xwl
 * @Date: 2019/6/25 21:31
 * @Description: 实现Runnable接口，模拟银行出号
 * 注意：不管是以这种方法还是用TicketWindow中使用 private static int index = 1;这种方法
 * 这两个程序多运行几次或者MAX值从50增贾到100,500甚至更大，都会出现一个号码出现多次的情况，也会出现某个号码不会出现的情况
 * 更会出现超过最大值的情况，这是因为共享支援index存在线程安全问题！！！
 *
 * synchronized关键字提供了一种互斥机制，也就是说同一时刻，只能有一个线程访问同步资源
 * synchronized关键字应该尽可能地只作用于共享资源（数据）的读写作用域
 */
public class TicketWindowRunnableWithSync implements Runnable {

	// 最多50个号
	private static final int MAX = 50;

	private int index = 1;

	// mutex 互斥对象
	private final static Object MUTEX = new Object();

	@Override
	public void run() {
		// 使用synchronized关键字
		synchronized (MUTEX) {
			while (index <= MAX) {
				System.out.println(Thread.currentThread() + "的号码是：" + index++);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		final TicketWindowRunnableWithSync task = new TicketWindowRunnableWithSync();

		Thread window1 = new Thread(task, "1号窗口");
		Thread window2 = new Thread(task, "2号窗口");
		Thread window3 = new Thread(task, "3号窗口");
		Thread window4 = new Thread(task, "4号窗口");

		window1.start();
		window2.start();
		window3.start();
		window4.start();
	}
}
