package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
//需要CrimeListFragment向用户展示crime列表，就要用到RecyclerView类；RecyclerView是ViewGroup的子类；
//每一个列表项都是作为一个View子对象显示的，每个列表都只显示Crime的标题和日期；并且View对象是一个包含两个TextView
//的LinearLayout,
public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    //RecyclerView仅限于回收和定位屏幕上的View,列表项View能够显示数据还离不开另外两个类的支持：ViewHolder子类
    //和Adapter子类；ViewHolder只做一件事:容纳View视图；Adapter是一个控制器对象，从模型层获取数据；
    //然后提供给RecyclerView显示，是沟通的桥梁；Adapter:创建必要的ViewHolder,绑定ViewHolder至模型层数据，
    //创建Adapter,首先定义RecyclerView.Adapter子类，然后由它封装从CrimeLab获取的crime；

    //RecyclerView视图需在CrimeListFragment的布局文件中定义（fragment_crime_list.xml）

    //修改本类文件，使用布局并找到布局中的RecyclerView视图，视图与fragment关联

    //需要LayoutManager的支持，RecyclerView视图创建完成后，就立即转交给了LayoutManager对象；
    //LayoutManager在屏幕上摆放类表象，还负责定义屏幕滚动行为；
    //除了一些Android操作系统内置版实现，LayoutManager还有很多第三方库实现版本；
    //我们使用的是LinearLayoutManager类，它支持以竖直列表的形式展示列表项，
    //GridLayoutManager类以网格形式展示列表项；
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            //指定要启动的activity为CrimeActivity, CrimeListFragment创建了一个显式intent;
            //至于Intent构造方法需要的Context对象，CrimeListFragment是通过;
            //使用getActivity()方法传入它托管的activity来满足的；

            //CrimeActivity中更新了，这里CrimeHolder，使用newIntent方法；
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            //调用Fragment.startActivity(Intent)方法，从Fragment中启动activity
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
