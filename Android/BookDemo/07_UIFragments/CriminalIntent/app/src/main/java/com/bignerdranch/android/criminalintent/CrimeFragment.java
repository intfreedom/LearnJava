package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import static android.widget.CompoundButton.*;
//fragment是一种控制器对象；activity可委派它执行任务；这些任务通常就是管理用户界面；
//管理用户界面的fragment又称UI fragment,它自己也有产生于布局文件的视图；
//fragment的生命周期方法由托管他的activity调用，而activity由操作系统调用；
public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    //Fragment.onCreate(Bundle)是公共方法，而Activity.onCreate(Bundle)是受保护方法；
    //Fragment的生命周期方法，必须是公共方法，因为托管fragment的activity要调用它们；
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }
    //Fragment的视图并没有在Fragment.onCreate(Bundle)方法中生成，该方法配置了fragment实例；
    //onCreateView创建和配置了fragment视图，该方法实例化fragment视图的布局，
    //然后将实例化的View返回给托管的activity;
    //调用LayoutInflater.inflate(...)方法并传入布局的资源ID生成；
    //ViewGroup是视图的父视图，需要父视图来正确配置组件；
    //第三个参数告诉布局生成器是否将生成的视图添加给父视图；

    //Fragment.onCreateView(...)方法中的组件引用几乎等同于Activity.onCreate(Bundle)方法的处理；
    //唯一的区别是，调用了fragment视图的View.findViewById(int)方法，
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        //View.findViewById(int)方法
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        //创建实现TextWatcher监听器接口的匿名内部类，只需关注其中的onTextChanged(...)方法；
        //在onTextChanged(...)方法中，调用CharSequence(代表用户输入)的toString()方法，该方法返回用来设置Crime标题的字符串；
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //处理Button组件，让它显示crime的发生日期，
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        //暂时禁用Button按钮，按钮应处于灰色状态；
        mDateButton.setEnabled(false);
        //处理CheckBox组件，应用它并设置监听器，根据用户操作，更新mSolved状态；
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }
}
