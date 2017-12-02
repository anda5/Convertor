package com.example.gogo.orar;

import android.os.Handler;
import com.dd.processbutton.ProcessButton;

import java.text.ParseException;
import java.util.Random;

/**
 * Created by paulodichone on 3/31/15.
 */
public class ProgressGenerator {
    public interface OnCompleteListener {

        public void onComplete() throws ParseException;
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final ProcessButton button) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 10;
                button.setProgress(mProgress);
                if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    try {
                        mListener.onComplete();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, generateDelay());
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(1000);
    }
}

