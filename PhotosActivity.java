package com.sugarcoder.instagramclient;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {


    public static final String CLIENT_ID = "22d24e36087d4440b66b56742bf8653b";


    // Creating an array of objects

    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter aPhotos;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();


        // 1. Create the adapter linking it to the source
        aPhotos = new InstagramPhotoAdapter(this, photos);


        // 2. Find the listview from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);


        // 3. Set the adapter binding it to the listview
        lvPhotos.setAdapter(aPhotos);


        // 4. Fetch the popular photos (Send out API request to Popular Photos)
        fetchPopularPhotos();



    }


    // Trigger API request
    public void fetchPopularPhotos(){
        /*
        - Client ID: 22d24e36087d4440b66b56742bf8653b
        - Popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        */


        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;


        // Creating the network client
        AsyncHttpClient client = new AsyncHttpClient();


        // Trigger the GET request

        client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess (worked, 200)

               @Override
              public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

              // Expecting a JSON object

              /*
              Attributes of the JSON:
                - Type: { "data" => [x] => "type"} ("image" or "video")
                - URL: { "data" => [x] => "images" => "standard_resolution" => "url" }
                - Caption: { "data" => [x] => "caption" => "text" }
                - Author Name: { "data" => [x] => "user" => "username" }
              */

              // This is to log the app

               Log.i("DEBUG", response.toString());


              // Iterate each of the photo items and decode the item into photo object


               JSONArray photosJSON;

               try {
                    photosJSON = response.getJSONArray("data");    // This gets us the array of posts

                   // Iterate the array of posts using a 'for' loop

                   for (int i = 0; i < photosJSON.length(); i++) {
                       // Get the JSON object at that position in the array
                       JSONObject photoJSON = photosJSON.getJSONObject(i);

                       // Decode the attributes of the JSON into a data model

                       InstagramPhoto photo = new InstagramPhoto();

                       photo.username = photoJSON.getJSONObject("user").getString("username");
                       photo.caption = photoJSON.getJSONObject("caption").getString("text");
                       photo.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                       photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                       photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");


                       // Add decoded objects to the photos' array

                       photos.add(photo);

                   }


               } catch (JSONException e) {
                   e.printStackTrace();
               }

                // Callback

                aPhotos.notifyDataSetChanged();


            }


            // onFailure (failed)

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Do something
            }

        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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
