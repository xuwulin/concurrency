package com.xwl.concurrent;

/**
 * @Author: xwl
 * @Date: 2019/6/25 20:40
 * @Description: 假设某银行大厅有4台出号机，下面使用程序模拟出号，假设每天最多出号50
 * 运行结果：
 * 柜台：2号出号机当前的号码是：1
 * 柜台：1号出号机当前的号码是：1
 * 柜台：4号出号机当前的号码是：1
 * 柜台：3号出号机当前的号码是：1
 * 柜台：4号出号机当前的号码是：2
 *
 * 之所以出现这个问题，根本原因是因为每一个线程的逻辑执行单元都不一样，我们新建了4个TicketWindow线程，他的票号都是从1到50
 * 并不是我们期望的4台机器交互执行，要解决这个问题，无论TicketWindow被实例化多少次，只需要保证index是唯一的即可，
 * 我们立即会想到使用static修饰index
 * private static int index = 1;
 * 就能得到我们想要的结果
 * 但是极其不推荐这样做，因为static修饰的变量生命周期很长。。。
 */
public class TicketWindow extends Thread {
	// 柜台名称
	private final String name;

	// 最多50个号
	private static final int MAX = 50;

//	private int index = 1;

	private static int index = 1;

	public TicketWindow(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		while (index <= MAX) {
			System.out.println("柜台：" + name + "当前的号码是：" + index++);
		}
	}

	public static void main(String[] args) {
		TicketWindow t1 = new TicketWindow("1号出号机");
		t1.start();

		TicketWindow t2 = new TicketWindow("2号出号机");
		t2.start();

		TicketWindow t3 = new TicketWindow("3号出号机");
		t3.start();

		TicketWindow t4 = new TicketWindow("4号出号机");
		t4.start();
	}
}
