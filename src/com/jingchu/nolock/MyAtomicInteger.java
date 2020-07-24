package com.jingchu.nolock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 对比交换测试源码
 * @author: JingChu
 * @createtime :2020-07-24 08:44:30
 **/
public class MyAtomicInteger {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.compareAndSet(5,2020)+" 现在atomicInteger的值："+atomicInteger);
        System.out.println(atomicInteger.compareAndSet(5,724)+" 现在atomicInteger的值："+atomicInteger);
    }
}
