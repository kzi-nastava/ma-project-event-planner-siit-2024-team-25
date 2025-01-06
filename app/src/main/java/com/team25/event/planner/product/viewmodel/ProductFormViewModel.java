package com.team25.event.planner.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.product.api.ProductApi;
import com.team25.event.planner.product.model.Product;
import com.team25.event.planner.product.model.ProductRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ProductFormViewModel extends ViewModel {
    private final ProductApi productApi = ConnectionParams.productApi;
    private final EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;
    private final OfferingCategoryApi offeringCategoryApi = ConnectionParams.offeringCategoryApi;

    // Entities for selections
    private final MutableLiveData<List<EventType>> _eventTypes = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<EventType>> eventTypes = _eventTypes;

    private final MutableLiveData<List<OfferingCategory>> _offeringCategories = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<OfferingCategory>> offeringCategories = _offeringCategories;

    // Form fields
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<Double> price = new MutableLiveData<>();
    public final MutableLiveData<Double> discount = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isVisible = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>(true);
    public final MutableLiveData<List<Long>> eventTypeIds = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<Long> offeringCategoryId = new MutableLiveData<>();
    public final MutableLiveData<String> offeringCategoryName = new MutableLiveData<>();
    public final MutableLiveData<List<File>> images = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<String>> imagesToDelete = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<List<String>> _existingImages = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<String>> existingImages = _existingImages;

    private Long productId;

    @Setter
    private Long ownerId;

    // Output fields
    @Data
    @Builder
    public static class ErrorUiState {
        private final String name;
        private final String description;
        private final String price;
        private final String discount;
        private final String isVisible;
        private final String isAvailable;
        private final String eventTypes;
        private final String offeringCategory;
        private final String images;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public final MutableLiveData<Boolean> successSignal = new MutableLiveData<>(false);

    public void loadEventTypes() {
        eventTypeApi.getEventTypes().enqueue(new ResponseCallback<>(
                _eventTypes::postValue,
                _serverError, "MyProductsViewModel")
        );
    }

    public void loadOfferingCategories() {
        offeringCategoryApi.getOfferingCategories().enqueue(new ResponseCallback<>(
                _offeringCategories::postValue,
                _serverError, "MyProductsViewModel")
        );
    }

    public void loadProduct(Long productId) {
        if (productId == null) {
            resetForm();
            return;
        }
        productApi.getProduct(productId).enqueue(new ResponseCallback<>(
                this::populateForm,
                _serverError, "ProductFormViewModel"
        ));
    }

    public void onSubmit() {
        if (validateForm()) {
            createProduct();
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        ErrorUiState.ErrorUiStateBuilder errorBuilder = ErrorUiState.builder();

        if (name.getValue() == null || name.getValue().trim().isEmpty()) {
            errorBuilder.name("Name is required.");
            isValid = false;
        }

        if (description.getValue() == null || description.getValue().trim().isEmpty()) {
            errorBuilder.description("Description is required.");
            isValid = false;
        }

        if (price.getValue() == null) {
            errorBuilder.price("Price is required.");
            isValid = false;
        } else if (price.getValue() <= 0) {
            errorBuilder.price("Price must be greater than 0.");
            isValid = false;
        }

        if (discount.getValue() == null) {
            errorBuilder.discount("Discount is required.");
            isValid = false;
        } else if (discount.getValue() < 0 || discount.getValue() > 100) {
            errorBuilder.discount("Discount must be between 0 and 100.");
            isValid = false;
        }

        if (eventTypeIds.getValue() == null || eventTypeIds.getValue().isEmpty()) {
            errorBuilder.eventTypes("At least one event type is required.");
            isValid = false;
        }

        if (offeringCategoryId.getValue() == null && (offeringCategoryName.getValue() == null || offeringCategoryName.getValue().trim().isEmpty())) {
            errorBuilder.offeringCategory("Offering category is required.");
            isValid = false;
        }

        if (images.getValue() == null || images.getValue().isEmpty()) {
            errorBuilder.images("At least one image is required.");
            isValid = false;
        }

        _errors.setValue(errorBuilder.build());

        return isValid;
    }

    private void createProduct() {
        if (ownerId == null) {
            _serverError.postValue("You must be owner to create a product!");
            return;
        }

        final RequestBody body = new ProductRequest(
                name.getValue(),
                description.getValue(),
                price.getValue(),
                discount.getValue(),
                images.getValue(),
                isVisible.getValue(),
                isAvailable.getValue(),
                eventTypeIds.getValue(),
                offeringCategoryId.getValue(),
                offeringCategoryName.getValue(),
                ownerId,
                imagesToDelete.getValue()
        ).buildBody();

        Call<Product> call = productId == null ? productApi.createProduct(body) : productApi.updateProject(productId, body);

        call.enqueue(new ResponseCallback<>(
                product -> {
                    successSignal.postValue(true);
                    resetForm();
                    productId = product.getId();
                    populateForm(product);
                },
                _serverError, "ProductFormViewModel"
        ));
    }

    private void populateForm(Product product) {
        name.setValue(product.getName());
        description.setValue(product.getDescription());
        price.setValue(product.getPrice());
        discount.setValue(product.getDiscount());
        isVisible.setValue(product.isVisible());
        isAvailable.setValue(product.isAvailable());
        eventTypeIds.setValue(product.getEventTypes().stream().map(et -> et.getId()).collect(Collectors.toList()));
        offeringCategoryId.setValue(product.getOfferingCategory().getId());
        offeringCategoryName.setValue(product.getOfferingCategory().getName());
        images.setValue(new ArrayList<>());
        imagesToDelete.setValue(new ArrayList<>());
        _existingImages.setValue(product.getImages());
    }

    private void resetForm() {
        name.setValue(null);
        description.setValue(null);
        price.setValue(null);
        discount.setValue(null);
        isVisible.setValue(true);
        isAvailable.setValue(true);
        eventTypeIds.setValue(new ArrayList<>());
        offeringCategoryId.setValue(null);
        offeringCategoryName.setValue(null);
        images.setValue(new ArrayList<>());
        imagesToDelete.setValue(new ArrayList<>());
        _existingImages.setValue(new ArrayList<>());
    }
}