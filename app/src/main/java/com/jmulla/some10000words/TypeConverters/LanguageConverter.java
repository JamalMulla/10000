package com.jmulla.some10000words.TypeConverters;

import androidx.room.TypeConverter;

import com.jmulla.some10000words.Language;

public class LanguageConverter {


  @TypeConverter
  public String fromLanguage(Language language) {
    if (language == null) {
      return null;
    }

    return language.toString();
  }

  @TypeConverter
  public Language toLanguage(String languageString) {
    if (languageString == null) {
      return null;
    }

    if (languageString.equals("ES")) {
      return Language.ES;
    } else if (languageString.equals("DE")){
      return Language.DE;
    }
    return null;
  }

}
