package com.jmulla.some10000words.Databases;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jmulla.some10000words.DAOs.WordPairDao;
import com.jmulla.some10000words.Entities.Pronunciation;
import com.jmulla.some10000words.Entities.WordPair;
import com.jmulla.some10000words.JSONModels.DEWordPair;
import com.jmulla.some10000words.JSONModels.ESWordPair;
import com.jmulla.some10000words.Spot;
import com.jmulla.some10000words.Utils.AssetUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {Pronunciation.class, Pronunciation.class}, version = 1)
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
              List<Spot> deSpots = setupDESpots(context);
              List<Spot> esSpots = setupESSpots(context);
              WordPairDao wordPairDao = instance.getWordPairDao();
              if (deSpots != null) {
                for (Spot spot : deSpots){
                  WordPair wordPair = new WordPair(spot.getForeignWord(), spot.getEnglishWord1(), spot.getEnglishWord2(), spot.getPronunciationList());
                  wordPairDao.insert(wordPair);
                }
              }

              if (esSpots != null) {
                for (Spot spot : esSpots){
                  WordPair wordPair = new WordPair(spot.getForeignWord(), spot.getEnglishWord1(), spot.getEnglishWord2(), spot.getPronunciationList());
                  wordPairDao.insert(wordPair);
                }
              }
            }
          });
          t.start();
        }
      }).build();
    }

    return instance;

  }


  private static List<Spot> setupDESpots(Context context) {
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
      List<Spot> spots = new ArrayList<>();
      for (DEWordPair pair : wordPairList) {
        spots.add(new Spot(pair.getDe(), pair.getEn()));
      }
      return spots;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static List<Spot> setupESSpots(Context context) {
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
      List<Spot> spots = new ArrayList<>();
      for (ESWordPair pair : wordPairList) {
        String es;
        String en1;
        String en2;
        es = pair.getBase().equals("") ? pair.getOriginal_es() : pair.getBase();
        en1 = pair.getDef1().equals("") ? pair.getOriginal_en() : pair.getDef1();
        en2 = pair.getDef2();
        spots.add(new Spot(es, en1, en2, pair.getPronunciation()));
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
