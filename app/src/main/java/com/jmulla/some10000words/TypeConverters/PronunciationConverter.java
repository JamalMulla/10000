package com.jmulla.some10000words.TypeConverters;

import androidx.room.TypeConverter;

import com.jmulla.some10000words.Entities.Pronunciation;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PronunciationConverter {

    private JsonAdapter<List<Pronunciation>> jsonAdapter;
    public PronunciationConverter(){
      Moshi moshi = new Moshi.Builder().build();
      Type pronunciationsType = Types.newParameterizedType(List.class, Pronunciation.class);
      jsonAdapter = moshi.adapter(pronunciationsType);
      jsonAdapter = jsonAdapter.lenient();
    }

    @TypeConverter
    public String fromPronunciations(List<Pronunciation> pronunciations) {
      if (pronunciations == null || pronunciations.isEmpty()) {
        return "[]";
      }

      return jsonAdapter.toJson(pronunciations);
    }

    @TypeConverter
    public List<Pronunciation> toPronunciations(String pronunciationsString) {
      if (pronunciationsString == null || pronunciationsString.equals("[]")) {
        return new ArrayList<>();
      }

      try {
        return jsonAdapter.fromJson(pronunciationsString);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }


}
