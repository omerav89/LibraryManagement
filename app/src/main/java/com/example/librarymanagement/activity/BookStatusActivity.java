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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.librarymanagement.R;
import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.model.BorrowingBook;
import com.example.librarymanagement.service.NotificationReceiver;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.FLAG_NO_CREATE;


public class BookStatusActivity extends AppCompatActivity {

    private TextView f_name,b_name,return_d;
    private BorrowingBook borrowingBook;
    private Gson gson= new Gson();
    private String book_data;
    private final static String SENDING_RESULT="sending_result";
    private DatePickerDialog datePickerDialog;
    private Button dateButton,send_sms_btn;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    final Calendar myCalendar = Calendar. getInstance () ;
/**check condition and send the sms **/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_status);
        initDatePicker();

        dateButton = findViewById(R.id.changeDate);
        f_name = findViewById(R.id.fullname);
        b_name = findViewById(R.id.book_name);
        return_d = findViewById(R.id.returndate);
        send_sms_btn = findViewById(R.id.send_sms_btn);

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

        send_sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String borrowing_obj_to_json = gson.toJson(borrowingBook);
                Intent intent = new Intent(BookStatusActivity.this,SendSMS.class);
                intent.putExtra(SENDING_RESULT,borrowing_obj_to_json);
                startActivity(intent);
            }
        });
    }
/**
 * check the date and show toast  For appropriate action
 * */
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;

                String newReturnDate = makeDateString(day, month, year);
                newReturnDate.replace(" ","/");
                String oldTakeDate =borrowingBook.get_tdate();
                try {

                    Date newDate= new SimpleDateFormat("dd/MM/yyyy").parse(newReturnDate);
                    Date oldDate= new SimpleDateFormat("dd/MM/yyyy").parse(oldTakeDate);
                    if (oldDate.after(newDate))
                    {
                        Toast.makeText(BookStatusActivity.this,"The Date is not Correct!",Toast.LENGTH_LONG).show();
                    }
                    else
                        {
                            //stopAlarm(BookStatusActivity.this);

                            int ok = DataAccess.getInstance(BookStatusActivity.this).updateReturnDateByBookId(borrowingBook.get_book().get_id(),newReturnDate);
                            if(ok==1)
                            {
                                setDate();
                                return_d.setText(newReturnDate);
                                Toast.makeText(BookStatusActivity.this,"The Date Updated !",Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(BookStatusActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(BookStatusActivity.this, "The Date Not Updated Pleas try again later !", Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(BookStatusActivity.this,HomeActivity.class);
                                startActivity(intent);
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
/**make the date to string **/
    private String makeDateString(int day, int month, int year)
    {
        return day+ "/" + month + "/" + year;
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
/**check condition and stop the alarm **/
    public void stopAlarm(Context context) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent your_intent = new Intent(this, NotificationReceiver.class);
            PendingIntent your_pending_intent = PendingIntent.getService(context, (int) borrowingBook.get_id(), your_intent, FLAG_NO_CREATE);
            if(your_pending_intent!=null && alarmManager!=null){
                alarmManager.cancel(your_pending_intent);
            }
    }

    private void scheduleNotification (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, NotificationReceiver. class ) ;
        notificationIntent.putExtra(NotificationReceiver. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationReceiver. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this,(int) borrowingBook.get_id(), notificationIntent , 0 ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        long futureInMillis = SystemClock.elapsedRealtime()+delay;

        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
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

    /**set the date and **/
    public void setDate () {
        try {
            String []splitDate = dateButton.getText().toString().split("/");
            String now=getTodaysDate();
            Date current =  new SimpleDateFormat("dd/MM/yyyy").parse(now);

            myCalendar.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
            myCalendar.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
            myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
            myCalendar.add(Calendar.DAY_OF_MONTH,-1);

            Date date = myCalendar .getTime() ;

            long diff=date.getTime()-current.getTime();

            scheduleNotification(getNotification(
                    borrowingBook.get_borrower().get_fname()+" "+borrowingBook.get_borrower().get_lname()+
                            " need to return the book: "+
                            borrowingBook.get_book().get_bname()+" at: "+ dateButton.getText().toString()
            ), diff) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
/**get today date**/
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day+"/"+month+"/"+year;
    }

}
