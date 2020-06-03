package com.wparja.veterinaryreports.persistence.entities;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Report extends BaseEntity {

    @DatabaseField (columnName = "clinic_name")
    private String mClinicName;

    @DatabaseField (columnName = "patient_name")
    private String mPatientName;

    @DatabaseField (columnName = "owner_name")
    private String mOwnerName;

    @DatabaseField (columnName = "patient_age")
    private String mPatientAge;

    @DatabaseField (columnName = "patient_weight")
    private String mPatientWeight;

    @DatabaseField (columnName = "patient_gender")
    private String mPatientGender;

    @DatabaseField (columnName = "patient_specie")
    private String mPatientSpecie;

    @DatabaseField (columnName = "patient_breed")
    private String mPatientBreed;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = "exams")
    private Items<String> mExams;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = "diagnostics")
    private Items<String> mDiagnostics;

    @DatabaseField(columnName = "procedure_performed")
    private String mProcedurePerformed;

    @DatabaseField(columnName = "recommendations")
    private String mRecommendations;

    @DatabaseField(columnName = "gallery_folder_path")
    private String mGalleryFolderPath;

    @DatabaseField(columnName = "main_photo")
    private String mMainPhoto;

    @DatabaseField(columnName = "date_procedure")
    private Date mDateProcedure;
}
