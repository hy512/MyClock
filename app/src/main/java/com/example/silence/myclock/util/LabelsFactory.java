package com.example.silence.myclock.util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.silence.myclock.R;
import com.example.silence.myclock.entity.WindowLabel;

public class LabelsFactory {
    private static LabelsFactory factory;
    Context context;
    double statusBarHeight;

    private LabelsFactory(Context context) {
        this.context = context;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight = Math.ceil(20 * context.getResources().getDisplayMetrics().density);
        }
    }

    public static LabelsFactory init(Context context) {
        if (factory == null)
            factory = new LabelsFactory(context);
        else {
            factory.context = context;
        }
        return factory;
    }

    public WindowLabel dateTime() {
        WindowManager.LayoutParams layout = new WindowManager.LayoutParams();
        layout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layout.format = PixelFormat.RGBA_8888;
        layout.gravity = Gravity.TOP | Gravity.LEFT;
        layout.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layout.height = WindowManager.LayoutParams.WRAP_CONTENT;

        WindowLabel label = new WindowLabel();
        label.setLayoutParams(layout);
        label.setCreate((WindowManager manager) -> {
            View view = LayoutInflater.from(context).inflate(R.layout.window, null);
            TextView text = view.findViewById(R.id.text);
            text.setText(new Util().timeStr());
            touchable(label, manager);
        });
        label.setUpdate(1000, (WindowManager window, WindowManager.LayoutParams playout) -> {
            View view = label.getContainerView();
            TextView text = view.findViewById(R.id.text);
            text.setText(new Util().timeStr());
        });
        return label;
    }

    public void touchable(WindowLabel label, WindowManager windowManager) {
        label.getContainerView().setOnTouchListener(new View.OnTouchListener() {
            float mTouchStartX;
            float mTouchStartY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                double x = event.getRawX(),
                        y = event.getRawY() - statusBarHeight;
                WindowManager.LayoutParams layout = label.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        layout.x = (int) (x - mTouchStartX);
                        layout.y = (int) (y - mTouchStartY);
//                        label.update(windowManager, layout);
                        windowManager.updateViewLayout(label.getContainerView(), layout);
                        break;
                    case MotionEvent.ACTION_UP:
                        layout.x = (int) (x - mTouchStartX);
                        layout.y = (int) (y - mTouchStartY);
                        windowManager.updateViewLayout(label.getContainerView(), layout);
//                        label.update(windowManager, layout);
                        mTouchStartX = mTouchStartY = 0;
                        break;
                }
                return true;
            }
        });
    }

}
