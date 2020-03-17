package com.example.huarongdao.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.huarongdao.BuildConfig;
import com.example.huarongdao.bean.RecordEntity;

@Database(entities = {RecordEntity.class}, version = 1)
public abstract class DB extends RoomDatabase {

    private static DB instance;

    public static DB getInstance(Context context){
        if (instance==null) {
            instance= Room.databaseBuilder(context.getApplicationContext(), DB.class, BuildConfig.APPLICATION_ID)
                    .build();
        }
        return instance;
    }

   public abstract RecordDao recordDao();

}
