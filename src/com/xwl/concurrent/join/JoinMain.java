package com.xwl.concurrent.join;

/**
 * @Author: xwl
 * @Date: 2019/7/19 14:08
 * @Description: 在主函数中，如果不使用join()方法等待AddThread,那么得到的i就可能是0或者是一个很小的树
 * ，因为AddThread还么开始执行，i的值就已经被输出了，但在使用join()方法后，
 * 表示主线程愿意等待AddThread执行完成，跟着AddThread一起往前走，故在join()方法返回时，
 * AddThread已经执行完成，因此i总是10000000
 */
public class JoinMain {
    public volatile static int i = 0;

    public static class AddThread extends Thread {
        @Override
        public void run() {
            for (i = 0; i < 10000000; i++) {}
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread at = new AddThread();
        at.start();
        at.join();

        // 主线程
        System.out.println(i);
    }
}
