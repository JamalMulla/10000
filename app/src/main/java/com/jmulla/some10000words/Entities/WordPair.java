package com.jmulla.some10000words.Entities;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.jmulla.some10000words.Language;

import java.util.List;

@Entity(tableName = "WordPairs")
public class WordPair {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String foreignWord;
  private String def1;
  private String def2;

  private Language language;
  private List<Pronunciation> pronunciation;

  private boolean isStarred;

  public WordPair(String foreignWord, String def1, String def2, List<Pronunciation> pronunciation, Language language) {
    this.foreignWord = foreignWord;
    this.def1 = def1;
    this.def2 = def2;
    this.pronunciation = pronunciation;
    this.language = language;
    this.isStarred = false;
  }

  @Ignore
  public WordPair(String foreignWord, String def1, String def2, List<Pronunciation> pronunciation, Language language, boolean isStarred){
    this(foreignWord, def1, def2, pronunciation, language);
    this.isStarred = isStarred;
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

  public Language getLanguage() {
    return language;
  }

  public boolean isStarred() {
    return isStarred;
  }

  public void setStarred(boolean starred){
    this.isStarred = starred;
  }
}
