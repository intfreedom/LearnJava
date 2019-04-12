package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;
//显示crime列表需在应用控制器层新增一个activity和一个fragment，activity为CrimeListActivity

//对于CrimeListActivity,我们仍可以，使用定义在activity_crime.xml文件中的布局，该布局提供了一个放置fragment的
//FrameLayout容器视图，其中的fragment可在activity中使用代码获取；

//需在配置文件（AndroidManifest.xml）声明它，应用启动后，用户看到的界面应该是crime列表，所以配置
//CrimeListActivity为launcher activity
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
