package com.example.librarymanagement.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.librarymanagement.Adapter.AdapterSearch;
import com.example.librarymanagement.db.DataAccess;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import java.util.ArrayList;
import java.util.List;

import com.example.librarymanagement.db.DataAccess;

public class SearchBookActivity extends AppCompatActivity implements Filterable {
    private AdapterSearch adapter;
    private final static String SENDING_ACTIVITY="sending_activity";
    private String incoming_activity="";
    private Intent intent=null;
    private Button btn_qr;
    private EditText b_name;
    private ArrayList<Book>bookList= new ArrayList<>();
    private ArrayList<Book>bookListFull= new ArrayList<>();
    private Cursor cursor;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);
        btn_qr= findViewById(R.id.qrscan);
        b_name= findViewById(R.id.bookname);
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

    btn_qr.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent qrscan =new Intent(SearchBookActivity.this,QrScan.class);
            qrscan.putExtra(SENDING_ACTIVITY,incoming_activity);
            startActivity(qrscan);
        }
    });


    switch (incoming_activity)
    {
        case "borrow": bookList=DataAccess.getInstance(this).getBookList();
        break;
    }

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        if(bookList==null || bookList.size()==0){
            Toast.makeText(this,"There is no books in DB",Toast.LENGTH_SHORT).show();
        }
        else {
            adapter = new AdapterSearch(bookList);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    private final Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredBook = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredBook.addAll(bookListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Book item : bookListFull) {
                    if (item.get_bname().toLowerCase().contains(filterPattern)) {
                        filteredBook.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredBook;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bookList.clear();
            bookList.addAll((List) results.values);
            //to do refresh!
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.searchmen, menu);

        MenuItem searchItem= menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}