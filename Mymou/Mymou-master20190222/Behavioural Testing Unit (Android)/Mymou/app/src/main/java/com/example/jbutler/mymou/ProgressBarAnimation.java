package com.example.jbutler.mymou;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
//View Animation
//视图动画，通多对整个视图不断做图像的变换（平移、缩放、旋转、透明度）产生的动画效果，是一种渐进式动画。
public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private float from;
    private float  to;

    public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
    }

}