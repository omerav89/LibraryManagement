package com.example.librarymanagement;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataAccess {
    private DatabaseHelper helper;

    /*
    insert new book to db
     */
    public long add_book(String book_name, String author, String description, int copy_number){
        SQLiteDatabase db =helper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("bookName",book_name);
        values.put("author",author);
        values.put("description",description);
        values.put("copy",copy_number);

        return db.insert(BooksSchema.TABLE_BOOKS,"null",values);
    }

    /*
    insert new borrower to db
     */
    public long add_borrower(String first_name, String last_name, String email, String phone_number){
        SQLiteDatabase db =helper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("firstName",first_name);
        values.put("lastName",last_name);
        values.put("email",email);
        values.put("phoneNumber",phone_number);

        return db.insert(BooksSchema.TABLE_BORROWER,"null",values);
    }

    public long add_borrowing(){
        SQLiteDatabase db =helper.getWritableDatabase();

        ContentValues values=new ContentValues();


        return db.insert(BooksSchema.TABLE_BORROWING,"null",values);
    }

    /*
    delete book from db by name and copy number
     */
    public boolean delete_book(String book_name,int copy){

        SQLiteDatabase db =helper.getWritableDatabase();

        return db.delete("Books","bookName="+book_name+" AND copy="+copy ,null)>0;
    }


}
