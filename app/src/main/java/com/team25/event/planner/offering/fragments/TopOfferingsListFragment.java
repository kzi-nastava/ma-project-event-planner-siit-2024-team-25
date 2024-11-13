package com.team25.event.planner.offering.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.team25.event.planner.databinding.FragmentTopOfferingsListBinding;
import com.team25.event.planner.offering.adapters.TopOfferingsListAdapter;
import com.team25.event.planner.offering.model.Offering;
import java.util.ArrayList;

public class TopOfferingsListFragment extends Fragment {

    private FragmentTopOfferingsListBinding binding;
    private TopOfferingsListAdapter adapter;
    private ArrayList<Offering> topOffers;
    private static final String ARG_PARAM = "param";

    public TopOfferingsListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTopOfferingsListBinding.inflate(inflater, container, false);

        ListView listView = binding.list;
        adapter = new TopOfferingsListAdapter(getContext(), topOffers);
        listView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Products List Fragment");
        if (getArguments() != null) {
            topOffers = getArguments().getParcelableArrayList(ARG_PARAM);
        } else {
            topOffers = new ArrayList<>();
        }
    }

    public static TopOfferingsListFragment newInstance(ArrayList<Offering> offers) {
        TopOfferingsListFragment fragment = new TopOfferingsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, offers);
        fragment.setArguments(args);
        return fragment;
    }
}
