package com.jmulla.some10000words;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.jmulla.some10000words.DAOs.WordPairDao;
import com.jmulla.some10000words.Databases.WordPairDatabase;
import com.jmulla.some10000words.Entities.WordPair;

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

  public LiveData<List<WordPair>> getRandomWordPairs(int numberOfPairs){
    return wordPairDao.getRandomWordPairs(numberOfPairs);
  }

  public LiveData<List<WordPair>> getAllPairs() {
    return wordPairDao.getAllPairs();
  }

}
