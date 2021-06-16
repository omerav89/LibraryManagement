package com.example.librarymanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Borrower implements Parcelable {

    private String _fname, _lname,  _pnumber;
    private int _id;

    public Borrower(int _id,String _fname, String _lname, String _pnumber) {
        this._id=_id;
        this._fname = _fname;
        this._lname = _lname;
        this._pnumber = _pnumber;

    }

    public Borrower(String _fname, String _lname, String _pnumber) {
        this._fname = _fname;
        this._lname = _lname;
        this._pnumber = _pnumber;


    }

    protected Borrower(Parcel in) {
        _fname = in.readString();
        _lname = in.readString();

        _pnumber = in.readString();

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



    public String get_pnumber() {
        return _pnumber;
    }



    @Override
    public String toString()
    {
        return "ID = " + get_id() + "; Name = " + get_fname() + " " + get_lname() + "; Phone Number = " + get_pnumber();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_fname);
        dest.writeString(_lname);

        dest.writeString(_pnumber);

        dest.writeInt(_id);
    }
}
