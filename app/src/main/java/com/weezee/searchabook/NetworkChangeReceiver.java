package com.weezee.searchabook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;
    private static final String TAG = "NetworkChangeReceiver";
    static boolean nullResultCozNoInternetConnection;


    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
   public void onReceive(final Context context, final Intent intent) {
        Log.e(TAG, "onReceive: in" );

       String status = NetworkUtil.getConnectivityStatusString(context);


        if (mainActivity.firstTimeNetworkReceiverRegistered){
           mainActivity.firstTimeNetworkReceiverRegistered =false;
           Log.e(TAG, "onReceive: did nothing");
       }
       else{
           if ((status.equals("Wifi enabled") || status.equals("Mobile data enabled"))&&!mainActivity.stuffBeingLoaded){
               Log.e(TAG, "onReceive: "+status );
               EditText editText = mainActivity.findViewById(R.id.edit_text);
               mainActivity.finalUrl = Utilities.takeSearchInputAndReturnURL(editText.getText().toString());
               if (mainActivity.isValidSearch(editText.getText().toString())&&editText.getText().toString().length()>0){
                   Log.e(TAG, "onReceive: now will restart adapter" );

                   mainActivity.getLoaderManager().restartLoader(1, null, mainActivity);}
           }
           else{ nullResultCozNoInternetConnection=true;
               Log.e(TAG, "onReceive: setting null boolean to true");}


       }
        Log.e(TAG, "onReceive: out" );





   }



}
