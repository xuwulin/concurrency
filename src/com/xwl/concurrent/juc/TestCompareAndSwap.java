package com.xwl.concurrent.juc;

/**
 * @author xwl
 * @date 2019-08-09 15:23
 * @description 模拟CAS算法
 */
public class TestCompareAndSwap {

    public static void main(String[] args) {
        final CompareAndSwap cas = new CompareAndSwap();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int expectedValue = cas.get();
                boolean b = cas.compareAndSet(expectedValue, (int) (Math.random() * 101));
                System.out.println(b);
            }).start();
        }
    }
}

class CompareAndSwap {
    private int value; // 默认值0

    // 获取内存值
    public synchronized int get() {
        return value;
    }

    /**
     * 比较
     * @param expectedValue 预估值 A
     * @param newValue 更新值 B
     * @return
     */
    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        // 读取旧值 V
        int oldValue = value;
        // 比较旧值和预估值是否相等：当且仅当 V == A 时， V = B（将B的值赋值给V）; 否则，不会执行任何操作。
        if (oldValue == expectedValue) {
            this.value = newValue;
        }

        return oldValue;
    }

    /**
     * 设置
     * @param expectedValue 预估值
     * @param newValue 更新值
     * @return
     */
    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}