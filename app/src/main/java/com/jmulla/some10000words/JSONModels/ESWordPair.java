package com.jmulla.some10000words.JSONModels;

import java.util.List;

public class ESWordPair {
  private String original_es;
  private String original_en;
  private String base;
  private String def1;
  private String def2;
  private List<Pronunciation> pronunciation;

  public ESWordPair(String original_es, String original_en, String base, String def1, String def2, List<Pronunciation> pronunciation) {
    this.original_es = original_es;
    this.original_en = original_en;
    this.base = base;
    this.def1 = def1;
    this.def2 = def2;
    this.pronunciation = pronunciation;
  }

  public String getOriginal_es() {
    return original_es;
  }

  public void setOriginal_es(String original_es) {
    this.original_es = original_es;
  }

  public String getOriginal_en() {
    return original_en;
  }

  public void setOriginal_en(String original_en) {
    this.original_en = original_en;
  }

  public String getBase() {
    return base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public String getDef1() {
    return def1;
  }

  public void setDef1(String def1) {
    this.def1 = def1;
  }

  public String getDef2() {
    return def2;
  }

  public void setDef2(String def2) {
    this.def2 = def2;
  }

  public List<Pronunciation> getPronunciation() {
    return pronunciation;
  }

  public void setPronunciation(List<Pronunciation> pronunciation) {
    this.pronunciation = pronunciation;
  }
}
