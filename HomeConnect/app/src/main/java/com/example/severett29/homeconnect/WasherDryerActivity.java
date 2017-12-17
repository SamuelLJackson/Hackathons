package com.example.severett29.homeconnect;

/**
 * Created by severett29 on 4/23/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WasherDryerActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.washerdryer);
        final Button startWasherButton = (Button) findViewById(R.id.start_washer_button);
        final TextView washerTimeRemainingTextView = (TextView) findViewById(R.id.washer_time_remaining_text_view);
        startWasherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getSite = "https://thingspace.io/dweet/for/washer?on=true";
                try {
                    URL url = new URL(getSite);
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                    httpCon.setDoOutput(true);
                    httpCon.setRequestMethod("GET");
                    UpdateListTask task = new UpdateListTask();
                    task.execute(getSite);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                new CountDownTimer(15000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        washerTimeRemainingTextView.setTextColor(Color.RED);
                        washerTimeRemainingTextView.setText(Long.toString(millisUntilFinished / 1000));
                    }

                    public void onFinish() {
                        washerTimeRemainingTextView.setText("done!");
                        washerTimeRemainingTextView.setTextColor(Color.GREEN);
                        String getSite = "https://thingspace.io/dweet/for/washer?on=false";
                        try {
                            URL url = new URL(getSite);
                            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                            httpCon.setDoOutput(true);
                            httpCon.setRequestMethod("GET");
                            UpdateListTask task = new UpdateListTask();
                            task.execute(getSite);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // We check what menu item was clicked and show a Toast
        if (id == R.id.lights) {
            Intent doorLocksIntent = new Intent(this, MainActivity.class);
            startActivity(doorLocksIntent);

            return true;

            // If exit was clicked close the app
        } else if (id == R.id.door_locks) {
            //finish();
            return true;
        } else if (id == R.id.oven_item) {
            Intent ovenIntent = new Intent (this, OvenActivity.class);
            startActivity(ovenIntent);
        } else if (id == R.id.lights) {
            Intent lightsIntent = new Intent(this, MainActivity.class);
            startActivity(lightsIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
