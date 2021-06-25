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

public class AdapterSearchBook extends RecyclerView.Adapter<AdapterSearchBook.ExampleViewHolder> implements Filterable {

    private List<Book> bookList;
    private List<Book> bookListFull;
    private List<BorrowingBook> borrowingBookList;
    private List<BorrowingBook> borrowingBookListFull;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView book_name_item;
        TextView author_name;

        ExampleViewHolder(View itemView , OnItemClickListener listener) {
            super(itemView);

            book_name_item = itemView.findViewById(R.id.book_name_item);
            author_name = itemView.findViewById(R.id.author_name);

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

    /**
     * make makes two identical lists of borrowingBookList
     * @param bookList the list
     */
    public AdapterSearchBook(List<Book> bookList) {
        this.bookList = bookList;
        bookListFull = new ArrayList<>(bookList);
    }

    /**
     *connect the book_item xml to his parent (book_search.xml)
     */
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,
                parent, false);
        return new ExampleViewHolder(v,mListener);
    }

    /**
     * set the item view (the data)
     * @param holder the view holder
     * @param position the position in the list (number)
     */
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Book currentItem = bookList.get(position);

        holder.book_name_item.setText(currentItem.get_bname());
        holder.author_name.setText(currentItem.get_author());
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    /**
     * filters the list by given text string
     */
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(bookListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Book item : bookListFull) {
                    if (item.get_bname().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        /**
         *publish the filter result
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bookList.clear();
           bookList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

