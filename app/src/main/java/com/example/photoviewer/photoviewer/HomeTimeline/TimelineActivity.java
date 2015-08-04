package com.example.photoviewer.photoviewer.HomeTimeline;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.photoviewer.photoviewer.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends Activity {

    public static final String ACCESS_TOKEN = "402526745.3a73893.9c0054ab116f443d9ea0e3820e5f8e2f";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        //Send out API request to Popular Photos
        photos = new ArrayList<>();
        // Create adapter linking it to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // find the ListView from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //set the adapter binding it to ListView
        lvPhotos.setAdapter(aPhotos);
        // fetch the popular photos
        fetchHomeTimeline();
    }

    //Trigger the API requesthttps://api.instagram.com/v1/users/self/feed?access_token=ACCESS-TOKEN
    public void fetchHomeTimeline() {
        /*
        CLient ID: 3a738930c86844a8b505115a3c2427c0
        -Popular Media: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        */

        // https://api.instagram.com/v1/users/self/feed?access_token=ACCESS-TOKEN

        String url = "https://api.instagram.com/v1/users/self/feed?access_token=" + ACCESS_TOKEN;

        //Create the client
        AsyncHttpClient client = new AsyncHttpClient();
        //  Trigger the GET request :D
        client.get(url, null, new JsonHttpResponseHandler() {
            //onSucces (worked)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Expecting a JSON object
                //-Type: { "data" => [set] => "type" } ("image or video")
                // Iterate each of the photo items and decode the item into a java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); //array of posts
                    //iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        //get the JSON object at tahat positiion
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the attributes of the JSON into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        //-Author Name: { "data" => [set] => "user" => "username" }
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        //-Caption: { "data" => [set] => "caption" => "text" }
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        //-URL: { "data" => [set] => "images" => "standard_resolution" => "url" }
                        photo.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // Height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        // Likes Count
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        //Add decoded objects to the photos Array
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //callback
                aPhotos.notifyDataSetChanged();
            }

            //onFailed (failed)

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Do something
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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
