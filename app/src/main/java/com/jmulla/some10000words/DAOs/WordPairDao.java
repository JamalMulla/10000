package com.jmulla.some10000words.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jmulla.some10000words.Entities.WordPair;

import java.util.List;

@Dao
public interface WordPairDao {

  @Insert
  void insert(WordPair wordPair);

  @Update
  void update(WordPair wordPair);

  void delete(WordPair wordPair);

  @Query("SELECT * FROM WordPairs WHERE id IN (SELECT id FROM WordPairs ORDER BY RANDOM() LIMIT :numberOfPairs)")
  LiveData<List<WordPair>> getRandomWordPairs(int numberOfPairs);

  @Query("SELECT * FROM WordPairs")
  LiveData<List<WordPair>> getAllPairs();

}
