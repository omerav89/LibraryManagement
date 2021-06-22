package com.example.librarymanagement.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.fragment.NoticeDialogFragment;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BorrowingBook;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.Serializable;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QrScan extends FragmentActivity implements ZXingScannerView.ResultHandler, NoticeDialogFragment.NoticeDialogListener{

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String TAG ="hii" ;
    private ZXingScannerView mScannerView;
    private final static String SENDING_ACTIVITY="sending_activity";
    private final static String SENDING_RESULT="sending_result";
    private String incoming_activity="";
    private Intent intent;
    enum Answer {YES,NO};
    private String barcode_res;
    private Book book = null;
    private int data_change=0;
    private boolean stop_loop=false;
    private Gson gson = new Gson();
    private String book_obj_as_json="";
    private BorrowingBook borrowingBook[] = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);// Set the scanner view as the content view
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
        if (!checkPermission()) {
            requestPermission();
        }

    }

    /**
     * chek if user gave camera permission
     * @return true if did else false
     */
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    /**
     * ask for camera permission
     */
    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    /**
     * check permission result
     * @param requestCode request code for camera
     * @param permissions permission for what
     * @param grantResults result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    /**
     * show message for asking permission
     * @param message content of the message
     * @param okListener listener for result
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(QrScan.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * show dialog box for asking if to add a book copy
     */
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NoticeDialogFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    /**
     * what to do if user touched the dialog's positive button
     * @param dialog the dialog box
     */
    public void onDialogPositiveClick(DialogFragment dialog) {
        addCopy();
        intent = new Intent(QrScan.this,HomeActivity.class);
        startActivity(intent);
    }

    /**
     * what to do if user touched the dialog's negative button
     * @param dialog the dialog box
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        intent = new Intent(QrScan.this,HomeActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    /**
     * add one copy to the current book
     */
    public void addCopy(){
        data_change=DataAccess.getInstance(this).addOneToCopyByBarcode(barcode_res,book.get_cnumber());
        int copy;
        if(data_change==1){
            copy=book.get_cnumber();
            copy+=1;
            Toast.makeText(this,"Add copy to the book "+book.get_bname()+
                    " current copy number: "+copy,Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Couldn't add copy to the book "+book.get_bname()+
                    ", try again later",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * handle the scan result
     * @param rawResult the scan result
     */
    @Override
    public void handleResult(Result rawResult) {

        // get the result here
        barcode_res= rawResult.getText();

        switch (incoming_activity){
            case "add": book=DataAccess.getInstance(this).getBookByBarcode(barcode_res);
                if (book==null){
                    intent=new Intent(QrScan.this,AddBookActivity.class);
                    intent.putExtra(SENDING_ACTIVITY, (Serializable) book);
                    intent.putExtra("barcode",barcode_res);
                    startActivity(intent);
                }
                else {
                    showNoticeDialog();
                }
                break;
            case "borrow": book=DataAccess.getInstance(this).getBookByBarcode(barcode_res);
                    book_obj_as_json = gson.toJson(book);
                    intent=new Intent(QrScan.this,BorrowBookActivity.class);
                    intent.putExtra(SENDING_RESULT,book_obj_as_json );
                    startActivity(intent);
                break;
            case "edit": book=DataAccess.getInstance(this).getBookByBarcode(barcode_res);
                    book_obj_as_json = gson.toJson(book);
                    intent=new Intent(QrScan.this,EditBookActivity.class);
                    intent.putExtra(SENDING_RESULT,book_obj_as_json );
                    startActivity(intent);
                break;
            case "remove":book = DataAccess.getInstance(this).getBookByBarcode(barcode_res);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Are you sure you want to delete the book: "+book.get_bname()+"?");
                dialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                /** check condition for action delete from db **/
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(book.get_cnumber()==1){
                            int delete=DataAccess.getInstance(QrScan.this).deleteBookById(book.get_id());
                            if(delete==1){
                                Toast.makeText(QrScan.this,"The book "+book.get_bname()+" was deleted from database",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(QrScan.this,"The book "+book.get_bname()+" was not deleted from database, try again later",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            int delete_copy = DataAccess.getInstance(QrScan.this).decreaseOneFromCopyByBarcode(book.get_barcode(),book.get_cnumber());
                            if(delete_copy==1){
                                Toast.makeText(QrScan.this,"One copy of the book "+book.get_bname()+" was deleted from database",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(QrScan.this,"Problem in deleting copy of "+book.get_bname()+" try again later",Toast.LENGTH_SHORT).show();
                            }
                        }
                        Intent intent=new Intent(QrScan.this,HomeActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();

                break;
        }

    }
}


