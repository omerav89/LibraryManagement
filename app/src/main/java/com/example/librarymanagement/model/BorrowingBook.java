package com.example.librarymanagement.model;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.Borrower;

import java.util.Date;

public class BorrowingBook {
    private Book _book;
    private Borrower _borrower;
    private Date _tdate,_rdate;

    public BorrowingBook(Book _book, Borrower _borrower, Date _tdate, Date _rdate) {
        this._book = _book;
        this._borrower = _borrower;
        this._tdate = _tdate;
        this._rdate = _rdate;
    }

    public Book get_book() {
        return _book;
    }

    public Borrower get_borrower() {
        return _borrower;
    }

    public Date get_tdate() {
        return _tdate;
    }

    public Date get_rdate() {
        return _rdate;
    }

    public void set_rdate(Date _rdate) {
        this._rdate = _rdate;
    }

    @Override
    public String toString()
    {
        return "Book ID = " + _book.get_id() + "; Borrower ID = " + _borrower.get_id() +
                ";Take Date = " + _tdate.toString()+"; Return Date = " + _rdate;
    }
}
