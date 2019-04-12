package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
//名为CrimeActivity的activity来托管CrimeFragment实例；

public class CrimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在setContentView()方法中调用onAttach(Context), onCreate(Bundle)和onCreateView(...)
        setContentView(R.layout.activity_crime);
        //Activity类中相应添加了FragmentManager类，负责管理fragment并将它们的视图添加到activity的视图层级结构中，
        //要以代码的方式将fragment添加给activity,需要直接调用activity的FragmentManager
        //首先是获取FragmentManager本身；
        FragmentManager fm = getSupportFragmentManager();
        //获取FragmentManager之后，再获取一个fragment交给它管理；
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        //FragmentManager.beginTransaction()方法创建并返回FragmentTransaction实例
        //FragmentTransaction类支持流接口(fluent interface)的链式方法调用，以此配置FragmentTransaction再返回它；
        //流式接口（fluent interface）是软件工程中面向对象API的一种实现方式，以提供更为可读的源代码
        if (fragment == null) {
            fragment = new CrimeFragment();
            //以下可解读为：创建一个新的fragment事务，执行一个fragment添加操作，然后提交该事务；
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        }
        //以上代码总结：
        //使用R.id.fragment_container的容器视图资源ID，向FragmentManager请求并获取fragment;
        //activity被销毁时，它的FragmentManager会将队列保存下来；
        //如果指定容器视图资源ID的fragment不存在，
        //如果指定容器视图资源ID的fragment不存在，则fragment变量为空值，这时新建CrimeFragment，
        // 并启动一个新的fragment事务；并将新建的Fragment添加到队列中；

        //activity的FragmentManager负责调用队列中fragment的生命周期方法；添加fragment供FragmentManager管理时，
        //onAttach(Context), onCreate(Bundle)和onCreateView(...)方法会被调用；

        //托管activity的onCreate(Bundle)方法执行后，onActivityCreated(Bundle)方法也会被调用；
        //因为是在CrimeActivity.onCreate(Bundle)方法中添加CrimeFragment，所以fragment被添加后，该方法会被调用；
    }
}
