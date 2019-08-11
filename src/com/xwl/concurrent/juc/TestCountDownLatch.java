package com.xwl.concurrent.juc;

import java.util.concurrent.CountDownLatch;

/**
 * @author xwl
 * @date 2019-08-10 10:58
 * @description CountDownLatch ：闭锁，在完成某些运算是，只有其他所有线程的运算全部完成，当前运算才继续执行
 * CountDownLatch相当于一个倒计时，当每有一个线程执行时：CountDownLatch(int count)中的变量（count）就要递减1
 * 当CountDownLatch(int count)中的变量(count)为0时，则说明它所有的任务完成了，其他的线程就可以执行了
 */
public class TestCountDownLatch {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(50);
        LatchDemo ld = new LatchDemo(latch);
        long start = System.currentTimeMillis();
        // 50个分线程
        for (int i = 0; i < 50; i++) {
            new Thread(ld).start();
        }
        try {
            // 必须要等待CountDownLatch(int count)中的变量(count)为0时，即50个分线程全部执行完，才能执行下面的代码
            latch.await();
        } catch (InterruptedException e) { // 应用中断异常
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        // 要计算10个分线程的执行时间，必须使主线程等待10个分线程执行完才能执行，就要使用闭锁
        System.out.println("耗费时间为：" + (end - start));
    }
}

class LatchDemo implements Runnable {

    private CountDownLatch latch;

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        /*synchronized (this) { // 多个线程访问存在线程安全问题，可以对其加锁
            try {
                for (int i = 0; i < 50000; i++) {
                    if (i % 2 == 0) {
                        System.out.println(i);
                    }
                }
            } finally {
                // 无论如何都要执行（使latch递减1）
                latch.countDown();
            }
        }*/
        try {
            for (int i = 0; i < 50000; i++) {
                if (i % 2 == 0) {
                    System.out.println(i);
                }
            }
        } finally {
            // 无论如何都要执行（使latch递减1）
            latch.countDown();
        }
    }
}
