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
    //onCreateView创建和配置了fragment视图，该方法实例化fragment视图的布局，
    //然后将实例化的View返回给托管的activity;
    //调用LayoutInflater.inflate(...)方法并传入布局的资源ID生成；
    //ViewGroup是视图的父视图，需要父视图来正确配置组件；
    //第三个参数告诉布局生成器是否将生成的视图添加给父视图；
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
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

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

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
