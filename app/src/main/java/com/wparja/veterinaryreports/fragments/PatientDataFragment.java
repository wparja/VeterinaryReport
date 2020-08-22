package com.wparja.veterinaryreports.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.wparja.veterinaryreports.R;
import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.persistence.entities.Report;
import com.wparja.veterinaryreports.persistence.entities.Specie;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDataFragment extends Fragment {

    private static final String ARG = "ARG";

    List<Specie> mSpecies;
    Spinner mGendersSpinner;
    AutoCompleteTextView mSpeciesActv;
    AutoCompleteTextView mBreedsActv;
    ImageButton mNewPhotoImgButton;
    ImageView mPatientMainPhoto;
    EditText mEditTextPatientName;
    EditText mEditTextPatientOwner;
    EditText mEditTextPatientAge;
    EditText mEditTextPatientMonths;
    EditText mEditTextPatientWeight;

    Report mPatient;

    public PatientDataFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PatientDataFragment newInstance(Report report) {
        PatientDataFragment patientDataFragment =  new PatientDataFragment();
        Bundle arg = new Bundle();
        arg.putSerializable(ARG, report);
        patientDataFragment.setArguments(arg);
        return patientDataFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPatient = (Report) getArguments().getSerializable(ARG);
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
        mEditTextPatientName = view.findViewById(R.id.patient_name);
        mEditTextPatientOwner = view.findViewById(R.id.owner);
        mEditTextPatientAge = view.findViewById(R.id.patient_age);
        mEditTextPatientMonths = view.findViewById(R.id.patient_months);
        mEditTextPatientWeight = view.findViewById(R.id.patient_weight);
        DataProvider.getInstance().loadData();
        fillGenderSpinner();
        fillAndSetListenerSpecieAutoCompleteTextView();
        bindPatientData();
        return view;
    }

    private void bindPatientData() {
        mEditTextPatientName.setText(mPatient.getPatientName());
        mEditTextPatientName.addTextChangedListener(new GenericTextWatcher(mEditTextPatientName));

        mEditTextPatientOwner.setText(mPatient.getOwnerName());
        mEditTextPatientOwner.addTextChangedListener(new GenericTextWatcher(mEditTextPatientOwner));

        mEditTextPatientAge.setText(mPatient.getPatientAge());
        mEditTextPatientAge.addTextChangedListener(new GenericTextWatcher(mEditTextPatientAge));

        mEditTextPatientMonths.setText(mPatient.getMouth());
        mEditTextPatientMonths.addTextChangedListener(new GenericTextWatcher(mEditTextPatientMonths));

        mEditTextPatientWeight.setText(mPatient.getPatientWeight());
        mEditTextPatientWeight.addTextChangedListener(new GenericTextWatcher(mEditTextPatientWeight));

        mSpeciesActv.setText(mPatient.getPatientSpecie());
        mBreedsActv.setText(mPatient.getPatientBreed());
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
                mBreedsActv.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mPatient.setPatientBreed(s.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPatient.setPatientSpecie(s.toString());
            }
        });
    }

    private void fillGenderSpinner() {
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new String[] {getString(R.string.male), getString(R.string.female)});
        mGendersSpinner.setAdapter(genderAdapter);
        int selectionPosition = genderAdapter.getPosition(mPatient.getPatientGender());
        if (selectionPosition != -1) {
            mGendersSpinner.setSelection(selectionPosition);
        } else {
            mGendersSpinner.setSelection(0);
        }

        mGendersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender = (String) parent.getItemAtPosition(position);
                mPatient.setPatientGender(gender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    class GenericTextWatcher implements TextWatcher {

        private View mView;

        public GenericTextWatcher(View view) {
            mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String value = s.toString();
            switch (mView.getId()) {
                case R.id.patient_name:
                    mPatient.setPatientName(value);
                    break;
                case R.id.owner:
                    mPatient.setOwnerName(value);
                    break;
                case R.id.patient_age:
                    mPatient.setPatientAge(value);
                    break;
                case R.id.patient_months:
                    mPatient.setMouth(value);
                case R.id.patient_weight:
                    mPatient.setPatientWeight(value);
            }
        }
    }
}