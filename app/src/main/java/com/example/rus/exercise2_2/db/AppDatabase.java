package com.example.rus.exercise2_2.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {NewsEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "NewsRoomBd.db";
    private static AppDatabase singleton;

    public static AppDatabase getAppDatabase(Context context) {
        if (singleton == null) {
            synchronized (AppDatabase.class) {
                if (singleton == null) {
                    singleton = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                            .build();
                }
            }
        }
        return singleton;
    }

    public abstract NewsDao newsDao();
}
