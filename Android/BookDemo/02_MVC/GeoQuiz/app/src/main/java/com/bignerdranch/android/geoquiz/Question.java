package com.bignerdranch.android.geoquiz;

public class Question {
    //mTextResId问题的文本；保存地理知识问题字符串的资源ID；
    private int mTextResId;
    //mAnswerTrue问题的答案；保存地理知识问题的答案，true或者false；
    private boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
