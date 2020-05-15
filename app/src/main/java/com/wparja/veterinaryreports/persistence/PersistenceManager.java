package com.wparja.veterinaryreports.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wparja.veterinaryreports.persistence.entities.Specie;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class PersistenceManager extends OrmLiteSqliteOpenHelper {

    private final String TAG = getClass().getName();
    private static final String DATABASE_NAME = "veterinary.reports.db";
    private static final int DATABASE_VERSION = 1;
    private Map<String, Dao> cache;
    private Set<Class<?>> entities;


    public PersistenceManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        cache = new HashMap<>();
        entities = new HashSet<>();


        initialize();
    }

    private void initialize() {
        try {
           entities.add(Specie.class);
        } catch (Exception e) {
            Log.wtf(TAG, "initialize: " + e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        dropTables();
        onCreate(db, connectionSource);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables();
        createTables();
    }

    private void createTables() {
        try {
            Log.i(TAG, "Creating new tables");

            for (Class entity : entities) {
                TableUtils.createTable(connectionSource, entity);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        } finally {
            entities = null;
        }
    }

    private void dropTables() {
        try {
            Log.i(TAG, "Dropping the tables");

            for (Class entity : entities) {
                TableUtils.dropTable(connectionSource, entity, true);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
    }

    private Dao getCachedDao(Class clazz) throws SQLException {
        Dao dao = cache.get(clazz.getName());

        if (dao == null) {
            dao = getDao(clazz);
            cache.put(clazz.getName(), dao);
        }

        return dao;
    }

    public <T> T persist(T entity) {
            try {
                CreateOrUpdateStatus status = getCachedDao(entity.getClass()).createOrUpdate(entity);
                if (status.isCreated() || status.isUpdated()) {
                    return entity;
                }
            } catch (SQLException e) {
                Log.e(TAG, e.toString());
            }
            return null;
    }

    public <T> T persistAll(final List<T> entities) {
        try {
            TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
                @Override
                public Void call() {
                    for (T entity : entities) {
                        persist(entity);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    public <T> T get(long id, Class clazz) {
        try {
            return (T) getCachedDao(clazz).queryForId(id);
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public <T> List<T> getAll(Class clazz) {
        try {
            return getCachedDao(clazz).queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public <T> int delete(T entity) {
        int result = -1;
        try {
            result = getCachedDao(entity.getClass()).delete(entity);
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    public <T> int delete(List<T> entities) {
        int result = -1;
        if (!entities.isEmpty()) {
            Class clazz = entities.get(0).getClass();
            try {
                result = getCachedDao(clazz).delete(entities);
            } catch (SQLException e) {
                Log.e(TAG, e.toString());
            }
        }
        return result;
    }

    public int deleteById(long id, Class clazz) {
        int result = -1;
        try {
            result = getCachedDao(clazz).deleteById(id);
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    public int deleteIds(List<Long> ids, Class clazz) {
        int result = -1;
        try {
            result = getCachedDao(clazz).deleteIds(ids);
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    public long countOf(Class clazz) {
        try {
            return getCachedDao(clazz).countOf();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return 0;
    }

    public QueryBuilder getQueryBuilder(Class clazz) {
        try {
            return getCachedDao(clazz).queryBuilder();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public DeleteBuilder getDeleteBuilder(Class clazz) {
        try {
            return getCachedDao(clazz).deleteBuilder();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public UpdateBuilder getUpdateBuilder(Class clazz) {
        try {
            return getCachedDao(clazz).updateBuilder();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public <T> List<T> getQueryRaw(Class clazz, String query, DataType... dataTypes) {
        try {
            return getCachedDao(clazz).queryRaw(query, dataTypes).getResults();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public <T> List<T> getQueryRaw(Class clazz, String query) {
        try {
            return getCachedDao(clazz).queryRaw(query).getResults();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public <T> List<T> query(Class clazz, PreparedQuery<T> prepare) {
        try {
            return getCachedDao(clazz).query(prepare);
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public void dropAndCreateTable(Class<?> entity) {
        try {
            Log.i(TAG, "drop e create table : " + entity.getName());

            TableUtils.dropTable(connectionSource, entity, true);
            TableUtils.createTableIfNotExists(connectionSource, entity);
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void close() {
        super.close();
        cache = null;
        entities = null;
    }
}
