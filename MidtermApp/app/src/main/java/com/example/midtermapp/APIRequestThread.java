package com.example.midtermapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

interface DataHandler{
    public void fetchAndDisplayContent(ArrayList<DataObj> dhMusics);
}
public class APIRequestThread extends AsyncTask<String, Void, ArrayList<DataObj>> {

    DataHandler dataHandler;
    public APIRequestThread(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    @Override
    protected ArrayList<DataObj> doInBackground(String... strings) {
        HttpURLConnection connection = null;
        ArrayList<DataObj> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        URL url = null;
        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);

                //ToDo: Change the name of parameter
                JSONObject searchResults = root.getJSONObject("message");
                JSONObject serachResults2 = searchResults.getJSONObject("body");
                JSONArray  tracks = serachResults2.getJSONArray("track_list");
                for (int i=0;i<tracks.length(); i++){
                    JSONObject dataObjJson = tracks.getJSONObject(i);
                    JSONObject dataObjJson2 = dataObjJson.getJSONObject("track");
                    DataObj dataObj = new DataObj();

                    //ToDO: Create objects by parsing Data
                    dataObj.track = dataObjJson2.getString("track_name");
                    dataObj.artist = dataObjJson2.getString("artist_name");
                    dataObj.album = dataObjJson2.getString("album_name");
                    dataObj.trackURL = dataObjJson2.getString("track_share_url");
                    String date = dataObjJson2.getString("updated_time");
                    String[] dateArray = date.split("T");
                    dataObj.date =dateFormat.parse(dateArray[0]);
                    result.add(dataObj);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<DataObj> dataObjs) {
        super.onPostExecute(dataObjs);
        dataHandler.fetchAndDisplayContent(dataObjs);
    }
}

