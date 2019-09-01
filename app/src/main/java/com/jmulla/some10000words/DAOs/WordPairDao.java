package com.jmulla.some10000words.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jmulla.some10000words.Entities.WordPair;
import com.jmulla.some10000words.Language;

import java.util.List;

@Dao
public interface WordPairDao {

  @Insert
  void insert(WordPair wordPair);

  @Update
  void update(WordPair wordPair);

  @Delete
  void delete(WordPair wordPair);

  @Insert
  void insertAll(List<WordPair> wordPairs);

  @Query("SELECT * FROM WordPairs WHERE id IN (SELECT id FROM WordPairs WHERE language = :language ORDER BY RANDOM() LIMIT :numberOfPairs)")
  LiveData<List<WordPair>> getRandomWordPairs(Language language, int numberOfPairs);

  @Query("SELECT * FROM WordPairs WHERE language = :language")
  LiveData<List<WordPair>> getAllPairs(Language language);

}
