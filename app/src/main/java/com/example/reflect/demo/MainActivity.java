package com.example.reflect.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.StringJoiner;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String str = "abc";
        Class<? extends String> c1 = str.getClass();
        try {
            Class c2 = Class.forName("java.lang.String");
            Class c3 = Class.forName("android.widget.Button");
            Class c5 = c3.getSuperclass();

            log(c1, c2, c3, c5);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Class c6 = String.class;
        Class c7 = java.lang.String.class;
//        Class c8 = MainActivity.InnerClass.class;
        Class c9 = int.class;
        Class c10 = int[].class;

        log(c6, c7, c9, c10);

        Class c11 = Boolean.TYPE;
        Class c12 = Byte.TYPE;
        Class c13 = Character.TYPE;
        Class c14 = Short.TYPE;
        Class c15 = Integer.TYPE;
        Class c16 = Long.TYPE;
        Class c17 = Float.TYPE;
        Class c18 = Double.TYPE;
        Class c19 = Void.TYPE;

        log(c11, c12, c13, c14, c15, c16, c17, c18, c19);

        try {
            reflectTestClass();
            reflectTestClass2();
            reflectTestClass3();
            reflectTestClass4();
            reflectTestClass5();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void reflectTestClass() {
        Log.d(TAG, "----------------------------------------reflectTestClass----------------------------------------");
        TestClass tc = new TestClass();
        Class<? extends TestClass> aClass = tc.getClass();
        //获取指定类名
        String className = aClass.getName();
        Log.d(TAG, "className: " + className);
        //获取所有类的构造方法，不管是public还是private的
        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        Log.d(TAG, "获取所有类的构造方法，不管是public还是private的, 方法数目: " + declaredConstructors.length);
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            //获取指定的公有构造方法
            int modifiers = declaredConstructor.getModifiers();
            Log.d(TAG, "获取指定的公有构造方法: " + Modifier.toString(modifiers) + ", className: " + className);
            //获取构造方法参数集合
            Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                Log.d(TAG, "parameterType: " + parameterType.getName());
            }
        }

        try {
            //获取无参构造函数
            Constructor<? extends TestClass> declaredConstructor = aClass.getDeclaredConstructor();
            //获取有一个参数的构造函数
            Class[] p = {String.class};
            Constructor<? extends TestClass> declaredConstructor1 = aClass.getDeclaredConstructor(p);
            //获取有两个参数的构造函数
            Class[] p2 = {int.class, String.class};
            Constructor<? extends TestClass> declaredConstructor2 = aClass.getDeclaredConstructor(p2);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void reflectTestClass2() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Log.d(TAG, "----------------------------------------reflectTestClass2----------------------------------------");
        Class<?> aClass = Class.forName("com.example.reflect.demo.TestClass");
        //获取无参构造函数
        Constructor declaredConstructor = aClass.getDeclaredConstructor();
        //通过newInstance()创建对象实例
        Object obj = declaredConstructor.newInstance();
        //获取有两个参数的构造函数
        Class[] p2 = {int.class, String.class};
        Constructor declaredConstructor2 = aClass.getDeclaredConstructor(p2);
        //通过newInstance()创建对象实例
        Object obj2 = declaredConstructor2.newInstance(10086, "hello reflect");
        Method method = aClass.getDeclaredMethod("doSomeThing", String.class);
        method.setAccessible(true);
        Object result = method.invoke(obj2, "reflect method");
        Log.d(TAG, "----------reflectTestClass2 " + aClass.getName() + " " + method.getName() + " return: " + result);
    }

    private void reflectTestClass3() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Log.d(TAG, "----------------------------------------reflectTestClass3----------------------------------------");
        Class<?> aClass = Class.forName("com.example.reflect.demo.TestClass");
        //静态方法直接调用
        Method doWork = aClass.getDeclaredMethod("doWork");
        doWork.setAccessible(true);
        doWork.invoke(null);
    }

    private void reflectTestClass4() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Log.d(TAG, "----------------------------------------reflectTestClass4----------------------------------------");
        Class<?> aClass = Class.forName("com.example.reflect.demo.TestClass");
        //获取有两个参数的构造函数
        Class[] p2 = {int.class, String.class};
        Constructor declaredConstructor2 = aClass.getDeclaredConstructor(p2);
        //通过newInstance()创建对象实例
        Object obj2 = declaredConstructor2.newInstance(10086, "hello reflect");
        //获取name字段 private
        Field name = aClass.getDeclaredField("name");
        name.setAccessible(true);
        //调用getName()方法
        Method getName = aClass.getDeclaredMethod("getName");
        Object result = getName.invoke(obj2);
        Log.d(TAG, "----------reflectTestClass4 " + aClass.getName() + " name修改前: "  + result);
        name.set(obj2, "hello world");
        //访问name字段，private
        Object field = name.get(obj2);
        Log.d(TAG, "----------reflectTestClass4 " + aClass.getName() + " name修改后: "  + field);
    }

    /**
     * 获取类的静态私有变量并修改他
     *
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    private void reflectTestClass5() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Log.d(TAG, "----------------------------------------reflectTestClass5----------------------------------------");
        Class<?> aClass = Class.forName("com.example.reflect.demo.TestClass");
        //获取无参构造函数
        Constructor declaredConstructor = aClass.getDeclaredConstructor();
        //通过newInstance()创建对象实例
        Object obj = declaredConstructor.newInstance();
        Field address = aClass.getDeclaredField("address");
        address.setAccessible(true);
        Object o1 = address.get(null);
        Log.d(TAG, "----------reflectTestClass5 " + aClass.getName() + " address修改前: "  + o1);
        address.set(o1, "北京");
        Object o2 = address.get(null);
        Log.d(TAG, "----------reflectTestClass5 " + aClass.getName() + " address修改后: "  + o2);
    }

    private void log(Object... obj) {
        StringJoiner sj = new StringJoiner(", ");
        for (Object o : obj) {
            sj.add(o.toString());
        }
        Log.d(TAG, sj.toString());
    }

    class Test {
    }
}