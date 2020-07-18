package com.wparja.veterinaryreports.logging;



import com.wparja.veterinaryreports.logging.abstracts.AbstractLogger;
import com.wparja.veterinaryreports.logging.interfaces.INotifyUI;
import com.wparja.veterinaryreports.logging.tinyLoggerImpl.TinyLoggerWrapper;

public class LoggerHelper extends AbstractLogger {

    AbstractLogger mLogging;

    private static LoggerHelper mInstance;

    static {
        try {
            mInstance = new LoggerHelper();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LoggerHelper getInstance() {return mInstance;}

    private LoggerHelper() throws Exception {
        init();
    }

    public void logTrace(String message) {
        mLogging.logTrace(message);
    }

    public void logDebug(String message) {
        mLogging.logDebug(message);
    }

    public void logInfo(String message) {
        mLogging.logInfo(message);
    }

    public void logWarning(String message) {
        mLogging.logWarning(message);
    }

    public void logError(String message) {
        mLogging.logError(message);
    }

    public void changeLogLevel(String level) {
        mLogging.changeLogLevel(level);
    }

    @Override
    protected void init() throws Exception {
        mLogging = new TinyLoggerWrapper();
    }

    @Override
    public void setNotifyUI(INotifyUI notifyUI) {
        mLogging.setNotifyUI(notifyUI);
    }
}
