package com.xwl.concurrent.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xwl
 * @date 2019-08-10 16:06
 * @description 编写一个程序，开启 3 个线程，这三个线程的 ID 分别为 A、B、C，每个线程将自己的 ID 在屏幕上打印 10 遍，要求输出的结果必须按顺序显示。
 * 如：ABCABCABC…… 依次递归
 * 三个线程交替，涉及到线程通讯
 */
public class TestABCAlternate {

    public static void main(String[] args) {
        AlternateDemo ad = new AlternateDemo();

        // 线程A
        new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                ad.loopA(i);
            }
        }, "A").start();

        // 线程B
        new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                ad.loopB(i);
            }
        }, "B").start();

        // 线程C
        new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                ad.loopC(i);
                System.out.println("-----------------------------------");
            }
        }, "C").start();
    }

}

/**
 * 交替Demo
 */
class AlternateDemo {

    private int number = 1; // 当前正在执行线程的标记

    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    /**
     * @param totalLoop : 循环第几轮
     */
    public void loopA(int totalLoop) {
        lock.lock(); // 上锁
        try {
            // 1. 判断
            if (number != 1) {
                // number != 1 线程必须等待
                condition1.await();
            }

            // 2. 打印,控制该线程打印几次
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }

            // 3. 唤醒2
            number = 2;
            condition2.signal(); // 只唤醒一个
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // 释放锁
        }
    }

    public void loopB(int totalLoop) {
        lock.lock();

        try {
            //1. 判断
            if (number != 2) {
                condition2.await();
            }

            //2. 打印
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }

            //3. 唤醒
            number = 3;
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopC(int totalLoop) {
        lock.lock();

        try {
            //1. 判断
            if (number != 3) {
                condition3.await();
            }

            //2. 打印
            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }

            //3. 唤醒
            number = 1;
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
