package com.example.librarymanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;

public class SearchBookActivity extends AppCompatActivity {

    private final static String SENDING_ACTIVITY="sending_activity";
    private String incoming_activity="";
    private Intent intent=null;
private EditText b_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);
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

        switch (incoming_activity){
            case "borrow": ;
            break;
            case "return": ;
            break;
            case "edit": ;
            break;
            case "status": ;
            break;
            case "remove": ;
            break;
        }
    }
}