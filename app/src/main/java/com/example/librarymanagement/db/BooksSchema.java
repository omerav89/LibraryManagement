package com.example.librarymanagement.db;

public class BooksSchema {

    protected final static String databaseName = "LibraryDatabase.db";

    /**********************************BOOK********************************************************/

    public final static String TABLE_BOOK="Books";
    public final static String COLUMN_BOOK_ID="_id";
    public final static String COLUMN_BOOK_BARCODE="barcode";
    public final static String COLUMN_BOOK_NAME="bookName";
    public final static String COLUMN_BOOK_AUTHOR ="author";
    public final static String COLUMN_BOOK_DESCRIPTION ="description";
    public final static String COLUMN_BOOK_COPY ="copy";

    /*
       create books table
        */
    protected final static String CREATE_TABLE_BOOKS="CREATE TABLE Books (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " barcode TEXT NOT NULL,"+
            " bookName TEXT NOT NULL," +
            " author TEXT NOT NULL," +
            " description TEXT,"+
            " copy INTEGER NOT NULL )";

    /**********************************BORROWER***************************************************/


    public final static String TABLE_BORROWER="Borrower";
    public final static String COLUMN_BORROWER_ID="_id";
    public final static String COLUMN_BORROWER_FIRST_NAME="firstName";
    public final static String COLUMN_BORROWERR_LAST_NAME="lastName";
    public final static String COLUMN_BORROWER_EMAIL="email";
    public final static String COLUMN_BORROWER_PHONE_NUMBER="phoneNumber";
    public final static String COLUMN_BORROWER_ADDRESS="address";

    /*
  create borrower table
   */
    protected final static String CREATE_TABLE_BORROWER="CREATE TABLE Borrowers (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " firstName TEXT NOT NULL," +
            " lastName TEXT NOT NULL," +
            " email TEXT NOT NULL,"+
            " phoneNumber TEXT,"+
            "address TEXT NOT NULL)";


    /**********************************BORROWING**************************************************/

    public final static String TABLE_BORROWING="Borrowing";
    public final static String COLUMN_BORROWING_BOOK_ID="Books_id";
    public final static String COLUMN_BORROWING_BORROWERS_ID="Borrowers_id";
    public final static String COLUMN_BORROWING_TAKE_DATE="takeDate";
    public final static String COLUMN_BORROWING_RETURN_DATE="returnDate";

    /*
    create borrowing table
     */
    protected final static String CREATE_TABLE_BORROWING="CREATE TABLE Borrowing (" +
            " Books_id INT NOT NULL," +
            " Borrowers_id  INT NOT NULL," +
            " takeDate DATE NOT NULL,"+
            " returnDate DATE NOT NULL,"+
            "PRIMARY KEY (" + COLUMN_BORROWING_BOOK_ID + ","+ COLUMN_BORROWING_BORROWERS_ID + ","+ COLUMN_BORROWING_TAKE_DATE +")," +
            "FOREIGN KEY (" +COLUMN_BORROWING_BOOK_ID + ") REFERENCES " + TABLE_BOOK +","+
            "FOREIGN KEY (" +COLUMN_BORROWING_BORROWERS_ID + ") REFERENCES " +TABLE_BORROWER +")";



}
