package com.example.librarymanagement.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BorrowingBook;
import com.google.zxing.pdf417.PDF417Writer;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ReportActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private TableLayout mTableLayout;
    private ProgressDialog mProgressBar;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<BorrowingBook> borrowingBooks = new ArrayList<>();
    private int mTexrColorRed,firstBackgroung,secondBackgroung;
    private int mTextViewBorderWidth, mTableBorderWidth;
    private ImageButton share,download;
    private final String BOOK_FILE="bookList.pdf";


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
                shareToWhatsapp();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(!checkPermission()){
                    requestPermission();

                }
                else {
                    createPdf();
                }
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
                BorrowingBook[] borrowingBooksSearch = DataAccess.getInstance(this).getBorrowingBookByBookId(books.get(currentRow).get_id());

                if (borrowingBooksSearch != null && borrowingBooksSearch.length == books.get(currentRow).get_cnumber()){
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
        File file=new File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),BOOK_FILE);
        Uri uri = FileProvider.getUriForFile(this,"com.mydomain.fileprovider",file);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivity(share);
    }

/**create the pdf file whit thr table **/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPdf() {

        Document document = new Document(PageSize.A4, 10, 10, 10, 10);
        File file=new File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),BOOK_FILE);
        try {
            PdfWriter.getInstance(document,new FileOutputStream(file));

            document.open();

            document.addTitle("Book list of all my books");


            if(document.isOpen()) {
                PdfPTable table = new PdfPTable(4);

                PdfPCell col1 = new PdfPCell(new Phrase("Book Name"));
                table.addCell(col1);
                PdfPCell col2 = new PdfPCell(new Phrase("Author"));
                table.addCell(col2);
                PdfPCell col3 = new PdfPCell(new Phrase("Total #copy's"));
                table.addCell(col3);
                PdfPCell col4 = new PdfPCell(new Phrase(" #copy's in use"));
                table.addCell(col4);


                for (int index = 0; index < books.size(); index++) {

                    col1 = new PdfPCell(new Phrase(books.get(index).get_bname()));
                    table.addCell(col1);

                    col2 = new PdfPCell(new Phrase(books.get(index).get_author()));
                    table.addCell(col2);

                    col3 = new PdfPCell(new Phrase(String.valueOf(books.get(index).get_cnumber())));
                    table.addCell(col3);

                    BorrowingBook[] list = DataAccess.getInstance(this).getBorrowingBookByBookId(books.get(index).get_id());

                    if (list != null) {
                        col4 = new PdfPCell(new Phrase(String.valueOf(list.length)));
                        table.addCell(col4);
                    } else {
                        col4 = new PdfPCell(new Phrase("0"));
                        table.addCell(col4);
                    }
                }
                document.add(table);

            }
            else {
                Toast.makeText(this, "Problem in open file, try again later", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"file not found",Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            document.close();
            Toast.makeText(this, "Download completed, file in Documents folder", Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                    createPdf();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}


