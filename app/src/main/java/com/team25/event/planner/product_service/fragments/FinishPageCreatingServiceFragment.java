package com.team25.event.planner.product_service.fragments;

import static android.app.Activity.RESULT_OK;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentFinishPageCreatingServiceBinding;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

public class FinishPageCreatingServiceFragment extends Fragment {

    private ServiceAddFormViewModel mViewModel;
    private NavController navController;
    private static final int SELECT_PICTURE = 200;
    private static final int PERMISSION_REQUEST_CODE = 101;
    ShapeableImageView BSelectImage;
    ImageView IVPreviewImage;

    public static FinishPageCreatingServiceFragment newInstance() {
        return new FinishPageCreatingServiceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentFinishPageCreatingServiceBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finish_page_creating_service, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mViewModel= new ViewModelProvider(this).get(ServiceAddFormViewModel.class);
        binding.setViewModel(mViewModel);

        if(getArguments() != null){
            TextView textView = binding.EditOrCreateServiceText;
            textView.setText(R.string.edit_the_service);
        }

        BSelectImage = binding.BSelectImage;
        IVPreviewImage = binding.IVPreviewImage;

        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        setObservers();
        return binding.getRoot();
    }

    public void setObservers(){
        mViewModel.toFinish.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("New service")
                        .setMessage("You are successfully added a new service!")
                        .create();
                alertDialog.show();
                mViewModel.toFinish.setValue(false);
                navController.navigate(R.id.ownerHomePage, null, new NavOptions.Builder()
                        .setPopUpTo(R.id.ownerHomePage, false).build());
            }
        });
        mViewModel.toSecond.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigateUp();
                mViewModel.toSecond.setValue(false);

            }
        });
    }

    void imageChooser() {
        /*if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE
            );
        } else*/
        {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageChooser();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Unable to access images.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    IVPreviewImage.setImageURI(selectedImageUri);
                }else{
                    Toast.makeText(getContext(), "You did not select any picture", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}