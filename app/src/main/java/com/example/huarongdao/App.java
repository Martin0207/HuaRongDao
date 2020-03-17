package com.example.huarongdao;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.huarongdao.dao.DB;

import timber.log.Timber;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
