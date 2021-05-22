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

    /*
    for drop table
     */
    protected static String TABLE_Books="TABLE Books (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " bookName TEXT NOT NULL," +
            " author TEXT NOT NULL," +
            " description TEXT,"+
            " copy INTEGER NOT NULL )";


    /*
    create borrower table
     */
    protected static String CREATE_TABLE_Borrower="CREATE TABLE Borrower (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " firstName TEXT NOT NULL," +
            " lastName TEXT NOT NULL," +
            " phoneNumber TEXT NOT NULL,,"+
            " email TEXT )";

    /*
    for drop table
     */
    protected static String TABLE_Borrower="TABLE Borrower (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " firstName TEXT NOT NULL," +
            " lastName TEXT NOT NULL," +
            " phoneNumber TEXT NOT NULL,,"+
            " email TEXT )";

    /*
    get all books from db
     */
    protected static String GET_ALL_BOOKS="SELECT * FROM Books";

    /*
    get all books from db
    only one show foreach book!
     */
    protected static String GET_ALL_BOOKS_DISTINCT="SELECT DISTINCT bookName FROM Books";

    /*
    get all books from db by book name
     */
    protected static String GET_ALL_BOOKS_BY_NAME="SELECT * FROM Books WHERE bookName=";

    /*
    get all books from db by book author
     */
    protected static String GET_ALL_BOOKS_BY_AUTHOR="SELECT * FROM Books WHERE author=";

    /*
    get copy of book by book name
     */
    protected static String GET_BOOK_COPY="SELECT copy FROM Books WHERE bookName=";
}
