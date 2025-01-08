package com.team25.event.planner.product_service.fragments;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentFinishPageCreatingServiceBinding;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

import java.util.ArrayList;
import java.util.List;

public class FinishPageCreatingServiceFragment extends Fragment {

    private ServiceAddFormViewModel mViewModel;
    private NavController navController;
    private static final int SELECT_PICTURE = 200;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private FragmentFinishPageCreatingServiceBinding binding;
    ImageView IVPreviewImage;
    private LinearLayout imageContainer;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> galleryLauncher;
    AuthViewModel authViewModel;

    public static FinishPageCreatingServiceFragment newInstance() {
        return new FinishPageCreatingServiceFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finish_page_creating_service, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mViewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.nav_graph)
        ).get(ServiceAddFormViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mViewModel.ownerId.postValue(authViewModel.getUserId());
        imageContainer = binding.imageContainer;
        binding.setViewModel(mViewModel);

        ImageButton btnAddImage = binding.btnAddImage;
        btnAddImage.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(1024) // max size 1MB
                    .createIntent((intent) -> {
                        startForProfileImageResult.launch(intent);
                        return null;
                    });
        });

        if(getArguments() != null){
            TextView textView = binding.EditOrCreateServiceText;
            textView.setText(R.string.edit_the_service);
        }
        mViewModel._addedService.setValue(false);
        setListeners();
        setObservers();
        return binding.getRoot();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel._addedService.setValue(false);
    }

    private void addImageToContainer(Uri imageUri) {
        ShapeableImageView imageView = new ShapeableImageView(requireContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16); // Margin između slika
        imageView.setLayoutParams(params);

        imageView.setImageURI(imageUri);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(4, 4, 4, 4);

        imageView.setShapeAppearanceModel(
                imageView.getShapeAppearanceModel()
                        .toBuilder()
                        .setAllCornerSizes(50)
                        .build()
        );


        imageContainer.addView(imageView);
    }

    public void setListeners(){
        binding.buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewModel.validateForm3()){
                    mViewModel.createService();
                }
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp();
            }
        });
    }
    public void setObservers(){

        mViewModel._addedService.observe(getViewLifecycleOwner(), check ->{
            if(check){
                new AlertDialog.Builder(requireContext())
                        .setTitle("Information")
                        .setMessage("You successfully added a new service")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                navController.navigate(R.id.action_finishPageCreateingCerviceFragment_to_ownerHomePage);
            }
        });

        mViewModel.errorMessageFromServer.observe(getViewLifecycleOwner(), errorMessage -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Information")
                    .setMessage(errorMessage)
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        });
        mViewModel.images.observe(getViewLifecycleOwner(), urls -> {
            LinearLayout container = requireView().findViewById(R.id.imageContainer);
            container.removeAllViews();

            for (int i = 0; i < urls.size(); i++) {
                String url = urls.get(i);

                LinearLayout rowLayout = new LinearLayout(requireContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                rowLayout.setPadding(16, 16, 16, 16);

                ImageView imageView = new ImageView(requireContext());
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                );
                imageView.setLayoutParams(imageParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(requireContext()).load(url).into(imageView);

                ImageButton deleteButton = new ImageButton(requireContext());
                deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                Drawable deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.delete_icon);
                if (deleteIcon != null) {
                    deleteIcon.setTint(Color.RED);
                }
                deleteButton.setImageDrawable(deleteIcon);
                deleteButton.setBackground(null);
                deleteButton.setPadding(16, 16, 16, 16);
                deleteButton.setOnClickListener(v -> {
                    mViewModel.removeImageUrl(url);
                });

                rowLayout.addView(imageView);
                rowLayout.addView(deleteButton);

                container.addView(rowLayout);
            }
        });

    }

    private final ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        int resultCode = result.getResultCode();
        Intent data = result.getData();

        if (resultCode == Activity.RESULT_OK) {
            Uri fileUri = data != null ? data.getData() : null;
            if (fileUri != null) {
                addImageToContainer(fileUri);
                List<String> img = mViewModel.images.getValue();
                if(img==null){
                    img = new ArrayList<>();
                }else {
                    img = new ArrayList<>(img);
                }
                img.add(String.valueOf(fileUri));
                mViewModel.images.postValue(img);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    });


}