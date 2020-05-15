package com.wparja.veterinaryreports.persistence.entities;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class BaseEntity implements Serializable {

    @DatabaseField(generatedId = true, unique = true)
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
