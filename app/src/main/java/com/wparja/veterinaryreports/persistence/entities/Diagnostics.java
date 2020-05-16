package com.wparja.veterinaryreports.persistence.entities;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Diagnostics extends CollectionEntity<String> {

    public Diagnostics() {
        setName(getClass().getSimpleName());
    }
}
