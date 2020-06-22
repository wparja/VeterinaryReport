package com.wparja.veterinaryreports;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.fragments.PatientDataFragment;
import com.wparja.veterinaryreports.fragments.ProcedureFragment;
import java.util.ArrayList;
import java.util.List;

public class NewReportActivity extends AppCompatActivity {

   private Toolbar mToolbar;
   private ViewPager viewPager;
   private TabLayout tabLayout;

   private PatientDataFragment mPatientDataFragment;
   private ProcedureFragment mProcedureFragment;

   private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        mPatientDataFragment = PatientDataFragment.newInstance();
        mFragments.add(mPatientDataFragment);

        mProcedureFragment = ProcedureFragment.newInstance();
        mFragments.add(mProcedureFragment);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_report);

        viewPager = findViewById(R.id.new_procedure_view_pager);
        tabLayout = findViewById(R.id.new_procedure_tab_layout);
        FragmentPagerAdapter fragmentPagerAdapter = new NewProcedurePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
}

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_PHOTO) {
//            updatePatientPhoto();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_report_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }  else if (item.getItemId() == R.id.action_export) {
            shared();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shared() {
        Intent i = new Intent(this, SharedPdfActivity.class);
        startActivity(i);
    }



    public void save(View view) {
        String specieName = mPatientDataFragment.getSpecieSelected();
        String breedName = mPatientDataFragment.getBreedSelected();
        DataProvider.getInstance().saveSpecie(specieName, breedName);
        DataProvider.getInstance().saveExams(mProcedureFragment.getExamsSelected());
        DataProvider.getInstance().saveDiagnostic(mProcedureFragment.getDiagnosticsSelected());
    }

    class NewProcedurePagerAdapter extends FragmentPagerAdapter {

        public NewProcedurePagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.Patient_info);
                case 1:
                    return getString(R.string.procedure);
                default:
                    return null;
            }
        }
    }
}
