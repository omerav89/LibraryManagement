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

        /**********************************ADD-BOOKS**************************************************/

        db.execSQL("INSERT INTO Books (_id, barcode, bookName, author,description,copy) VALUES (1,'123456' ,'Harry Potter and the Deathly Hallows','J.K. Rowling','alot of magic',7)");
        db.execSQL("INSERT INTO Books (_id, barcode, bookName, author,description,copy) VALUES (2,'234561' ,'The Hunger Games','Suzanne Collins','you have to read this book',2)");
        db.execSQL("INSERT INTO Books (_id, barcode, bookName, author,description,copy) VALUES (3,'345612' ,'The Book Thief','Markus Zusak','really nice book, try it',1)");
        db.execSQL("INSERT INTO Books (_id, barcode, bookName, author,description,copy) VALUES (4,'456123' ,'The Time Travelers Wife','Audrey Niffenegger','must read it',1)");
        db.execSQL("INSERT INTO Books (_id, barcode, bookName, author,description,copy) VALUES (5,'561234' ,'The Kite Runner','Khaled Hosseini','beautifull story',3)");
        db.execSQL("INSERT INTO Books (_id, barcode, bookName, author,description,copy) VALUES (6,'612345' ,'The Help','Kathryn Stockett','help me alot in bad days',2)");
        db.execSQL("INSERT INTO Books (_id, barcode, bookName, author,description,copy) VALUES (7,'162534' ,'A Thousand Splendid Suns','Khaled Hosseini','',1)");
        db.execSQL("INSERT INTO Books (_id, barcode, bookName, author,description,copy) VALUES (8,'253416' ,'Water for Elephants','Sara Gruen','',4)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BOOK);
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BORROWER);
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BORROWING);

        onCreate(db);
    }
}
