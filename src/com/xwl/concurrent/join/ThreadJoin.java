package com.xwl.concurrent.join;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @Auther: xwl
 * @Date: 2019/7/19 10:18
 * @Description:
 */
public class ThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        // 定义两个线程，并保存在threads中
        List<Thread> threads = IntStream.range(1, 3).mapToObj(ThreadJoin::create).collect(toList());
        // 启动这两个线程
        threads.forEach(Thread::start);
        // 执行这两个线程的join方法
        for (Thread thread : threads) {
            thread.join(); // join方法由主线程调用
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "#" + i);
            shortSleep();
        }


    }

    // 构造一个简单的线程
    public static Thread create(int seq) {
        return new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                System.out.println(Thread.currentThread().getName() + "#" + i);
                shortSleep();
            }
        }, String.valueOf(seq));
    }

    public static void shortSleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
