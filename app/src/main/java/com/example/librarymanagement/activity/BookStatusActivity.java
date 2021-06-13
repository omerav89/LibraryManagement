package com.example.librarymanagement.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.librarymanagement.R;
import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.model.BorrowingBook;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class BookStatusActivity extends AppCompatActivity {

    private TextView f_name,b_name,return_d;
    private BorrowingBook borrowingBook;
    private Gson gson= new Gson();
    private String book_data;
    private final static String SENDING_RESULT="sending_result";
    private DatePickerDialog datePickerDialog;
    private Button dateButton;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_status);
        initDatePicker();
        dateButton = findViewById(R.id.changeDate);

        f_name=findViewById(R.id.fullname);
        b_name=findViewById(R.id.book_name);
        return_d=findViewById(R.id.returndate);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                borrowingBook= null;
            } else {
                book_data = getIntent().getStringExtra(SENDING_RESULT);
                borrowingBook = gson.fromJson(book_data, BorrowingBook.class);
            }
        } else {
            book_data = getIntent().getStringExtra(SENDING_RESULT);
            borrowingBook = gson.fromJson(book_data,BorrowingBook.class);
        }
        if(borrowingBook != null){
            f_name.setText(borrowingBook.get_borrower().get_fname() + " "+ borrowingBook.get_borrower().get_lname());
            b_name.setText(borrowingBook.get_book().get_bname());
            return_d.setText(borrowingBook.get_rdate());
        }
    dateButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openDatePicker(v);
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
                date.replace(" ","-");
                String replasrDate =getTodaysDate();
                replasrDate.replace(" ","-");
                try {

                    Date date1= new SimpleDateFormat("dd/MM/yyyy hh:mm:ff").parse(date);
                    Date date2= new SimpleDateFormat("dd/MM/yyyy hh:mm:ff").parse(replasrDate);
                    if (date2.after(date1))
                    {
                        Toast.makeText(BookStatusActivity.this,"The Date is not Correct!",Toast.LENGTH_LONG).show();
                    }
                    else
                        {
                            int ok = DataAccess.getInstance(BookStatusActivity.this).updateReturnDateByBookId(borrowingBook.get_book().get_id(),date);
                            if(ok==1)
                            {
                                return_d.setText(date);
                                Toast.makeText(BookStatusActivity.this,"The Date Updated !",Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(BookStatusActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }else
                                {
                                Toast.makeText(BookStatusActivity.this,"The Date Not Updated Pleas try again later !",Toast.LENGTH_LONG).show();
                                }
                        }

                } catch (ParseException e) {
                    e.printStackTrace();
                }




            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
       // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return day+ "/" + month + "/" + year;
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}
