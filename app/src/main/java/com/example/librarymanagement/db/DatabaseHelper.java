package com.example.librarymanagement.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BooksSchema.CREATE_TABLE_BOOKS);
        db.execSQL(BooksSchema.CREATE_TABLE_BORROWER);
        db.execSQL(BooksSchema.CREATE_TABLE_BORROWING);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BOOK);
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BORROWER);
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BORROWING);

        onCreate(db);
    }
}