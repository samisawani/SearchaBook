package com.weezee.searchabook;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//-----------------------------------------------------------------------------------------
    public   class BookResultsLoader extends AsyncTaskLoader<ArrayList<Book>> {
        private String theFinalURL;
        private static final String TAG = "BookResultsLoader";

        BookResultsLoader(Context context, String theFinalURL) {
            super(context);
            this.theFinalURL = theFinalURL;
        }

        @Override
        public ArrayList<Book> loadInBackground() {
            Log.e(TAG, "loadInBackground: loading from scratch" );
            HttpURLConnection httpURLConnection; JSONObject jsonObject; BufferedReader bufferedReader;
            try {
                URL url = Utilities.returnURLObject(theFinalURL);
                httpURLConnection = Utilities.returnHTTPURLConnectionObject(url);
                bufferedReader = Utilities.returnBufferedReaderObject(httpURLConnection);
                jsonObject = Utilities.returnTheJsonResponse(bufferedReader);
                Utilities.closeBufferedAndHttp(bufferedReader,httpURLConnection );
                return Utilities.parseTheJsonResponseAndReturnTheListOfBooks(jsonObject);

            }
            catch (IOException | JSONException e) {
                Log.e(TAG, "loadInBackground: " + e.getMessage());
                Log.e(TAG, "loadInBackground: now got out of exceptions and will return null");
                return null;

            }
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }
    }
