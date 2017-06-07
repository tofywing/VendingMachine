package tools;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import static java.lang.Thread.sleep;

/**
 * Created by Yee on 6/1/17.
 */

public class FadeEffect {
    private TextView mView;
    private Handler mHandler;
    private int val;
    private boolean notInterrupt;

    public FadeEffect() {

    }

    public void action(final int interval, TextView textView) {
        mView = textView;
        notInterrupt = true;
        mHandler = new Handler();
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (int i = 255; i >= 0 && notInterrupt; i--) {
                    val = i;
                    setupColor(interval);
                    if (i == 0) {
                        for (; i < 255; i++) {
                            val = i;
                            setupColor(interval);
                        }
                    }
                }
            }

        };
        thread.start();
    }

    private void setupColor(int interval) {
        mHandler.post(new Runnable() {
            public void run() {
                mView.setTextColor(Color.argb(255, val, val, val));
            }
        });
        try {
            sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void interrupt() {
        notInterrupt = false;
    }
}
