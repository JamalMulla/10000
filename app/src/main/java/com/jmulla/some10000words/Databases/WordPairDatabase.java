package com.jmulla.some10000words.Databases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jmulla.some10000words.DAOs.WordPairDao;
import com.jmulla.some10000words.Entities.Pronunciation;
import com.jmulla.some10000words.Entities.WordPair;
import com.jmulla.some10000words.JSONModels.DEWordPair;
import com.jmulla.some10000words.JSONModels.ESWordPair;
import com.jmulla.some10000words.Language;
import com.jmulla.some10000words.TypeConverters.LanguageConverter;
import com.jmulla.some10000words.TypeConverters.PronunciationConverter;
import com.jmulla.some10000words.Utils.AssetUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {WordPair.class, Pronunciation.class}, version = 1)
@TypeConverters({PronunciationConverter.class, LanguageConverter.class})
public abstract class WordPairDatabase extends RoomDatabase {

  private static WordPairDatabase instance;

  public abstract WordPairDao getWordPairDao();

  public static synchronized WordPairDatabase getInstance(final Context context) {

    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(), WordPairDatabase.class, "word_pair_db").fallbackToDestructiveMigration().addCallback(new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
          super.onCreate(db);
          Thread t = new Thread(new Runnable() {
            public void run() {
              List<WordPair> deSpots = setupDESpots(context);
              List<WordPair> esSpots = setupESSpots(context);
              List<WordPair> all = new ArrayList<>();


              WordPairDao wordPairDao = instance.getWordPairDao();
              if (deSpots != null) {
                all.addAll(deSpots);
              }

              if (esSpots != null) {
                all.addAll(esSpots);
              }
              wordPairDao.insertAll(all);
            }
          });
          t.start();
        }
      }).build();
    }

    return instance;

  }


  private static List<WordPair> setupDESpots(Context context) {
    String deWordList = loadJSONAsString(context, "de_wordlist.json");

    if (deWordList == null) {
      return null;
    }
    Moshi moshi = new Moshi.Builder().build();
    Type listWords = Types.newParameterizedType(List.class, DEWordPair.class);
    JsonAdapter<List<DEWordPair>> jsonAdapter = moshi.adapter(listWords);
    jsonAdapter = jsonAdapter.lenient();

    List<DEWordPair> wordPairList;
    try {
      wordPairList = jsonAdapter.fromJson(deWordList);
      if (wordPairList == null) {
        return null;
      }
      List<WordPair> spots = new ArrayList<>();
      for (DEWordPair pair : wordPairList) {
        spots.add(new WordPair(pair.getDe(), pair.getEn(), "", new ArrayList<Pronunciation>(), Language.DE));
      }
      return spots;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static List<WordPair> setupESSpots(Context context) {
    String esWordList = loadJSONAsString(context, "es_wordlist.json");

    if (esWordList == null) {
      return null;
    }


    Moshi moshi = new Moshi.Builder().build();
    Type listWords = Types.newParameterizedType(List.class, ESWordPair.class);
    JsonAdapter<List<ESWordPair>> jsonAdapter = moshi.adapter(listWords);

    List<ESWordPair> wordPairList;
    try {
      wordPairList = jsonAdapter.fromJson(esWordList);
      if (wordPairList == null) {
        return null;
      }
      List<WordPair> spots = new ArrayList<>();
      for (ESWordPair pair : wordPairList) {
        String es;
        String en1;
        String en2;
        es = pair.getBase().equals("") ? pair.getOriginal_es() : pair.getBase();
        en1 = pair.getDef1().equals("") ? pair.getOriginal_en() : pair.getDef1();
        en2 = pair.getDef2();
        spots.add(new WordPair(es, en1, en2, pair.getPronunciation(), Language.ES));
      }
      return spots;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  private static String loadJSONAsString(Context context, String filename) {
    String JSONString = AssetUtils.loadJSONFromAsset(context, filename);
    if (JSONString == null) {
      System.out.println("Error in loading word store");
      return null;
    }
    return JSONString;
  }


}
