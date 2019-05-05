package com.bignerdranch.android.geoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//QuizActivity是Activity的子类；

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
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
    //activity子类的实例创建后，onCreate(Bundle)方法会被调用
    //千万不要自己去调用onCreate(Bundle)方法或者任何其他activity生命周期方法
    //为通知activity状态变化，你只需在Activity子类里覆盖这些方法，Android会适时调用他们
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(int layoutResID)获取activity的用户界面；
        //根据传入的布局资源ID参数R.layout.activity_quiz
        //activity_quiz.xml布局的资源ID为R.layout.activity_quiz
        //根据activity_quiz.xml制定的布局的视图，体现在屏幕上；
        setContentView(R.layout.activity_quiz);
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
            @Override
            //匿名内部类实现了OnClickListener接口，它需要实现该接口的唯一方法onClick(View);
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //匿名内部类实现了OnClickListener接口，它需要实现该接口的唯一方法onClick(View);
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        //以R.id.next_button为参数，返回一个视图对象，转化为Button组件
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                //确保可以循环使用， 用%代替循环；
                updateQuestion();
            }
        });

        updateQuestion();//because private int mCurrentIndex = 0;所以这一个永远指向第一个问题；
        //会不会与后面旋转就变到第一个问题有关；
    }

    private void updateQuestion() {
        //看Question.java中，这是在给问题文本赋值；
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
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
    //注意这里this的变化，在匿名内部类中this，必须写成QuizActivity.this，因为匿名内部类中，this指的是监听器
    //View.OnClickListener;而private void checkAnswer(...) 中this，指的是传入QuizActivity实例
}
