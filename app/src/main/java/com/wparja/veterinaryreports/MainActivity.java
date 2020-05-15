package com.wparja.veterinaryreports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(R.string.app_name);

    }

    public void callActivity(View view) {

        Intent activity;
        if (view.getId() == R.id.new_report_button) {
            activity = new Intent(this, NewReportActivity.class);
        } else {
            activity = new Intent(this, ReportsActivity.class);
        }

        startActivity(activity);
    }
}
