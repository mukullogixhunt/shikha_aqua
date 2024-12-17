package com.logixhunt.shikhaaqua.database;

import android.content.Context;

import androidx.room.Room;

import com.logixhunt.shikhaaqua.utils.Constant;


public class DatabaseClient {

    private Context context;
    private static DatabaseClient databaseClient;

    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        this.context = context;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, Constant.DB_NAME)
                .fallbackToDestructiveMigration().build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (databaseClient == null) {
            databaseClient = new DatabaseClient(mCtx);
        }
        return databaseClient;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
