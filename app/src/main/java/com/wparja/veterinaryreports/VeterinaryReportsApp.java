package com.wparja.veterinaryreports;

import android.app.Application;

import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.logging.LoggerHelper;
import com.wparja.veterinaryreports.utils.FileHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VeterinaryReportsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new VeterinaryReportsExceptionHandler());
        DataProvider.getInstance().init(getApplicationContext());
    }

    private static class VeterinaryReportsExceptionHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler defaultUEH;
        private File folder;

        public VeterinaryReportsExceptionHandler() {
            try {
                this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
                folder = FileHelper.geLogsFolder("Temp");
                if (!folder.exists()) {
                    folder.mkdir();
                }

                if (folder.listFiles() != null) {
                    List<File> files = Arrays.asList(folder.listFiles());
                    Collections.sort(files, (o1, o2) -> ((int) o1.lastModified()) - ((int) o2.lastModified()));
                    int index = 0;
                    int size = files.size();
                    while (size > 3) {
                        files.get(index).delete();
                        index++;
                        size--;
                    }
                }
            } catch (Exception ex) {
                LoggerHelper.getInstance().logError(ex.getMessage());
            }
        }

        public void uncaughtException(Thread t, Throwable e) {
            StackTraceElement[] arr = e.getStackTrace();
            StringBuilder report = new StringBuilder(e.toString() + "\n\n");
            report.append("--------- Stack trace ---------\n\n");
            for (StackTraceElement stackTraceElement : arr) {
                report.append("    ").append(stackTraceElement.toString()).append("\n");
            }
            report.append("-------------------------------\n\n");

            // If the exception was thrown in a background thread inside
            // AsyncTask, then the actual exception can be found with getCause

            report.append("--------- Cause ---------\n\n");
            Throwable cause = e.getCause();
            if (cause != null) {
                report.append(cause.toString()).append("\n\n");
                arr = cause.getStackTrace();
                for (StackTraceElement stackTraceElement : arr) {
                    report.append("    ").append(stackTraceElement.toString()).append("\n");
                }
            }
            report.append("-------------------------------\n\n");

            try {
                File trace = new File(folder, "FatalError.txt");
                FileWriter writer = new FileWriter(trace);
                writer.append(report.toString());
                writer.flush();
                writer.close();
            } catch (IOException ioe) {
                // ...
            }

            defaultUEH.uncaughtException(t, e);
        }
    }
}
