package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//应用模型层将新增一个CrimeLab对象，该对象是一个数据集中存储池，用来存储Crime对象；

//crime数组对象将存储在一个单例里，单例是特殊的Java类，在创建实列时，一个单例类仅允许创建一个实例
//应用能在内存里存在多久，单例就可以，因此对象列表保存在单例里的话，就能随时获取crime数据；不管activity和fragment的生命周期怎么变化；

//要创建单例，需要创建一个带有私有构造方法及get()方法的类，如果实例还不存在，get()方法就会调用构造方法创建它；
public class CrimeLab {
    //按照Android约定，带s前缀，sCrimeLab是一个静态变量；
    private static CrimeLab sCrimeLab;
    //创建一个空List用来保存Crime对象，
    //常见的List实现有ArrayList（使用常规Java数组存储列表元素）；既然mCrimes含有ArrayList，而ArrayList也是一个List
    //那么对于mCrimes来说，ArrayList和List都是有效的类型，推荐声明变量的时候使用List接口类型；
    //这样若有需要，还可以方便的使用其他List实现，如LinkedList
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }
    //CrimeLab的私有构造方法，显然其他类无法创建CrimeLab对象，除非调用get()方法；

    //往CrimeLab里存储Crime对象
    private CrimeLab(Context context) {
        //mCrimes实例化语句使用了Java7引入的<>符号，该符号告诉编译器，List中的元素类型可以基于变量声明传入的抽象参数来确定；
        //编译器可据此推测出ArrayList里可放入Crime对象
        //但在Java7之前，必须这么写：mCrimes=new ArrayList<Crime>();
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }
    //返回数组列表；
    public List<Crime> getCrimes() {
        return mCrimes;
    }
    //返回带指定ID的Crime对象；
    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }

        return null;
    }
}
