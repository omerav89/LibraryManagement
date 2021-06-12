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

import java.util.ArrayList;
import java.util.List;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ExampleViewHolder> implements Filterable {

    private List<Book> bookList;
    private List<Book> bookListFull;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        TextView textView2;

        ExampleViewHolder(View itemView , OnItemClickListener listener) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.text_view1);
            textView2 = itemView.findViewById(R.id.text_view2);

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

    public AdapterSearch(List<Book> bookList) {
        this.bookList = bookList;
        bookListFull = new ArrayList<>(bookList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem,
                parent, false);
        return new ExampleViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Book currentItem = bookList.get(position);


        holder.textView1.setText(currentItem.get_bname());
        holder.textView2.setText(currentItem.get_author());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

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

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bookList.clear();
           bookList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

