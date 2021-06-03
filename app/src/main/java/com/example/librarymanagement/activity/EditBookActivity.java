package com.example.librarymanagement.activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;

public class EditBookActivity extends AppCompatActivity {
    private EditText b_name,b_author,summery;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book);

        b_name=findViewById(R.id.bookname);
        b_author=findViewById(R.id.author);
        summery=findViewById(R.id.summery);
    }
}
