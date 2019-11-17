package com.example.mso_projectxxx.ui.map;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mso_projectxxx.R;


import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;


public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;

    private MapView mapView;
    private String style = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        String token = "pk.eyJ1IjoibWFya3VzY29lbGxlbiIsImEiOiJjazMycWZmbTMwNG91M21ueWpxNTd2eXNsIn0.768mEmx89ddTwAaAhky6JQ";

        Mapbox.getInstance(this.getContext(), token);


        String[] asd= {"https://172.17.0.2:8080/styles/4aec165c-f1b5-47e7-ab6b-c9aab7bc2dd6/{z}/{x}/{y}.png"};
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) root.findViewById(R.id.map);

        //final ITileSource tileSource = new XYTileSource("Pastel",1, 18, 256, ".png", asd);
        //map.setTileSource(tileSource);


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                //InputStream is = getResources().openRawResource(R.raw.style);
                //String style = convertStreamToString(is);



                mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments


                    }
                });

            }
        });

        return root;
    }


    private static String convertStreamToString(InputStream stream){


       try {
           if (stream != null) {
               Writer writer = new StringWriter();

               char[] buffer = new char[1024];
               try {
                   Reader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                   int n;
                   while ((n = reader.read(buffer)) != -1) {
                       writer.write(buffer, 0, n);
                   }
               } finally {
                   stream.close();
               }
               return writer.toString();
           }
           return "";
       }
       catch (IOException e){
           return "";
       }
    }


}
