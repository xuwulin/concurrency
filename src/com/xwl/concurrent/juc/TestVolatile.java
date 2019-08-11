package com.xwl.concurrent.juc;

/**
 * @author xwl
 * @date 2019-08-09 14:26
 * @description volatile 关键字
 * 一、volatile 关键字：当多个线程进行操作共享数据时，可以保证内存中的数据彼此可见。
 * 相较于 synchronized 是一种较为轻量级的同步策略。
 * 注意：
 *  1. volatile 不具备“互斥性”（synchronized是互斥锁，即当一个线程进入synchronized代码块，其余线程必须等待它执行完才能进入）
 *  2. volatile 不能保证变量的“原子性”（不可分割）
 *
 * flag为td线程和主线程的共享数据，使用volatile关键字修饰，保证内存中数据可见
 *
 * 内存可见性问题：当程序运行时，JVM会为每一个线程分配一个独立的缓存（用于提高效率），
 * 当多个线程操作共享数据时，彼此不可见
 * 可以理解为：flag在主内存中，当线程td和主线程需要flag时，首先会去主内存中获取flag的值，并改变flag的值然后再写入主内存中
 *
 */
public class TestVolatile {
    public static void main(String[] args) {
        // 启动线程
        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();

        // 主线程
        while (true) {
            if (td.isFlag()) {
                System.out.println("--------------------");
                break;
            }

            // 也可以使用synchronized,则flag则无需使用volatile修饰
            // 使用synchronized(同步锁)关键字，让线程重复地到主内存中去读取需要的数据（flag）
            // 但是使用synchronized效率特别低，特别是遇到多个线程时，synchronized会阻塞
            /*synchronized (td) {
                if (td.isFlag()) {
                    System.out.println("--------------------");
                    break;
                }
            }*/
        }
    }
}

class ThreadDemo implements Runnable {

//    private boolean flag = false; // 永远不会打印 --------------------
    // 使用volatile关键字
    private volatile boolean flag = false; // 会打印 --------------------

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
        System.out.println("flag = " + isFlag());
    }
}
