package com.xwl.concurrent.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xwl
 * @date 2019-08-09 15:03
 * @description 原子变量与CAS算法（CAS效率高）
 * 一、i++ 的原子性问题：i++ 的操作实际上分为三个步骤“读-改-写”
 * int i = 10;
 * i = i++; // 10
 * <p>
 * int temp = i; // 读
 * i = i + 1; // 写
 * i = temp; // 改
 * <p>
 *
 * 二、原子变量：在 java.util.concurrent.atomic 包下提供了一些原子变量。
 * 1. volatile 保证内存可见性
 * 2. CAS（Compare-And-Swap） 算法保证数据变量的原子性
 * CAS 算法是硬件对于并发操作的支持
 * CAS 包含了三个操作数：
 * ①内存值  V
 * ②预估值  A
 * ③更新值  B
 * 当且仅当 V == A 时， V = B（将B的值赋值给V）; 否则，不会执行任何操作。
 */
public class TestAtomicDemo {
    public static void main(String[] args) {
        AtomicDemo ad = new AtomicDemo();

        for (int i = 0; i < 10; i++) {
            new Thread(ad).start();
        }
    }
}

class AtomicDemo implements Runnable {
    // 多线程安全问题并不是每次运行都会出现
//    private int serialNumber = 0; // 会产生多线程安全问题，用此例来说就是会产生相同的数据如：0 0 1 3 7 2 2 6 4 5
//    private volatile int serialNumber = 0; // 使用volatile关键字不能保证原子性，依然存在多线程安全问题
    // 使用原子变量 AtomicInteger，解决内存可见性问题，又保证了变量的原子性
    private AtomicInteger serialNumber = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(getSerialNumber());
    }

    public int getSerialNumber() {
//        return serialNumber++;
        return serialNumber.getAndIncrement();
    }
}