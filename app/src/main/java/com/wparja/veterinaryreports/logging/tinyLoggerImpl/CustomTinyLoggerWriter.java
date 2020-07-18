package com.wparja.veterinaryreports.logging.tinyLoggerImpl;

import org.tinylog.core.LogEntry;
import org.tinylog.core.LogEntryValue;
import org.tinylog.writers.Writer;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

public class CustomTinyLoggerWriter implements Writer {

    private OnWrite mOnWrite;

    public CustomTinyLoggerWriter(Map<String, String> properties) {
    }

    @Override
    public Collection<LogEntryValue> getRequiredLogEntryValues() {
        return EnumSet.of(LogEntryValue.LEVEL, LogEntryValue.MESSAGE);
    }

    @Override
    public void write(LogEntry logEntry) throws Exception {
        if (mOnWrite != null) {
            mOnWrite.onWrite(logEntry.getLevel() + "::" + logEntry.getMessage());
        }
    }

    @Override
    public void flush() throws Exception {
        System.out.flush();
    }

    @Override
    public void close() throws Exception {
    }

    public void setOnWrite(OnWrite onWrite) {
        mOnWrite = onWrite;
    }

    public interface OnWrite {
        void onWrite(String message);
    }
}
