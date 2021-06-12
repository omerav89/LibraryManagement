package com.example.librarymanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Borrower implements Parcelable {

    private String _fname, _lname, _email, _pnumber,_address;
    private int _id;

    public Borrower(int _id,String _fname, String _lname, String _email, String _pnumber,String _address) {
        this._id=_id;
        this._fname = _fname;
        this._lname = _lname;
        this._email = _email;
        this._pnumber = _pnumber;
        this._address=_address;
    }

    public Borrower(String _fname, String _lname, String _pnumber) {
        this._fname = _fname;
        this._lname = _lname;
        this._pnumber = _pnumber;
        this._email ="";
        this._address="";
    }

    protected Borrower(Parcel in) {
        _fname = in.readString();
        _lname = in.readString();
        _email = in.readString();
        _pnumber = in.readString();
        _address = in.readString();
        _id = in.readInt();
    }

    public static final Creator<Borrower> CREATOR = new Creator<Borrower>() {
        @Override
        public Borrower createFromParcel(Parcel in) {
            return new Borrower(in);
        }

        @Override
        public Borrower[] newArray(int size) {
            return new Borrower[size];
        }
    };

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

    public String get_address() { return _address; }

    @Override
    public String toString()
    {
        return "ID = " + get_id() + "; Name = " + get_fname() + " " + get_lname() +
                "; Email = " + get_email()+"; Phone Number = " + get_pnumber() + "; Address = " + get_address();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_fname);
        dest.writeString(_lname);
        dest.writeString(_email);
        dest.writeString(_pnumber);
        dest.writeString(_address);
        dest.writeInt(_id);
    }
}
