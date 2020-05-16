package com.wparja.veterinaryreports.persistence.entities;


import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Exams extends CollectionEntity<String> {

    public Exams() {
        setName(getClass().getSimpleName());
    }
}
