package com.xwl.concurrent.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xwl
 * @date 2019-08-11 20:34
 * @description ReadWriteLock : 读写锁
 * 写写/读写 需要“互斥”
 * 读读 不需要互斥
 */
public class TestReadWriteLock {

    public static void main(String[] args) {
        ReadWriteLockDemo rw = new ReadWriteLockDemo();

        // 1个线程写
        new Thread(() -> rw.set((int) (Math.random() * 101)), "Write:").start();

        // 100个线程读
        for (int i = 0; i < 100; i++) {
            new Thread(() -> rw.get()).start();
        }
    }
}

class ReadWriteLockDemo {

    // 共享数据，考虑线程安全问题
    private int number = 0;

    // 读写锁
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    // 读
    public void get() {
        lock.readLock().lock(); // 上锁
        try {
            System.out.println(Thread.currentThread().getName() + " : " + number);
        } finally {
            lock.readLock().unlock(); // 释放锁
        }
    }

    // 写
    public void set(int number) {
        lock.writeLock().lock(); // 上锁
        try {
            System.out.println(Thread.currentThread().getName());
            this.number = number;
        } finally {
            lock.writeLock().unlock(); // 释放锁
        }
    }
}