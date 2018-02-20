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
            String photo_reference = googlePlace.get("photo_reference");


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
           // info_temp.setImage("beer0"); // here goes the image
            info_temp.setInstitution("Rating: " + rating);
            info_temp.setReview(open_now);
            //info.setPrice(types);

            info_temp.setImage("beer3");

            //check if photo reference is retrieved correctly:
            info_temp.setPrice(photo_reference);


            LatLng latLng = new LatLng(lat, lng);
            markerOptions_temp.position(latLng);
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

            try{
                double rating_d = Double.parseDouble(rating)*10;
                if (isBetween(rating_d, 0, 9)) {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beer0));
                    //info_temp.setImage("beer0");
                }
                if (isBetween(rating_d, 10, 19)) {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beer1));
                   // info_temp.setImage("beer1");
                }
                if (isBetween(rating_d, 20, 29)) {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beer2));
                   // info_temp.setImage("beer2");
                }
                if (isBetween(rating_d, 30, 39)) {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beer3));
                   // info_temp.setImage("beer3");
                }
                if (isBetween(rating_d, 40, 49)) {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beer4));
                   // info_temp.setImage("beer4");
                }
                if (isBetween(rating_d, 50, 50)) {
                    markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beer5));
                   // info_temp.setImage("beer5");
                }
            }catch(NumberFormatException e){
                markerOptions_temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.beermugsmall));
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
            // String photo_url = url;

            //constructing the photo url:
            // https://maps.googleapis.com/maps/api/place/photo?maxwidth=400
            // &photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU
            // &key=YOUR_API_KEY

            StringBuilder googlePictureURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400");
            //StringBuilder googlePictureURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePictureURL.append("&photoreference=CmRaAAAARtnVI-mDfl0UEYp2oS4yOumDwhJs4GV_Y5Yi8XyK-OokwZHqla0XNEquAfgoSR2dUp8zWVaniUpLDluWuyIkvbDuQpUr-3RQy04UKI3_gYvCUa4hdx8293eu_Wa0IFtcEhCQWlbCH1jkn9toQaY5V8GJGhQ50c3imi9DjT_CQVjow8LkzFMzZA");
            googlePictureURL.append("&key=" + "AIzaSyBUzyWVeFIzmAuDU4-083QM-gdbFZG8izc");
            String photo_url = googlePictureURL.toString();


            //create string
            //  String photo_url = url;
            //prepare call for  picture request


            // String photo_url = url;
            //prepare call for  picture request
            Object[] DataTransfer = new Object[9];
            DataTransfer[0] = mMap;
            DataTransfer[1] = photo_url;
            DataTransfer[2] = (Object) infoList.get(i);
            new GetPictures().execute(DataTransfer);
        }
        /*
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            //create string
           // String photo_url = url;
            String photo_url = url;
            //prepare call for  picture request
            Object[] DataTransfer = new Object[9];
            DataTransfer[0] = mMap;
            DataTransfer[1] = photo_url;
            DataTransfer[2] = (Object) infoList.get(i);
            new GetPictures().execute(DataTransfer);
        }
        */




    }

    public static boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }



}