package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
//显示crime列表需在应用控制器层新增一个activity和一个fragment，fragment为CrimeListFragment

//需要CrimeListFragment向用户展示crime列表，就要用到RecyclerView类；RecyclerView是ViewGroup的子类；
//每一个列表项都是作为一个View子对象显示的，每个列表都只显示Crime的标题和日期；并且View对象是一个包含两个TextView
//的LinearLayout,
public class CrimeListFragment extends Fragment {

    //RecyclerView创建刚好充棉屏幕的12个View,而不是100个，用户滑动屏幕切换视图时，上一个视图会回收利用，
    //顾名思义，RecyclerView就是回收再利用，循环往复；

    //RecyclerView仅限于回收和定位屏幕上的View,列表项View能够显示数据还离不开另外两个类的支持：ViewHolder子类
    //和Adapter子类；ViewHolder只做一件事:容纳View视图；Adapter是一个控制器对象，从模型层获取数据；
    //然后提供给RecyclerView显示，是沟通的桥梁；Adapter:创建必要的ViewHolder,绑定ViewHolder至模型层数据，
    //创建Adapter,首先定义RecyclerView.Adapter子类，然后由它封装从CrimeLab获取的crime；

    //RecyclerView视图需在CrimeListFragment的布局文件中定义（fragment_crime_list.xml）

    //修改本类文件，使用布局并找到布局中的RecyclerView视图，视图与fragment关联

    //需要LayoutManager的支持，RecyclerView视图创建完成后，就立即转交给了LayoutManager对象；
    //LayoutManager在屏幕上摆放类表象，还负责定义屏幕滚动行为；
    //除了一些Android操作系统内置版实现，LayoutManager还有很多第三方库实现版本；
    //我们使用的是LinearLayoutManager类，它支持以竖直列表的形式展示列表项，GridLayoutManager类以网格形式展示列表项；
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

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
    //搞定了Adapter后，将它和RecyclerView关联起来；实现一个设置CrimeListFragment用户界面的updateUI方法；
    //该方法创建CrimeAdapter,然后设置给RecyclerView,
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }
    //定义ViewHolder内部类，它会实例化并使用list_item_crime布局，

    //设置OnClickListener监听器，既然列表项视图都关联着ViewHolder，就可以让ViewHolder为它监听用户触摸事件；
    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Crime mCrime;
        //把视图绑定工作放入CrimeHolder类里，绑定之前，首先实例化相关组件；
        //由于这是一次性任务，直接放入构造方法里处理；
        private TextView mTitleTextView;
        private TextView mDateTextView;
        //在CrimeHolder的构造方法里，我们首先实例化list_item_crime布局，然后传给super(...)方法，也就是ViewHolder
        //的构造方法，基类ViewHolder因而实际引用这个视图，可以在ViewHolder的itemView变量里找到它；
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
        }
        //CrimeHolder需要一个bind(Crime)方法，每次有新的Crime要在CrimeHolder中显示时，都要调用它一次；现在实现这个方法；
        //现在，只要取到一个Crime,CrimeHolder就会刷新显示TextView标题视图和TextView日期视图；
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),
                    mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    //创建Adapter
    //需要显示新创建的ViewHolder或让Crime对象和已创建的ViewHolder关联时，RecyclerView会调用Adapter的方法；
    //修改CrimeAdapter类，使用bind(Crime)方法，每次RecyclerView要求CrimeHolder绑定对应的Crime时，都会调用bind(Crime)方法；
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }
        //RecyclerView需要新的ViewHolder来显示列表项时，会调用onCreateViewHolder方法，
        //在这个方法内部，我们创建一个LayoutInflater，然后用它创建CrimeHolder;
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }
        //CrimeAdapter必须覆盖onBindViewHolder()方法；
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
