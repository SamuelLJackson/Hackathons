package com.example.severett29.homeconnect;

/**
 * Created by severett29 on 4/23/17.
 */
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.widget.EditText;
import android.widget.Toast;


public class OvenActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the layout we created
        setContentView(R.layout.oven);

        final Button setTemperatureButton = (Button) findViewById(R.id.set_oven_temperature_button);
        final TextView currentTemperatureTextView = (TextView) findViewById(R.id.current_temperature_text_view);
        final EditText preheatTemperatureEditText = (EditText) findViewById(R.id.preheat_temperature_edit_text);

        setTemperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preheatTemperatureEditText.toString() == "") {
                    Toast.makeText(getApplicationContext(), "Please enter a temperature.", Toast.LENGTH_SHORT).show();
                } else {
                    String postSite = "https://thingspace.io/dweet/for/oven?temp=" + preheatTemperatureEditText.toString();

                    try {
                        URL url = new URL(postSite);
                        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                        httpCon.setDoOutput(true);
                        httpCon.setRequestMethod("GET");
                        UpdateListTask task = new UpdateListTask();
                        task.execute(postSite);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
            Intent doorLocksIntent = new Intent(this, LockActivity.class);
            startActivity(doorLocksIntent);
            //finish();
            return true;
        } else if (id == R.id.oven_item) {
            //finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
