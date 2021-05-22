package com.example.librarymanagement;

public class BooksSchema {

    protected static String TABLE_BOOK="Books";
    protected static String TABLE_BORROWER="Borrower";
    protected static String TABLE_BORRWING="Borrowing";

    /*
    create books table
     */
    protected static String CREATE_TABLE_BOOKS="CREATE TABLE Books (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " bookName TEXT NOT NULL," +
            " author TEXT NOT NULL," +
            " description TEXT,"+
            " copy INTEGER NOT NULL )";

    /*
    create borrower table
     */
    protected static String CREATE_TABLE_BORROWER="CREATE TABLE Borrowers (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " firstName TEXT NOT NULL," +
            " lastName TEXT NOT NULL," +
            " email TEXT NOT NULL,,"+
            " phoneNumber TEXT )";

    /*
    create borrowing table
     */
    protected static String CREATE_TABLE_BORROWING="CREATE TABLE Borrowing (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " Books_id INT NOT NULL," +
            " Borrowers_id  INT NOT NULL," +
            " takeDate DATE NOT NULL,"+
            " returnDate DATE NOT NULL,"+
            "FOREIGN KEY (Book_id) REFERENCES Books(_id),"+
            "FOREIGN KEY (Borrowers_id) REFERENCES Borrowers(_id))";


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
