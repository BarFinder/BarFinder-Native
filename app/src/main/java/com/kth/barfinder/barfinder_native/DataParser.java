package com.kth.barfinder.barfinder_native;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String place_id = "";
        String opening_hours = "";
        String rating = "";
        String types = "";
        String open_now = "";
        String photos = "";
        String photo_reference = "";


        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if (!googlePlaceJson.isNull("place_id")) {
                place_id = googlePlaceJson.getString("place_id");
            }
            if (!googlePlaceJson.isNull("rating")) {
                rating = googlePlaceJson.getString("rating");
            }
            if (!googlePlaceJson.isNull("types")) {
                types = googlePlaceJson.getString("types");
            }
            if (!googlePlaceJson.isNull("opening_hours")) {
                opening_hours = googlePlaceJson.getString("opening_hours");
                if (!googlePlaceJson.getJSONObject("opening_hours").isNull("open_now")) {
                    open_now = googlePlaceJson.getJSONObject("opening_hours").getString("open_now");
                }
            }
            if (!googlePlaceJson.isNull("photos")) {
                photos = googlePlaceJson.getString("photos");
            }
            if (!googlePlaceJson.isNull("photos")) {
                photo_reference = googlePlaceJson.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            place_id = googlePlaceJson.getString("place_id");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("place_id", place_id);
            googlePlaceMap.put("opening_hours", opening_hours);
            googlePlaceMap.put("rating", rating);
            googlePlaceMap.put("types", types);
            googlePlaceMap.put("open_now", open_now);
            googlePlaceMap.put("photos", photos);
            googlePlaceMap.put("photo_reference", photo_reference);

            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}
