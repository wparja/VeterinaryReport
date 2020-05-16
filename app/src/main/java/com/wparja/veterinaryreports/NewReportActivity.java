package com.wparja.veterinaryreports;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.persistence.entities.Specie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NewReportActivity extends AppCompatActivity {

    Toolbar mToolbar;

    AutoCompleteTextView mGenders;
    AutoCompleteTextView mSpeciesActv;
    AutoCompleteTextView mBreeds;

    EditText mEditText;
    List<Specie> mSpecies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        mGenders = findViewById(R.id.gender_actv);
        mSpeciesActv = findViewById(R.id.specie_actv);
        mBreeds = findViewById(R.id.breed_actv);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.new_report);


        mEditText = findViewById(R.id.previous_exams);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> data = new ArrayList<>();
                data.add("Teste");
                data.add("Teste 2");

                List<String> data1 = new ArrayList<>();
                data1.add("Teste");
                createChoiceDialog(mEditText, data, data1);
            }
        });


//        DataProvider.getInstance().loadData();
//        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new String[] {getString(R.string.male), getString(R.string.female)});
//        mGenders.setAdapter(genderAdapter);
//
//        mSpecies = DataProvider.getInstance().getSpecies();
//        List<String> specieNames = mSpecies.stream().map( x -> x.getName()).collect(Collectors.toList());
//        ArrayAdapter<String> speciesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, specieNames);
//        mSpeciesActv.setAdapter(speciesAdapter);
//
//        mSpeciesActv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Optional<Specie> specie = DataProvider.getInstance().getSpecies().stream().filter(x -> x.getName().equals(s)).findFirst();
//                if (specie.isPresent()) {
//                    ArrayAdapter<String> breedsAdapter = new ArrayAdapter<>(NewReportActivity.this, android.R.layout.simple_dropdown_item_1line, specie.get().getBreeds());
//                    mBreeds.setAdapter(breedsAdapter);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

    }

    private void createChoiceDialog(EditText editText, List<String> data, List<String> data1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewReportActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(NewReportActivity.this);
        View view = inflater.inflate(R.layout.dialog_choice_data, null, false);
        EditText newExam = view.findViewById(R.id.new_exam);
        builder.setView(view);
        builder.setTitle(getString(R.string.previous_exams));

        boolean[] checkedItems = new boolean[data.size()];
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data1.size(); j++) {
                if (data.get(i).equals(data1.get(j))) {
                    checkedItems[i] = true;
                    break;
                }
            }
        }
        builder.setMultiChoiceItems(data.toArray(new CharSequence[data.size()]), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    data1.add(data.get(which));
                } else {
                    data1.remove(data.get(which));
                }
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!TextUtils.isEmpty(newExam.getText()) && !data1.contains(newExam.getText().toString())) {
                    data1.add(newExam.getText().toString());
                }

                editText.setText(TextUtils.join(", ", data1));
                dialog.dismiss();
            }
        });


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
        String breedName = mBreeds.getText().toString();
        DataProvider.getInstance().UpdateSpecie(specieName, breedName);
    }
}
