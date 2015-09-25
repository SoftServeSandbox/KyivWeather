package com.example.administrator.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView6;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTextView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestClass task = new TestClass();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q={Kyiv}&units=metric");

        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView3 = (TextView) findViewById(R.id.textView3);
        mTextView4 = (TextView) findViewById(R.id.textView4);
        mTextView5 = (TextView) findViewById(R.id.textView5);
        mTextView6 = (TextView) findViewById(R.id.textView6);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TestClass extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            InputStream InputStr = null;
            try {
                InputStr = new URL(params[0]).openStream();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            StringBuilder inputStringBuilder;
            String resultString = null;
            String line;
            try {
                inputStringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(InputStr, "UTF-8"));
                line = bufferedReader.readLine();
                while (line != null) {
                    inputStringBuilder.append(line);
                    inputStringBuilder.append('\n');
                    line = bufferedReader.readLine();
                }
                resultString = inputStringBuilder.toString();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            JSONObject json = null;
            try {
                json = new JSONObject(resultString);
                Log.v("checking return data", json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject firstJSON = jsonObject.getJSONObject("main");
                mTextView2.setText("Temperature: " + firstJSON.getString("temp") + " \u2103");
                mTextView5.setText("Humidity: " + firstJSON.getString("humidity") + " %");
                mTextView6.setText("Pressure: " + firstJSON.getString("pressure") + " hPa");
                JSONObject secondJSON = jsonObject.getJSONObject("wind");
                mTextView3.setText("Wind speed: " + secondJSON.getString("speed") + " meter/sec");
                mTextView4.setText("Wind direction: " + secondJSON.getString("deg") + " \u00b0");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

