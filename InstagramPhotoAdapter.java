package com.sugarcoder.instagramclient;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {

    // What data we need from the activity
    // We need to take the Context, Data Source

    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }


    // What our item looks like


    // Use the template to display each photo  (getView tells the data to turn it into a view)

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstagramPhoto photo = getItem(position);


        // Check if we are using a recycled view. If not, we need to inflate
        if (convertView == null) {
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }


        // Lookup the views for populating the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);


        // Insert the model data into each of the view items
        tvCaption.setText(photo.caption);


        // Clear out the image view right away, if it was a recycled item
        ivPhoto.setImageResource(0);


        // Insert the image view using Picasso (send out a async request)
        Picasso.with(getContext()).load(photo.imageURL).into(ivPhoto);


        // Return the created item as a view
        return convertView;

    }

}

