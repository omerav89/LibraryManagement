package com.example.librarymanagement.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;
import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.model.Book;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BorrowBookActivity extends AppCompatActivity {

    private EditText f_name,l_name,p_number,mail_address;
    private Book book=null;
    private TextView book_title;
    private Button btn_borrow;
    private final static String SENDING_RESULT="sending_result";
    private Gson gson= new Gson();
    private String book_data;
    private int store;
    private DatePickerDialog datePickerDialog;
    private Button take_date, return_date_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_book);
        initDatePicker();

        take_date = findViewById(R.id.take_date);
        return_date_button = findViewById(R.id.return_date_btn);
        f_name = findViewById(R.id.fname);
        l_name = findViewById(R.id.lname);
        p_number = findViewById(R.id.phonenum);
        mail_address = findViewById(R.id.mail_address);
        book_title = findViewById(R.id.book_title);
        btn_borrow = findViewById(R.id.btn_borrow);
        btn_borrow = findViewById(R.id.btn_borrow);

        take_date.setText(getTodaysDate());

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

        take_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
                store=1;
            }
        });


        return_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
                store=2;
            }
        });

        btn_borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f_name.getText().toString().matches("")||
                        l_name.getText().toString().matches("")||
                        p_number.getText().toString().matches("")){

                    Toast.makeText(BorrowBookActivity.this,"Make sure to fill first name, last name and phone number",Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        Date takeDate= new SimpleDateFormat("dd/MM/yyyy").parse(take_date.getText().toString());
                        Date returnDate= new SimpleDateFormat("dd/MM/yyyy").parse(return_date_button.getText().toString());

                        if(takeDate.after(returnDate)){
                            Toast.makeText(BorrowBookActivity.this,"take date must be after return date!!",Toast.LENGTH_SHORT).show();
                        }
                        else if (takeDate.compareTo(returnDate)==0){
                            Toast.makeText(BorrowBookActivity.this,"take date must be after return date!!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            long ok = DataAccess.getInstance(BorrowBookActivity.this).add_borrower(f_name.getText().toString(),
                                    l_name.getText().toString(),
                                    mail_address.getText().toString(),
                                    p_number.getText().toString());

                            if(ok>0){
                                Toast.makeText(BorrowBookActivity.this,"The book "+book.get_bname()+
                                        "was booked to "+ f_name.getText().toString()+" "+
                                        l_name.getText().toString()+" until "+
                                        returnDate.toString(),Toast.LENGTH_LONG);

                                Intent intent = new Intent(BorrowBookActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }




                }
            }
        });
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day+"/"+month+"/"+year;
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = day+"/"+month+"/"+year;
              if (store==1) {
                  take_date.setText(date);
              }
              else if (store==2)
              {
                  return_date_button.setText(date);
              }

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        return month + " " + day + " " + year;
    }



    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}

