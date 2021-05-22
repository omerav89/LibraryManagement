package com.example.librarymanagement;

import java.util.Date;

public class BorrowingBook {
    private Book book;
    private Borrower borrower;
    private Date take_Date,return_date;

    public BorrowingBook(Book book, Borrower borrower, Date take_Date, Date return_date) {
        this.book = book;
        this.borrower = borrower;
        this.take_Date = take_Date;
        this.return_date = return_date;
    }

    public Book getBook() {
        return book;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public Date getTake_Date() {
        return take_Date;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }
}
