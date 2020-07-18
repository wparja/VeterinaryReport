package com.wparja.veterinaryreports.logging.abstracts;


import com.wparja.veterinaryreports.logging.interfaces.ILogging;
import com.wparja.veterinaryreports.logging.interfaces.INotifyUI;

public abstract class AbstractLogger implements ILogging {

    protected abstract void init() throws Exception;
    protected INotifyUI mNotifyUI;

    public void setNotifyUI(INotifyUI notifyUI) {
        mNotifyUI = notifyUI;
    }
}
