package com.wparja.veterinaryreports.persistence.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class CollectionEntity<T> extends NamedEntity {

    public CollectionEntity() {
        mItems = new Items<>();
    }

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Items<T> mItems;

    public Items<T> getItems() {
        return mItems;
    }

    public void setItems(Items<T> items) {
        mItems = items;
    }
}
