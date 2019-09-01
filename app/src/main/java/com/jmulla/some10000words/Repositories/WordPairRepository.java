package com.jmulla.some10000words.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.jmulla.some10000words.DAOs.WordPairDao;
import com.jmulla.some10000words.Databases.WordPairDatabase;
import com.jmulla.some10000words.Entities.WordPair;
import com.jmulla.some10000words.Language;

import java.util.List;

public class WordPairRepository {
  private WordPairDao wordPairDao;

  public WordPairRepository(Application application){
    WordPairDatabase wordPairDatabase = WordPairDatabase.getInstance(application);
    wordPairDao = wordPairDatabase.getWordPairDao();
  }

  public void insert(final WordPair wordPair){
    Thread t = new Thread(new Runnable() {
      public void run() {
        wordPairDao.insert(wordPair);
      }
    });
    t.start();
  }


  public void update(final WordPair wordPair){
    Thread t = new Thread(new Runnable() {
      public void run() {
        wordPairDao.update(wordPair);
      }
    });
    t.start();
  }

  public void delete(final WordPair wordPair){
    Thread t = new Thread(new Runnable() {
      public void run() {
        wordPairDao.delete(wordPair);
      }
    });
    t.start();
  }

  public void insertAll(final List<WordPair> wordPairs){
    Thread t = new Thread(new Runnable() {
      public void run() {
        wordPairDao.insertAll(wordPairs);
      }
    });
    t.start();
  }

  public LiveData<List<WordPair>> getRandomWordPairs(Language language, int numberOfPairs){
    LiveData<List<WordPair>> randomWordPairs = wordPairDao.getRandomWordPairs(language, numberOfPairs);
    return randomWordPairs;
  }

  public LiveData<List<WordPair>> getAllPairs(Language language) {
    return wordPairDao.getAllPairs(language);
  }

}
