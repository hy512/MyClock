package com.example.silence.myclock.entity;

import android.view.View;
import android.view.WindowManager;

import java.io.Serializable;


public class WindowLabel implements Serializable {
    private static final long serialVersionUID = 1L;

    // 容器 view
    View containerView;
    // 是否需要更新
    boolean requireUpdate;
    // 更新频率 毫秒
    long updateDelay;

    WindowManager.LayoutParams layout;

    // 创建
    Consumer<WindowManager> create;

    // 更新
    BiConsumer<WindowManager, WindowManager.LayoutParams> update;
    public WindowLabel(){}
    public WindowLabel(View containerView) {
        this.containerView = containerView;
    }
    public WindowLabel(View containerView, Consumer<WindowManager> create) {
        this.containerView = containerView;
        this.create = create;
    }


    public View getContainerView() {
        return containerView;
    }


    public void create(WindowManager manager) {
        create.accept(manager);
    }

    public void update(WindowManager manager, WindowManager.LayoutParams layout) {
        update.accept(manager, layout);
    }

    public void setContainerView(View containerView) {
        this.containerView = containerView;
    }

    public void setCreate(Consumer<WindowManager> create) {
        this.create = create;
    }
    public void setUpdate(long updateDelay, BiConsumer<WindowManager, WindowManager.LayoutParams> update) {
        this.updateDelay = updateDelay;
        this.update = update;
    }

    public void setLayoutParams(WindowManager.LayoutParams layout) {
        this.layout = layout;
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return layout;
    }

    public long getUpdateDelay() {
        return updateDelay;
    }

    public static interface Consumer<T> extends Serializable{
         static final long serialVersionUID = WindowLabel.serialVersionUID;
        public void accept(T t);
    }

    public static interface BiConsumer<T, R> extends Serializable{
        static final long serialVersionUID = WindowLabel.serialVersionUID;
        public void accept(T t, R r);
    }
}
