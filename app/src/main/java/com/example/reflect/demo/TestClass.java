package com.example.reflect.demo;

import android.util.Log;

public class TestClass {

    public static final String TAG = "MainTag";

    private static final String address = "西安";

    private String name;

    public TestClass() {
        this.name = "name";
    }

    public TestClass(String name) {
        this.name = name;
    }

    public TestClass(int a, String name) {
        this.name = name;
    }

    private TestClass(int a, double name) {

    }

    public String doSomeThing(String d) {
        Log.d(TAG, "--------doSomeThing method invoke, param: " + d);
        return "doSomeThing";
    }

    private static void doWork() {
        Log.d(TAG, "--------doWork method invoke----------");
    }

    public String getName() {
        return name;
    }
}
