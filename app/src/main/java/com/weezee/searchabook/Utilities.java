package com.weezee.searchabook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Utilities {
    private static final String  initialURL = "https://www.googleapis.com/books/v1/volumes?q=#&maxResults=20&key=AIzaSyB3mDq_sa6KRYKf43qUpxCEPL5Uf8LjBvs";

    private static final String TAG = "Utilities";

    public static URL returnURLObject(String stringUrl) throws MalformedURLException {
        return new URL(stringUrl);

    }


    public static HttpURLConnection returnHTTPURLConnectionObject(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(1500);
        httpURLConnection.setReadTimeout(1000);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        return httpURLConnection;


    }

    public static BufferedReader returnBufferedReaderObject(HttpURLConnection httpURLConnection) throws IOException {
        return new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));


    }


    public static JSONObject returnTheJsonResponse(BufferedReader bufferedReader) throws JSONException, IOException {
        StringBuilder stringBuilder = new StringBuilder("");
        String temp = bufferedReader.readLine();
        while (temp != null) {
            stringBuilder.append(temp);
            temp = bufferedReader.readLine();
        }
        return new JSONObject(stringBuilder.toString());


    }


    public static ArrayList<Book> parseTheJsonResponseAndReturnTheListOfBooks(JSONObject jsonObject) throws JSONException {
        JSONArray items = jsonObject.optJSONArray("items");
        ArrayList<Book> arrayList = new ArrayList<>();
        String thumbnailUrl="No thumbnailUrl available";

        for (int i = 0; i < items.length(); i++) {

            JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
            JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
            //-----------------------------------------------------------------------------------
            String title = volumeInfo.optString("title","No title available");
            String description = volumeInfo.optString("description","No description available");
            float rating = (float)volumeInfo.optDouble("averageRating", 0.0);
            String infoUrl = volumeInfo.optString("infoLink");
            if (imageLinks!=null) thumbnailUrl=imageLinks.optString("thumbnail");
            arrayList.add(new Book(description, rating, thumbnailUrl, title, infoUrl));
        }
        return arrayList;

    }
    public static String takeSearchInputAndReturnURL(String searchTyped) {
        String[] array = initialURL.split("#");
//        Log.e(TAG, "takeSearchInputAndReturnURL: the url is supposed to be alright which is\n:" + array[0] + searchTyped + array[1]);
        return array[0] + searchTyped + array[1];
    }
    public static void closeBufferedAndHttp(BufferedReader bufferedReader, HttpURLConnection httpURLConnection) throws IOException{
            bufferedReader.close();
            httpURLConnection.disconnect();

    }


}
