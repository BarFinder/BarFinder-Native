package com.kth.barfinder.barfinder_native;

/**
 * Created by armin on 08.02.2018.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import com.squareup.picasso.Callback;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private boolean testen = false;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {return null;}


    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_info_contents, null);



        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.details);
        ImageView img = view.findViewById(R.id.pic);

        TextView Institution_tv = view.findViewById(R.id.Institution);
        TextView Review_tv = view.findViewById(R.id.Review);
        TextView Price_tv = view.findViewById(R.id.Price);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        testen = infoWindowData.getpic_there();


        if (testen) {
            Picasso.with(context)
                    .load(infoWindowData.geturl())
                    .resize(400, 400)
                    .into(img);
        } else { // if it's the first time, load the image with the callback set
            testen=true;
            Picasso.with(context)
                    .load(infoWindowData.geturl())
                    .resize(400, 400)
                    .into(img,new InfoWindowRefresher(marker));
            infoWindowData.setpic_there(true);
        }

        Institution_tv.setText(infoWindowData.getInstitution());
        Review_tv.setText(infoWindowData.getReview());
        Price_tv.setText(infoWindowData.getPrice());

        return view;
    }

    private class InfoWindowRefresher implements Callback {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        //@Override
        public void onSuccess() {
            markerToRefresh.showInfoWindow();
        }

        //@Override
        public void onError() {}
    }
}


