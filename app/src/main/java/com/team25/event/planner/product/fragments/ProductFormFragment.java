package com.team25.event.planner.product.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.team25.event.planner.R;
import com.team25.event.planner.core.FileUtils;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.FragmentProductFormBinding;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.product.adapters.ProductImagesAdapter;
import com.team25.event.planner.product.model.ProductImage;
import com.team25.event.planner.product.viewmodel.ProductFormViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductFormFragment extends Fragment {
    public static final String ID_ARG_NAME = "PRODUCT_ID";

    private FragmentProductFormBinding binding;
    private ProductFormViewModel viewModel;
    private NavController navController;
    private ProductImagesAdapter imagesAdapter;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private final MediatorLiveData<List<ProductImage>> imagesLiveData = new MediatorLiveData<>(new ArrayList<>());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_form, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        viewModel = new ViewModelProvider(this).get(ProductFormViewModel.class);
        binding.setViewModel(viewModel);

        AuthViewModel authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        authViewModel.user.observe(getViewLifecycleOwner(), user -> viewModel.setOwnerId(user.getUserId()));

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        setupImagePicker();
        setupRecyclerView();
        setupObservers();
        setupListeners();

        viewModel.loadEventTypes();
        viewModel.loadOfferingCategories();

        if (getArguments() != null) {
            long productId = getArguments().getLong(ID_ARG_NAME);
            if (productId != -1) {
                viewModel.loadProduct(productId);
            }
        }

        return binding.getRoot();
    }

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
                            List<File> currentImages = viewModel.newImages.getValue();
                            assert currentImages != null;
                            currentImages.addAll(selectedImages);
                            viewModel.newImages.setValue(currentImages);
                        }
                    }
                }
        );
    }

    private void setupRecyclerView() {
        imagesAdapter = new ProductImagesAdapter(
                new ArrayList<>(),
                new ProductImagesAdapter.OnImageDeleteListener() {
                    @Override
                    public void onDeleteNewImage(File image) {
                        viewModel.removeNewImage(image);
                    }

                    @Override
                    public void onDeleteExistingImage(String url) {
                        viewModel.removeExistingImage(url);
                    }
                }
        );
        binding.imagesRecyclerView.setAdapter(imagesAdapter);
        binding.imagesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
    }

    private void setupObservers() {
        viewModel.eventTypes.observe(getViewLifecycleOwner(), this::updateEventTypeChips);

        viewModel.offeringCategories.observe(getViewLifecycleOwner(), this::updateOfferingCategoryDropdown);

        imagesLiveData.addSource(viewModel.newImages, newImages -> {
            List<String> existingImages = viewModel.existingImages.getValue();
            if (existingImages == null) existingImages = new ArrayList<>();
            List<ProductImage> images = existingImages.stream().map(ProductImage::fromUrl).collect(Collectors.toList());
            if (newImages != null) {
                images.addAll(newImages.stream().map(ProductImage::fromFile).collect(Collectors.toList()));
            }
            imagesLiveData.setValue(images);
        });

        imagesLiveData.addSource(viewModel.existingImages, existingImages -> {
            if (existingImages == null) existingImages = new ArrayList<>();
            List<ProductImage> images = existingImages.stream().map(ProductImage::fromUrl).collect(Collectors.toList());
            List<File> newImages = viewModel.newImages.getValue();
            if (newImages != null) {
                images.addAll(newImages.stream().map(ProductImage::fromFile).collect(Collectors.toList()));
            }
            imagesLiveData.setValue(images);
        });

        imagesLiveData.observe(getViewLifecycleOwner(), images -> imagesAdapter.refreshImages(images));

        viewModel.errors.observe(getViewLifecycleOwner(), errors -> {
            if (errors.getEventTypes() == null || errors.getEventTypes().isEmpty()) {
                binding.eventTypesErrorText.setVisibility(View.GONE);
            } else {
                binding.eventTypesErrorText.setVisibility(View.VISIBLE);
            }
            if (errors.getImages() == null || errors.getImages().isEmpty()) {
                binding.imagesErrorText.setVisibility(View.GONE);
            } else {
                binding.imagesErrorText.setVisibility(View.VISIBLE);
            }
        });

        viewModel.successSignal.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                viewModel.successSignal.setValue(false);
                Snackbar.make(binding.getRoot(), "Product saved successfully", Snackbar.LENGTH_LONG).show();
                navController.navigateUp();
            }
        });

        viewModel.serverError.observe(getViewLifecycleOwner(), errorMessage -> {
            Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG).show();
        });
    }

    private void setupListeners() {
        binding.selectImagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            imagePickerLauncher.launch(intent);
        });
    }

    private void updateEventTypeChips(List<EventType> eventTypes) {
        binding.eventTypesChipGroup.removeAllViews();
        for (EventType eventType : eventTypes) {
            Chip chip = new Chip(requireContext());
            chip.setText(eventType.getName());
            chip.setCheckable(true);
            chip.setChecked(viewModel.eventTypeIds.getValue().contains(eventType.getId()));
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                List<Long> selectedIds = new ArrayList<>(viewModel.eventTypeIds.getValue());
                if (isChecked) {
                    selectedIds.add(eventType.getId());
                } else {
                    selectedIds.remove(eventType.getId());
                }
                viewModel.eventTypeIds.setValue(selectedIds);
            });
            binding.eventTypesChipGroup.addView(chip);
        }
    }

    private void updateOfferingCategoryDropdown(List<OfferingCategory> categories) {
        AutoCompleteTextView dropdown = binding.offeringCategoryDropdown;
        ArrayAdapter<OfferingCategory> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categories
        );
        dropdown.setAdapter(adapter);
        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            OfferingCategory selected = adapter.getItem(position);
            if (selected == null) {
                viewModel.offeringCategoryId.setValue(null);
                viewModel.offeringCategoryName.setValue(null);
            } else {
                viewModel.offeringCategoryId.setValue(selected.getId());
                viewModel.offeringCategoryName.setValue(selected.getName());
            }
        });
    }
}