package com.wparja.veterinaryreports.persistence.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ReportEntity extends BaseEntity {

    @DatabaseField
    private String mPatientMainPhoto = "main.jpg";

    @DatabaseField
    private String mPatientName;

    public String getPatientMainPhoto() {
        return mPatientMainPhoto;
    }

    public void setPatientMainPhoto(String patientMainPhoto) {
        mPatientMainPhoto = patientMainPhoto;
    }

    public String getPatientName() {
        return mPatientName;
    }

    public void setPatientName(String patientName) {
        mPatientName = patientName;
    }
}
