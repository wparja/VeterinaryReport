package com.wparja.veterinaryreports.logging.tinyLoggerImpl;
import org.tinylog.Level;
import org.tinylog.core.TinylogLoggingProvider;
import org.tinylog.format.MessageFormatter;
import org.tinylog.provider.ContextProvider;
import org.tinylog.provider.LoggingProvider;

public class CustomTinyLoggingProvider implements LoggingProvider {

    TinylogLoggingProvider realProvider = new TinylogLoggingProvider();
    private Level logLevel = Level.TRACE;

    public void changeLogLevel(String level)  {
        switch (level) {
            case "trace" : logLevel = Level.TRACE; break;
            case "debug" : logLevel = Level.DEBUG; break;
            case "info" : logLevel = Level.INFO; break;
            case "warning" : logLevel = Level.WARN; break;
            case "error" : logLevel = Level.ERROR; break;
        }
    }

    @Override
    public ContextProvider getContextProvider() {
        return realProvider.getContextProvider();
    }

    @Override
    public Level getMinimumLevel() {
        return Level.TRACE;
    }

    @Override
    public Level getMinimumLevel(String tag) {
        return Level.TRACE;
    }

    @Override
    public boolean isEnabled(int depth, String tag, Level level) {
        return realProvider.isEnabled(depth + 1, tag, level);
    }

    @Override
    public void log(int depth, String tag, Level level, Throwable exception, MessageFormatter formatter, Object obj, Object... arguments) {
        if (level.ordinal() >= logLevel.ordinal()) {
            realProvider.log(depth + 1, tag, level, exception, null, obj, arguments);
        }
    }

    @Override
    public void log(String loggerClassName, String tag, Level level, Throwable exception, MessageFormatter formatter, Object obj, Object... arguments) {
        if (level.ordinal() >= logLevel.ordinal()) {
            realProvider.log(loggerClassName, tag, level, exception, null, obj, arguments);
        }
    }

    @Override
    public void shutdown() throws InterruptedException {
        realProvider.shutdown();
    }
}
