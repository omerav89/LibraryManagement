package com.example.librarymanagement.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.librarymanagement.Adapter.AdapterSearchBook;
import com.example.librarymanagement.Adapter.AdapterSearchBorrowingBook;
import com.example.librarymanagement.db.DataAccess;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.librarymanagement.model.BorrowingBook;
import com.example.librarymanagement.service.NotificationReceiver;
import com.google.gson.Gson;




public class SearchBookActivity extends AppCompatActivity  {

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
    private ArrayList<Book> unfilterd_books = new ArrayList<>();
    private ArrayList <BorrowingBook> borrowingBookList = new ArrayList<>();
    private ArrayList <BorrowingBook> borrowingBookFullList = new ArrayList<>();
    private Cursor cursor;
    private Gson gson = new Gson();
    private String book_obj_as_json="";
    private int current_position=-1;


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


        if(incoming_activity.matches("status")){
            btn_qr.setVisibility(View.INVISIBLE);
        }
        else if(incoming_activity.matches("return")){
            btn_qr.setVisibility(View.INVISIBLE);
        }
        else {
            btn_qr.setVisibility(View.VISIBLE);
        }


        switch (incoming_activity)
        {
            case "status":
            case "return": borrowingBookList = DataAccess.getInstance(this).getAllBorrowingsList();
                bookList = DataAccess.getInstance(this).getBookList();
                break;
            case "borrow": unfilterd_books = DataAccess.getInstance(this).getBookList();
                borrowingBookList = DataAccess.getInstance(this).getAllBorrowingsList();
                setBookIlst(unfilterd_books);
                break;
            case "edit":
            case "remove":
                bookList = DataAccess.getInstance(this).getBookList();
                break;
        }

        setUpRecyclerView();

    }

    private void setBookIlst(ArrayList<Book> unfilterd_books) {
        Book book;
        BorrowingBook[] books;

        if(borrowingBookList.size()==0){
            bookList=unfilterd_books;
        }else {
            for (int i=0;i<unfilterd_books.size();i++){
                book = unfilterd_books.get(i);
                books = DataAccess.getInstance(SearchBookActivity.this).getBorrowingBookByBookId(book.get_id());
                if(book.get_cnumber()>books.length){
                    bookList.add(book);
                }
            }
        }
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

                    current_position=position;
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
                        case "remove":Book book = DataAccess.getInstance(SearchBookActivity.this).getBookById(bookList.get(position).get_id());
                            BorrowingBook []borrowingBook = DataAccess.getInstance(SearchBookActivity.this).getBorrowingBookByBookId(book.get_id());
                            boolean ok=false;
                            if(borrowingBook.length!=0){
                                if(borrowingBook.length==book.get_cnumber()){
                                    Toast.makeText(SearchBookActivity.this,"Can`t delete book, all copy`s are in borrowed status",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    ok=true;
                                }
                            }
                            else {
                                ok=true;
                            }
                            if(ok){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(SearchBookActivity.this);
                                dialog.setMessage("Are you sure you want to delete the book: "+book.get_bname()+"?");
                                dialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(book.get_cnumber()==1){
                                            int delete=DataAccess.getInstance(SearchBookActivity.this).deleteBookById(book.get_id());
                                            if(delete==1){
                                                Toast.makeText(SearchBookActivity.this,"The book "+book.get_bname()+" was deleted from database",Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(SearchBookActivity.this,"The book "+book.get_bname()+" was not deleted from database, try again later",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            int delete_copy = DataAccess.getInstance(SearchBookActivity.this).decreaseOneFromCopyByBarcode(book.get_barcode(),book.get_cnumber());
                                            if(delete_copy==1){
                                                Toast.makeText(SearchBookActivity.this,"One copy of the book "+book.get_bname()+" was deleted from database",Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(SearchBookActivity.this,"Problem in deleting copy of "+book.get_bname()+" try again later",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        Intent intent=new Intent(SearchBookActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                dialog.show();
                            }
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
                        case "return":
                            try {
                                Date check_date = getCheckDate(borrowingBookList.get(position).get_rdate());
                                Date tooday = new SimpleDateFormat("dd/MM/yyyy").parse(getTodaysDate());

                                if(check_date.after(tooday)){
                                    //stopAlarm(SearchBookActivity.this);
                                }
                                AlertDialog.Builder dialog = new AlertDialog.Builder(SearchBookActivity.this);
                                dialog.setMessage("Do you wish to end this book borrow?");
                                dialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int returned = DataAccess.getInstance(SearchBookActivity.this).
                                                deleteBorrowingBookByAllParameters(borrowingBookList.get(position).get_book().get_id(),
                                                        borrowingBookList.get(position).get_borrower().get_id(),
                                                        borrowingBookList.get(position).get_tdate(),
                                                        borrowingBookList.get(position).get_rdate());
                                        if(returned==1){
                                            Toast.makeText(SearchBookActivity.this,"The book: "+borrowingBookList.get(position).get_book().get_bname()+
                                                            " returned by: "+borrowingBookList.get(position).get_borrower().get_fname()+" "+borrowingBookList.get(position).get_borrower().get_lname(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(SearchBookActivity.this,"Problem in returning of the book: "+borrowingBookList.get(position).get_book().get_bname()+
                                                            " returned by: "+borrowingBookList.get(position).get_borrower().get_fname()+" "+borrowingBookList.get(position).get_borrower().get_lname()+
                                                            " try again later",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        Intent intent1 = new Intent(SearchBookActivity.this,HomeActivity.class);
                                        startActivity(intent1);
                                    }
                                });
                                dialog.show();
                            }catch (ParseException e) {
                                e.printStackTrace();
                            }


                            break;
                        case "status":book_obj_as_json = gson.toJson(borrowingBookList.get(position));
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

    public void stopAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent your_intent = new Intent(this, NotificationReceiver.class);
        PendingIntent your_pending_intent = PendingIntent.getService(context, (int) borrowingBookList.get(current_position).get_id(), your_intent, PendingIntent.FLAG_NO_CREATE);
        if(your_pending_intent!=null && alarmManager!=null){
            alarmManager.cancel(your_pending_intent);
        }
    }

    private Date getCheckDate(String date_check)
    {
        Calendar cal = Calendar.getInstance();
        String[] splitDate = date_check.toString().split("/");
        cal.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
        cal.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
        cal.add(Calendar.DAY_OF_MONTH, -1);

        Date date = cal.getTime();

       return date;
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day+"/"+month+"/"+year;
    }
}