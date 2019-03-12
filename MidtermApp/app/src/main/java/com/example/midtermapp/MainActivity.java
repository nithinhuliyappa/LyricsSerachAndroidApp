package com.example.midtermapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements DataHandler {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<DataObj> dataObjs = new ArrayList<>();
    Intent i;
    int searchLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToDO: Give App Name
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("MusixMatch Track Search");

        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        //SeekBar progress
        SeekBar seekBar = findViewById(R.id.limitSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                searchLimit = progress+5;
                TextView textView = findViewById(R.id.limitText);
                textView.setText("Limit :"+searchLimit);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (isConnected()){
                    EditText searchBar = findViewById(R.id.searchBarTextView);
                    String artistkey = searchBar.getText().toString();

                    //ToDO: build the URL
                    String urlStr = null;
                    try {
                        urlStr = "https://api.musixmatch.com/ws/1.1/track.search?" +
                                        "apikey="+URLEncoder.encode("cb98035f1d94f23bd575a5e2508f4d6d","utf-8") +
                                        "&q_artist="+ URLEncoder.encode(artistkey, "utf-8") +
                                        "&page_size="+URLEncoder.encode(String.valueOf(searchLimit),"utf-8") +
                                        "&s_track_rating=desc";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    new APIRequestThread(MainActivity.this).execute(urlStr);

                }else{
                    Toast.makeText(MainActivity.this, "Internet not present", Toast.LENGTH_SHORT).show();
                }

                RadioGroup radioGroup =findViewById(R.id.radioGroup);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = findViewById(checkedId);
                        EditText searchBar = findViewById(R.id.searchBarTextView);
                        String artistkey = searchBar.getText().toString();
                        if (radioButton.getText().toString().contains("Track Rating")){
                            //ToDO: build the URL
                            Log.d("demo", "Track Rating");
                            String urlStr = null;
                            try {
                                urlStr = "https://api.musixmatch.com/ws/1.1/track.search?" +
                                        "apikey="+URLEncoder.encode("cb98035f1d94f23bd575a5e2508f4d6d","utf-8") +
                                        "&q_artist="+ URLEncoder.encode(artistkey, "utf-8") +
                                        "&page_size="+URLEncoder.encode(String.valueOf(searchLimit),"utf-8") +
                                        "&s_track_rating=desc";
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                            new APIRequestThread(MainActivity.this).execute(urlStr);
                            mAdapter.notifyDataSetChanged();
                        }else{
                            //ToDO: build the URL
                            Log.d("demo", "artist Rating");
                            String urlStr = null;
                            try {
                                urlStr = "https://api.musixmatch.com/ws/1.1/track.search?" +
                                        "apikey="+URLEncoder.encode("cb98035f1d94f23bd575a5e2508f4d6d","utf-8") +
                                        "&q_artist="+ URLEncoder.encode(artistkey, "utf-8") +
                                        "&page_size="+URLEncoder.encode(String.valueOf(searchLimit),"utf-8") +
                                        "&s_artist_rating=desc";
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                            new APIRequestThread(MainActivity.this).execute(urlStr);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
/*        toolbar toolbar = findviewbyid(r.id.toolbar);
        setsupportactionbar(toolbar);

        floatingactionbutton fab = findviewbyid(r.id.fab);
        fab.setonclicklistener(new view.onclicklistener() {
            @override
            public void onclick(view view) {
                snackbar.make(view, "replace with your own action", snackbar.length_long)
                        .setaction("action", null).show();
            }
        });*/
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void fetchAndDisplayContent(ArrayList<DataObj> dhObjects) {

        dataObjs = dhObjects;
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Natural Sorting
        Collections.sort(dataObjs);
        mAdapter = new MyAdapter(dataObjs);
        recyclerView.setAdapter(mAdapter);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

    }

/*    @Override
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
    }*/
}
