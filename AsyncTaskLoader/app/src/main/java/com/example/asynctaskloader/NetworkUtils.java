package com.example.asynctaskloader;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class NetworkUtils {

    private static final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";

    public static String getBookInfo(String queryString) {
        HttpURLConnection httpURLConnection=null;
        BufferedReader reader=null;
        String bookJSONString=null;

        try {
            Uri builtUri=Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS,"1")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL url=new URL(builtUri.toString());

            httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream=httpURLConnection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder=new StringBuilder();

            String line;
            while ((line=reader.readLine())!=null) {
                builder.append(line+"\n");
            }
            if (builder.length()==0) {
                return null;
            }
            bookJSONString=builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookJSONString;
    }
}