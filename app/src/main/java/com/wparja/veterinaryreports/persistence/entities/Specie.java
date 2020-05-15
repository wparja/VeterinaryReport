package com.wparja.veterinaryreports.persistence.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Specie extends BaseEntity {

    @DatabaseField(columnName = "name")
    String mName;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = "breeds")
    Breeds<String> mBreeds;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Breeds<String> getBreeds() {
        return mBreeds;
    }

    public void setBreeds(Breeds<String> breeds) {
        mBreeds = breeds;
    }
}
