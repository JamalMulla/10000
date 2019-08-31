package com.jmulla.some10000words.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Pronunciations")
public class Pronunciation {
  @PrimaryKey(autoGenerate = true)
  private int id;

  private String syllable;
  private boolean emphasis;

  Pronunciation(String syllable, boolean emphasis) {
    this.syllable = syllable;
    this.emphasis = emphasis;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSyllable() {
    return syllable;
  }


  public boolean hasEmphasis() {
    return emphasis;
  }


}
