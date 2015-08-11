package com.example.photoviewer.photoviewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.android.gms.wallet.wobs.UriData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends Activity {
     ///**emergency token**/ public static final String ACCESS_TOKEN = "402526745.3a73893.9c0054ab116f443d9ea0e3820e5f8e2f";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    InstagramUser user;
    String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Bundle extras = getIntent().getExtras();
        ACCESS_TOKEN = extras.getString("request_token");
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
        //fetchUserInfo();
    }

    public void fetchUserInfo() {
        String urlprof = "https://api.instagram.com/v1/users/self/?access_token=" + ACCESS_TOKEN;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlprof, null, new JsonHttpResponseHandler() {
            //onSuccess (worked)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Expecting a JSON object
                //-Type: { "data" => [set] => "type" } ("image or video")
                // Iterate each of the photo items and decode the item into a java object
                JSONObject userJSON = null;
                try {
                    userJSON = response.getJSONObject("data");
                    //decode the attributes of the JSON into a data model
                    InstagramUser user = new InstagramUser();
                    user.username = userJSON.getJSONObject("user").getString("username");
                    user.full_name = userJSON.getJSONObject("full_name").getString("full_name");
                    //-URL: { "data" => [set] => "images" => "standard_resolution" => "url" }
                    user.profile_picture = userJSON.getJSONObject("profile").getString("profile_picture");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                populateProfileHeader();
            }
            //onFailed (failed)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Do something
            }
        });
    }

    public void populateProfileHeader() {
        TextView tvUsername = (TextView) findViewById(R.id.tvSelfUsername);
        TextView tvFullname = (TextView) findViewById(R.id.tvSelfFullName);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.UserImage);
        tvUsername.setText(user.username);
        tvFullname.setText(user.full_name);
        Picasso.with(this).load(user.profile_picture).into(ivProfileImage);
    }

    //Trigger the API requesthttps://api.instagram.com/v1/users/self/feed?access_token=ACCESS-TOKEN
    public void fetchHomeTimeline() {
        /*
        CLient ID: 3a738930c86844a8b505115a3c2427c0
        -Popular Media: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        -Self Feed : https://api.instagram.com/v1/users/self/feed?access_token=ACCESS-TOKEN
        -Recent Comment :
*/
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
                JSONArray commentJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); //array of posts
                    commentJSON = response.getJSONObject("comments").getJSONArray("data");
                    //iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        //get the JSON object at that positiion
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
                        // Comment Count
                        photo.commentsCount = photoJSON.getJSONObject("comments").getInt("count");
                        // Profile Picture
                        photo.profilePicture = photoJSON.getJSONObject("user").getString("profile_picture");
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