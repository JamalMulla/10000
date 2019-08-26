package com.jmulla.some10000words;

import com.jmulla.some10000words.JSONModels.Pronunciation;

import java.util.ArrayList;
import java.util.List;

public class Spot {
  private static Long STATIC_ID = 0L;
  private String foreignWord;
  private String englishWord1;
  private String englishWord2;
  private List<Pronunciation> pronunciationList;
  private Long id;

  public Spot(String foreignWord, String englishWord1, String englishWord2, List<Pronunciation> pronunciationList) {
    this.id = STATIC_ID;
    this.foreignWord = foreignWord;
    this.englishWord1 = englishWord1;
    this.englishWord2 = englishWord2;
    this.pronunciationList = pronunciationList;
    STATIC_ID += 1;
  }

  public Spot(String foreignWord, String englishWord) {
    this.id = STATIC_ID;
    this.foreignWord = foreignWord;
    this.englishWord1 = englishWord;
    this.englishWord2 = "";
    this.pronunciationList = new ArrayList<>();
    STATIC_ID += 1;
  }



  public Long getId() {
    return id;
  }

  public String getForeignWord() {
    return foreignWord;
  }

  public void setForeignWord(String foreignWord) {
    this.foreignWord = foreignWord;
  }

  public String getEnglishWord1() {
    return englishWord1;
  }

  public void setEnglishWord1(String englishWord1) {
    this.englishWord1 = englishWord1;
  }

  public String getEnglishWord2() {
    return englishWord2;
  }

  public void setEnglishWord2(String englishWord2) {
    this.englishWord2 = englishWord2;
  }

  public List<Pronunciation> getPronunciationList() {
    return pronunciationList;
  }

  public void setPronunciationList(List<Pronunciation> pronunciationList) {
    this.pronunciationList = pronunciationList;
  }
}
