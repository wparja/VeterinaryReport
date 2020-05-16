package com.wparja.veterinaryreports;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.wparja.veterinaryreports.data.DataProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VeterinaryReports extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
      //  Thread.setDefaultUncaughtExceptionHandler(new VeterinaryReportsExceptionHandler(getApplicationContext()));
        DataProvider.getInstance().init(getApplicationContext());
    }

    private class VeterinaryReportsExceptionHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler defaultUEH;
        private Context app = null;
        private File folder;

        public VeterinaryReportsExceptionHandler(Context applicationContext) {
            this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
            this.app = app;
            folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/VeterinaryReports/");
            if (!folder.exists()) {
                folder.mkdir();
            }
            try {
                List<File> files = Arrays.asList(folder.listFiles());
                Collections.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return ((int)o1.lastModified()) - ((int)o2.lastModified());
                    }
                });
                int index = 0;
                int size = files.size();
                while (size > 3) {
                    files.get(index).delete();
                    index++;
                    size--;
                }

            } catch (Exception ex) {
                Log.i("VeterinaryReports", "VeterinaryReportsExceptionHandler: " + ex.getMessage());
            }
        }
        public void uncaughtException(Thread t, Throwable e) {
            StackTraceElement[] arr = e.getStackTrace();
            String report = e.toString() + "\n\n";
            report += "--------- Stack trace ---------\n\n";
            for (int i = 0; i < arr.length; i++) {
                report += "    " + arr[i].toString() + "\n";
            }
            report += "-------------------------------\n\n";

            // If the exception was thrown in a background thread inside
            // AsyncTask, then the actual exception can be found with getCause

            report += "--------- Cause ---------\n\n";
            Throwable cause = e.getCause();
            if (cause != null) {
                report += cause.toString() + "\n\n";
                arr = cause.getStackTrace();
                for (int i = 0; i < arr.length; i++) {
                    report += "    " + arr[i].toString() + "\n";
                }
            }
            report += "-------------------------------\n\n";

            try {
                File strace = new File(folder, "VeterinaryReportsError.txt");
                FileWriter writer = new FileWriter(strace);
                writer.append(report);
                writer.flush();
                writer.close();
            } catch (IOException ioe) {
                // ...
            }

            defaultUEH.uncaughtException(t, e);
        }
    }
}
