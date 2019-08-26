package com.jmulla.some10000words.Utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AssetUtils {

  public static String loadJSONFromAsset(Context context, String filename) {
    String json;
    try {
      InputStream is = context.getAssets().open(filename);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, StandardCharsets.UTF_8);
      return json;
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

}
