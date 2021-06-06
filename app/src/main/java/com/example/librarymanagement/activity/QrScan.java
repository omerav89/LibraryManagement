package com.example.librarymanagement.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.Result;

import java.io.IOException;
import java.io.Serializable;

import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.*;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrScan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG ="hii" ;
    private ZXingScannerView mScannerView;
    private final static String SENDING_ACTIVITY="sending_activity";
    private String incoming_activity="";
    private Intent intent;
    enum Answer {YES,NO};
    private String barcode_res;
    private Book book = null;
    private int data_change=0;
    private boolean stop_loop=false;

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

    public void addCopy(){
        data_change=DataAccess.getInstance(this).addOneToCopyByBarcode(barcode_res,book.get_cnumber());
        if(data_change==1){
            Toast.makeText(this,"Add copy to the book "+book.get_bname()+
                    "current copy number: "+book.get_cnumber()+1,Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Couldn't add copy to the book "+book.get_bname()+
                    ", try again later",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleResult(Result rawResult) {

        boolean found=false;
        boolean book_copy=false;
        final boolean add_or_not;
        String res="";
        // get the result here
        barcode_res= rawResult.getText();

        switch (incoming_activity){
            case "add": book=DataAccess.getInstance(this).getBookByBarcode(barcode_res);
                if (book==null){
                    intent=new Intent(QrScan.this,AddBookActivity.class);
                    intent.putExtra(SENDING_ACTIVITY, (Serializable) book);
                    intent.putExtra("barcode",barcode_res);
                    found=true;
                    stop_loop=true;
                }
                else {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                    dialog.setTitle("Book already exist");
                    dialog.setMessage("Do you wish to add another copy?");
                    dialog.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addCopy();
                                }
                            });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.create().show();
                }
                break;
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

        // If you would like to resume scanning, call this method below:
      //  mScannerView.resumeCameraPreview(this);

        if(found){
            startActivity(intent);
        }
        else {
            intent = new Intent(QrScan.this,HomeActivity.class);
            startActivity(intent);
        }

    }
}


