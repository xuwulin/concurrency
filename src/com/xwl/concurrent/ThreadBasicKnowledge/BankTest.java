package com.xwl.concurrent.ThreadBasicKnowledge;

/**
 * 使用同步机制将单例模式中的懒汉式改写为线程安全的
 *
 * @author shkstart
 * @create 2019-02-15 下午 2:50
 */
public class BankTest {

}

/**
 * 单例模式：懒汉式
 * Bank()这个类要求要是单例的，所以只能创建一个实例
 */
class Bank{
    // 无参构造
    private Bank(){}

    private static Bank instance = null; // 懒汉式，null

    public static Bank getInstance(){
        // 方式一：效率稍差
//        synchronized (Bank.class) {
//            if(instance == null){
//                instance = new Bank();
//            }
//            return instance;
//        }

        // 方式二：效率更高
        if(instance == null){
            synchronized (Bank.class) {
                if(instance == null){
                    instance = new Bank();
                }
            }
        }
        return instance;
    }
}
