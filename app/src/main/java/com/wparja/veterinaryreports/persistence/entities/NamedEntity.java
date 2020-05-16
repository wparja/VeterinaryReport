package com.wparja.veterinaryreports.persistence.entities;

import com.j256.ormlite.field.DatabaseField;

public class NamedEntity extends BaseEntity {

    @DatabaseField(columnName = "name")
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
