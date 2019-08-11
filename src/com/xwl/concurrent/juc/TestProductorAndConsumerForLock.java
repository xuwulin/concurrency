package com.xwl.concurrent.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xwl
 * @date 2019-08-10 15:47
 * @description 生产者消费者案例：使用同步锁和Condition
 */
public class TestProductorAndConsumerForLock {

    public static void main(String[] args) {
        Clerk2 clerk2 = new Clerk2();

        Productor2 pro = new Productor2(clerk2);
        Consumer2 con = new Consumer2(clerk2);

        new Thread(pro, "生产者 A").start();
        new Thread(con, "消费者 B").start();

//		 new Thread(pro, "生产者 C").start();
//		 new Thread(con, "消费者 D").start();
    }
}

class Clerk2 {
    private int product = 0;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    // 进货
    public void get() {
        // 上锁
        lock.lock();
        try {
            if (product >= 1) { // 为了避免虚假唤醒，应该总是使用在循环中。
                System.out.println("产品已满！");
                try {
                    // lock有它自己的wait和notify方式，即condition的 await() 和signal()/signalAll()
                    condition.await();
                } catch (InterruptedException e) {
                }
            }
            System.out.println(Thread.currentThread().getName() + " : " + ++product);
            // 唤醒
            condition.signalAll();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    // 卖货
    public void sale() {
        // 上锁
        lock.lock();
        try {
            if (product <= 0) {
                System.out.println("缺货！");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                }
            }
            System.out.println(Thread.currentThread().getName() + " : " + --product);
            condition.signalAll();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}

// 生产者
class Productor2 implements Runnable {

    private Clerk2 clerk2;

    public Productor2(Clerk2 clerk2) {
        this.clerk2 = clerk2;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            clerk2.get();
        }
    }
}

// 消费者
class Consumer2 implements Runnable {

    private Clerk2 clerk2;

    public Consumer2(Clerk2 clerk2) {
        this.clerk2 = clerk2;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk2.sale();
        }
    }
}
