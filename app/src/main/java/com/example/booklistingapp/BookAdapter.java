package com.example.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private static final String TAG = "BookAdapter";

    private List<Book> mBooks = new ArrayList<>();
    private Context mContext;


    public BookAdapter(Context mContext, List<Book> mBooks) {
        this.mBooks = mBooks;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_recycler_view_item, parent,false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Find the current Book that was clicked on
                Book currentBook = mBooks.get(myViewHolder.getLayoutPosition());

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getPreviewLink());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                mContext.startActivity(websiteIntent);

            }
        });

        //return new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.mBookTitle.setText(mBooks.get(position).getTitle());
        holder.mBookAuthors.setText(mBooks.get(position).getAuthors());

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mBookTitle, mBookAuthors;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mBookTitle = itemView.findViewById(R.id.book_title_text_view);
            mBookAuthors = itemView.findViewById(R.id.book_authors_text_view);

        }
    }

    void clear(){
        mBooks.clear();
        this.notifyDataSetChanged();
    }

    void addAll(List<Book> books){
        mBooks = books;
        this.notifyDataSetChanged();

    }
}
