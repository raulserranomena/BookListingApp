package com.example.booklistingapp;

public class Book {


    /**
     * Title Of the Book*/
    private String mTitle;

    /**
     * Authors Of the Book*/
    private String mAuthors;

    /**
     * Link for the Image Of the Book*/
    private String mImageLink;

    /**
     * Link for the Preview Of the Book*/
    private String mPreviewLink;



    /**
     * Construct a new {@link Book} object
     *
     * @param title Book Title
     * @param authors Book Authors
     * @param imageLink Book Image Link
     * @param previewLink Book Preview Link
     *
     *
     * */
    public Book (String title, String authors, String imageLink, String previewLink){
        mTitle = title;
        mAuthors = authors;
        mImageLink = imageLink;
        mPreviewLink = previewLink;
    }

    public Book (String title, String authors){
        mTitle = title;
        mAuthors = authors;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getImageLink() {
        return mImageLink;
    }

    public String getPreviewLink(){
        return mPreviewLink;
    }
}
