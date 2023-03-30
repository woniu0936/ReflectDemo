package com.example.reflect.demo;

import static com.example.reflect.demo.joor.Reflect.on;

import android.util.Log;

import com.example.reflect.demo.joor.Reflect;
import com.example.reflect.demo.joor.ReflectException;

import java.lang.reflect.Proxy;

/**
 * 使用joor封装过之后的面向对象风格的反射调用来重新实现下ReflectDemo中的代码
 */
public class JoorDemo {

    public static final String TAG = "JoorDemo";

    public void test() {
        demo01();
        demo02();
        demo03();
        demo04();
    }

    /**
     * joor的基本使用
     */
    private void demo01() {
        Log.d(TAG, "----------------------------------------JoorDemo-----------demo01-----------------------------");
        String str = "abc";
        Class<? extends String> c1 = str.getClass();

        //以下三个语法等效
        Reflect r1 = on(Object.class);
        Reflect r2 = on("java.lang.Object");
        Reflect r3 = on("java.lang.Object", ClassLoader.getSystemClassLoader());

        //以下两个语法等效，实例化以下Object变量，得到Object.class
        Object o1 = on(Object.class).get();
        Object o2 = on("java.lang.Object").get();

        String j2 = on((Object) "abc").get();
        int j3 = on(1).get();

        //等价于Class.forName()
        try {
            Class j4 = on("android.widget.Button").type();
        } catch (ReflectException e) {

        }
    }

    /**
     * 获取类的成员变量
     */
    private void demo02() {
        Log.d(TAG, "----------------------------------------JoorDemo-----------demo02-----------------------------");
        try {
            TestClass tc = new TestClass();
            Class aClass = tc.getClass();
            //获取指定的类名
            String name = aClass.getName();
            Log.d(TAG, "---------------------获取指定的类名-------------------name: " + name);

            //public构造函数
            //无参
            Object obj = on(aClass).create().get();
            //有参
            Object obj2 = on(aClass).create(123, "abc").get();
            Log.d(TAG, "---------------------public构造函数-------------------obj: " + obj + ", obj2: " + obj2);

            //private构造函数
            TestClass obj3 = on(TestClass.class).create(1, 1.1).get();
            String a = obj3.getName();
            Log.d(TAG, "---------------------private构造函数-------------------name: " + a);

            //以下4句话创建一个对象
            TestClass tc2 = new TestClass();
            Class temp = tc2.getClass();
            Reflect reflect = on(temp).create();
            //调用一个实例方法
            String a1 = reflect.call("doSomeThing", "param1").get();
            Log.d(TAG, "---------------------调用一个实例方法-------------------doSomeThing: " + a1);
            //调用一个静态方法
            on(TestClass.class).call("doWork").get();
            Log.d(TAG, "---------------------调用一个静态方法-------------------doWork");
            //获取类的私有实例字段并修改
            Reflect reflect1 = on("com.example.reflect.demo.TestClass").create(1, 1.1);
            reflect1.set("name", "OOOOOOO");
            Object name1 = reflect1.get("name");
            Log.d(TAG, "---------------------获取类的私有实例字段并修改-------------------name: " + name1);
            //获取类的私有静态字段并修改
            on("com.example.reflect.demo.TestClass").set("address", "NNNNNNNNNN");
            Object address = on("com.example.reflect.demo.TestClass").get("address");
            Log.d(TAG, "---------------------获取类的私有静态字段并修改-------------------address: " + address);
        } catch (ReflectException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void demo03() {
        Log.d(TAG, "----------------------------------------JoorDemo-----------demo03-----------------------------");
        try {
            //获取AMN的gDefault单例gDefault，gDefault是静态的
            Object gDefault = on("com.example.reflect.demo.AMN").get("gDefault");
            // gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的mInstance字段
            // mInstance就是原始的ClassB2Interface对象
            Object mInstance = on(gDefault).get("mInstance");
            //创建一个这个对象的代理对象ClassB2Mock，然后替换这个字段，让我们的代理对象帮忙干活
            Class<?> classB2Interface = on("com.example.reflect.demo.ClassB2Interface").type();
            Object object = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{classB2Interface}, new ClassB2Mock(mInstance));
            on(gDefault).set("mInstance", object);
            Log.d(TAG, "---------------------获取类的私有静态字段并修改-------------------object: " + object);
        } catch (ReflectException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 下面代码在执行set语法时必然报错，抛出一个NoSuchFieldException，究其原因，是JOOR的Reflect的set方法会在遇到final时，尝试反射出Field类的modifiers字段，
     * Java环境中是有这个字段的，但是Android版本的Field类并没有这个字段，于是就报错了
     */
    private void demo04() {
        Log.d(TAG, "----------------------------------------JoorDemo-----------demo04-----------------------------");
        try {
            //实例字段
            Reflect reflect = on("com.example.reflect.demo.User").create();
            reflect.set("name", "demo04");
            Object name = reflect.get("name");

            //静态字段
            Reflect reflect1 = on("com.example.reflect.demo.User");
            reflect1.set("userId", "100111");
            Object userId = reflect1.get("userId");
        } catch (ReflectException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e1) {
            Log.e(TAG, e1.getMessage());
        }

    }

}
