package com.example.librarymanagement.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import com.google.gson.Gson;

import java.util.Calendar;

public class BorrowBookActivity extends AppCompatActivity {

    private EditText f_name,l_name,p_number,mail;
    private Book book=null;
    private TextView book_title;
    private Button btn_borrow;
    private final static String SENDING_RESULT="sending_result";
    private Gson gson= new Gson();
    private String book_data;
    private int store;
    private DatePickerDialog datePickerDialog;
    private Button dateButton,returndateButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_book);
        initDatePicker();
        dateButton = findViewById(R.id.borrow);
        returndateButton=findViewById(R.id.return_d);
        dateButton.setText(getTodaysDate());
        f_name = findViewById(R.id.fname);
        l_name = findViewById(R.id.lname);
        p_number = findViewById(R.id.phonenum);
        mail = findViewById(R.id.mailaddress);
        book_title = findViewById(R.id.book_title);
        btn_borrow = findViewById(R.id.btn_borrow);

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

    dateButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openDatePicker(v);
            store=1;
        }
    });
        returndateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
                store=2;
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
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
              if (store==1) {
                  dateButton.setText(date);
              }
              else if (store==2)
              {
                  returndateButton.setText(date);
              }

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

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

