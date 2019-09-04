package com.jmulla.some10000words.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jmulla.some10000words.Entities.WordPair;
import com.jmulla.some10000words.Language;
import com.jmulla.some10000words.Repositories.WordPairRepository;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class WordPairViewModel extends AndroidViewModel {

  private WordPairRepository repository;
  private List<WordPair> esWords;
  private List<WordPair> deWords;
  private MutableLiveData<List<WordPair>> currentWords;
  private Stack<Direction> directions = new Stack<>();
  private int currentPosition = 0;

  public WordPairViewModel(@NonNull Application application) {
    super(application);
    repository = new WordPairRepository(application);
    esWords = repository.getAllPairs(Language.ES);
    deWords = repository.getAllPairs(Language.DE);

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

  public LiveData<List<WordPair>> getRandomWordPairs(Language language, int numberOfPairs){
    if (currentWords == null){
      MutableLiveData<List<WordPair>> liveData = new MutableLiveData<>();
      List<WordPair> wordPairs = new ArrayList<>();
      if (language == Language.ES){
        wordPairs = pickNRandomElements(esWords, numberOfPairs);
      } else if (language == Language.DE){
        wordPairs = pickNRandomElements(deWords, numberOfPairs);
      }

      liveData.setValue(wordPairs);
      currentWords = liveData;
      return currentWords;
    }

    return currentWords;
  }

  public LiveData<List<WordPair>> getAllPairs(Language language) {
    MutableLiveData<List<WordPair>> liveData = new MutableLiveData<>();
    if (language == Language.ES) {
      liveData.setValue(esWords);
    } else {
      liveData.setValue(esWords);
    }
    return liveData;
  }

  public LiveData<List<WordPair>> getAllStarred(){
    return repository.getAllStarred();
  }

  //https://stackoverflow.com/questions/4702036/take-n-random-elements-from-a-liste
  private static <E> List<E> pickNRandomElements(List<E> list, int n) {
    int length = list.size();
    Random r = ThreadLocalRandom.current();

    if (length < n) return null;

    //We don't need to shuffle the whole list
    for (int i = length - 1; i >= length - n; --i)
    {
      Collections.swap(list, i , r.nextInt(i + 1));
    }
    return list.subList(length - n, length);
  }

  public void swapDeck(Language language, int numberOfPairs){
    if (currentWords == null){
      currentWords = new MutableLiveData<>();
    }
    List<WordPair> wordPairs = new ArrayList<>();
    if (language == Language.ES){
      wordPairs = pickNRandomElements(esWords, numberOfPairs);
    } else if (language == Language.DE){
      wordPairs = pickNRandomElements(deWords, numberOfPairs);
    }

    currentWords.setValue(wordPairs);
  }


  public int getCurrentPosition() {
    return currentPosition;
  }

  public void setCurrentPosition(int currentPosition) {
    this.currentPosition = currentPosition;
  }

  public Direction popDirection() {
    return directions.pop();
  }

  public Direction peekDirection(){
    return directions.peek();
  }

  public void pushDirection(Direction direction) {
    this.directions.push(direction);
  }

  public boolean directionsIsEmpty(){
    return directions.isEmpty();
  }

  public void clearDirections(){
    directions.clear();
  }


}
