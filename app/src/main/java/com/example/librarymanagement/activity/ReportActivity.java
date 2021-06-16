package com.example.librarymanagement.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BorrowingBook;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private TableLayout mTableLayout;
    private ProgressDialog mProgressBar;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<BorrowingBook> borrowingBooks = new ArrayList<>();
    private int mTextColor, mBorderColor,mTexrColorRed;
    private int mTextViewBorderWidth, mTableBorderWidth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        books = DataAccess.getInstance(this).getBookList();
        borrowingBooks = DataAccess.getInstance(this).getAllBorrowingsList();


        setTable();

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


            textViewBookCopy = tableRow.findViewById(R.id.copy_col);
            textViewBookCopy.setText(String.valueOf(books.get(currentRow).get_cnumber()));


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

            mTableLayout.addView(tableRow);
        }
    }


}
