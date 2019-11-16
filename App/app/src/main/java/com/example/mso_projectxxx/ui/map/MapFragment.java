package com.example.mso_projectxxx.ui.map;

import android.os.Bundle;
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

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.api.IMapController;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;


    MapView map = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        String[] asd= {"https://172.17.0.2:8080/styles/4aec165c-f1b5-47e7-ab6b-c9aab7bc2dd6/{z}/{x}/{y}.png"};
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        map = root.findViewById(R.id.map);

        //final ITileSource tileSource = new XYTileSource("Pastel",1, 18, 256, ".png", asd);
        //map.setTileSource(tileSource);

        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(49.488668, 8.479966);
        mapController.setCenter(startPoint);

        return root;
    }
}