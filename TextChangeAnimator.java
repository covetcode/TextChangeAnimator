package com.wayww.windowtest.TextChangeAnimator;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.widget.TextView;

/**
 * Created by wayww on 2017/3/10.
 * github: https://github.com/covetcode
 */

public class TextChangeAnimator  {
    private TextView mTextView;
    private ValueAnimator mValueAnimator;
    private long mDuration;
    private long mDelay;

    private int mOldColor;
    private ColorStateList mColorStateList;

    private String mChangedText;
    private int mChangedColor;
    public TextChangeAnimator(TextView textView){
        mTextView = textView;
        mDuration = 1000;
        mChangedColor = -1;
    }

    public static TextChangeAnimator create(TextView textView){
        return new TextChangeAnimator(textView);
    }

    public TextChangeAnimator setText(String text) {
        mChangedText = text;
        return this;
    }

    public TextChangeAnimator setDuration(long duration){
        mDuration = duration;
        return this;
    }

    public TextChangeAnimator setDelay(long delay){
        mDelay = delay;
        return this;
    }

    public TextChangeAnimator withColorChange(int color){
        mChangedColor = color;
        return this;
    }



    public TextChangeAnimator start(){
        if (mValueAnimator != null){
            if (mValueAnimator.isRunning()){
                mValueAnimator.cancel();
            }
        }else {
            mValueAnimator = ValueAnimator.ofFloat(1,0,0,1);
            mValueAnimator.setDuration(mDuration);
            mValueAnimator.setStartDelay(mDelay);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                boolean change = false;
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    if (value == 0 && !change){
                        change = true;
                        mOldColor = mChangedColor;
                        mTextView.setText(mChangedText);
                    }else if (value == 1){
                        change = false;
                    }

                    int color = mOldColor&0x00FFFFFF|((int)((mOldColor>>>24)*value)<<24);
                    mTextView.setTextColor(color);
                }
            });

            /**
             * 对ColorStateList的支持
             */
//        mValueAnimator.addListener(new AnimatorListenerAdapter(){
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mTextView.setTextColor(mColorStateList);
//            }
//        });
//            mColorStateList = mTextView.getTextColors();

            mOldColor = mTextView.getCurrentTextColor();
            if (mChangedColor == -1){
                mChangedColor = mOldColor;
            }

        }
        mValueAnimator.start();
        return this;
    }
}
