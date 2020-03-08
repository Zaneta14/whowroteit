package com.example.asynctaskloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    EditText editText;
    TextView author;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.bookInput);
        author = (TextView) findViewById(R.id.authorText);
        title = (TextView) findViewById(R.id.titleText);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    public void searchBooks(View view) {
        String query = editText.getText().toString();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (query.length() != 0 && networkInfo != null && networkInfo.isConnected()) {
            author.setText("");
            title.setText(R.string.loading);
            Bundle bundle = new Bundle();
            bundle.putString("queryString", query);
            getSupportLoaderManager().restartLoader(0, bundle, this);
        } else {
            if (query.length() == 0) {
                author.setText("");
                title.setText(R.string.no_search_term);
            } else {
                author.setText("");
                title.setText(R.string.no_network);
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, args.getString("queryString"));
    }

    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            int i = 0;
            String title1 = null;
            String authors = null;

            while (i < jsonArray.length() || (authors == null && title1 == null)) {
                JSONObject book = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                try {
                    title1 = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
            if (title1 != null && authors != null) {
                title.setText(title1);
                author.setText(authors);
                editText.setText("");
            } else {
                title.setText(R.string.no_results);
                author.setText("");
            }
        } catch (Exception e) {
            title.setText(R.string.no_results);
            author.setText("");
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<String> loader) {

    }
}