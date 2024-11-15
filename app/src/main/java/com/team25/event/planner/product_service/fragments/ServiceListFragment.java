package com.team25.event.planner.product_service.fragments;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team25.event.planner.R;
import com.team25.event.planner.product_service.adapters.ServiceListAdapter;
import com.team25.event.planner.product_service.model.Service;
import java.util.ArrayList;

public class ServiceListFragment extends ListFragment {
    private ServiceListAdapter adapter;
    private ArrayList<Service> services;
    private static final String ARG_PARAM = "param";

    public ServiceListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Products List Fragment");

        if (getArguments() != null) {
            services = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ServiceListAdapter(getActivity(), services, requireActivity());
            setListAdapter(adapter);
            //recyclerView.setAdapter(adapter);
        }
    }

    public static ServiceListFragment newInstance(ArrayList<Service> services){
        ServiceListFragment fragment = new ServiceListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, services);
        fragment.setArguments(args);
        return fragment;
    }
}