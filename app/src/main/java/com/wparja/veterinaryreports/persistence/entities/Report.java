package com.wparja.veterinaryreports.persistence.entities;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Report extends BaseEntity {

    public Report() {
        mExams = new Items<>();
        mDiagnostics = new Items<>();
    }

    @DatabaseField
    private String mSurgeon;

    @DatabaseField (columnName = "clinic_name")
    private String mClinicName;

    @DatabaseField (columnName = "patient_name")
    private String mPatientName;

    @DatabaseField (columnName = "owner_name")
    private String mOwnerName;

    @DatabaseField (columnName = "patient_age")
    private String mPatientAge;

    @DatabaseField (columnName = "patient_mouth")
    private String mMouth;

    @DatabaseField (columnName = "patient_weight")
    private String mPatientWeight;

    @DatabaseField (columnName = "patient_gender")
    private String mPatientGender;

    @DatabaseField (columnName = "patient_specie")
    private String mPatientSpecie;

    @DatabaseField (columnName = "patient_breed")
    private String mPatientBreed;

    @DatabaseField(columnName = "medical_history")
    private String mMedicalHistory;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = "exams")
    private Items<String> mExams;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = "diagnostics")
    private Items<String> mDiagnostics;

    @DatabaseField(columnName = "procedure_performed")
    private String mProcedurePerformed;

    @DatabaseField(columnName = "recommendations")
    private String mRecommendations;

    @DatabaseField(columnName = "start_procedure")
    private String mStart;

    @DatabaseField(columnName = "end_procedure")
    private String mEnd;

    public String getSurgeon() {
        return mSurgeon;
    }

    public void setSurgeon(String surgeon) {
        mSurgeon = surgeon;
    }

    public String getClinicName() {
        return mClinicName;
    }

    public void setClinicName(String clinicName) {
        mClinicName = clinicName;
    }

    public String getPatientName() {
        return mPatientName;
    }

    public void setPatientName(String patientName) {
        mPatientName = patientName;
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }

    public String getPatientAge() {
        return mPatientAge;
    }

    public void setPatientAge(String patientAge) {
        mPatientAge = patientAge;
    }

    public String getMouth() {
        return mMouth;
    }

    public void setMouth(String mouth) {
        mMouth = mouth;
    }

    public String getPatientWeight() {
        return mPatientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        mPatientWeight = patientWeight;
    }

    public String getPatientGender() {
        return mPatientGender;
    }

    public void setPatientGender(String patientGender) {
        mPatientGender = patientGender;
    }

    public String getPatientSpecie() {
        return mPatientSpecie;
    }

    public void setPatientSpecie(String patientSpecie) {
        mPatientSpecie = patientSpecie;
    }

    public String getPatientBreed() {
        return mPatientBreed;
    }

    public void setPatientBreed(String patientBreed) {
        mPatientBreed = patientBreed;
    }

    public String getMedicalHistory() {
        return mMedicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        mMedicalHistory = medicalHistory;
    }

    public Items<String> getExams() {
        return mExams;
    }

    public void setExams(Items<String> exams) {
        mExams = exams;
    }

    public Items<String> getDiagnostics() {
        return mDiagnostics;
    }

    public void setDiagnostics(Items<String> diagnostics) {
        mDiagnostics = diagnostics;
    }

    public String getProcedurePerformed() {
        return mProcedurePerformed;
    }

    public void setProcedurePerformed(String procedurePerformed) {
        mProcedurePerformed = procedurePerformed;
    }

    public String getRecommendations() {
        return mRecommendations;
    }

    public void setRecommendations(String recommendations) {
        mRecommendations = recommendations;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String start) {
        mStart = start;
    }

    public String getEnd() {
        return mEnd;
    }

    public void setEnd(String end) {
        mEnd = end;
    }
}
