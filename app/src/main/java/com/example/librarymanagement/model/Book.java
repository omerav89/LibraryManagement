package com.example.librarymanagement.model;

public class Book {

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
}
