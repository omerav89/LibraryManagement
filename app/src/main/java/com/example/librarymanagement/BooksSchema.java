package com.example.librarymanagement;

public class BooksSchema {

    /*
    create books table
     */
    protected static String CREATE_TABLE_Books="CREATE TABLE Books (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " bookName TEXT NOT NULL," +
            " author TEXT NOT NULL," +
            " description TEXT,"+
            " copy INTEGER NOT NULL )";

    protected static String TABLE_Books="TABLE Books (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " bookName TEXT NOT NULL," +
            " author TEXT NOT NULL," +
            " description TEXT,"+
            " copy INTEGER NOT NULL )";

    /*
    get all books from db
     */
    protected static String GET_ALL_BOOKS="SELECT * FROM Books";


}
