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

import com.example.librarymanagement.Adapter.AdapterSearchBook;
import com.example.librarymanagement.Adapter.AdapterSearchBorrowingBook;
import com.example.librarymanagement.db.DataAccess;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;

import java.util.ArrayList;
import java.util.List;

import com.example.librarymanagement.model.BorrowingBook;
import com.google.gson.Gson;

public class SearchBookActivity extends AppCompatActivity implements Filterable {

    private AdapterSearchBook bookAdapter;
    private AdapterSearchBorrowingBook borrowingAdapter;
    private final static String SENDING_ACTIVITY="sending_activity";
    private final static String SENDING_RESULT="sending_result";
    private String incoming_activity="";
    private Intent intent=null;
    private Button btn_qr;
    private EditText b_name;
    private ArrayList<Book>bookList= new ArrayList<>();
    private ArrayList<Book>bookListFull= new ArrayList<>();
    private ArrayList <BorrowingBook> borrowingBookList = new ArrayList<>();
    private ArrayList <BorrowingBook> borrowingBookFullList = new ArrayList<>();
    private Cursor cursor;
    private Gson gson = new Gson();
    private String book_obj_as_json="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);
        btn_qr= findViewById(R.id.qrscan);
        b_name= findViewById(R.id.book_name);
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
        case "status":
        case "return": borrowingBookList = DataAccess.getInstance(this).getAllBorrwingsList();
            bookList = DataAccess.getInstance(this).getBookList();
            break;
        case "edit":
        case "borrow":
        case "remove":
            bookList = DataAccess.getInstance(this).getBookList();
            break;
    }

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        boolean ok=false;
        int adapter=0;
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        switch (incoming_activity){
            case "status":
            case "return":
                if(borrowingBookList==null || borrowingBookList.size()==0){
                    Toast.makeText(this,"There is no borrowing in DB",Toast.LENGTH_SHORT).show();
                }
                else {
                    ok=true;
                    adapter=2;
                }
                break;
            case "edit":
            case "borrow":
            case "remove":
                if(bookList==null || bookList.size()==0){
                    Toast.makeText(this,"There is no borrowing in DB",Toast.LENGTH_SHORT).show();
                }
                else {
                    ok=true;
                    adapter=1;
                }
                break;
        }


        if(ok && adapter==1 ) {
            bookAdapter = new AdapterSearchBook(bookList);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(bookAdapter);

            bookAdapter.setOnItemClickListener(new AdapterSearchBook.OnItemClickListener() {
                @Override
                public void OnItemClick(int position) {

                    Intent intent = null;

                    switch (incoming_activity){
                        case "borrow":
                            book_obj_as_json = gson.toJson(bookList.get(position));
                            intent=new Intent(SearchBookActivity.this,BorrowBookActivity.class);
                            intent.putExtra(SENDING_RESULT,book_obj_as_json );
                            startActivity(intent);
                            break;
                        case "edit":
                            book_obj_as_json = gson.toJson(bookList.get(position));
                            intent=new Intent(SearchBookActivity.this,EditBookActivity.class);
                            intent.putExtra(SENDING_RESULT,book_obj_as_json );
                            startActivity(intent);
                            break;
                        case "remove": ;
                            break;
                    }

                }
            });
        }

        else if(ok && adapter==2 ) {
            borrowingAdapter = new AdapterSearchBorrowingBook(borrowingBookList);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(borrowingAdapter);

            borrowingAdapter.setOnItemClickListener(new AdapterSearchBook.OnItemClickListener() {
                @Override
                public void OnItemClick(int position) {

                    Intent intent = null;

                    switch (incoming_activity){
                        case "return": ;
                            break;
                        case "status":book_obj_as_json = gson.toJson(bookList.get(position));
                            intent=new Intent(SearchBookActivity.this,BookStatusActivity.class);
                            intent.putExtra(SENDING_RESULT,book_obj_as_json );
                            startActivity(intent);
                            break;
                    }

                }
            });
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
                bookAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}