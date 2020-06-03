package com.wparja.veterinaryreports.persistence.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ReportEntity extends BaseEntity {

    @DatabaseField
    private String mPatientMainPhoto = "main.jpg";

    public String getPatientMainPhoto() {
        return mPatientMainPhoto;
    }

}
