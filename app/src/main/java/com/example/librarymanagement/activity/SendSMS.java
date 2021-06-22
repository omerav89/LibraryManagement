package com.example.librarymanagement.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.BorrowingBook;
import com.google.gson.Gson;

public class SendSMS extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private String message;
    private TextView f_name,l_name,phone_number,smsText;
    private Button sendSms;
    private BorrowingBook borrowingBook;
    private Gson gson= new Gson();
    private String book_data;
    private final static String SENDING_RESULT="sending_result";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_sms);

        f_name = findViewById(R.id.fname);
        l_name = findViewById(R.id.lname);
        phone_number = findViewById(R.id.phone_number);
        smsText = findViewById(R.id.smsText);
        sendSms = findViewById(R.id.sendBtn);

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
            f_name.setText(borrowingBook.get_borrower().get_fname());
            l_name.setText(borrowingBook.get_borrower().get_lname());
            phone_number.setText(borrowingBook.get_borrower().get_pnumber());
        }

        message = "Hello dear "+ borrowingBook.get_borrower().get_fname()+" "+
                borrowingBook.get_borrower().get_lname()+ " you need to return the book "+
                borrowingBook.get_book().get_bname()+" tomorrow, \n" +
                "Thank you, regards";

        smsText.setText(message);

        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });
    }


/**check if have permission **/
    protected void sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }  else Send();


    }
/**check condition for sending the sms and if everythig ok show the sms send**/
private void Send(){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(this.phone_number.getText().toString(), null, smsText.getText().toString(), null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SendSMS.this,HomeActivity.class);
        startActivity(intent);
    }
/**check if the user confirm to send sms **/
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   Send();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}

