package com.wparja.veterinaryreports.logging.tinyLoggerImpl;


import com.wparja.veterinaryreports.logging.abstracts.AbstractLogger;
import com.wparja.veterinaryreports.utils.FileHelper;

import org.tinylog.Logger;
import org.tinylog.core.TinylogLoggingProvider;
import org.tinylog.writers.Writer;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;

public class TinyLoggerWrapper extends AbstractLogger {

    public TinyLoggerWrapper() throws Exception {
        init();
    }

    @Override
    public void init() throws Exception {
        File folder = FileHelper.getRootFolder();
        System.setProperty("logs.folder", folder.getAbsolutePath());
        setLogLevel();
        setUiListener();
    }

    private void setUiListener() {
        try {
            Field provider = Logger.class.getDeclaredField("provider");
            provider.setAccessible(true);
            CustomTinyLoggingProvider customTinyLoggingProvider = (CustomTinyLoggingProvider) provider.get(null);
            Field realProvider = customTinyLoggingProvider.getClass().getDeclaredField("realProvider");
            realProvider.setAccessible(true);
            TinylogLoggingProvider tinylogLoggingProvider = (TinylogLoggingProvider) realProvider.get(customTinyLoggingProvider);
            Field writersField = tinylogLoggingProvider.getClass().getDeclaredField("writers");
            writersField.setAccessible(true);
            Collection<Writer>[][] writers = (Collection<Writer>[][]) writersField.get(tinylogLoggingProvider);
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 4; j++)
                    for (Writer writer : writers[i][j]) {
                        if (writer instanceof CustomTinyLoggerWriter) {
                            ((CustomTinyLoggerWriter) writer).setOnWrite(new CustomTinyLoggerWriter.OnWrite() {
                                @Override
                                public void onWrite(String message) {
                                    if (mNotifyUI != null) {
                                        mNotifyUI.onNotifyUI(message);
                                    }
                                }
                            });
                        }
                    }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLogLevel() {
        changeLogLevel("trace");
    }

    @Override
    public void logInfo(String message) {
        org.tinylog.Logger.info(message);
    }

    @Override
    public void logTrace(String message) {
        org.tinylog.Logger.trace(message);
    }
    @Override
    public void logDebug(String message) {
        org.tinylog.Logger.debug(message);
    }

    @Override
    public void logWarning(String message) {
        org.tinylog.Logger.warn(message);
    }

    @Override
    public void logError(String message) {
        org.tinylog.Logger.error(message);
    }

    @Override
    public void changeLogLevel(String level)  {
        try {
            Field customTinyLoggingProviderField = Logger.class.getDeclaredField("provider");
            customTinyLoggingProviderField.setAccessible(true);
            CustomTinyLoggingProvider customTinyLoggingProvider = (CustomTinyLoggingProvider) customTinyLoggingProviderField.get(null);
            customTinyLoggingProvider.changeLogLevel(level);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
