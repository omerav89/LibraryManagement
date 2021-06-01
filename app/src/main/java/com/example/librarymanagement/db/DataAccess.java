package com.example.librarymanagement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.Borrower;
import com.example.librarymanagement.model.BorrowingBook;

import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;

public class DataAccess {
    private DatabaseHelper helper;
    private static DataAccess instance = null;
    //FirebaseDatabase storage;
    private DataAccess(Context c) {

        context = c;
        helper = new DatabaseHelper(context, BooksSchema.databaseName, null, 1);
        //storage = FirebaseDatabase.getInstance();
    }

    /**
     * Gets the singleton instance of the Sailors Data Access class
     * @param c The context in which the database works
     * @return The instance
     */
    public static DataAccess getInstance(Context c) {
        if (instance == null)
        {
            instance = new DataAccess(c);
        }
        return instance;
    }

    /**
     * The context for the database helper
     */
    Context context;
    /**
     * The helper object to help open and close
     */

    /**********************************BOOK********************************************************/

    /**
     *Adds a book to the database
     * @param book_name Book name
     * @param author Author name
     * @param description Book description
     * @param copy_number Book copy number
     * @return The row id of the new book, -1 if the insert failed
     */
    public long add_book(String book_name, String author, String description, int copy_number){
        SQLiteDatabase db =helper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(BooksSchema.COLUMN_BOOK_NAME,book_name);
        values.put(BooksSchema.COLUMN_BOOK_AUTHOR,author);
        values.put(BooksSchema.COLUMN_BOOK_DESCRIPTION,description);
        values.put(BooksSchema.COLUMN_BOOK_COPY,copy_number);

        return db.insert(BooksSchema.TABLE_BOOK,"null",values);
    }

    /**
     *Gets all of the books from the database
     * @return An cursor pointing to the books
     */
    public Cursor getAllBooks(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BOOK,
                new String[]{BooksSchema.COLUMN_BOOK_ID,
                        BooksSchema.COLUMN_BOOK_NAME,
                        BooksSchema.COLUMN_BOOK_AUTHOR,
                        BooksSchema.COLUMN_BOOK_DESCRIPTION},
                "",null, "", "", "");

        return cursor;
    }

    /**
     *Gets a book based on its ID
     * @param bid The book ID to look for
     * @return The book as a book object.  Null if no such sailor in the database
     */
    public Book getBookById(int bid) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BOOK,
                new String[]{BooksSchema.COLUMN_BOOK_ID,
                        BooksSchema.COLUMN_BOOK_NAME,
                        BooksSchema.COLUMN_BOOK_AUTHOR,
                        BooksSchema.COLUMN_BOOK_DESCRIPTION,
                        BooksSchema.COLUMN_BOOK_COPY},
                BooksSchema.COLUMN_BOOK_ID + "=?",
                new String[]{Integer.toString(bid)}, "", "", "");

        // check if there are results
        if (cursor.moveToFirst()) {
            Book s = new Book(
                    cursor.getInt(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_ID)),
                    cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_NAME)),
                    cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_COPY)));

            cursor.close();
            return s;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * Gets a book based on its ID
     * @param bid The book ID to look for
     * @return The book's data pointed to by a database cursor.  If there is no such book
     * a Cursor pointing to an empty table it returned.
     */
    public Cursor getBookByIDCursor(int bid) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BOOK,
                new String[]{BooksSchema.COLUMN_BOOK_ID,
                        BooksSchema.COLUMN_BOOK_NAME,
                        BooksSchema.COLUMN_BOOK_AUTHOR,
                        BooksSchema.COLUMN_BOOK_DESCRIPTION,
                        BooksSchema.COLUMN_BOOK_COPY},
                BooksSchema.COLUMN_BOOK_ID + "=?",
                new String[]{Integer.toString(bid)}, "", "", "");

        // that's the results
        return cursor;
    }

    /**
     *Gets a book based on its name
     * @param name The book name to look for
     * @return The books for the name in an array.  If there are not books for the name,
     * an empty array will be returned.
     */
    public Book[] getBookByName(String name) {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BOOK,
                new String[]{BooksSchema.COLUMN_BOOK_ID,
                        BooksSchema.COLUMN_BOOK_NAME,
                        BooksSchema.COLUMN_BOOK_AUTHOR,
                        BooksSchema.COLUMN_BOOK_DESCRIPTION,
                        BooksSchema.COLUMN_BOOK_COPY},
                BooksSchema.COLUMN_BOOK_NAME + "=?",
                new String[]{name}, "", "", "");

        Book[] books=new Book[cursor.getCount()];

        int i=0;

        while (cursor.moveToNext()){

            int _id = cursor.getInt(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_ID));
            String bname = cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_NAME));
            String author = cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_AUTHOR));
            String description = cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_DESCRIPTION));
            int copy = cursor.getInt(cursor.getColumnIndex(BooksSchema.COLUMN_BOOK_COPY));

            Book book=new Book(_id,bname,author,description,copy);
            books[i]=book;
            i++;

        }

        // close up shop and return
        cursor.close();
        return null;

    }


    /**
     * Deletes a Book based on its ID
     * @param bid The Book ID to delete
     * @return The number of rows deleted.  Will be 1 if the delete was successful and 0 if not.
     */
    public int deleteBookById (int bid){
        // do the deletion
        SQLiteDatabase db = helper.getWritableDatabase();
        // do the delete query
        return db.delete(BooksSchema.TABLE_BOOK, BooksSchema.COLUMN_BOOK_ID + "=?",
                new String[] {Integer.toString(bid)});

    }
    /**********************************BOOK END****************************************************/


     /**********************************BORROWER***************************************************/
    /**
     *Adds a borrower to the database
     * @param first_name borrower first name
     * @param last_name borrower last name
     * @param email borrower email
     * @param phone_number borrower phone number
     * @return The row id of the new borrower, -1 if the insert failed
     */
    public long add_borrower(String first_name, String last_name, String email, String phone_number){
        SQLiteDatabase db =helper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(BooksSchema.COLUMN_BORROWER_FIRST_NAME,first_name);
        values.put(BooksSchema.COLUMN_BORROWERR_LAST_NAME,last_name);
        values.put(BooksSchema.COLUMN_BORROWER_EMAIL,email);
        values.put(BooksSchema.COLUMN_BORROWER_PHONE_NUMBER,phone_number);

        return db.insert(BooksSchema.TABLE_BORROWER,"null",values);
    }

    /**
     *Gets all of the Borrowers from the database
     * @return An cursor pointing to the Borrowers
     */
    public Cursor getAllBorrowers(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BORROWER,
                new String[]{BooksSchema.COLUMN_BORROWER_ID,
                        BooksSchema.COLUMN_BORROWER_FIRST_NAME,
                        BooksSchema.COLUMN_BORROWERR_LAST_NAME,
                        BooksSchema.COLUMN_BORROWER_EMAIL,
                        BooksSchema.COLUMN_BORROWER_PHONE_NUMBER},
                "",null, "", "", "");

        return cursor;
    }

    /**
     *Gets a Borrower based on its ID
     * @param bid The Borrower ID to look for
     * @return The Borrower as a Borrower object.  Null if no such sailor in the database
     */
    public Borrower getBorrowersById(int bid) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BOOK,
                new String[]{BooksSchema.COLUMN_BORROWER_ID,
                        BooksSchema.COLUMN_BORROWER_FIRST_NAME,
                        BooksSchema.COLUMN_BORROWERR_LAST_NAME,
                        BooksSchema.COLUMN_BORROWER_EMAIL,
                        BooksSchema.COLUMN_BORROWER_PHONE_NUMBER},
                BooksSchema.COLUMN_BORROWER_ID + "=?",
                new String[]{Integer.toString(bid)}, "", "", "");

        // check if there are results
        if (cursor.moveToFirst()) {
            Borrower s = new Borrower(
                    cursor.getInt(cursor.getColumnIndex(BooksSchema.COLUMN_BORROWER_ID)),
                    cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BORROWER_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BORROWERR_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BORROWER_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BORROWER_PHONE_NUMBER)));

            cursor.close();
            return s;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * Gets a Borrower based on its ID
     * @param bid The Borrower ID to look for
     * @return The Borrower's data pointed to by a database cursor.  If there is no such Borrower
     * a Cursor pointing to an empty table it returned.
     */
    public Cursor getBorrowerByIDCursor(int bid) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BOOK,
                new String[]{BooksSchema.COLUMN_BORROWER_ID,
                        BooksSchema.COLUMN_BORROWER_FIRST_NAME,
                        BooksSchema.COLUMN_BORROWERR_LAST_NAME,
                        BooksSchema.COLUMN_BORROWER_EMAIL,
                        BooksSchema.COLUMN_BORROWER_PHONE_NUMBER},
                BooksSchema.COLUMN_BORROWER_ID + "=?",
                new String[]{Integer.toString(bid)}, "", "", "");

        // that's the results
        return cursor;
    }

    /**********************************BORROWER END************************************************/

     /**********************************BORROWING**************************************************/
    /**
     *Adds a borrowing to the database
     * @param bid book id
     * @param brid borrower id
     * @param tdate borrowing start date
     * @param rdate borrowing return date
     * @return The row id of the new borrowing, -1 if the insert failed
     */
    public long add_borrowing(int bid, int brid, Date tdate, Date rdate){
        SQLiteDatabase db =helper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(BooksSchema.COLUMN_BORROWING_BOOK_ID,bid);
        values.put(BooksSchema.COLUMN_BORROWING_BORROWERS_ID,brid);
        values.put(BooksSchema.COLUMN_BORROWING_TAKE_DATE,tdate.toString());
        values.put(BooksSchema.COLUMN_BORROWING_RETURN_DATE,rdate.toString());

        return db.insert(BooksSchema.TABLE_BORROWING,"null",values);
    }

    /**
     *Gets all of the Borrowings from the database
     * @return An cursor pointing to the Borrowings
     */
    public Cursor getAllBorrowings(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BORROWING,
                new String[]{BooksSchema.COLUMN_BORROWING_BOOK_ID,
                        BooksSchema.COLUMN_BORROWING_BORROWERS_ID,
                        BooksSchema.COLUMN_BORROWING_TAKE_DATE,
                        BooksSchema.COLUMN_BORROWING_RETURN_DATE},
                "",null, "", "", "");

        return cursor;
    }

    /**
     * Gets the BorrowingBook for a given book by its ID.
     * @param bid The book ID to look for
     * @return The BorrowingBook for the book in an array.  If there are not BorrowingBook for the book,
     * an empty array will be returned.  If the BorrowingBook is for a non-existent borrower, the BorrowingBook
     * is skipped.
     */
    public BorrowingBook[] getBorrowingBookByBookId (int bid) {

        // get the database results for the given book id
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(BooksSchema.TABLE_BOOK,
                new String[] {BooksSchema.COLUMN_BORROWING_BOOK_ID, BooksSchema.COLUMN_BORROWING_BORROWERS_ID,
                        BooksSchema.COLUMN_BORROWING_TAKE_DATE,BooksSchema.COLUMN_BORROWING_RETURN_DATE},
                BooksSchema.COLUMN_BORROWING_BOOK_ID + "=?",
                new String[] {Integer.toString(bid)}, "", "", "");


        // the BorrowingBook results
        BorrowingBook[] borrowingBook = new BorrowingBook[cursor.getCount()];
        Book theBook = getBookById(bid);
        // if the book doesn't exist, we're done
        if ( theBook == null)
        {
            return new BorrowingBook[0];
        }

        Hashtable<Integer, Borrower> borrower = new Hashtable<>();

        int i = 0;
        // see if there are results
        while (cursor.moveToNext())
        {
            // get the Borrower for the row if we don't have it already
            int borrowerId = cursor.getInt(cursor.getColumnIndex(BooksSchema.COLUMN_BORROWING_BORROWERS_ID));
            Borrower theBorrower = null;
            if (borrower.containsKey(borrowerId)) {
                theBorrower = getBorrowersById(borrowerId);
                borrower.put(borrowerId, theBorrower);
            }
            else
            {
                theBorrower = borrower.get(borrowerId);
            }

            // if the borrower doesn't exist, skip it
            if ( theBorrower == null)
            {
                continue;
            }

            // make the reservation
            BorrowingBook borrowingBooks = new BorrowingBook(theBook, theBorrower,
                    new Date (cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BORROWING_TAKE_DATE))),
                    new Date(cursor.getString(cursor.getColumnIndex(BooksSchema.COLUMN_BORROWING_RETURN_DATE))));
            borrowingBook[i] = borrowingBooks;
            i++;
        }

        // close up shop and return
        cursor.close();
        return borrowingBook;
    }

    /**
     * Deletes a Borrowing based on the Book,Borrower, take date, and return Date
     * @param bid The book id
     * @param brid The borrower id
     * @param tdate The take date
     * @param rdate The return Date
     * @return he number of rows deleted.  Will be 1 if the delete was successful and 0 if not.
     */
    public int deleteBorrowingBookById (int bid, int brid, String tdate,String rdate){
        // do the deletion
        SQLiteDatabase db = helper.getWritableDatabase();
        // do the delete query
        return db.delete(BooksSchema.TABLE_BORROWING, BooksSchema.COLUMN_BORROWING_BOOK_ID + " = ? "
                        + BooksSchema.COLUMN_BORROWING_BORROWERS_ID + " = ? " + BooksSchema.COLUMN_BORROWING_TAKE_DATE + " = ?"+
                        BooksSchema.COLUMN_BORROWING_RETURN_DATE + " = ? ",
                new String[] {Integer.toString(bid), Integer.toString(brid), tdate,rdate});

    }

    /**
     *
     * @param bid
     * @param brid
     * @param tdate
     * @param nrdate
     * @return
     */
    public int updateReturnDateByBookId(int bid,int brid, String tdate,String nrdate){
        // do the deletion
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(BooksSchema.COLUMN_BORROWING_RETURN_DATE,nrdate);

        // do the delete query
        return db.update(BooksSchema.TABLE_BORROWING,values,BooksSchema.COLUMN_BORROWING_BOOK_ID + " = ? ",
                new String[] {Integer.toString(bid), Integer.toString(brid), tdate,nrdate});
    }
    /**********************************BORROWING END***********************************************/


}