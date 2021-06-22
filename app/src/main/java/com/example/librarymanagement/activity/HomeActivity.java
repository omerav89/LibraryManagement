package com.example.librarymanagement.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
/**open the Borrow book activity**/
        borrow_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, SearchBookActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"borrow");
                startActivity(intent);
            }
        });
/**open the Return book activity**/
        return_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, SearchBookActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"return");
                startActivity(intent);
            }
        });
/**open the edit book activity**/
        edit_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this,SearchBookActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"edit");
                startActivity(intent);
            }
        });
/**open the Report activity**/
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

/**open the Book status activity**/
        b_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, SearchBookActivity.class);
                intent.putExtra(SENDING_ACTIVITY,"status");
                startActivity(intent);
            }
        });
/**open the Add book activity**/
        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(HomeActivity.this, QrScan.class);
                intent.putExtra(SENDING_ACTIVITY,"add");
                startActivity(intent);
            }
        });
/**open the Remove book activity**/
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