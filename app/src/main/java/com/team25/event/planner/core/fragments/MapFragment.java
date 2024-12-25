package com.team25.event.planner.core.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentMapBinding;
import com.team25.event.planner.user.model.Location;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {
    public static final String TITLE_ARG = "title";
    public static final String LOCATION_ARG = "location";

    private MapView map;
    private String title;
    private double latitude;
    private double longitude;
    private String address;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE_ARG);
            Location location = getArguments().getParcelable(LOCATION_ARG);
            if (location != null) {
                latitude = location.getLatitude() != null ? location.getLatitude() : 0.0;
                longitude = location.getLongitude() != null ? location.getLongitude() : 0.0;
                address = location.getAddress();
            }
        }

        // Initialize OSMdroid configuration
        Configuration.getInstance().setDebugMode(true);
        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        Configuration.getInstance().setUserAgentValue(requireActivity().getPackageName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMapBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        map = binding.map;

        if (hasAllPermissions()) {
            setupMap();
        } else {
            requestPermissions();
        }

        return binding.getRoot();
    }

    private boolean hasAllPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        requestPermissionLauncher.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        });
    }

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean allGranted = result.values().stream().allMatch(granted -> granted);
                if (allGranted) {
                    setupMap();
                }
            });

    private void setupMap() {
        // Set to HTTP tile source as a workaround for SSL handshake error
        map.setTileSource(new OnlineTileSourceBase("CustomTileSource",
                1, 20, 256, ".png",
                new String[]{"http://a.tile.openstreetmap.org/"}) {
            @Override
            public String getTileURLString(long pMapTileIndex) {
                return getBaseUrl() + MapTileIndex.getZoom(pMapTileIndex) + "/" +
                        MapTileIndex.getX(pMapTileIndex) + "/" +
                        MapTileIndex.getY(pMapTileIndex) + mImageFilenameEnding;
            }
        });
        // map.setTileSource(TileSourceFactory.MAPNIK);


        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        // Set the map center and zoom level
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        map.getController().setZoom(16.0);
        map.getController().setCenter(startPoint);

        // Add marker for the location
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        marker.setSnippet(address);
        map.getOverlays().add(marker);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}
