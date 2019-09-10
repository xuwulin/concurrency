package com.xwl.concurrent.juc;

/**
 * @author xwl
 * @date 2019-08-10 11:56
 * @description 生产者和消费者案例
 * <p>
 * /**
 * 此时运行会出现的问题（面试可能会问）：
 * 一旦生产者发现生产的产品已经满了，它没有停止，还在不断的生产（即打印：产品已满！）
 * 而消费者发现已经没有产品了，还在不断的消费（即打印：缺货！）
 * <p>
 * 对应真实的情况：
 * 添加和创建数据的线程为生产者线程
 * 删除和销毁数据的线程为消费者线程
 * 1、当生产者线程过快，也就是说生产者不断地发数据，而消费者可能已经接收不到数据了，这样就可能会导致数据丢失
 * 2、当消费者过快，也就是说消费之不断地消费数据，而生产者可能已经不发数据了，这就可能会出现重复的数据或者是错误的数据
 * <p>
 * 所以说生产者消费者案例不使用等待唤醒机制，就很有可能会出现上述问题
 */
public class TestProductorAndConsumer {
    public static void main(String[] args) {
        // 共享数据
        Clerk clerk = new Clerk();

        // 生成者和消费者都去访问共享数据clerk,存在线程安全问题
        Productor productor = new Productor(clerk);
        Consumer consumer = new Consumer(clerk);

        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();

//        new Thread(productor, "生产者C").start();
//        new Thread(consumer, "消费者D").start();
    }
}

/**
 * 店员
 */
class Clerk {
    // 共享数据，存在多线程安全问题，需要解决
    private int product = 0;

    // 进货
    // 使用synchronized关键字修饰方法，因为访问的是共享数据，存在多线程安全问题
    // 1、不使用等待唤醒机制，依然存在多线程安全问题
    /*public synchronized void get() {
        // 假设最多能进10个货物
        if (product >= 10) {
            System.out.println("产品已满！");
        } else {
            System.out.println(Thread.currentThread().getName() + " : " + ++product);
        }
    }*/

    // 2、使用等待唤醒机制解决多线程安全问题
    public synchronized void get() {
        // 假设最多能进10个货物
//        if (product >= 1) {
        while (product >= 1) { // 将if换成while; 为了避免虚假唤醒，应该总是使用在循环中！！！
            System.out.println("产品已满！");
            try {
                // 产品已满！生产者就不能再继续生产了，必须等待消费者给他通知（消费者消费完了，通知继续生产）
                this.wait(); // 等待并释放锁资源（等在这一行代码），当被唤醒（notify/notifyAll）以后，从这行代码往下继续执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } /*else {
            System.out.println(Thread.currentThread().getName() + " : " + ++product);
            // 成功生产,通知消费者消费（即可以卖货）
            this.notifyAll();
        }*/
        System.out.println(Thread.currentThread().getName() + " : " + ++product);
        // 成功生产,通知消费者消费（即可以卖货）
        this.notifyAll();
    }

    // 卖货
    // 使用synchronized关键字修饰方法，因为访问的是共享数据，存在多线程安全问题
    // 1、不使用等待唤醒机制，依然存在多线程安全问题
    /*public synchronized void sale() {
        if (product <= 0) {
            System.out.println("缺货！");
        } else {
            System.out.println(Thread.currentThread().getName() + " : " + --product);
        }
    }*/

    // 2、使用等待唤醒机制解决多线程安全问题
    public synchronized void sale() {
//        if (product <= 0) { // 当给生产者加上延时后，消费者的速度可能大于生产者，会先执行
        while (product <= 0) { // 将if换成while；为了避免虚假唤醒，应该总是使用再循环中！！！
            System.out.println("缺货！");
            try {
                // 缺货，等待生产者生产
                this.wait(); // 等待并释放锁资源（等在这一行代码），当被唤醒（notify/notifyAll）以后，从这行代码往下继续执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } /*else {
            System.out.println(Thread.currentThread().getName() + " : " + --product);
            // 成功卖出产品，说明店员那里有空位，通知生产者生产产品
            this.notifyAll();
        }*/
        System.out.println(Thread.currentThread().getName() + " : " + --product);
        // 成功卖出产品，说明店员那里有空位，通知生产者生产产品
        this.notifyAll();
    }
}

/**
 * 生产者
 */
class Productor implements Runnable {
    private Clerk clerk;

    public Productor(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.get();
        }
    }
}

/**
 * 消费者
 */
class Consumer implements Runnable {
    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            // 使用 标记为 2 的代码，依然存在问题，比如给生产者加上0.2秒的延时(这在网络传输中有延时很正常，必须考虑)
            // 此时消费者的速度可能就会大于生产者
            // 出现的问题：程序运行完了，但是并没有停止
            // 出现问题的原因：在于生产者和消费者中的else代码块，生产者生产了产品并且等待，等待消费者唤醒，但是消费者已经退出没有人去唤醒生产者
            // 如何解决：去掉else
            // 但去掉else后依然还有问题：当有多个生产者和消费者时,product可能为负数，这就是虚假唤醒问题！！
            // 如何解决：将if换成while
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clerk.sale();
        }
    }
}