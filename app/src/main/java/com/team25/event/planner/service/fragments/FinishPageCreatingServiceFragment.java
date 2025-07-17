package com.team25.event.planner.service.fragments;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.team25.event.planner.R;
import com.team25.event.planner.core.FileUtils;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentFinishPageCreatingServiceBinding;
import com.team25.event.planner.product.adapters.ProductImagesAdapter;
import com.team25.event.planner.product.model.ProductImage;
import com.team25.event.planner.service.adapters.ServiceImageAdapter;
import com.team25.event.planner.service.model.ServiceImage;
import com.team25.event.planner.service.viewModels.ServiceAddFormViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private final MediatorLiveData<List<ServiceImage>> imagesLiveData = new MediatorLiveData<>(new ArrayList<>());
    private ServiceImageAdapter imagesAdapter;
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
        binding.setViewModel(mViewModel);

        ImageButton btnAddImage = binding.selectImagesButton;
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
        setupImagePicker();
        setupRecyclerView();
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
        binding.selectImagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            imagePickerLauncher.launch(intent);
        });
    }
    public void setObservers(){
        mViewModel.errors.observe(getViewLifecycleOwner(), errors -> {
            if (errors != null && errors.getImage() != null) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Failed")
                        .setMessage("You must chose 1 image at least")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
        mViewModel._addedService.observe(getViewLifecycleOwner(), check ->{
            if(check){
                new AlertDialog.Builder(requireContext())
                        .setTitle("Information")
                        .setMessage("You successfully saved a service")
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
        imagesLiveData.addSource(mViewModel.newImages, newImages -> {
            List<String> existingImages = mViewModel.existingImages.getValue();
            if (existingImages == null) existingImages = new ArrayList<>();
            List<ServiceImage> images = existingImages.stream().map(ServiceImage::fromUrl).collect(Collectors.toList());
            if (newImages != null) {
                images.addAll(newImages.stream().map(ServiceImage::fromFile).collect(Collectors.toList()));
            }
            imagesLiveData.setValue(images);
        });

        imagesLiveData.addSource(mViewModel.existingImages, existingImages -> {
            if (existingImages == null) existingImages = new ArrayList<>();
            List<ServiceImage> images = existingImages.stream().map(ServiceImage::fromUrl).collect(Collectors.toList());
            List<File> newImages = mViewModel.newImages.getValue();
            if (newImages != null) {
                images.addAll(newImages.stream().map(ServiceImage::fromFile).collect(Collectors.toList()));
            }
            imagesLiveData.setValue(images);
        });

        imagesLiveData.observe(getViewLifecycleOwner(), images -> imagesAdapter.refreshImages(images));
    }
    private void observeImageSources() {
        imagesLiveData.addSource(mViewModel.newImages, newImages -> combineImages());
        imagesLiveData.addSource(mViewModel.existingImages, existingImages -> combineImages());
    }

    private void combineImages() {
        List<String> existingImages = mViewModel.existingImages.getValue();
        List<File> newImages = mViewModel.newImages.getValue();

        List<ServiceImage> combined = new ArrayList<>();

        if (existingImages != null) {
            combined.addAll(existingImages.stream().map(ServiceImage::fromUrl).collect(Collectors.toList()));
        }

        if (newImages != null) {
            combined.addAll(newImages.stream().map(ServiceImage::fromFile).collect(Collectors.toList()));
        }

        imagesLiveData.setValue(combined);
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
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            List<File> selectedImages = new ArrayList<>();
                            if (data.getClipData() != null) {
                                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                    Uri uri = data.getClipData().getItemAt(i).getUri();
                                    File file = FileUtils.getFileFromUri(getContext(), uri);
                                    selectedImages.add(file);
                                }
                            } else if (data.getData() != null) {
                                Uri uri = data.getData();
                                File file = FileUtils.getFileFromUri(getContext(), uri);
                                selectedImages.add(file);
                            }
                            if (selectedImages.isEmpty()) return;
                            List<File> currentImages = mViewModel.newImages.getValue();
                            assert currentImages != null;
                            currentImages.addAll(selectedImages);
                            mViewModel.newImages.setValue(currentImages);
                        }
                    }
                }
        );
    }
    private void setupRecyclerView() {
        imagesAdapter = new ServiceImageAdapter(
                new ArrayList<>(),
                new ServiceImageAdapter.OnImageDeleteListener() {
                    @Override
                    public void onDeleteNewImage(File image) {
                        mViewModel.removeNewImage(image);
                    }

                    @Override
                    public void onDeleteExistingImage(String url) {
                        mViewModel.removeExistingImage(url);
                    }
                }
        );
        binding.imagesRecyclerView.setAdapter(imagesAdapter);
        binding.imagesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
    }

}