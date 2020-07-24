package com.jingchu.nolock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @description: 带有时间戳对象引用实例
 * @author: JingChu
 * @createtime :2020-07-24 14:35:19
 **/
public class MyAtomicStampedReference {
    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        System.out.println("有ABA问题的形式：------------------------------");
        //未使用版本号管理
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            System.out.println(Thread.currentThread().getName() + "\t " + atomicReference.get());
            atomicReference.compareAndSet(101, 100);
            System.out.println(Thread.currentThread().getName() + "\t " + atomicReference.get());
        }, "t1").start();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t sleep 1s \t" + atomicReference.get());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicReference.compareAndSet(100, 2020);
            System.out.println(Thread.currentThread().getName() + "\t " + atomicReference.get());
        }, "t2").start();

        System.out.println("解决了ABA问题的形式：------------------------------");
        //使用版本号管理
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第1次拿到的版本号：" + stamp);
            try {
                //线程休息1s，为了保证t4线程也获得这个版本号
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicStampedReference.compareAndSet(100, 101,
                    stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t 第2次拿到的版本号：" + atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 第3次拿到的版本号：" + atomicStampedReference.getStamp());
        }, "t3").start();
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第1次拿到的版本号：" + stamp);
            try {
                //线程休息2s，为了保证t3线程已经修改版本号
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean falg = atomicStampedReference.compareAndSet(100, 2020,
                    stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t 修改结果：" + falg + "" +
                    "\t 第2次版本号" + atomicStampedReference.getStamp()+"\t 现在的值"+atomicStampedReference.getReference());
            falg = atomicStampedReference.compareAndSet(100, 2020,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 修改结果：" + falg + "" +
                    "\t 第3次版本号" + atomicStampedReference.getStamp()+"\t 现在的值"+atomicStampedReference.getReference());

        }, "t4").start();
    }
}
