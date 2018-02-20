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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shayan on 2018-02-07.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;



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
        List<Marker> markerList = new ArrayList<Marker>();
        List<InfoWindowData> infoList = new ArrayList<InfoWindowData>();
        List<MarkerOptions> markerOptionsList = new ArrayList<MarkerOptions>();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions_temp = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String lat_s = googlePlace.get("lat");
            String lng_s = googlePlace.get("lng");
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            String open_now  = googlePlace.get("open_now");
            String rating  = googlePlace.get("rating");
            String types  = googlePlace.get("types");


            //checking strings
            if(placeName == null ||  placeName.isEmpty()) { placeName= "no place info"; }
            if(vicinity == null ||  vicinity.isEmpty()) { vicinity= "no vicinity info"; }
            if(open_now == null ||  open_now.isEmpty()) { open_now= "no opening info"; }
            if (open_now == "true"){open_now= "Place is currently open";}
            if (open_now == "false"){open_now= "Place is currently closed";}
            if(rating == null ||  rating.isEmpty()) { rating= "no rating info"; }
            if(types == null ||  types.isEmpty()) { types= "no rating info"; }

            //create photo url



            InfoWindowData info_temp = new InfoWindowData();

            markerOptions_temp.title(placeName);
            markerOptions_temp.snippet(vicinity);
            //info_temp.setImage("beermugsmall"); // here goes the image
            info_temp.setInstitution("Rating: " + rating);
            info_temp.setReview(open_now);
            //info.setPrice(types);

            LatLng latLng = new LatLng(lat, lng);
            markerOptions_temp.position(latLng);
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            try{
                double rating_d = Double.parseDouble(rating)*10;
                if (isBetween(rating_d, 35, 43)) {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beermug_halfrating));
                } else if (isBetween(rating_d, 43, 50)) {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beermug_fullrating));
                } else {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beermug_worstrating));
                }
            }catch(NumberFormatException e){
                markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beermug_worstrating));
            }


            //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("beermug1",100,100)));
            Marker m = mMap.addMarker(markerOptions_temp);
            // from tutorial for custom info window
            m.setTag(info_temp);

            markerList.add(m);
            markerOptionsList.add(markerOptions_temp);
            infoList.add(info_temp);

        }

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            //create string
            String photo_url = url;
            //prepare call for  picture request
            Object[] DataTransfer = new Object[9];
            DataTransfer[0] = mMap;
            DataTransfer[1] = photo_url;
            DataTransfer[2] = (Object) infoList.get(i);
            new GetPictures().execute(DataTransfer);
        }




    }

    public static boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }



}