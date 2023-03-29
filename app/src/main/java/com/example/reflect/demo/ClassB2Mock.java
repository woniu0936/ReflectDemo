package com.example.reflect.demo;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ClassB2Mock implements InvocationHandler {

    private Object obj;

    public ClassB2Mock(Object classB2Interface) {
        this.obj = classB2Interface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d(JoorDemo.TAG, "method: " + method.getName());

        if ("doSomeThing".equals(method.getName())) {
            Log.d(JoorDemo.TAG, "hello doSomeThing！");
        }

        return method.invoke(obj, args);
    }
}
