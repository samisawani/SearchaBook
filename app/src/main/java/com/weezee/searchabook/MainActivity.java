package com.weezee.searchabook;

import android.app.LoaderManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static final String TAG = "MainActivity";
     static String finalUrl="";
     boolean stuffBeingLoaded;
     boolean firstTimeNetworkReceiverRegistered;
     boolean firstTimeOnLoadFinishedCalled;


     NetworkChangeReceiver networkChangeReceiver;  IntentFilter intentFilter;
     ProgressBar progressBar;
     ImageView noInternetImageView;
     ListView listView;
     CustomArrayAdapter customArrayAdapter;
     RelativeLayout relativeLayout;
     EditText userInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressBar = findViewById(R.id.progress);
        noInternetImageView = findViewById(R.id.empty_view);
        listView=findViewById(R.id.list_view);

        relativeLayout = findViewById(R.id.relative_layout);
        userInputEditText = findViewById(R.id.edit_text);
        Button searchButton = findViewById(R.id.search);




        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
                if (!stuffBeingLoaded) {
                    if (!isValidSearch(userInputEditText.getText().toString())) {
                        Toast.makeText(MainActivity.this, "please type at least one letter/number!", Toast.LENGTH_SHORT).show();

                    } else {
                            noInternetImageView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            if (customArrayAdapter != null) customArrayAdapter.clear();
                            finalUrl = Utilities.takeSearchInputAndReturnURL(userInputEditText.getText().toString());
                            getLoaderManager().restartLoader(1, null, MainActivity.this);
                            stuffBeingLoaded = true;



                    }


                } else {
                    Toast.makeText(MainActivity.this, "wait for loading!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        Log.d(TAG, "onCreate: out");
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReceiver.setMainActivity(this);



    }



    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: in");
        if (NetworkChangeReceiver.nullResultCozNoInternetConnection)firstTimeOnLoadFinishedCalled=false;
        else firstTimeOnLoadFinishedCalled=true;
        firstTimeNetworkReceiverRegistered = true;
        super.onResume();
        getLoaderManager().initLoader(1, null, MainActivity.this);
        registerReceiver(networkChangeReceiver, intentFilter);
        Log.d(TAG, "onResume: out");
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: in");
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
        Log.d(TAG, "onPause: out");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: in");
        super.onStop();
        Log.d(TAG, "onStop: out");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: in");
        super.onDestroy();
        Log.d(TAG, "onDestroy: out");
    }




    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        stuffBeingLoaded=true;
        if (customArrayAdapter!=null)customArrayAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "onCreateLoader: called");
        return new BookResultsLoader(this, finalUrl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        progressBar.setVisibility(View.GONE);

        stuffBeingLoaded = false;

            if (data == null) {
                if (firstTimeOnLoadFinishedCalled) {

                    Log.e(TAG, "onLoadFinished: first time called now setting the flag to false");
                    firstTimeOnLoadFinishedCalled = false;
                }
                else {
                    Log.e(TAG, "onLoadFinished: now setting Oops to visible");
                    if (customArrayAdapter!=null)customArrayAdapter.clear();

                    noInternetImageView.setVisibility(View.VISIBLE);

                }
            } else {
                if (firstTimeOnLoadFinishedCalled) {

                    Log.e(TAG, "onLoadFinished: first time called now setting the flag to false");
                    firstTimeOnLoadFinishedCalled = false;
                }
                    noInternetImageView.setVisibility(View.GONE);
                    customArrayAdapter = new CustomArrayAdapter(this, data);
                    listView.setAdapter(customArrayAdapter);
                    Log.e(TAG, "onLoadFinished: successfully loaded search result");






        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        Log.d(TAG, "onLoaderReset: called");
    }


    public boolean isValidSearch(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) != ' ') {
                return true;
            }
        }
        return false;
    }


}
