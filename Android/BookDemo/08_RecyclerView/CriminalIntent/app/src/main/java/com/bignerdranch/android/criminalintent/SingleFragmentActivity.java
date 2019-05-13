package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

//通用代码的作用，我们设置，从activity_fragment.xml布局里实例化activity视图，
//然后在容器中查找FragmentManager里的fragment;
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();//新增了名为createFragment()的抽象方法
    //SingleFragmentActivity的子类会实现该方法，来返回由activity托管的fragment实例；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在setContentView()方法中调用onAttach(Context), onCreate(Bundle)和onCreateView(...)
        //从activity_fragment.xml布局里实例化activity视图
        setContentView(R.layout.activity_fragment);
        //Activity类中相应添加了FragmentManager类，负责管理fragment并将它们的视图添加到activity的视图层级结构中，
        //要以代码的方式将fragment添加给activity,需要直接调用activity的FragmentManager
        //首先是获取FragmentManager本身；
        FragmentManager fm = getSupportFragmentManager();
        //获取FragmentManager之后，再获取一个fragment交给它管理；
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        //在容器中查找FragmentManager里的fragment，如果找不到，就新建fragment并将其添加到容器中
        if (fragment == null) {
            //与原来代码的区别，为了实例化新的fragment，我们新增了名为createFragment()的抽象方法
            //去实现不同的fragment，如CrimeFragment()或者CrimeListFragment();
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
