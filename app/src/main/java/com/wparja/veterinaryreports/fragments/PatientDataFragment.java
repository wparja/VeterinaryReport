package com.wparja.veterinaryreports.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.wparja.veterinaryreports.BuildConfig;
import com.wparja.veterinaryreports.R;
import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.persistence.entities.Specie;
import com.wparja.veterinaryreports.utils.PhotoUtils;
import java.io.File;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDataFragment extends Fragment {



    List<Specie> mSpecies;
    Spinner mGendersSpinner;
    AutoCompleteTextView mSpeciesActv;
    AutoCompleteTextView mBreedsActv;
    ImageButton mNewPhotoImgButton;
    ImageView mPatientMainPhoto;

    public PatientDataFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PatientDataFragment newInstance() {
        return new PatientDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_data, container, false);
        mGendersSpinner = view.findViewById(R.id.gender_spinner);
        mSpeciesActv = view.findViewById(R.id.specie_actv);
        mBreedsActv = view.findViewById(R.id.breed_actv);
        mNewPhotoImgButton = view.findViewById(R.id.new_photo);
        mPatientMainPhoto = view.findViewById(R.id.patient_photo);

        DataProvider.getInstance().loadData();
        fillGenderSpinner();

        fillAndSetListenerSpecieAutoCompleteTextView();


        return view;
    }

    private void fillAndSetListenerSpecieAutoCompleteTextView() {
        mSpecies = DataProvider.getInstance().getSpecies();
        List<String> specieNames = mSpecies.stream().map(x -> x.getName()).collect(Collectors.toList());
        ArrayAdapter<String> speciesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, specieNames);
        mSpeciesActv.setAdapter(speciesAdapter);

        mSpeciesActv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Optional<Specie> specie = mSpecies.stream().filter(x -> x.getName().equals(s.toString())).findFirst();
                if (specie.isPresent()) {
                    ArrayAdapter<String> breedsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, specie.get().getItems());
                    mBreedsActv.setAdapter(breedsAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void fillGenderSpinner() {
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new String[] {getString(R.string.male), getString(R.string.female)});
        mGendersSpinner.setAdapter(genderAdapter);
        mGendersSpinner.setSelection(0);
    }


    public String getSpecieSelected() {
        return mSpeciesActv.getText().toString();
    }

    public String getBreedSelected() {
        return mBreedsActv.getText().toString();
    }
}