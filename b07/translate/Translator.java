package com.b07.translate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.example.androidlayouts.R;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;

public class Translator {

  private Translate translate;
  private Context context;

  public Translator(Context context) {
    this.context = context;
    getTranslateService();
  }

  private void getTranslateService() {

    if (isNetworkAvailable()) {
      StrictMode.ThreadPolicy policy =
          new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);

      try (InputStream is = context.getResources().openRawResource(R.raw.credentials)) {

        //Get credentials:
        final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

        //Set credentials and get translate service:
        TranslateOptions translateOptions =
            TranslateOptions.newBuilder().setCredentials(myCredentials).build();
        translate = translateOptions.getService();

      } catch (IOException ioe) {
        ioe.printStackTrace();

      }
    }
  }

  public String translate(String originalText, Language language) {
    if (!language.equals(Language.ENGLISH) && isNetworkAvailable()) {
      //Get input text to be translated:
      Translation translation =
          translate.translate(originalText,
              Translate.TranslateOption.targetLanguage(language.getLanguage()),
              Translate.TranslateOption.model("nmt"));
      return translation.getTranslatedText();
    }
    return originalText;
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
