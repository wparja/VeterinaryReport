package com.wparja.veterinaryreports.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.wparja.veterinaryreports.R;
import com.wparja.veterinaryreports.data.DataProvider;
import com.wparja.veterinaryreports.persistence.entities.Items;
import com.wparja.veterinaryreports.persistence.entities.Report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProcedureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProcedureFragment extends Fragment {

    private static final String ARG = "ARG";

    EditText mExams;
    EditText mDiagnostics;
    EditText mProcedurePerformed;
    ImageButton mProcedurePerformedImgBtn;
    EditText mRecommendations;
    EditText mHistory;
    List<String> mExamsSelected = new ArrayList<>();
    List<String> mDiagnosticsSelected = new ArrayList<>();
    TextInputLayout mTextInputLayout;
    Report mPatient;

    public ProcedureFragment() {
        // Required empty public constructor
    }

    public static ProcedureFragment newInstance(Report report) {
        ProcedureFragment procedureFragment = new ProcedureFragment();
        Bundle arg = new Bundle();
        arg.putSerializable(ARG, report);
        procedureFragment.setArguments(arg);
        return procedureFragment;
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
        View view =  inflater.inflate(R.layout.fragment_procedure, container, false);
        mExams = view.findViewById(R.id.previous_exams);
        mExams.setText(mPatient.getExamsFormatted());
        mExamsSelected.addAll(mPatient.getExams());
        mExams.addTextChangedListener(new GenericTextWatcher(mExams));

        mDiagnostics = view.findViewById(R.id.diagnostics);
        mDiagnostics.setText(mPatient.getDiagnosticsFormatted());
        mDiagnosticsSelected.addAll(mPatient.getDiagnostics());
        mDiagnostics.addTextChangedListener(new GenericTextWatcher(mDiagnostics));

        mProcedurePerformed = view.findViewById(R.id.procedure_performed);
        mProcedurePerformed.setText(mPatient.getProcedurePerformed());
        mProcedurePerformed.addTextChangedListener(new GenericTextWatcher(mProcedurePerformed));

        mRecommendations = view.findViewById(R.id.recommendations);
        mRecommendations.setText(mPatient.getRecommendations());
        mRecommendations.addTextChangedListener(new GenericTextWatcher(mRecommendations));

        mHistory = view.findViewById(R.id.history);
        mHistory.setText(mPatient.getMedicalHistory());
        mHistory.addTextChangedListener(new GenericTextWatcher(mHistory));

        //  mProcedurePerformedImgBtn = view.findViewById(R.id.procedure_performed_img_btn);
        mTextInputLayout = view.findViewById(R.id.previous_exams_text_input_layout);

        mExams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChoiceDialog(getString(R.string.previous_exams), getString(R.string.new_exam), mExams, DataProvider.getInstance().getExams().getItems(), mExamsSelected);
            }
        });


        mDiagnostics.setOnClickListener(v -> createChoiceDialog(getString(R.string.diagnostics), getString(R.string.new_diagnostic), mDiagnostics, DataProvider.getInstance().getDiagnostics().getItems(), mDiagnosticsSelected));

     //   mProcedurePerformedImgBtn.setOnClickListener(v -> createEditTextDialog(getString(R.string.procedure_performed), mProcedurePerformed));

        mRecommendations.setOnClickListener(v -> createEditTextDialog(getString(R.string.recommendations), mRecommendations));
        mHistory.setOnClickListener(v -> createEditTextDialog(getString(R.string.history), mHistory));
        return view;
    }

    private void createChoiceDialog(String title, String label, TextView editText, List<String> allData, List<String> allDataSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
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
            mTextInputLayout.invalidate();
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            dialog.dismiss();
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createEditTextDialog(String title, TextView text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
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
            String value = s.toString().replace("\\s+", "");
            switch (mView.getId()) {
                case R.id.previous_exams:
                    mPatient.getExams().clear();
                    Collections.addAll(mPatient.getExams(), value.split(","));
                    break;
                case R.id.diagnostics:
                    mPatient.getDiagnostics().clear();
                    Collections.addAll(mPatient.getDiagnostics(), value.split(","));
                    break;
                case R.id.procedure_performed:
                    mPatient.setProcedurePerformed(value);
                    break;
                case R.id.recommendations:
                    mPatient.setRecommendations(value);
                case R.id.history:
                    mPatient.setMedicalHistory(value);
            }
        }
    }
}