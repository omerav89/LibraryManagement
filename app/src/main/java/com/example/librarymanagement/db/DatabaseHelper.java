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

        //create tables for thr data
        db.execSQL(BooksSchema.CREATE_TABLE_BOOKS);
        db.execSQL(BooksSchema.CREATE_TABLE_BORROWER);
        db.execSQL(BooksSchema.CREATE_TABLE_BORROWING);


        /**********************************ADD-BOOKS**************************************************/
        db.execSQL("INSERT INTO Books (barcode, bookName, author,description,copy) VALUES ('123456' ,'Harry Potter and the Deathly Hallows','J.K. Rowling','alot of magic',7)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('234561' ,'The Hunger Games','Suzanne Collins','you have to read this book',2)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('345612' ,'The Book Thief','Markus Zusak','really nice book, try it',1)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('456123' ,'The Time Travelers Wife','Audrey Niffenegger','must read it',1)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('561234' ,'The Kite Runner','Khaled Hosseini','beautifull story',3)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('612345' ,'The Help','Kathryn Stockett','help me alot in bad days',2)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('162534' ,'A Thousand Splendid Suns','Khaled Hosseini','',1)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('253416' ,'Water for Elephants','Sara Gruen','',4)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('253426' ,'Water for Elephants','Sara Gruen','',4)");
        db.execSQL("INSERT INTO Books ( barcode, bookName, author,description,copy) VALUES ('253416' ,'Water for Elephants','Sara Gruen','',4)");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BOOK);
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BORROWER);
        db.execSQL("DROP TABLE " + BooksSchema.TABLE_BORROWING);

        onCreate(db);
    }
}
