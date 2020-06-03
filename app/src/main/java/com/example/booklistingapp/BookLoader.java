package com.example.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private static final String TAG = "BookLoader";

    private String mUrl;

    public BookLoader(@NonNull Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Nullable
    @Override
    public List<Book> loadInBackground() {
        Log.d(TAG, "loadInBackground: called");

        if (mUrl == null || !QueryUtils.isNetworkActive(getContext())) {
            Log.d(TAG, "loadInBackground: No Internet connection");;
            return null;
        }
        Log.d(TAG, "loadInBackground: Internet connection, Fetching Book Data");;
        List<Book> bookList = QueryUtils.fetchBookData(mUrl);
        return bookList;
    }

    public boolean isNetworkActive() {
        // Check for connectivity status
        ConnectivityManager cm = (ConnectivityManager) getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
