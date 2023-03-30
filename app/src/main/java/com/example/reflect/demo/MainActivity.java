package com.example.reflect.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //原生的反射api使用示例
        new ReflectDemo().test();
        //joor封装的反射api使用实例
        new JoorDemo().test();

    }

}