package com.example.librarymanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.librarymanagement.R;

public class HomeActivity extends AppCompatActivity {

    private ImageView borrow_b,add_b,edit_b,return_b,report,b_status,remove_b;
    private Intent intent;
    private final static String SENDING_ACTIVITY="sending_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        borrow_b =findViewById(R.id.calander);
        return_b=findViewById(R.id.returnbook);
        edit_b=findViewById(R.id.editbook);
        report=findViewById(R.id.report);
        b_status=findViewById(R.id.status);
        add_b=findViewById(R.id.addBook);
        remove_b=findViewById(R.id.remove);

        borrow_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, BorrowBookActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"borrow");
                startActivity(intent);
            }
        });

        return_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, SearchBookActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"return");
                startActivity(intent);
            }
        });

        edit_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this,EditBookActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"edit");
                startActivity(intent);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        b_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, BookStatusActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"status");
                startActivity(intent);
            }
        });

        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

        remove_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this,SearchBookActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"remove");
                startActivity(intent);
            }
        });
    }

}