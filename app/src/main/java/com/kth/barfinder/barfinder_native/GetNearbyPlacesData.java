package com.kth.barfinder.barfinder_native;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Shayan on 2018-02-07.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;
    public static String newline = System.getProperty("line.separator");


    @Override
    protected String doInBackground(Object... params) {
        try {
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {


        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            //String opening_hours  = googlePlace.get("opening_hours");
            String rating  = googlePlace.get("rating");
            String types  = googlePlace.get("types");


            //checking strings
            if(placeName == null ||  placeName.isEmpty()) { placeName= "no place info"; }
            if(vicinity == null ||  vicinity.isEmpty()) { vicinity= "no vicinity info"; }
            //if(opening_hours == null ||  opening_hours.isEmpty()) { opening_hours= "no opening info"; }
            if(rating == null ||  rating.isEmpty()) { rating= "no rating info"; }
            if(types == null ||  types.isEmpty()) { types= "no rating info"; }

            InfoWindowData info = new InfoWindowData();
            markerOptions.title(placeName);
            markerOptions.snippet(vicinity);
            info.setImage("beermugsmall");
            //info.setInstitution("Opening_hours: " + opening_hours);
            info.setInstitution("Rating: " + rating);
            info.setReview("Types: " + types);
            //info.setPrice(types);


            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);


            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.beermugsmall));

            //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("beermug1",100,100)));
            Marker m = mMap.addMarker(markerOptions);
            // from tutorial for custom info window
            m.setTag(info);
        }
    }



}