package com.b07.translate;

public class LanguageSingleton {

  private static LanguageSingleton instance;

  public static LanguageSingleton getInstance() {
    if (instance == null) {
      instance = new LanguageSingleton();
    }
    return instance;
  }

  private LanguageSingleton() {
  }

  private Language language = Language.ENGLISH;

  public Language getValue() {
    return language;
  }

  public void setValue(Language language) {
    if (language != null) {
      this.language = language;
    }
  }
}