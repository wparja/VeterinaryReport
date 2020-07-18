package com.wparja.veterinaryreports;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.fragments.PatientDataFragment;
import com.wparja.veterinaryreports.fragments.PhotoGalleryFragment;
import com.wparja.veterinaryreports.fragments.ProcedureFragment;
import com.wparja.veterinaryreports.logging.LoggerHelper;
import com.wparja.veterinaryreports.utils.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewReportActivity extends AppCompatActivity {

    private static final int REQUEST_PHOTO = 2;

    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    BoomMenuButton bmb;

    private PatientDataFragment mPatientDataFragment;
    private ProcedureFragment mProcedureFragment;
    private PhotoGalleryFragment mPhotoGalleryFragment;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        mPatientDataFragment = PatientDataFragment.newInstance();
        mFragments.add(mPatientDataFragment);

        mProcedureFragment = ProcedureFragment.newInstance();
        mFragments.add(mProcedureFragment);

        mPhotoGalleryFragment = PhotoGalleryFragment.newInstance("");
        mFragments.add(mPhotoGalleryFragment);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_report);

        viewPager = findViewById(R.id.new_procedure_view_pager);
        tabLayout = findViewById(R.id.new_procedure_tab_layout);
        FragmentPagerAdapter fragmentPagerAdapter = new NewProcedurePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        bmb = findViewById(R.id.bmb);
        HamButton.Builder builderSave = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_baseline_save_white_24)
                .normalTextRes(R.string.save)
                .subNormalTextRes(R.string.save_all_data);
        bmb.addBuilder(builderSave);

        HamButton.Builder builderShare = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_baseline_share_white_24)
                .normalTextRes(R.string.shared)
                .subNormalTextRes(R.string.shared_this_procedure);
        bmb.addBuilder(builderShare);

        HamButton.Builder builderPhoto = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_add_a_photo_white_24dp)
                .normalTextRes(R.string.photo)
                .subNormalTextRes(R.string.attach_new_photo)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        try {
                            takePhoto();
                        } catch (Exception e) {
                            LoggerHelper.getInstance().logError("Error taken photo" + e.getMessage());
                        }
                    }
                });
        bmb.addBuilder(builderPhoto);

}

    private void takePhoto() throws Exception {
        String fileName = "temp_" + System.currentTimeMillis() + ".jpg";
        File photoFile = new File(FileHelper.gePhotoFolder("Temp"),fileName);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTackPhoto = captureImage.resolveActivity(getPackageManager()) != null;

        if (canTackPhoto) {
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".fileprovider", photoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        startActivityForResult(captureImage, REQUEST_PHOTO);
    }

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
                case 2:
                    return getString(R.string.gallery);
                default:
                    return null;
            }
        }
    }
}
