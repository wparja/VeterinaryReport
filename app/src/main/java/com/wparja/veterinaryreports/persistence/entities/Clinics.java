package com.wparja.veterinaryreports.persistence.entities;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Clinics extends CollectionEntity {

    public Clinics() {
        setName(getClass().getSimpleName());
    }
}
