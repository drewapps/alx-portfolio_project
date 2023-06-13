package com.drewapps.ai.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class Typewriter {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 40; //Default 500ms delay
    Context context;
    TextView textView;
    Listener listener;

    public Typewriter(Context context, TextView textView, Listener listener) {
        this.context = context;
        this.textView = textView;
        this.listener = listener;
    }


    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            textView.setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }else {
                Util.showLog("DONE");
                listener.onComplete();
            }
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        textView.setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }

    public interface Listener{
        void onComplete();
    }
}
