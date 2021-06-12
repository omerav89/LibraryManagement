package com.example.librarymanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    private String _bname, _author, _description, _barcode;
    private int _cnumber;
    private int _id;



    public Book(int _id,String _barcode,String _bname, String _author, String _description, int _cnumber) {
        this._id=_id;
        this._barcode=_barcode;
        this._bname = _bname;
        this._author = _author;
        this._description = _description;
        this._cnumber = _cnumber;
    }

    public Book(int _id,String _barcode,String _bname, String _author, int _cnumber) {
        this._id=_id;
        this._barcode=_barcode;
        this._bname = _bname;
        this._author = _author;
        this._description ="";
        this._cnumber = _cnumber;
    }

    protected Book(Parcel in) {
        _bname = in.readString();
        _author = in.readString();
        _description = in.readString();
        _barcode = in.readString();
        _cnumber = in.readInt();
        _id = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int get_id() {return _id;}

    public String get_barcode() { return _barcode; }

    public String get_bname() {
        return _bname;
    }

    public void set_bname(String _bname) {
        this._bname = _bname;
    }

    public String get_author() {
        return _author;
    }

    public void set_author(String _author) {
        this._author = _author;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public int get_cnumber() {
        return _cnumber;
    }

    public void set_cnumber(int _cnumber) {
        this._cnumber = _cnumber;
    }

    @Override
    public String toString()
    {
        return "ID = " + get_id() +"; Barcode = "+ get_barcode() +"; Name = " + get_bname() +
                "; Author = " + get_author()+"; Description = " + get_description() +
                "; Copy = " + get_cnumber();
    }

    @Override
    public int describeContents() { return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_bname);
        dest.writeString(_author);
        dest.writeString(_description);
        dest.writeString(_barcode);
        dest.writeInt(_cnumber);
        dest.writeInt(_id);
    }
}
