package com.team25.event.planner.product_service.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentOwnerHomePageBinding;

import java.util.ArrayList;
import java.util.List;


public class OwnerHomePage extends Fragment {
    private FragmentOwnerHomePageBinding binding;


    public OwnerHomePage() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOwnerHomePageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddServiceFragment fullScreenFragment = new AddServiceFragment();
                fullScreenFragment.show(getParentFragmentManager(), "FullScreenFragment");
            }
        });



        binding.imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
                View dialogView = getLayoutInflater().inflate(R.layout.fragment_filter_service, null);

                Spinner spinner = dialogView.findViewById(R.id.spinner);
                List<String> options = new ArrayList<>();
                options.add("Select a category"); // Placeholder
                options.add("category 1");
                options.add("category 2");
                options.add("category 3");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                Spinner spinner2 = dialogView.findViewById(R.id.spinner2);
                options.clear();
                options.add("Select an event type");
                options.add("event type 1");
                options.add("event type 3");
                options.add("event type 2");

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                bottomSheetDialog.setContentView(dialogView);
                Button cancelButton = dialogView.findViewById(R.id.button); // Zamenite sa tačnim ID-jem vašeg cancel dugmeta
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Zatvorite BottomSheetDialog
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }

        });

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(binding.scrollServices.getId(), new ServiceContainerFragment())
                    .commit();
        }

    }
}