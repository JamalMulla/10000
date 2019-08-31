package com.jmulla.some10000words.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jmulla.some10000words.DAOs.WordPairDao;
import com.jmulla.some10000words.Entities.Pronunciation;

@Database(entities = {Pronunciation.class, Pronunciation.class}, version = 1)
public abstract class WordPairDatabase extends RoomDatabase {

  private static WordPairDatabase instance;

  private static WordPairDao wordPairDao;

  public static synchronized WordPairDatabase getInstance(Context context) {

    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(), WordPairDatabase.class, "word_pair_db").fallbackToDestructiveMigration().build();
    }
    return instance;

  }

}
