package com.example.severett29.homeconnect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.SeekBar;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import android.widget.Toast;
import java.util.StringTokenizer;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractQueue;
import java.util.ArrayList;
import android.content.Intent;
import android.view.MenuItem;
import android.view.Menu;
import android.support.v7.app.ActionBar;

public class MainActivity extends ActionBarActivity {
    static String output = "dude";

    // onCreate is executed when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the file activity_main.xml as the user interface
        setContentView(R.layout.activity_main);

        // To be able to edit the TextView with our code we have to create it and
        // bind it to a TextView object. I need to use final because it will be
        // used in the inner class below

        final TextView kitchenLightTextView = (TextView) findViewById(R.id.kitchen_light_text_view);
        final TextView bathroomLightTextView = (TextView) findViewById(R.id.bathroom_light_text_view);
        final TextView frontPorchLightTextView = (TextView) findViewById(R.id.front_porch_light_text_view);
        final TextView backPorchLightTextView = (TextView) findViewById(R.id.back_porch_light_text_view);



        //setting defaults based on current state
        /*
        *
        * *  THIS IS WHERE I NEED THE CODE TO GO
        * *
        * *   MY MAIN ISSUE IS BEING ABLE TO HANDLE RESPONSES
        * *
        * *
         */




        final SeekBar livingRoomLightSeekBar = (SeekBar) findViewById(R.id.living_room_light_seekbar);
        livingRoomLightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                //Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_SHORT).show();
                float dimness = Float.valueOf(progress) / 100;
                /*/
                *
                *
                * THIS IS AN EXAMPLE OF SUCCESSFUL REQUEST
                *
                *
                 */
                String postSite = "https://thingspace.io/dweet/for/livingroom_light?dim=" + Float.toString(dimness);

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

        final SeekBar bedroom1LightSeekBar = (SeekBar) findViewById(R.id.bedroom1_light_seekbar);
        bedroom1LightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                //Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_SHORT).show();
                float dimness = Float.valueOf(progress) / 100;


                String postSite = "https://thingspace.io/dweet/for/bedroom1_light?dim=" + Float.toString(dimness);

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

        final SeekBar bedroom2LightSeekBar = (SeekBar) findViewById(R.id.bedroom2_light_seekbar);
        bedroom2LightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                //Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_SHORT).show();
                float dimness = Float.valueOf(progress) / 100;


                String postSite = "https://thingspace.io/dweet/for/bedroom2_light?dim=" + Float.toString(dimness);

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
        // I set up the Button just like I did the TextView
        final Button kitchenLightButton = (Button) findViewById(R.id.kitchen_light_button);
        final Button frontPorchLightButton = (Button) findViewById(R.id.front_porch_light_button);
        final Button backPorchLightButton = (Button) findViewById(R.id.back_porch_light_button);
        final Button bathroomLightButton = (Button) findViewById(R.id.bathroom_light_button);

        // This is how you make the Button change the text in the TextView when it is clicked
        kitchenLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postSite = "";
                if (kitchenLightButton.getText().toString() == "ON") {
                    postSite = "https://thingspace.io/dweet/for/kitchen_light?on=true";
                    kitchenLightTextView.setBackgroundColor(Color.YELLOW);
                    kitchenLightButton.setText("OFF");
                } else {
                    postSite = "https://thingspace.io/dweet/for/kitchen_light?on=false";
                    kitchenLightButton.setText("ON");
                    kitchenLightTextView.setBackgroundColor(Color.WHITE);
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
        frontPorchLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postSite = "";
                if (frontPorchLightButton.getText().toString() == "ON") {
                    postSite = "https://thingspace.io/dweet/for/frontporch_light?on=true";
                    frontPorchLightTextView.setBackgroundColor(Color.YELLOW);
                    frontPorchLightButton.setText("OFF");
                } else {
                    postSite = "https://thingspace.io/dweet/for/frontporch_light?on=false";
                    frontPorchLightButton.setText("ON");
                    frontPorchLightTextView.setBackgroundColor(Color.WHITE);
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
        backPorchLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postSite = "";
                if (backPorchLightButton.getText().toString() == "ON") {
                    postSite = "https://thingspace.io/dweet/for/backporch_light?on=true";
                    backPorchLightTextView.setBackgroundColor(Color.YELLOW);
                    backPorchLightButton.setText("OFF");
                } else {
                    postSite = "https://thingspace.io/dweet/for/backporch_light?on=false";
                    backPorchLightButton.setText("ON");
                    backPorchLightTextView.setBackgroundColor(Color.WHITE);
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

        bathroomLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postSite = "";
                if (bathroomLightButton.getText().toString() == "ON") {
                    postSite = "https://thingspace.io/dweet/for/bathroom_light?on=true";
                    bathroomLightTextView.setBackgroundColor(Color.YELLOW);
                    bathroomLightButton.setText("OFF");
                } else {
                    postSite = "https://thingspace.io/dweet/for/bathroom_light?on=false";
                    bathroomLightButton.setText("ON");
                    bathroomLightTextView.setBackgroundColor(Color.WHITE);
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

            return true;

            // If exit was clicked close the app
        } else if (id == R.id.door_locks) {
            Intent doorLocksIntent = new Intent(this, LockActivity.class);
            startActivity(doorLocksIntent);
            //finish();
            return true;
        } else if (id == R.id.oven_item) {
            Intent ovenIntent = new Intent(this, OvenActivity.class);
            startActivity(ovenIntent);
        } else if (id == R.id.washerdryer_item) {
            Intent washerDryerIntent = new Intent(this, WasherDryerActivity.class);
            startActivity(washerDryerIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && requestCode == 400){

            //perform a get-request
            UpdateListTask task = new UpdateListTask();
            task.execute("http://thingspace.io/get/latest/dweet/for/kitchen_light");
            TextView titleText = (TextView) findViewById(R.id.lights_title);
            titleText.setText("doooood");
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public static String getRoughData(String object) throws IOException {
        URL url = new URL("https://thingspace.io/get/latest/dweet/for/" + object);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("GET");

        InputStream inputStream = httpCon.getErrorStream();
        if (inputStream == null)
            inputStream = httpCon.getInputStream();

        // Read everything from our stream
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = responseReader.readLine()) != null) {
            response.append(inputLine);
        }
        responseReader.close();

        return response.toString();
    }

    public static String[][] parseData(String response) {

        int contentLoc = response.indexOf("content");
        String dataString = response.substring(contentLoc + 10, response.indexOf('}',contentLoc));

        StringTokenizer st = new StringTokenizer(dataString, ", : \"");

        String[][] data = new String[st.countTokens() / 2][2];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = st.nextToken();
            data[i][1] = st.nextToken();
        }

        return data;
    }

}

