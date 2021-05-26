package com.example.librarymanagement.db;

public class BooksSchema {

    protected final static String databaseName = "LibraryDatabase.db";

    protected static String TABLE_BOOK="Books";
    public final static String COLUMN_BOOK_ID="_id";
    public final static String COLUMN_BOOK_NAME="bookName";
    public final static String COLUMN_BOOK_AUTHOR ="author";
    public final static String COLUMN_BOOK_DESCRIPTION ="description";
    public final static String COLUMN_BOOK_COPY ="copy";

    /*
       create books table
        */
    protected static String CREATE_TABLE_BOOKS="CREATE TABLE Books (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " bookName TEXT NOT NULL," +
            " author TEXT NOT NULL," +
            " description TEXT,"+
            " copy INTEGER NOT NULL )";

    protected static String TABLE_BORROWER="Borrower";
    public final static String COLUMN_BORROWER_ID="_id";
    public final static String COLUMN_BORROWER_FIRST_NAME="firstName";
    public final static String COLUMN_BORROWERR_LAST_NAME="lastName";
    public final static String COLUMN_BORROWER_EMAIL="email";
    public final static String COLUMN_BORROWER_PHONE_NUMBER="phoneNumber";

    /*
  create borrower table
   */
    protected static String CREATE_TABLE_BORROWER="CREATE TABLE Borrowers (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " firstName TEXT NOT NULL," +
            " lastName TEXT NOT NULL," +
            " email TEXT NOT NULL,,"+
            " phoneNumber TEXT )";


    protected static String TABLE_BORROWING="Borrowing";
    public final static String COLUMN_BORROWING_BOOK_ID="Books_id";
    public final static String COLUMN_BORROWING_BORROWERS_ID="Borrowers_id";
    public final static String COLUMN_BORROWING_TAKE_DATE="takeDate";
    public final static String COLUMN_BORROWING_RETURN_DATE="returnDate";

    /*
    create borrowing table
     */
    protected static String CREATE_TABLE_BORROWING="CREATE TABLE Borrowing (" +
            " Books_id INT NOT NULL," +
            " Borrowers_id  INT NOT NULL," +
            " takeDate DATE NOT NULL,"+
            " returnDate DATE NOT NULL,"+
            "PRIMARY KEY (Books_id, Borrowers_id, takeDate)," +
            "FOREIGN KEY (Book_id) REFERENCES Books,"+
            "FOREIGN KEY (Borrowers_id) REFERENCES Borrowers)";


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
