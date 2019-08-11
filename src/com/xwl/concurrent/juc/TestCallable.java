package com.xwl.concurrent.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author xwl
 * @date 2019-08-10 11:17
 * @description 一、创建执行线程的方式三：实现 Callable 接口。 相较于实现 Runnable 接口的方式，方法可以有返回值，并且可以抛出异常。
 * 二、执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。  FutureTask 是  Future 接口的实现类
 * <p>
 * 注意：创建可执行线程的方式总共有4种：
 * 1、继承Thread类
 * 2、实现Runnable接口
 * 3、实现Callable接口
 * 4、线程池
 */
public class TestCallable {

    public static void main(String[] args) {
        ThreadDemo2 td2 = new ThreadDemo2();

        // 1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
        FutureTask<Integer> result = new FutureTask<>(td2);
        new Thread(result).start();
        // 2.接收线程运算后的结果
        try {
            // 只有当上边的线程执行完了以后，才会执行下面的代码，闭锁
            // 所以 FutureTask 可用于 闭锁
            Integer sum = result.get();
            System.out.println(sum);
            System.out.println("------------------------------------");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}

/**
 * 实现Callable接口：与实现Runnable接口的区别：有返回值，并且抛出异常
 */
class ThreadDemo2 implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= 100000; i++) {
            sum += i;
        }
        return sum;
    }
}

/**
 * 实现Runnable接口方式
 */
/*
class ThreadDemo2 implements Runnable{
	@Override
	public void run() {
	}
}*/
