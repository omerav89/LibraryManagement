package com.example.librarymanagement.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.librarymanagement.R;
import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.Borrower;
import com.example.librarymanagement.service.NotificationReceiver;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BorrowBookActivity extends AppCompatActivity {

    private EditText f_name,l_name,p_number;
    private Book book=null;
    private TextView book_title, take_date;
    private Button btn_borrow;
    private final static String SENDING_RESULT="sending_result";
    private Gson gson= new Gson();
    private String book_data;
    private String return_book_str="";
    private DatePickerDialog datePickerDialog;
    private Button return_date_button;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    final Calendar myCalendar = Calendar. getInstance () ;
    private long ok_adding_borrowing;
    private boolean date_is_good=false;

    /**
     * for borrow the book need to check if something is missed like
     * phone num or first name and show msg if something wrong
     * @param savedInstanceState
     */
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

        book_title = findViewById(R.id.book_title);
        btn_borrow = findViewById(R.id.btn_borrow);
        btn_borrow = findViewById(R.id.btn_borrow);

        return_book_str=return_date_button.getText().toString();

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

        return_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });


        btn_borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long ok_adding_borrower = 0;

                if (f_name.getText().toString().matches("") ||
                        l_name.getText().toString().matches("") ||
                        p_number.getText().toString().matches("")||
                        !date_is_good) {

                    Toast.makeText(BorrowBookActivity.this, "Make sure to fill first name, last name , phone number and return date", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (p_number.getText().toString().length() < 10) {
                        Toast.makeText(BorrowBookActivity.this, "please enter vaild phone number", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Borrower if_exists = DataAccess.getInstance(BorrowBookActivity.this).getBorrowerByNamePhone(
                                f_name.getText().toString(),
                                l_name.getText().toString(),
                                p_number.getText().toString()
                        );
                        if (if_exists == null) {
                            ok_adding_borrower = DataAccess.getInstance(BorrowBookActivity.this).add_borrower(f_name.getText().toString(),
                                    l_name.getText().toString(),
                                    p_number.getText().toString());
                        }
                        ok_adding_borrowing = DataAccess.getInstance(BorrowBookActivity.this).add_borrowing(book.get_id(),
                                (int) ok_adding_borrower, take_date.getText().toString(), return_date_button.getText().toString());

                        if (ok_adding_borrowing > 0) {

                            setDate(v);
                            Toast.makeText(BorrowBookActivity.this, "The book " + book.get_bname() +
                                            " was booked to " + f_name.getText().toString() + " " +
                                            l_name.getText().toString() + " until " +
                                            return_date_button.getText().toString()
                                            ,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BorrowBookActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(BorrowBookActivity.this, "problem in DB try again later", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BorrowBookActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    /**
     * get the date for today
      * @return string of current date
     */
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day+"/"+month+"/"+year;
    }

    /**
     * check condition for change date for return book
     */
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = day+"/"+month+"/"+year;
                try {
                    Date takeDate = new SimpleDateFormat("dd/MM/yyyy").parse(take_date.getText().toString());
                    Date returnDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                    if (takeDate.after(returnDate)) {
                        Toast.makeText(BorrowBookActivity.this, "take date must be after return date!!", Toast.LENGTH_SHORT).show();
                        return_date_button.setText(return_book_str);
                    } else if (takeDate.compareTo(returnDate) == 0) {
                        Toast.makeText(BorrowBookActivity.this, "take date must be after return date!!", Toast.LENGTH_SHORT).show();
                        return_date_button.setText(return_book_str);
                    } else {
                        date_is_good=true;
                        return_date_button.setText(date);
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
    }


    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    private void scheduleNotification (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, NotificationReceiver. class ) ;
        notificationIntent.putExtra(NotificationReceiver. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationReceiver. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, (int) ok_adding_borrowing, notificationIntent , 0 ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        long futureInMillis = SystemClock.elapsedRealtime()+delay;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP ,futureInMillis, pendingIntent); ;
    }


    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Book Deadline Notification" ) ;
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setSmallIcon(R.drawable.notification_book ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    /**
     * set date for return book notification
     * @param view
     */
    public void setDate (View view) {
        try {
            String []splitDate = return_date_button.getText().toString().split("/");
            String now=getTodaysDate();

            Date current =  new SimpleDateFormat("dd/MM/yyyy").parse(now);


            int day=Integer.parseInt(splitDate[0])-1;
            String date1= day+"/"+splitDate[1]+"/"+splitDate[2];
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(date1);


            long diff=Math.abs(date.getTime()-current.getTime());
            long diffInDays =  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            long result_in_millis = diffInDays*24*60*60*1000;
            
            scheduleNotification(getNotification(
                    f_name.getText().toString()+" "+l_name.getText().toString()+
                            " need to return the book: "+
                            book_title.getText().toString()+" at: "+ return_date_button.getText().toString()
            ), result_in_millis) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

