package com.jmulla.some10000words.Entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "WordPairs")
public class WordPair {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String originalForeign;
  private String originalEn;
  private String base;
  private String def1;
  private String def2;
  private List<Pronunciation> pronunciation;

  public WordPair(String originalForeign, String originalEn, String base, String def1, String def2, List<Pronunciation> pronunciation) {
    this.originalForeign = originalForeign;
    this.originalEn = originalEn;
    this.base = base;
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

  public String getOriginalForeign() {
    return originalForeign;
  }

  public String getOriginalEn() {
    return originalEn;
  }

  public String getBase() {
    return base;
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

}
