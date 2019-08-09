package com.xwl.concurrent.join;

import sun.font.CreatedFontTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author: xwl
 * @Date: 2019/7/19 10:56
 * @Description: join方法：英文join的翻译，通常是加入的意思，一个线程要加入另外一个线程，最好的方法就是等着他一起走
 */
public class FightQueryExample {
    // 1.合作的航空公司
    private static List<String> fightCompany = Arrays.asList("CAS", "CEA", "HNA");

    public static void main(String[] args) {
        List<String> results = search("SH", "BJ");
        System.out.println("================results==================");
        results.forEach(System.out::println);
    }

    private static List<String> search(String original, String destination) {
        final List<String> result = new ArrayList<>();
        // 2.创建查询航班信息线程列表
        List<FightQueryTask> tasks = fightCompany
                .stream()
                .map(f -> createSearchTask(f, original, destination))
                .collect(toList());

        // 3.分别启动这几个线程
        tasks.forEach(Thread::start);

        // 4.分别调用每一个线程的join方法(由主线程调用)，阻塞当前线程
        tasks.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        tasks.stream().map(FightQueryTask::get).forEach(result::addAll);
        return result;
    }

    private static FightQueryTask createSearchTask(String fight, String original, String destination) {
        FightQueryTask fightQueryTask = new FightQueryTask(fight, original, destination);
        return fightQueryTask;
    }
}
