package com.example.rus.exercise2_2.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM NewsEntity")
    Observable<List<NewsEntity>> getAll();

    @Query("SELECT * FROM NewsEntity WHERE id = :id")
    Single<NewsEntity> getNewsById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NewsEntity... newsEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsEntity newsEntity);

    @Delete
    void delete(NewsEntity newsEntity);

    @Query("DELETE FROM NewsEntity")
    void deleteAll();
}
