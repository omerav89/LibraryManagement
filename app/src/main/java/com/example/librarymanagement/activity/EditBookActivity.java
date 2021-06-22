package com.example.librarymanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.R;
import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.model.Book;
import com.google.gson.Gson;

public class EditBookActivity extends AppCompatActivity {

    private EditText b_author,summery;
    private TextView b_name;
    private Button save_btn,cancel_btn;
    private final static String SENDING_RESULT="sending_result";
    private Gson gson= new Gson();
    private String book_data;
    private Book book=null;
    private Intent intent=null;
    private String book_obj_as_json="";

    @Override

    /**
     * check the conditions for edit a book
     * **/
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book);

        b_name=findViewById(R.id.book_name);
        b_author=findViewById(R.id.author);
        summery=findViewById(R.id.summery);
        save_btn=findViewById(R.id.save_btn);
        cancel_btn=findViewById(R.id.cancel_btn);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                book= null;
            } else {
                book_data = getIntent().getStringExtra(SENDING_RESULT);
                book = gson.fromJson(book_data, Book.class);
            }
        } else {
            book_data = getIntent().getStringExtra(SENDING_RESULT);
            book = gson.fromJson(book_data,Book.class);
        }

        if(book != null){
            b_name.setText(book.get_bname());
            b_author.setText(book.get_author());
            summery.setText(book.get_description());
        }
/**check if author field its ok **/
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b_author.getText().toString().matches("")){
                    Toast.makeText(EditBookActivity.this,"Please fill author field",Toast.LENGTH_SHORT).show();
                }
                else {
                    updateBookData();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EditBookActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }
/**update the book **/
    public void updateBookData(){
        int result;
        result= DataAccess.getInstance(this).updateBookDataByName(b_name.getText().toString(),b_author.getText().toString(),summery.getText().toString());
        if(result==1){
            Toast.makeText(this,"The book "+book.get_bname()+" successfully updated",Toast.LENGTH_SHORT ).show();
        }
        else {
            Toast.makeText(this,"Problem in update book, try again later",Toast.LENGTH_SHORT ).show();
        }
        intent= new Intent(EditBookActivity.this,HomeActivity.class);
        startActivity(intent);

    }
}
