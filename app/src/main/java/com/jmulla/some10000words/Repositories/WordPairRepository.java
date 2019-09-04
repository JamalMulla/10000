package com.jmulla.some10000words.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.jmulla.some10000words.DAOs.WordPairDao;
import com.jmulla.some10000words.Databases.WordPairDatabase;
import com.jmulla.some10000words.Entities.WordPair;
import com.jmulla.some10000words.Language;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

  public List<WordPair> getRandomWordPairs(final Language language, final int numberOfPairs){
    ExecutorService executor = Executors.newFixedThreadPool(1);
    Callable<List<WordPair>> callable = new Callable<List<WordPair>>() {
      @Override
      public List<WordPair> call() {
        return wordPairDao.getRandomWordPairs(language, numberOfPairs);
      }
    };
    Future<List<WordPair>> future = executor.submit(callable);
    try {
      return future.get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<WordPair> getAllPairs(final Language language) {
    ExecutorService executor = Executors.newFixedThreadPool(1);
    Callable<List<WordPair>> callable = new Callable<List<WordPair>>() {
      @Override
      public List<WordPair> call() {
        return wordPairDao.getAllPairs(language);
      }
    };
    Future<List<WordPair>> future = executor.submit(callable);
    try {
      return future.get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public LiveData<List<WordPair>> getAllStarred(){
    return wordPairDao.getAllStarred();
  }

}
