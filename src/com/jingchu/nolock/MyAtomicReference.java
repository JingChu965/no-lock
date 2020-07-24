package com.jingchu.nolock;


import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: 原子对象引用 测试代码
 * @author: JingChu
 * @createtime :2020-07-24 14:25:12
 **/
public class MyAtomicReference {
    public static void main(String[] args) {
        User user1 = new User("zhangsan",23);
        User user2 = new User("lisi",24);

        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(user1);

        System.out.println(atomicReference.compareAndSet(user1,user2)+"\t "+atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(user1,user2)+"\t "+atomicReference.get().toString());

    }


}
class User{
    String name;
    int age;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}