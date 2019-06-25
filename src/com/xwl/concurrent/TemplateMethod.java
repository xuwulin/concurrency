package com.xwl.concurrent;

/**
 * @Auther: xwl
 * @Date: 2019/6/17 22:00
 * @Description: 设计模式--模板方法模式
 * Thread的run和start就是一个比较典型的模板方法模式，父代码编写算法结构代码，子类实现逻辑细节
 * 下面通过一个简单的例子来看看模板方法模式
 *
 * print方法类似于Thread的start方法，而wrapPrint则类似于run方法，
 * 这样做的好处是，程序结构由父类控制，并且是final修饰的，不允许被重写
 * 子类只需要实现想要的逻辑任务即可
 */
public class TemplateMethod {

	public final void print(String message) {
		System.out.println("################");
		wrapPrint(message);
		System.out.println("################");
	}

	protected void wrapPrint(String message) {

	}

	public static void main(String[] args) {
		TemplateMethod t1 = new TemplateMethod() {
			@Override
			protected void wrapPrint(String message) {
				System.out.println("*" + message + "*");
			}
		};
		/*
		 * ################
		 * *hello thread*
		 * ################
		 */
		t1.print("hello thread");

		TemplateMethod t2 = new TemplateMethod() {
			@Override
			protected void wrapPrint(String message) {
				System.out.println("*" + message + "*");
			}
		};
		/*
		 * ################
		 * *hello world*
		 * ################
		 */
		t2.print("hello world");
	}
}
