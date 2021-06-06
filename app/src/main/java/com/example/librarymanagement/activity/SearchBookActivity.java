package com.example.librarymanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.librarymanagement.db.DataAccess;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;

public class SearchBookActivity extends AppCompatActivity {

    private final static String SENDING_ACTIVITY="sending_activity";
    private String incoming_activity="";
    private Intent intent=null;
    private Button btn_qr;
private EditText b_name;
private ArrayList<Book>bookList= new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);
        btn_qr= findViewById(R.id.qrscan);
        b_name= findViewById(R.id.bookname);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                incoming_activity= null;
            } else {
                incoming_activity= extras.getString(SENDING_ACTIVITY);
            }
        } else {
            incoming_activity= (String) savedInstanceState.getSerializable(SENDING_ACTIVITY);
        }

        bookList=DataAccess.getInstance(this).getBookList();
       
    btn_qr.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent qrscan =new Intent(SearchBookActivity.this,QrScan.class);
            startActivity(qrscan);
        }
    });


    }
}