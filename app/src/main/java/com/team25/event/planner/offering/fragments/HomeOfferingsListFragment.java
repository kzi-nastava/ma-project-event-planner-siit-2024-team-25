package com.team25.event.planner.offering.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.ListFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.event.adapters.HomeEventListAdapter;
import com.team25.event.planner.offering.adapters.HomeOfferingListAdapter;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.viewmodel.HomeOfferingViewModel;

import java.util.ArrayList;

public class HomeOfferingsListFragment extends ListFragment implements SensorEventListener {
    private HomeOfferingListAdapter adapter;
    private HomeOfferingViewModel _homeOfferingViewModel;
    private Long _eventId;
    private SensorManager sensorManager;
    private final int SHAKE_THRESHOLD = 800;
    private final int SHAKE_TIME_LAPSE = 200;
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;
    private long timeShake = 0;



    private HomeOfferingsListFragment(HomeOfferingViewModel homeOfferingViewModel, Long eventId) {
        this._homeOfferingViewModel = homeOfferingViewModel;
        this._eventId = eventId;
    }


    public static HomeOfferingsListFragment newInstance(HomeOfferingViewModel homeOfferingViewModel, Long eventId) {
        return new HomeOfferingsListFragment(homeOfferingViewModel,eventId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _homeOfferingViewModel.allOfferings.observe(getViewLifecycleOwner(), (offerings -> {
            NavController navController = Navigation.findNavController(requireView());
            adapter = new HomeOfferingListAdapter(getActivity(), offerings, navController, this._eventId);
            setListAdapter(adapter);
        }));
        return inflater.inflate(R.layout.fragment_home_offerings_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > SHAKE_TIME_LAPSE) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = sensorEvent.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    onShake();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void onShake() {
        timeShake++;
        if(timeShake%2 == 0){
            _homeOfferingViewModel.offeringFilterDTO.selectedSortCriteria.setValue("Ascending");

        }else{
            _homeOfferingViewModel.offeringFilterDTO.selectedSortCriteria.setValue("Descending");
        }
        _homeOfferingViewModel.offeringFilterDTO.selectedSortBy.setValue("Price");
        _homeOfferingViewModel.getOfferings();
    }
}