package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//QuizActivity是Activity的子类；
public class QuizActivity extends AppCompatActivity {
    //Android的android.util.Log类能够向系统级共享日志中心发送日志信息；
    //Log类的其中一个记录日志方法，public static int d(String tag, String msg),d代表"debug",表示日志信息级别；
    //该方法的第一个参数通常以类名为值的TAG常量传入，这样，很容易看出日志信息的来源；
    private static final String TAG = "QuizActivity";
    //设备旋转，会新建QuizActivity,需要保存以前的数据，知道mCurrentIndex变量的原值；
    //覆盖Activity的protected void onSaveInstanceState(Bundle outState)方法
    //该方法默认实现要求所有activity视图将自身状态数据保存在Bundle对象中；
    //Bundle是存储字符串键与限定类型值之间映射关系（键—值对）的一种结构
    //可以通过覆盖onSaveInstanceState(Bundle)方法，将一些数据保存在bundle中，在onCreate(Bundle)方法中取回
    //这些数据。新增一个常量KEY_INDEX作为将要存储在bundle中的键-值对的键；
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    //activity子类的实例创建后，onCreate(Bundle)方法会被调用
    //千万不要自己去调用onCreate(Bundle)方法或者任何其他activity生命周期方法
    //为通知activity状态变化，你只需在Activity子类里覆盖这些方法，Android会适时调用他们
    @Override
    //覆盖onCreate(Bundle)方法时，实际是在调用activity超类的onCreate(Bundle)方法,并传入收到的bundle；
    //在超类代码实现里，通过取出保存的视图状态数据，activity的视图层级结构得以重建；
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        //setContentView(int layoutResID)获取activity的用户界面；
        //根据传入的布局资源ID参数R.layout.activity_quiz
        //activity_quiz.xml布局的资源ID为R.layout.activity_quiz
        //根据activity_quiz.xml制定的布局的视图，体现在屏幕上；
        setContentView(R.layout.activity_quiz);
        //每次旋转Android重新创建了QuizActivity新实例，mCurrentIndex在onCreate(Bundle)方法中被初始化为0；


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        //以R.id.question_text_view为参数，返回一个视图对象，转化为TextView组件，在updateQuestion中使用
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //public View findViewById(int id)以组件的资源ID为参数，返回一个视图对象；
        //需对返回的视图对象，强制转换；（Button）
        //R.id.true_button在activity_quiz.xml文件android:id="@+id/true_button"中生成；
        mTrueButton = (Button) findViewById(R.id.true_button);
        //Android应用属于典型的事件驱动类型；为响应某个事件而创建的对象叫做事件监听器；（listener）
        //监听器会实现特定事件的监听器接口（listener interface）
        //监听用户的按钮点击事件-----实现View.OnClickListener接口；
        //传入setOnClickListener(OnClickListener)方法的参数是一个监听器；
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            //匿名内部类实现了OnClickListener接口，它需要实现该接口的唯一方法onClick(View);
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        //以R.id.next_button为参数，返回一个视图对象，转化为Button组件
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确保可以循环使用， 用%代替循环；
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        //一个Activity启动另一个activity最简单的方式是使用startActivity方法；
        //public void startActivity(Intent intent),调用请求实际发给了操作系统的ActivityManager
        //ActivityManager负责创建Activity实例并调用其onCreate(Bundle)方法
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                //注意：显示intent与隐式intent
                //intent对象是component用来与操作系统通信的一种媒介工具
                //component包括，正在用的activity,其他一些：service,broadcast receiver以及content provider;
                //intent用来告诉ActivityManager该启动哪个activity
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }
    //之前覆盖了onCreate(Bundle)方法， 现在覆盖其他五个生命周期方法，
    //先是调用了超类的实现方法，然后才调用具体的日志记录方法；
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
//onSaveInstanceState(Bundle)方法用来保存mCurrentIndex值的键值结构；
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //以新增的常量KEY_INDEX值作为键，将mCurrentIndex变量值保存到bundle中；
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        //看Question.java中，这是在给问题文本赋值；
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        //调用Toast类可以创建toast;
        //public static Toast makeText(Context context, int resId, int duration)
        //Activity是Context的子类，该方法的Context参数通常是Activity的一个实例；
        //this就是Activity的实例？为何在mTrueButton里调用就会需要写QuizActivity.this?
        //this返回QuizActivity对象的句柄；
        //第二个参数显示字符串消息的资源ID；第三个指定toast的停留时间；
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }
}
