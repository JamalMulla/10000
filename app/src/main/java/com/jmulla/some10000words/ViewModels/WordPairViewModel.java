package com.jmulla.some10000words.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jmulla.some10000words.Entities.WordPair;
import com.jmulla.some10000words.Language;
import com.jmulla.some10000words.Repositories.WordPairRepository;

import java.util.List;

public class WordPairViewModel extends AndroidViewModel {

  private WordPairRepository repository;

  public WordPairViewModel(@NonNull Application application) {
    super(application);
    repository = new WordPairRepository(application);
  }

  public void insert(WordPair wordPair){
    repository.insert(wordPair);
  }

  public void update(WordPair wordPair){
    repository.update(wordPair);
  }

  public void delete(WordPair wordPair){
    repository.delete(wordPair);
  }

  public void insertAll(List<WordPair> wordPairs){
    repository.insertAll(wordPairs);
  }

  public LiveData<List<WordPair>> getRandomWordPairs(int numberOfPairs){
    return repository.getRandomWordPairs(numberOfPairs);
  }

  public LiveData<List<WordPair>> getAllPairs(Language language) {
    return repository.getAllPairs(language);
  }



}
