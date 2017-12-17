package com.example.severett29.homeconnect;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by severett29 on 4/23/17.
 */

//how to run an asynchronous task in the background
class UpdateListTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {

        String response = "";
        for (String url : urls) {
            HttpURLConnection urlConnection = null;
            try {
                //make get-request
                URL _url = new URL(url);
                urlConnection = (HttpURLConnection) _url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                response = readStream(in);

                //parse the JSON response
                JSONArray jsonArray = new JSONArray(response);


                //list.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    //list.add(obj.getString("first") + " " + obj.getString("last"));
                }
            } catch (Exception e) {
                Log.i("home screen", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
        return response;
    }


    //parsing the response to a string
    private String readStream(InputStream is) {

        try {
            String response = "";
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

            return response;
        } catch (IOException e) {
            return "error";
        }
    }



}