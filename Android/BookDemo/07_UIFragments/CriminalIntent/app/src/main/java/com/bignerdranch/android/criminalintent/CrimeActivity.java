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
        setContentView(R.layout.activity_crime);
        //Activity类中相应添加了FragmentManager类，负责管理fragment并将它们的视图添加到activity的视图层级结构中，
        FragmentManager fm = getSupportFragmentManager();
        //获取FragmentManager之后，再获取一个fragment交给它管理；
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new CrimeFragment();
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        }
    }
}
