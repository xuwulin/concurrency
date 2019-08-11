package com.xwl.concurrent.juc;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xwl
 * @date 2019-08-10 10:48
 * @description CopyOnWriteArrayList/CopyOnWriteArraySet : “写入并复制”
 * 注意：添加操作多时，效率低，因为每次添加时都会进行复制，开销非常的大。并发迭代操作多时可以选择。
 */
public class TestCopyOnWriteArrayList {
    public static void main(String[] args) {
        HelloThread ht = new HelloThread();
        for (int i = 0; i < 10; i++) {
            new Thread(ht).start();
        }
    }
}

class HelloThread implements Runnable {

    // 会出现并发修改异常：java.util.ConcurrentModificationException
//	private static List<String> list = Collections.synchronizedList(new ArrayList<String>());
    // 使用CopyOnWriteArrayList则不会出现并发修改异常（每次写入时都会先复制）
    private static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

    static {
        list.add("AA");
        list.add("BB");
        list.add("CC");
    }

    @Override
    public void run() {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            // 边迭代取值
            System.out.println(it.next());
            // 边添加元素
            // 结果：会出现并发修改异常：java.util.ConcurrentModificationException
            // 一个线程都会出现此异常，更别说10个线程并发访问
            list.add("AA");
        }
    }
}