package com.example.librarymanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BorrowingBook;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearchBorrowingBook extends RecyclerView.Adapter<AdapterSearchBorrowingBook.ExampleViewHolder> implements Filterable {

    private List<BorrowingBook> borrowingBookList;
    private List<BorrowingBook> borrowingBookListFull;
    private AdapterSearchBook.OnItemClickListener mListener;

    public void setOnItemClickListener(AdapterSearchBook.OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView book_name;
        TextView author_name;
        TextView return_date;

        ExampleViewHolder(View itemView , AdapterSearchBook.OnItemClickListener listener) {
            super(itemView);

            book_name = itemView.findViewById(R.id.book_name);
            author_name = itemView.findViewById(R.id.borrower_name);
            return_date = itemView.findViewById(R.id.return_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }


    }

    public AdapterSearchBorrowingBook(List<BorrowingBook> borrowingBookList) {
        this.borrowingBookList = borrowingBookList;
        borrowingBookListFull = new ArrayList<>(this.borrowingBookList );
    }

    @NonNull
    @Override
    public AdapterSearchBorrowingBook.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.borrowing_item,
                parent, false);
        return new AdapterSearchBorrowingBook.ExampleViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchBorrowingBook.ExampleViewHolder holder, int position) {
        BorrowingBook currentItem = borrowingBookList.get(position);

        holder.book_name.setText(currentItem.get_book().get_bname());
        holder.author_name.setText(currentItem.get_borrower().get_fname()+" "+currentItem.get_borrower().get_lname());
        holder.return_date.setText(currentItem.get_rdate());
    }

    @Override
    public int getItemCount() {
        return borrowingBookList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BorrowingBook> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(borrowingBookListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (BorrowingBook item : borrowingBookListFull) {
                    if (item.get_book().get_bname().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            borrowingBookList.clear();
            borrowingBookList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
