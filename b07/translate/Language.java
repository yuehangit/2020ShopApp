package com.b07.translate;

public enum Language {

  AFRIKAANS("af"),
  ALBANIAN("sq"),
  AMHARIC("am"),
  ARABIC("ar"),
  ARMENIAN("hy"),
  AZERBAIJANI("az"),
  BASQUE("eu"),
  BELARUSIAN("be"),
  BENGALI("bn"),
  BOSNIAN("bs"),
  BULGARIAN("bg"),
  CATALAN("ca"),
  CEBUANO("ceb"),
  CHINESE_SM("zh-CN"),
  CHINESE_TR("zh-TW"),
  CORSICAN("co"),
  CROATIAN("hr"),
  CZECH("cs"),
  DANISH("da"),
  DUTCH("nl"),
  ENGLISH("en"),
  ESPERANTO("eo"),
  ESTONIAN("et"),
  FINNISH("fi"),
  FRENCH("fr"),
  FRISIAN("fy"),
  GALICIAN("gl"),
  GEORGIAN("ka"),
  GERMAN("de"),
  GREEK("el"),
  GUJARATI("gu"),
  HAITIAN_CREOLE("ht"),
  HAUSA("ha"),
  HAWAIIAN("haw"),
  HEBREW("he"),
  HINDI("hi"),
  HMONG("hmn"),
  HUNGARIAN("hu"),
  ICELANDIC("is"),
  IGBO("ig"),
  INDONESIAN("id"),
  IRISH("ga"),
  ITALIAN("it"),
  JAPANESE("ja"),
  JAVANESE("jv"),
  KANNADA("kn"),
  KAZAKH("kk"),
  KHMER("km"),
  KOREAN("ko"),
  KURDISH("ku"),
  KYRGYZ("ky"),
  LAO("lo"),
  LATIN("la"),
  LATVIAN("lv"),
  LITHUANIAN("lt"),
  LUXEMBOURGISH("lb"),
  MACEDONIAN("mk"),
  MALAGASY("mg"),
  MALAY("ms"),
  MALAYALAM("ml"),
  MALTESE("mt"),
  MAORI("mi"),
  MARATHI("mr"),
  MONGOLIAN("mn"),
  MYANMAR("my"),
  NEPALI("ne"),
  NORWEGIAN("no"),
  NYANJA("ny"),
  PASHTO("ps"),
  PERSIAN("fa"),
  POLISH("pl"),
  PORTUGUESE("pt"),
  PUNJABI("pa"),
  ROMANIAN("ro"),
  RUSSIAN("ru"),
  SAMOAN("sm"),
  GAELIC("gd"),
  SERBIAN("sr"),
  SESOTHO("st"),
  SHONA("sn"),
  SINDHI("sd"),
  SINHALA("si"),
  SLOVAK("sk"),
  SLOVENIAN("sl"),
  SOMALI("so"),
  SPANISH("es"),
  SUNDANESE("su"),
  SWAHILI("sw"),
  SWEDISH("sv"),
  TAGALOG("tl"),
  TAJIK("tg"),
  TAMIL("ta"),
  TELUGU("te"),
  THAI("th"),
  TURKISH("tr"),
  UKRAINIAN("uk"),
  URDU("ur"),
  UZBEK("uz"),
  VIETNAMESE("vi"),
  WELSH("cy"),
  XHOSA("xh"),
  YIDDISH("yi"),
  YORUBA("yo"),
  ZULU("zu");

  private String language;

  private Language(String language) {
    this.language = language;
  }

  public String getLanguage() {
    return this.language;
  }

  public static Language getEnum(String language) {
    for (Language enumerator : Language.values()) {
      if (enumerator.toString().equalsIgnoreCase(language)) {
        return enumerator;
      }
    }
    return null;
  }
}
