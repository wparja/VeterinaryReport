package com.wparja.veterinaryreports.data;

import android.content.Context;
import com.wparja.veterinaryreports.persistence.PersistenceManager;
import com.wparja.veterinaryreports.persistence.entities.Specie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataProvider {

    private static final DataProvider ourInstance = new DataProvider();
    private PersistenceManager mPersistenceManager;
    public static DataProvider getInstance() {
        return ourInstance;
    }

    private List<Specie> mSpecies = new ArrayList<>();

    public void init(Context context) {
        mPersistenceManager = new PersistenceManager(context);
    }


    private DataProvider() {
    }

    public void loadData() {
        mSpecies = mPersistenceManager.getAll(Specie.class);
    }

    public List<Specie> getSpecies() {
        return mSpecies;
    }

    public void UpdateSpecie(String specieName, String breedName) {
        boolean save = false;
        Specie specie = null;
        Optional<Specie> specieFound = mSpecies.stream().filter(x -> x.getName() == specieName).findFirst();
        if (specieFound.isPresent()) {
            if (!specieFound.get().getBreeds().contains(breedName)) {
                specie = specieFound.get();
                specie.getBreeds().add(breedName);
                save = true;
            }
        } else {
            specie = new Specie();
            specie.setName(specieName);
            specie.getBreeds().add(breedName);
            save = true;
        }

        if (save) {
            mPersistenceManager.persist(specie);
        }
    }
}
