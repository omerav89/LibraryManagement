package com.example.librarymanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BorrowingBook;
import com.google.zxing.pdf417.PDF417Writer;

import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private TableLayout mTableLayout;
    private ProgressDialog mProgressBar;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<BorrowingBook> borrowingBooks = new ArrayList<>();
    private int mTexrColorRed,firstBackgroung,secondBackgroung;
    private int mTextViewBorderWidth, mTableBorderWidth;
    private ImageButton share,download;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        books = DataAccess.getInstance(this).getBookList();
        borrowingBooks = DataAccess.getInstance(this).getAllBorrowingsList();

        share = findViewById(R.id.share);
        download = findViewById(R.id.download);

        mTexrColorRed = Color.RED;
        firstBackgroung = Color.LTGRAY;
        secondBackgroung = Color.WHITE;
        setTable();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setTable() {
        TextView textViewBookName,textViewBookAuthor,textViewBookCopy;
        mTableLayout = findViewById(R.id.tableLayout);

        mTableLayout.setStretchAllColumns(true);
      // mTableLayout.bringToFront();

        for (int currentRow = 0; currentRow < books.size(); currentRow++) {

            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item,null,false);

            textViewBookName = tableRow.findViewById(R.id.book_col);
            textViewBookName.setText(books.get(currentRow).get_bname());

            textViewBookName.setPadding(0, 6, 0, 6);

            textViewBookAuthor = tableRow.findViewById(R.id.author_col);
            textViewBookAuthor.setText(books.get(currentRow).get_author());

            textViewBookAuthor.setPadding(0, 6, 0, 6);

            textViewBookCopy = tableRow.findViewById(R.id.copy_col);
            textViewBookCopy.setText(String.valueOf(books.get(currentRow).get_cnumber()));

            textViewBookCopy.setPadding(0, 6, 0, 6);

            if(this.borrowingBooks!=null){
                int counter = 0;
                for(int i=0;i<borrowingBooks.size();i++){
                    if(borrowingBooks.get(i).get_book().get_bname().matches(books.get(currentRow).get_bname())){
                        counter++;
                    }
                }
                if (counter == books.get(currentRow).get_cnumber()){
                    textViewBookAuthor.setTextColor(mTexrColorRed);
                    textViewBookCopy.setTextColor(mTexrColorRed);
                    textViewBookName.setTextColor(mTexrColorRed);
                }
            }
            if(currentRow%2==0){
                textViewBookAuthor.setBackgroundColor(firstBackgroung);
                textViewBookCopy.setBackgroundColor(firstBackgroung);
                textViewBookName.setBackgroundColor(firstBackgroung);
            }
            else {
                textViewBookAuthor.setBackgroundColor(secondBackgroung);
                textViewBookCopy.setBackgroundColor(secondBackgroung);
                textViewBookName.setBackgroundColor(secondBackgroung);
            }

            mTableLayout.addView(tableRow);
        }
    }

    public void shareToWhatsapp(){
        File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), "example.pdf");
        Uri uri = Uri.fromFile(outputFile);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setPackage("com.whatsapp");

        startActivity(share);
    }

    public void createPdf(){

    }


}
