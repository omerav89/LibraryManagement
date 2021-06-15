package com.example.librarymanagement.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.service.NotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddBookActivity extends AppCompatActivity {

    private EditText b_name,b_author,summery;
    private Button add_btn;
    private String barcode;
    private Integer copy_number=1;
    private final static String SENDING_ACTIVITY="sending_activity";
    private Book incoming_book;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                incoming_book= null;
                barcode="";
            } else {
                incoming_book=(Book) extras.getSerializable(SENDING_ACTIVITY);
                barcode= extras.getString("barcode");
            }
        } else {
            incoming_book= (Book) savedInstanceState.getSerializable(SENDING_ACTIVITY);
            barcode = savedInstanceState.getString("barcode");
        }


        b_name=findViewById(R.id.book_name);
        b_author=findViewById(R.id.author);
        summery=findViewById(R.id.summery);
        add_btn=findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b_name.getText().toString().matches("")||b_author.getText().toString().matches("")){
                    Toast.makeText(AddBookActivity.this,"make sure to fill book name and author",Toast.LENGTH_SHORT).show();
                }
                else {
                   long bid=DataAccess.getInstance(AddBookActivity.this).add_book(barcode,b_name.getText().toString(),
                           b_author.getText().toString(),summery.getText().toString(),copy_number);

                   Toast.makeText(AddBookActivity.this,"The book "+b_name.getText().toString()+" added with id: "+bid,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AddBookActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }



}
