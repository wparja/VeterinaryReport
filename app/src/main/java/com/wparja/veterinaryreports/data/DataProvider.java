package com.wparja.veterinaryreports.data;

import android.content.Context;
import com.wparja.veterinaryreports.persistence.PersistenceManager;
import com.wparja.veterinaryreports.persistence.entities.Clinics;
import com.wparja.veterinaryreports.persistence.entities.Diagnostics;
import com.wparja.veterinaryreports.persistence.entities.Exams;
import com.wparja.veterinaryreports.persistence.entities.Report;
import com.wparja.veterinaryreports.persistence.entities.Specie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataProvider {

    private static final DataProvider ourInstance = new DataProvider();
    private PersistenceManager mPersistenceManager;
    public static DataProvider getInstance() {
        return ourInstance;
    }

    private List<Specie> mSpecies = new ArrayList<>();
    private Diagnostics mDiagnostics;
    private Exams mExams;
    private Clinics mClinics;

    public void init(Context context) {
        mPersistenceManager = new PersistenceManager(context);
    }

    private DataProvider() {
    }

    private void createDefaultEntities() {
        mDiagnostics = new Diagnostics();
        mExams = new Exams();
        mClinics = new Clinics();
        mDiagnostics = mPersistenceManager.persist(mDiagnostics);
        mExams = mPersistenceManager.persist(mExams);
        mClinics = mPersistenceManager.persist(mClinics);
    }

    public void loadData() {
        mSpecies = mPersistenceManager.getAll(Specie.class);
        mDiagnostics = mPersistenceManager.get(1, Diagnostics.class);
        mExams = mPersistenceManager.get(1, Exams.class);
        mClinics = mPersistenceManager.get(1, Clinics.class);
        if (mDiagnostics == null && mExams == null) {
            createDefaultEntities();
        }
    }

    public List<Specie> getSpecies() {
        return mSpecies;
    }
    public Diagnostics getDiagnostics() {return  mDiagnostics;}
    public Exams getExams() { return mExams; }
    public Clinics getClinics() { return mClinics; }

    public void saveSpecie(String specieName, String breedName) {
        boolean save = false;
        Specie specie = null;
        Optional<Specie> specieFound = mSpecies.stream().filter(x -> x.getName().equals(specieName)).findFirst();
        if (specieFound.isPresent()) {
            if (!specieFound.get().getItems().contains(breedName)) {
                specie = specieFound.get();
                specie.getItems().add(breedName);
                save = true;
            }
        } else {
            specie = new Specie();
            specie.setName(specieName);
            specie.getItems().add(breedName);
            save = true;
        }

        if (save) {
            mPersistenceManager.persist(specie);
        }
    }

    public void saveDiagnostic(List<String> diagnostics) {
        boolean save = false;
        for (String diagnostic : diagnostics) {
            if (!mDiagnostics.getItems().contains(diagnostic)) {
                mDiagnostics.getItems().add(diagnostic);
                save = true;
            }
        }

        if (save) {
            mPersistenceManager.persist(mDiagnostics);
        }
    }

    public void saveExams(List<String> exams) {
        boolean save = false;
        for (String exam : exams) {
            if (!mExams.getItems().contains(exam)) {
                mExams.getItems().add(exam);
                save = true;
            }
        }

        if (save) {
            mPersistenceManager.persist(mExams);
        }
    }

    public void saveClinics(String clinicName) {
        if (!mClinics.getItems().contains(clinicName)) {
            mClinics.getItems().add(clinicName);
            mPersistenceManager.persist(mClinics);
        }
    }

    public void savePatient(Report patient) {
        // todo create a folder
        // move all photo inside temp to new folder
        // delete all photo from temp
        saveSpecie(patient.getPatientSpecie(), patient.getPatientBreed());
        saveExams(patient.getExams());
        saveDiagnostic(patient.getDiagnostics());
        mPersistenceManager.persist(patient);
    }

    public List<Report> search(String criteria) throws SQLException {
        return mPersistenceManager.getQueryBuilder(Report.class).where().eq("patient_name", criteria).query();
    }
}
