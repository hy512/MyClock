package com.example.silence.myclock;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.WindowManager;

import com.example.silence.myclock.entity.WindowLabel;

import java.io.FileDescriptor;
import java.util.Map;

public class LabIBinder implements IBinder {
    WindowManager manager;
    Map<String, WindowLabel> labels;
    boolean gone = false;

    public LabIBinder() {}
    public LabIBinder(WindowManager manager) {
        this.manager = manager;
    }
    public LabIBinder(WindowManager manager, Map<String, WindowLabel> labels) {
        this.manager = manager;
        this.labels = labels;
    }


    public void put (String identity, WindowLabel label) {
        label.create(manager);
        labels.put(identity, label);
    }

    public void setGone(boolean gone) {
        this.gone = gone;
    }

    @androidx.annotation.Nullable
    @Override
    public String getInterfaceDescriptor() throws RemoteException {
        return "com.example.silence.myclock.LabIBinder";
    }

    @Override
    public boolean pingBinder() {
        return !gone;
    }

    @Override
    public boolean isBinderAlive() {
        return !gone;
    }

    @androidx.annotation.Nullable
    @Override
    public IInterface queryLocalInterface(@androidx.annotation.NonNull String descriptor) {
        return null;
    }

    @Override
    public void dump(@androidx.annotation.NonNull FileDescriptor fd, @androidx.annotation.Nullable String[] args) throws RemoteException {

    }

    @Override
    public void dumpAsync(@androidx.annotation.NonNull FileDescriptor fd, @androidx.annotation.Nullable String[] args) throws RemoteException {

    }

    @Override
    public boolean transact(int code, @androidx.annotation.NonNull Parcel data, @androidx.annotation.Nullable Parcel reply, int flags) throws RemoteException {
        return false;
    }

    @Override
    public void linkToDeath(@androidx.annotation.NonNull IBinder.DeathRecipient recipient, int flags) throws RemoteException {

    }

    @Override
    public boolean unlinkToDeath(@androidx.annotation.NonNull IBinder.DeathRecipient recipient, int flags) {
        return false;
    }
}
