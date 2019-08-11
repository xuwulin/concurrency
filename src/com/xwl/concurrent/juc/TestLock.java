package com.xwl.concurrent.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xwl
 * @date 2019-08-10 11:33
 * @description 同步锁Lock
 * 一、用于解决多线程安全问题的方式有3种：
 * jdk 1.5 前 使用synchronized:隐式锁
 * 1、同步代码块
 * 2、同步方法
 * <p>
 * jdk 1.5 后 使用 同步锁Lock：
 * 3. 同步锁 Lock
 * 注意：是一个显示锁（更加灵活），需要通过 lock() 方法上锁，必须通过 unlock() 方法进行释放锁
 */
public class TestLock {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(ticket, "1号窗口").start();
        new Thread(ticket, "2号窗口").start();
        new Thread(ticket, "3号窗口").start();
    }
}

class Ticket implements Runnable {

    private int tick = 100;
    // 如果不加锁，则会出现多线程安全问题
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            // 上锁
            lock.lock();
            try {
                if (tick > 0) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 完成售票，余票为：" + --tick);
                }
            } finally {
                // 释放锁
                // 要保证unlock()方法必须执行
                lock.unlock();
            }
        }
    }
}
