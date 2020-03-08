package com.example.asynctaskloader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;


public class BookLoader extends AsyncTaskLoader<String> {
    private String QueryString;

    public BookLoader(Context context, String queryString) {
        super(context);
        QueryString=queryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(QueryString);
    }
}