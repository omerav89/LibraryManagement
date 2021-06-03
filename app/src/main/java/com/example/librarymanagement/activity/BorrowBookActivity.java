package com.example.librarymanagement.activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;

public class BorrowBookActivity extends AppCompatActivity {
 private EditText f_name,l_name,p_number,mail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_book);

        f_name=findViewById(R.id.fname);
        l_name=findViewById(R.id.lname);
        p_number=findViewById(R.id.phonenum);
        mail=findViewById(R.id.mailaddress);

    }
}
