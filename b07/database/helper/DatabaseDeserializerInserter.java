package com.b07.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.b07.database.DatabaseDriverAndroid;

/**
 * This class is meant as a temporary fix to allow the insertion of passwords without hashing them.
 * This class is to only be used by the DatabaseDeserializer class.
 *
 * @author vladislavtrukhin
 */
public class DatabaseDeserializerInserter extends DatabaseDriverAndroid {

  private Context context;

  public DatabaseDeserializerInserter(Context context) {
    super(context);
    this.context = context;
  }

  /**
   * Inserts the user without modifying password
   * @param name the name of the user
   * @param age the age of the user
   * @param address the address of the user
   * @param password the password for the account
   * @return long the id of the user inserted
   */
  public long insertPreviousUser(String name, int age, String address, String password) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("NAME", name);
    contentValues.put("AGE", age);
    contentValues.put("ADDRESS", address);

    long id = sqLiteDatabase.insert("USERS", null, contentValues);
    sqLiteDatabase.close();

    insertPassword(password, (int) id);
    return id;
  }

  private void insertPassword(String password, int userId) {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();

    contentValues.put("USERID", userId);
    contentValues.put("PASSWORD", password);
    sqLiteDatabase.insert("USERPW", null, contentValues);
    sqLiteDatabase.close();
  }
}
