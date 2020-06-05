package com.example.booklistingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private static final String TAG = "MainActivity";

    private static final int BOOK_LOADER_ID = 1;
    private List<Book> mBooksList = new ArrayList<>();
    private RecyclerView mBookListRecyclerView;
    private BookAdapter mAdapter;
    private EditText mSearchTextView;
    private TextView mEmptyStateTextView;
    private Button mRetryButton;
    private String mSearchQuery = "";
    private ProgressBar mProgressCircularBar;

    private String mUrl = "https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBooksList.add(new Book("Harry Potter 1", "J.K. Rowling"));
        mBooksList.add(new Book("Harry Potter 2", "J.K. Rowling"));
        mBooksList.add(new Book("Harry Potter 3", "J.K. Rowling"));

        //Find the reference of the SearchView
        mSearchTextView = findViewById(R.id.search_text_view);
        //Find the reference of the EmptyStateView
        mEmptyStateTextView = findViewById(R.id.empty_state_view);
        //Find the reference of the RetryButton
        mRetryButton = findViewById(R.id.retry_button);
        //Find the reference of the ProgressCircularBar
        mProgressCircularBar = findViewById(R.id.progress_circular_bar);

        //Find the reference of the BookListRecyclerView
        mBookListRecyclerView = findViewById(R.id.book_list_recycler_view);

        //Create a new LinearLayoutManager for the BookListRecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //Set the LinearLayoutManager to the BookListRecyclerView
        mBookListRecyclerView.setLayoutManager(linearLayoutManager);

        //Create a new BookAdapter for the BookListRecyclerView
        mAdapter = new BookAdapter(this, mBooksList);
        //Set the Adapter for the BookListRecyclerView
        mBookListRecyclerView.setAdapter(mAdapter);

        //Listen for an Search Action on the the SearchBar
        mSearchTextView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //get the text to search
                    mSearchQuery = mSearchTextView.getText().toString();
                    mSearchQuery = mSearchQuery.replaceAll(" ", "+");

                    //If there is no text to search, show a toast
                    if (mSearchQuery.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Hey!, no has ingresado nada para buscar!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Do the search
                        searchConfirmed();
                        //Hide Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    handled = true;
                }
                return handled;
            }
        });


        // Set an item click listener on the Retry button if there is no Internet Connection
        // to try to Start the Loader again. Has a delay of 1 second to show the Progress Bar
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                mRetryButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchConfirmed();
                    }
                }, 1000);
            }
        });
    }


    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: called");
        return new BookLoader(this, mUrl + mSearchQuery);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> bookList) {
        //Clear the Books within the Recycler Books List
        mAdapter.clear();
        showProgressBar(false);

        //
        if (bookList == null && !QueryUtils.isNetworkActive(this)) {
            setEmptyView();
        }


        if (bookList != null && !bookList.isEmpty()) {
            mAdapter.addAll(bookList);
            showRecyclerView();
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        //Clear the Books within the Recycler Books List
        mAdapter.clear();

    }


    private void searchConfirmed() {

        //shows ProgressBar while fetching the book data
        showProgressBar(true);
        //Restart the Loader with new url
        androidx.loader.app.LoaderManager.getInstance(this).restartLoader(BOOK_LOADER_ID, null, this);
        //Init the Loader
        androidx.loader.app.LoaderManager.getInstance(this).initLoader(BOOK_LOADER_ID, null, this).forceLoad();

    }

    private void setEmptyView() {
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        mRetryButton.setVisibility(View.VISIBLE);
        mBookListRecyclerView.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        mEmptyStateTextView.setVisibility(View.GONE);
        mRetryButton.setVisibility(View.GONE);
        mBookListRecyclerView.setVisibility(View.VISIBLE);

    }

    private void showProgressBar(Boolean set) {

        int visibility;

        if (set == true) {
            visibility = View.VISIBLE;
            mEmptyStateTextView.setVisibility(View.GONE);
            mRetryButton.setVisibility(View.GONE);
            mBookListRecyclerView.setVisibility(View.GONE);

        } else {
            visibility = View.GONE;
        }
        mProgressCircularBar.setVisibility(visibility);

    }


}