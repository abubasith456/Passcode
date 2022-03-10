package com.example.passcode.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.passcode.model.PinModel;

@Database(entities = {PinModel.class}, version  = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NewsDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "OFFLINE_PIN")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }
}
