package com.example.librarymanagement.model;

public class Borrower {

    private String _fname, _lname, _email, _pnumber;
    private int _id;

    public Borrower(int _id,String _fname, String _lname, String _email, String _pnumber) {
        this._id=_id;
        this._fname = _fname;
        this._lname = _lname;
        this._email = _email;
        this._pnumber = _pnumber;
    }

    public Borrower(String _fname, String _lname, String _pnumber) {
        this._fname = _fname;
        this._lname = _lname;
        this._pnumber = _pnumber;
        this._email ="";
    }

    public int get_id() {return _id;}

    public String get_fname() {
        return _fname;
    }

    public String get_lname() {
        return _lname;
    }

    public String get_email() {
        return _email;
    }

    public String get_pnumber() {
        return _pnumber;
    }

    @Override
    public String toString()
    {
        return "ID = " + get_id() + "; Name = " + get_fname() + " " + get_lname() +
                "; Email = " + get_email()+"; Phone Number = " + get_pnumber();
    }
}
