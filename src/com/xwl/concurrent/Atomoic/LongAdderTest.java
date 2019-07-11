package com.xwl.concurrent.Atomoic;

/**
 * @Auther: xwl
 * @Date: 2019/7/11 14:58
 * @Description: AtomicLong通过CAS提供了非阻塞的原子性操作，相比使用阻塞算法的同步器来说它的性能已经很好了，
 * 但是在高并发下大量线程会同时去竞争更新同一个原子变量，但是由于同时只有一个线程的CAS操作会成功，
 * 这就造成了大量线程竞争失败后，会通过无线循环不断进行自旋尝试CAS的操作，而这会白白浪费CPU资源
 *
 * 因此JDK 8新增了一个原子性递增或者递减类LongAdder用来克服在高并发下使用AtomicLong的缺点。既然AtomicLong的
 * 性能瓶颈是由于过多线程同时去竞争一个变量的更新而产生的，那么如果把一个变量分解为多个变量，让同样多的线程
 * 去竞争多个资源，就解决了性能问题
 */
public class LongAdderTest {
}
