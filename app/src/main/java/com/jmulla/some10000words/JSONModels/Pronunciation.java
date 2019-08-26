package com.jmulla.some10000words.JSONModels;

public class Pronunciation {
  private String syllable;
  private boolean emphasis;

  Pronunciation(String syllable, boolean emphasis) {
    this.syllable = syllable;
    this.emphasis = emphasis;
  }

  public String getSyllable() {
    return syllable;
  }

  public void setSyllable(String syllable) {
    this.syllable = syllable;
  }

  public boolean isEmphasis() {
    return emphasis;
  }

  public void setEmphasis(boolean emphasis) {
    this.emphasis = emphasis;
  }
}
