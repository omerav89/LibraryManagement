package com.example.librarymanagement.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;

public class BookStatusActivity extends AppCompatActivity {
private TextView f_name,b_name,return_d;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_status);

        f_name=findViewById(R.id.fullname);
        b_name=findViewById(R.id.bookname);
        return_d=findViewById(R.id.returndate);
    }
}
