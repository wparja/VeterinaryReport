package com.wparja.veterinaryreports;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.persistence.entities.Specie;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class NewReportActivity extends AppCompatActivity {

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
