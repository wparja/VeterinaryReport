package com.wparja.veterinaryreports;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wparja.veterinaryreports.customcomponents.PdfExportLayout;
import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.persistence.entities.ReportEntity;
import com.wparja.veterinaryreports.persistence.entities.Specie;
import com.wparja.veterinaryreports.utils.PhotoUtils;
import com.wparja.veterinaryreports.utils.PictureUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class NewReportActivity extends AppCompatActivity {

    private static final int REQUEST_PHOTO= 2;

    Toolbar mToolbar;

    Spinner mGendersSpinner;
    AutoCompleteTextView mSpeciesActv;
    AutoCompleteTextView mBreedsActv;

    TextView mExams;
    TextView mDiagnostics;

    EditText mProcedurePerformed;
    ImageButton mProcedurePerformedImgBtn;

    TextView mRecommendations;
    TextView mHistory;

    List<Specie> mSpecies;

    List<String> mExamsSelected = new ArrayList<>();
    List<String> mDiagnosticsSelected = new ArrayList<>();

    ImageButton mNewPhotoImgButton;
    File mPatientMainPhotoFile;
    ImageView mPatientMainPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        mGendersSpinner = findViewById(R.id.gender_spinner);
        mSpeciesActv = findViewById(R.id.specie_actv);
        mBreedsActv = findViewById(R.id.breed_actv);
        mExams = findViewById(R.id.previous_exams);
        mDiagnostics = findViewById(R.id.diagnostics);
        mProcedurePerformed = findViewById(R.id.procedure_performed);
        mProcedurePerformedImgBtn = findViewById(R.id.procedure_performed_img_btn);
        mRecommendations = findViewById(R.id.recommendations);
        mHistory = findViewById(R.id.history);
        mNewPhotoImgButton = findViewById(R.id.new_photo);
        mPatientMainPhoto = findViewById(R.id.patient_photo);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_report);

        DataProvider.getInstance().loadData();
        fillGenderSpinner();

        fillAndSetListenerSpecieAutoCompleteTextView();

        mExams.setOnClickListener(v ->  createChoiceDialog(getString(R.string.previous_exams), getString(R.string.new_exam), mExams, DataProvider.getInstance().getExams().getItems(), mExamsSelected));
        mDiagnostics.setOnClickListener(v -> createChoiceDialog(getString(R.string.diagnostics), getString(R.string.new_diagnostic), mDiagnostics, DataProvider.getInstance().getDiagnostics().getItems(), mDiagnosticsSelected));

        mProcedurePerformedImgBtn.setOnClickListener(v -> createEditTextDialog(getString(R.string.procedure_performed), mProcedurePerformed));
        mRecommendations.setOnClickListener(v -> createEditTextDialog(getString(R.string.recommendations), mRecommendations));
        mHistory.setOnClickListener(v -> createEditTextDialog(getString(R.string.history), mHistory));

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mPatientMainPhotoFile = PhotoUtils.getPhoto(getApplicationContext(), "mainPhoto1.jpg");
        boolean canTackPhoto = mPatientMainPhotoFile != null && captureImage.resolveActivity(getPackageManager()) != null;

        if (canTackPhoto) {
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".fileprovider", mPatientMainPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mNewPhotoImgButton.setOnClickListener(v -> startActivityForResult(captureImage, REQUEST_PHOTO));
     //   updatePatientPhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO) {
            updatePatientPhoto();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_report_toolbar_menu, menu);
        return true;
    }

    private void shared() {
        Intent i = new Intent(this, SharedPdfActivity.class);
        startActivity(i);
    }

    private void updatePatientPhoto() {
        if (mPatientMainPhotoFile == null || !mPatientMainPhotoFile.exists()) {
            mPatientMainPhoto.setImageDrawable(null);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(mPatientMainPhotoFile.getPath());
            mPatientMainPhoto.setImageBitmap(bitmap);
            mPatientMainPhoto.setRotation(90);
            // Create a shiny new (but blank) PDF document in memory
            // We want it to optionally be printable, so add PrintAttributes
            // and use a PrintedPdfDocument. Simpler: new PdfDocument().
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            PrintAttributes printAttrs = new PrintAttributes.Builder().
                    setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                    setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                    setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, width, height)).
                    setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                    build();
            PdfDocument document = new PrintedPdfDocument(this, printAttrs);
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
            // create a new page from the PageInfo
            PdfDocument.Page page = document.startPage(pageInfo);

            RelativeLayout r = findViewById(R.id.relative);
            // repaint the user's text into the page
            r.draw(page.getCanvas());
            // do final processing of the page
            document.finishPage(page);
            // Here you could add more pages in a longer doc app, but you'd have
            // to handle page-breaking yourself in e.g., write your own word processor...
            // Now write the PDF document to a file; it actually needs to be a file
            // since the Share mechanism can't accept a byte[]. though it can
            // accept a String/CharSequence. Meh.
            try {
                File pdfDirPath = new File(getFilesDir(), "pdfs");
                pdfDirPath.mkdirs();
                File file = new File(pdfDirPath, "pdfsend.pdf");
                Uri contentUri = FileProvider.getUriForFile(this, "com.wparja.veterinaryreports.fileprovider", file);
                FileOutputStream os = new FileOutputStream(file);
                document.writeTo(os);
                document.close();
                os.close();
            } catch (IOException e) {
                throw new RuntimeException("Error generating file", e);
            }
        }
    }

    private void fillAndSetListenerSpecieAutoCompleteTextView() {
        mSpecies = DataProvider.getInstance().getSpecies();
        List<String> specieNames = mSpecies.stream().map(x -> x.getName()).collect(Collectors.toList());
        ArrayAdapter<String> speciesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, specieNames);
        mSpeciesActv.setAdapter(speciesAdapter);

        mSpeciesActv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Optional<Specie> specie = mSpecies.stream().filter(x -> x.getName().equals(s.toString())).findFirst();
                if (specie.isPresent()) {
                    ArrayAdapter<String> breedsAdapter = new ArrayAdapter<>(NewReportActivity.this, android.R.layout.simple_dropdown_item_1line, specie.get().getItems());
                    mBreedsActv.setAdapter(breedsAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void fillGenderSpinner() {
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new String[] {getString(R.string.male), getString(R.string.female)});
        mGendersSpinner.setAdapter(genderAdapter);
        mGendersSpinner.setSelection(0);
    }

    private void createChoiceDialog(String title, String label, TextView editText, List<String> allData, List<String> allDataSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewReportActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(NewReportActivity.this);
        View view = inflater.inflate(R.layout.dialog_choice_data, null, false);
        TextView newDataLabel = view.findViewById(R.id.new_data_text_view);
        EditText newDataInput = view.findViewById(R.id.new_data);

        builder.setView(view);
        builder.setTitle(title);
        newDataLabel.setText(label);

        boolean[] checkedItems = new boolean[allData.size()];
        for (int i = 0; i < allData.size(); i++) {
            for (int j = 0; j < allDataSelected.size(); j++) {
                if (allData.get(i).equals(allDataSelected.get(j))) {
                    checkedItems[i] = true;
                    break;
                }
            }
        }
        builder.setMultiChoiceItems(allData.toArray(new CharSequence[allData.size()]), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    allDataSelected.add(allData.get(which));
                } else {
                    allDataSelected.remove(allData.get(which));
                }
            }
        });

        builder.setPositiveButton(R.string.ok, (dialog, which) -> {

            if (!TextUtils.isEmpty(newDataInput.getText()) && !allDataSelected.contains(newDataInput.getText().toString())) {
                allDataSelected.add(newDataInput.getText().toString());
            }

            editText.setText(TextUtils.join(", ", allDataSelected));
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
           dialog.dismiss();
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void createEditTextDialog(String title, TextView text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewReportActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(NewReportActivity.this);
        View view = inflater.inflate(R.layout.dialog_edit_data, null, false);
        EditText data = view. findViewById(R.id.edit_text_data);

        builder.setView(view);
        builder.setTitle(title);
        data.setText(text.getText());

        builder.setPositiveButton(R.string.ok, (dialog, which) -> text.setText(data.getText()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

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

    public void save(View view) {
        String specieName = mSpeciesActv.getText().toString();
        String breedName = mBreedsActv.getText().toString();
        DataProvider.getInstance().saveSpecie(specieName, breedName);
        DataProvider.getInstance().saveExams(mExamsSelected);
        DataProvider.getInstance().saveDiagnostic(mDiagnosticsSelected);
    }
}
