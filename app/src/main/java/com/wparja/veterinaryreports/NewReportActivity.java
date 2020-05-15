package com.wparja.veterinaryreports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.persistence.entities.Specie;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class NewReportActivity extends AppCompatActivity {

    Toolbar mToolbar;

    AutoCompleteTextView mGenders;
    AutoCompleteTextView mSpeciesActv;
    AutoCompleteTextView mBreeds;

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
        DataProvider.getInstance().loadData();
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new String[] {getString(R.string.male), getString(R.string.female)});
        mGenders.setAdapter(genderAdapter);

        mSpecies = DataProvider.getInstance().getSpecies();
        List<String> specieNames = mSpecies.stream().map( x -> x.getName()).collect(Collectors.toList());
        ArrayAdapter<String> speciesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, specieNames);
        mSpeciesActv.setAdapter(speciesAdapter);

        mSpeciesActv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Optional<Specie> specie = DataProvider.getInstance().getSpecies().stream().filter(x -> x.getName().equals(s)).findFirst();
                if (specie.isPresent()) {
                    ArrayAdapter<String> breedsAdapter = new ArrayAdapter<>(NewReportActivity.this, android.R.layout.simple_dropdown_item_1line, specie.get().getBreeds());
                    mBreeds.setAdapter(breedsAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
