package com.example.librarymanagement.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import com.google.gson.Gson;

public class BorrowBookActivity extends AppCompatActivity {

    private EditText f_name,l_name,p_number,mail;
    private Book book=null;
    private TextView book_title;
    private final static String SENDING_RESULT="sending_result";
    Gson gson= new Gson();
    String book_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_book);

        f_name=findViewById(R.id.fname);
        l_name=findViewById(R.id.lname);
        p_number=findViewById(R.id.phonenum);
        mail=findViewById(R.id.mailaddress);
        book_title=findViewById(R.id.book_title);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                book= null;
            } else {
                book_data = getIntent().getStringExtra(SENDING_RESULT);
                book = gson.fromJson(book_data,Book.class);
            }
        } else {
            book_data = getIntent().getStringExtra(SENDING_RESULT);
            book = gson.fromJson(book_data,Book.class);
        }

        if(book != null){
            book_title.setText(book.get_bname());
        }



    }
}
