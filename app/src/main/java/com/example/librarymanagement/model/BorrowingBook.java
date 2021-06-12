package com.example.librarymanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.Borrower;

import java.util.Date;

public class BorrowingBook implements Parcelable {
    private Book _book;
    private Borrower _borrower;
    private String _tdate,_rdate;

    public BorrowingBook(Book _book, Borrower _borrower, String _tdate, String _rdate) {
        this._book = _book;
        this._borrower = _borrower;
        this._tdate = _tdate;
        this._rdate = _rdate;
    }

    protected BorrowingBook(Parcel in) {
        _book = in.readParcelable(Book.class.getClassLoader());
        _borrower = in.readParcelable(Borrower.class.getClassLoader());
        _tdate = in.readString();
        _rdate = in.readString();
    }

    public static final Creator<BorrowingBook> CREATOR = new Creator<BorrowingBook>() {
        @Override
        public BorrowingBook createFromParcel(Parcel in) {
            return new BorrowingBook(in);
        }

        @Override
        public BorrowingBook[] newArray(int size) {
            return new BorrowingBook[size];
        }
    };

    public Book get_book() {
        return _book;
    }

    public Borrower get_borrower() {
        return _borrower;
    }

    public String get_tdate() {
        return _tdate;
    }

    public String get_rdate() {
        return _rdate;
    }

    public void set_rdate(String _rdate) {
        this._rdate = _rdate;
    }

    @Override
    public String toString()
    {
        return "Book ID = " + _book.get_id() + "; Borrower ID = " + _borrower.get_id() +
                ";Take Date = " + _tdate.toString()+"; Return Date = " + _rdate.toString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(_book, flags);
        dest.writeParcelable(_borrower, flags);
        dest.writeString(_tdate);
        dest.writeString(_rdate);
    }
}
