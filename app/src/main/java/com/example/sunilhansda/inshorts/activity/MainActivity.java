package com.example.sunilhansda.inshorts.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sunilhansda.inshorts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ArrayList<HashMap<String, String>> newsList;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        newsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new GetNewsHeadlines().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String news_url,news_title;
                news_url = newsList.get(position).get("URL");
                news_title = newsList.get(position).get("TITLE");
                Intent intent = new Intent(MainActivity.this,Webview.class);
                intent.putExtra("URL",news_url);
                intent.putExtra("TITLE",news_title);
                startActivity(intent);
            }
        });


    }


    private class GetNewsHeadlines extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"News feed is downloading", Toast.LENGTH_LONG).show();
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            //Making a request to url and getting response
            String url = "http://starlord.hackerearth.com/newsjson";
            String jsonStr = handler.makeServiceCall(url);

            if (jsonStr != null)
            {
                try {

                    //Getting JSON Array node
                    JSONArray newsjson = new JSONArray(jsonStr);

                    //looping through all news
                    for (int i = 0;i < newsjson.length(); i++)
                    {
                        JSONObject c = newsjson.getJSONObject(i);
                        int id = c.getInt("ID");
                        String id2 = "" + id;
                        String title = c.getString("TITLE");
                        String url2 = c.getString("URL");
                        String publisher = c.getString("PUBLISHER");
                        String category = c.getString("CATEGORY");
                        String hostname = c.getString("HOSTNAME");
                        String epochtimestamp = c.getString("TIMESTAMP");

                        String newCat = checkCategory(category);
                        String readableTimestamp = convertEpochToReadableTimestamp(epochtimestamp);
                        //temp hashMap for single newsfeed
                        HashMap<String, String> news = new HashMap<>();

                        //adding each child node to Hashmap
                        news.put("ID",id2);
                        news.put("TITLE",title);
                        news.put("URL",url2);
                        news.put("PUBLISHER",publisher);
                        news.put("CATEGORY",newCat);
                        news.put("HOSTNAME",hostname);
                        news.put("TIMESTAMP",readableTimestamp);

                        //adding news to news list
                        newsList.add(news);
                    }
                }
                catch (final JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "json parsing error (Exception)" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, newsList,
                    R.layout.list_item, new String[] {"ID","TITLE","PUBLISHER","CATEGORY",
                    "TIMESTAMP","URL"},
                    new int[] {R.id.id1, R.id.title, R.id.publisher, R.id.category,
                     R.id.timestamp, R.id.url3});
            lv.setAdapter(adapter);

            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        }

    }

    private String convertEpochToReadableTimestamp(String epochtimestamp) {
        long epoch = Long.parseLong(epochtimestamp);
        String date = new java.text.SimpleDateFormat("dd/MM/yyyy")
                .format(new java.util.Date(epoch*1000));

        return date;
    }

    private String checkCategory(String category) {
       if (category.equals("b"))
           return "Business";
        else if (category.equals("t"))
            return "Science & Technology";
       else if (category.equals("e"))
           return "Education";
       else if (category.equals("m"))
           return "Health";

        return "NULL";
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

}