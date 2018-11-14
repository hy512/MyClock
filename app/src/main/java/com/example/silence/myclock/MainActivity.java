package com.example.silence.myclock;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.silence.myclock.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    TextView mText;
    View mView;
    WindowManager mWindowManager;
    WindowManager.LayoutParams mWindowLayout;
    Handler mHandler;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

//        run();
    }

    void run() {
        if (mWindowManager != null) return;
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(createView(), createLayoutParams());
        mWindowManager = windowManager;

        mHandler = new Handler();
        ExecutorService es =  Executors.newSingleThreadExecutor();
        es.execute(()-> {
            Util util = new Util();
            while(true) {
                try {
                    mHandler.post(() -> mText.setText(util.timeStr()));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
    }

    WindowManager.LayoutParams createLayoutParams() {
        WindowManager.LayoutParams layout = new WindowManager.LayoutParams();
        layout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layout.format = PixelFormat.RGBA_8888;
        layout.gravity = Gravity.TOP | Gravity.LEFT;
        layout.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layout.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayout = layout;
        return layout;
    }


    View createView() {
//        LinearLayout linear = new LinearLayout(this);
//        linear.setOrientation(LinearLayout.HORIZONTAL);
//        linear.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));

//        TextView text = new TextView(this);
//        text.setText("我是时钟啊.");
//        text.setBackgroundColor("");


//        linear.addView(text);
//        return linear;
        double statusBarHeight = 0;
        {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);
            } else {
                statusBarHeight = Math.ceil(20 * this.getResources().getDisplayMetrics().density);
            }
        }
        final  double fstatusBarH = statusBarHeight;
        View view = LayoutInflater.from(this).inflate(R.layout.window, null);
        view.setOnTouchListener(new View.OnTouchListener() {
            float mTouchStartX;
            float mTouchStartY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                double x = event.getRawX(),
                        y = event.getRawY() - fstatusBarH;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mWindowLayout.x = (int) (x - mTouchStartX);
                        mWindowLayout.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(mView, mWindowLayout);
                        break;
                    case MotionEvent.ACTION_UP:
                        mWindowLayout.x = (int) (x - mTouchStartX);
                        mWindowLayout.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(mView, mWindowLayout);
                        mTouchStartX = mTouchStartY = 0;
                        break;
                }
                return true;
            }
        });
        TextView text = view.findViewById(R.id.text);
//        text = view.findViewById(R.id.text);
//        text.setText(new Util().timeStr());
        mText = text;
        mView = view;
        return view;
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
