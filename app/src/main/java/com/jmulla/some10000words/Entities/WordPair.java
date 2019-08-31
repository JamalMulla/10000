package com.jmulla.some10000words.Entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "WordPairs")
public class WordPair {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String foreignWord;
  private String def1;
  private String def2;
  private List<Pronunciation> pronunciation;

  public WordPair(String foreignWord, String def1, String def2, List<Pronunciation> pronunciation) {
    this.foreignWord = foreignWord;
    this.def1 = def1;
    this.def2 = def2;
    this.pronunciation = pronunciation;
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getDef1() {
    return def1;
  }


  public String getDef2() {
    return def2;
  }


  public List<Pronunciation> getPronunciation() {
    return pronunciation;
  }

  public String getForeignWord() {
    return foreignWord;
  }

}
