package com.example.librarymanagement.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.db.DataAccess;
import com.example.librarymanagement.R;

public class AddBookActivity extends AppCompatActivity {

    private EditText b_name,b_author,summery;
    private Button add_btn;
    private String barcode="234234";
    private Integer copy_number=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);

        b_name=findViewById(R.id.bookname);
        b_author=findViewById(R.id.author);
        summery=findViewById(R.id.summery);
        add_btn=findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b_name.getText().toString().matches("")||b_author.getText().toString().matches("")){
                    Toast.makeText(AddBookActivity.this,"make sure to fill book name and author",Toast.LENGTH_SHORT).show();
                }
                else {
                   long bid=DataAccess.getInstance(AddBookActivity.this).add_book(barcode,b_name.getText().toString(),
                           b_author.getText().toString(),summery.getText().toString(),copy_number);

                   Toast.makeText(AddBookActivity.this,"The book "+b_name.getText().toString()+" added with id: "+bid,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
