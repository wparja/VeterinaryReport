package com.wparja.veterinaryreports.logging.interfaces;

public interface ILogging {
    void logTrace(String message);
    void logDebug(String message);
    void logInfo(String message);
    void logWarning(String message);
    void logError(String message);
    void changeLogLevel(String level);
}
