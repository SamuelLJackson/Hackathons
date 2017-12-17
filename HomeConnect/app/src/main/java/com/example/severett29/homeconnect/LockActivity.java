package com.example.severett29.homeconnect;

/**
 * Created by severett29 on 4/23/17.
 */
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.widget.Toast;

public class LockActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the layout we created
        setContentView(R.layout.lock);
        // #F44336 = red
        // #F4CAF50 = green
        final Button frontDoorLockButton = (Button) findViewById(R.id.front_door_lock_button);
        final Button backDoorLockButton = (Button) findViewById(R.id.back_door_lock_button);

        final TextView frontDoorLockTextView = (TextView) findViewById(R.id.front_door_lock_text_view);
        final TextView backDoorLockTextView = (TextView) findViewById(R.id.back_door_lock_text_view);

        frontDoorLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), frontDoorLockButton.getText().toString(), Toast.LENGTH_LONG).show();
                String postSite = "";
                if (frontDoorLockButton.getText().toString().compareTo("LOCK") == 0) {
                    postSite = "https://thingspace.io/dweet/for/frontporch_door?locked=false";
                    frontDoorLockTextView.setBackgroundColor(Color.RED);
                    frontDoorLockButton.setText("UNLOCK");
                } else {
                    postSite = "https://thingspace.io/dweet/for/frontporch_door?locked=true";
                    frontDoorLockButton.setText("LOCK");
                    frontDoorLockTextView.setBackgroundColor(Color.GREEN);
                }
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
        });

        backDoorLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postSite = "";
                if (backDoorLockButton.getText().toString().compareTo("LOCK") == 0) {
                    postSite = "https://thingspace.io/dweet/for/backporch_door?locked=false";
                    backDoorLockTextView.setBackgroundColor(Color.RED);
                    backDoorLockButton.setText("UNLOCK");
                } else {
                    postSite = "https://thingspace.io/dweet/for/backporch_door?locked=true";
                    backDoorLockButton.setText("LOCK");
                    backDoorLockTextView.setBackgroundColor(Color.GREEN);
                }
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
        } else if (id == R.id.washerdryer_item) {
            Intent washerDryerIntent = new Intent(this, WasherDryerActivity.class);
            startActivity(washerDryerIntent);
        }
        return super.onOptionsItemSelected(item);
    }


}