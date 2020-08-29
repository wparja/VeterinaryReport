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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.fragments.PatientDataFragment;
import com.wparja.veterinaryreports.fragments.PhotoGalleryFragment;
import com.wparja.veterinaryreports.fragments.ProcedureFragment;
import com.wparja.veterinaryreports.logging.LoggerHelper;
import com.wparja.veterinaryreports.persistence.entities.Report;
import com.wparja.veterinaryreports.utils.FileHelper;
import com.wparja.veterinaryreports.utils.SequenceGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewProcedureActivity extends AppCompatActivity {

    private static final String ARG = "ARG";
    private static final int REQUEST_PHOTO = 2;

    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private BoomMenuButton bmb;

    private PatientDataFragment mPatientDataFragment;
    private ProcedureFragment mProcedureFragment;
    private PhotoGalleryFragment mPhotoGalleryFragment;
    private Report mPatient;

    private List<Fragment> mFragments = new ArrayList<>();

    public static Intent newInstance(Context context, Report report) {
        Intent intent = new Intent(context, NewProcedureActivity.class);
        intent.putExtra(ARG, report);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_procedure);

        if (getIntent() != null) {
            mPatient = (Report) getIntent().getSerializableExtra(ARG);
            if (mPatient == null) {
                mPatient = new Report(String.valueOf(SequenceGenerator.getInstance().nextId()));
            }
        }

        mPatientDataFragment = PatientDataFragment.newInstance(mPatient);
        mFragments.add(mPatientDataFragment);

        mProcedureFragment = ProcedureFragment.newInstance(mPatient);
        mFragments.add(mProcedureFragment);

        mPhotoGalleryFragment = PhotoGalleryFragment.newInstance(mPatient.getFolderName());
        mFragments.add(mPhotoGalleryFragment);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_report);

        viewPager = findViewById(R.id.new_procedure_view_pager);
        tabLayout = findViewById(R.id.new_procedure_tab_layout);
        FragmentPagerAdapter fragmentPagerAdapter = new NewProcedurePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        bmb = findViewById(R.id.bmb);
        HamButton.Builder builderSave = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_baseline_save_white_24)
                .normalTextRes(R.string.save)
                .listener(index -> {
                   save();
                })
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
                .listener(index -> {
                    try {
                        takePhoto();
                    } catch (Exception e) {
                        LoggerHelper.getInstance().logError("Error taken photo" + e.getMessage());
                    }
                });
        bmb.addBuilder(builderPhoto);

}

    private void takePhoto() throws Exception {
        String fileName = System.currentTimeMillis() + FileHelper.PHOTO_EXTENSION;
        File photoFile = new File(FileHelper.gePhotoFolder(mPatient.getFolderName()), fileName);
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


    public void save() {
        DataProvider.getInstance().savePatient(mPatient);
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
