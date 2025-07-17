package com.team25.event.planner.service.viewModels;

import android.util.Log;
import android.widget.SeekBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.model.EventTypePreviewDTO;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.service.api.ServiceApi;
import com.team25.event.planner.service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.service.dto.ServiceCreateResponseDTO;
import com.team25.event.planner.service.enums.ReservationType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAddFormViewModel extends ViewModel {
    private final ServiceApi serviceApi = ConnectionParams.serviceApi;
    private final EventTypeApi eventTypeApi = ConnectionParams.eventTypeApi;
    private final OfferingCategoryApi offeringCategoryApi = ConnectionParams.offeringCategoryApi;
    public final MutableLiveData<Boolean> isEditMode = new MutableLiveData<>(false);
    public MutableLiveData<String> errorMessageFromServer = new MutableLiveData<>();

    private ServiceCreateRequestDTO serviceCreateRequestDTO;

    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public String nameService;
    public final MutableLiveData<String> description = new MutableLiveData<>("");
    public final MutableLiveData<String> specifics = new MutableLiveData<>();
    public final MutableLiveData<String> priceString = new MutableLiveData<>();
    public final MutableLiveData<Double> price = new MutableLiveData<>(0.0);
    public final MutableLiveData<Integer> discount = new MutableLiveData<>(0);
    public final MutableLiveData<List<String>> images = new MutableLiveData<>(List.of());
    public final MutableLiveData<Boolean> isVisible = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>(false);
    public final MutableLiveData<Integer> reservationDeadline = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> cancelDeadline = new MutableLiveData<>(0);
    public final MutableLiveData<ReservationType> confirmationType = new MutableLiveData<>();
    public final MutableLiveData<Boolean> confirmationTypeToggle = new MutableLiveData<>();
    public final MutableLiveData<String> categoryInput = new MutableLiveData<>();
    public final MutableLiveData<Boolean> categoryInputEnabled = new MutableLiveData<>();
    public final MutableLiveData<Integer> duration = new MutableLiveData<>(1);
    public final MutableLiveData<Integer> minArrangement = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> maxArrangement = new MutableLiveData<>(0);
    public final MutableLiveData<Boolean> toggle = new MutableLiveData<>();
    public final MutableLiveData<Long> offeringCategoryId = new MutableLiveData<>();
    public final MutableLiveData<String> offeringCategoryNewName = new MutableLiveData<>();
    public final MutableLiveData<List<Long>> eventTypeIds = new MutableLiveData<>();
    public final MutableLiveData<Long> ownerId = new MutableLiveData<>();
    public final MutableLiveData<Long> serviceId = new MutableLiveData<>();
    public final MutableLiveData<List<EventType>> eventTypesLive = new MutableLiveData<>();
    public final MutableLiveData<OfferingCategory> offeringCategoryMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<File>> newImages = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<String>> imagesToDelete = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<List<String>> _existingImages = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<String>> existingImages = _existingImages;

    public void removeImageUrl(String url) {
        if (images.getValue() != null) {
            List<String> updatedList = new ArrayList<>(images.getValue());
            updatedList.remove(url);
            images.setValue(updatedList);
        }
    }

    @Data
    @Builder(toBuilder = true)
    public static class ErrorUiState {
        private final String name;
        private final String description;
        private final String price;
        private final String arrangement;
        private final String image;
        private final String offeringCategory;
        private final String eventType;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;
    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;
    public final MutableLiveData<Boolean> _addedService = new MutableLiveData<>(false);


    public void onSeeking(SeekBar seekBar, Integer seekParams) {
        if (seekBar.getId() == R.id.durationValue) {
            duration.setValue(seekParams);
        } else if (seekBar.getId() == R.id.minArrValue) {
            minArrangement.setValue(seekParams);
        } else if (seekBar.getId() == R.id.maxArrValue) {
            maxArrangement.setValue(seekParams);
        } else if (seekBar.getId() == R.id.priceSeekBar2) {
            discount.setValue(seekParams);
        } else if (seekBar.getId() == R.id.cancellationSeekbar) {
            cancelDeadline.setValue(seekParams);
        } else if (seekBar.getId() == R.id.reservationSeekbar) {
            reservationDeadline.setValue(seekParams);
        }
    }

    public boolean validateInputNumber(String text) {

        try {
            Double number = Double.parseDouble(text);
            price.setValue(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public boolean validateForm3(){

        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;

        if ((newImages.getValue() == null || newImages.getValue().isEmpty()) &&
                (existingImages.getValue() == null || existingImages.getValue().isEmpty())
        ){
            errorUiStateBuilder.image("You choose at least 1 image");
            isValid = false;
        }

        _errors.setValue(errorUiStateBuilder.build());
        return isValid;
    }
    public boolean validateForm2(){
        Integer minArrangement = this.minArrangement.getValue();
        Integer maxArrangement = this.maxArrangement.getValue();
        Long offeringId = this.offeringCategoryId.getValue();
        List<Long> eventIds = this.eventTypeIds.getValue();
        Boolean toggle = this.toggle.getValue();
        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;
        boolean check = false;
        if(offeringId == null && Objects.equals(categoryInput.getValue(), "")){
            errorUiStateBuilder.offeringCategory("Offering category is required");
            isValid = false;
        }

        if(eventIds == null || eventIds.isEmpty()){
            errorUiStateBuilder.eventType("You must have at least 1 event type");
            isValid = false;
        }
        if(Boolean.TRUE.equals(toggle)){
            if(minArrangement == null || minArrangement == 0){
                errorUiStateBuilder.arrangement("Minimum arrangement is required");
                isValid = false;
                check = true;
            } else if (maxArrangement == null || maxArrangement == 0) {
                errorUiStateBuilder.arrangement("Maximum arrangement is required");
                isValid = false;
                check = true;
            }
            if(!check){
                if(minArrangement > maxArrangement){
                    errorUiStateBuilder.arrangement("Maximum arrangement must be greater than minimum");
                    isValid = false;
                }
            }
        }
        _errors.setValue(errorUiStateBuilder.build());
        return isValid;
    }

    public boolean validateForm() {
        String name = this.name.getValue();
        String description = this.description.getValue();
        String priceStringValue = this.priceString.getValue();
        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;

        if (priceStringValue == null || priceStringValue.isBlank()) {
            errorUiStateBuilder.price("Price is required");
            isValid = false;
        } else {
            if (validateInputNumber(priceStringValue)) {
                price.setValue(Double.valueOf(priceStringValue));
            } else {
                errorUiStateBuilder.price("Price must be a number");
                isValid = false;
            }
        }

        if (name == null || name.isBlank()) {
            errorUiStateBuilder.name("Name is required.");
            isValid = false;
        }
        if (description == null || description.isBlank()) {
            errorUiStateBuilder.description("Description is required.");
            isValid = false;
        }


        _errors.setValue(errorUiStateBuilder.build());
        return isValid;

    }

    private void syncFront() {
        if (confirmationType.getValue() == ReservationType.AUTOMATIC) {
            confirmationTypeToggle.setValue(true);
        } else {
            confirmationTypeToggle.setValue(false);
        }
        categoryInputEnabled.setValue(false);
    }

    public void restart() {
        categoryInputEnabled.setValue(true);
    }

    private ReservationType getReservationType() {
        if (Boolean.TRUE.equals(confirmationTypeToggle.getValue())) {
            return ReservationType.AUTOMATIC;
        } else {
            return ReservationType.MANUAL;
        }
    }

    public void setUpServiceId(Long id){
        serviceId.setValue(id);
        if(id!=null){
            fetchService(id);
        }else{
            resetForm();
            restart();
        }
    }

    public final MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();
    public void deleteService(Long serviceId){
        serviceApi.deleteService(serviceId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isDeleted.setValue(true);
                } else {
                    errorMessageFromServer.setValue(response.errorBody().toString());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void fetchService(Long idService) {
        serviceApi.getServiceResponse(idService).enqueue(new Callback<ServiceCreateResponseDTO>() {
            @Override
            public void onResponse(Call<ServiceCreateResponseDTO> call, Response<ServiceCreateResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    serviceId.postValue(idService);
                    fillTheForm(response.body());
                } else {
                    errorMessageFromServer.setValue(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ServiceCreateResponseDTO> call, Throwable t) {

            }
        });
    }
    public Map<String, RequestBody> convertToRequestBodyMap(ServiceCreateRequestDTO dto) {
        Map<String, RequestBody> map = new HashMap<>();

        if (dto.getName() != null)
            map.put("name", createPart(dto.getName()));

        map.put("description", createPart(dto.getDescription()));
        map.put("price", createPart(String.valueOf(dto.getPrice())));
        map.put("discount", createPart(String.valueOf(dto.getDiscount())));
        map.put("visible", createPart(String.valueOf(dto.isVisible())));
        map.put("available", createPart(String.valueOf(dto.isAvailable())));
        map.put("specifics", createPart(dto.getSpecifics()));
        map.put("reservationType", createPart(dto.getReservationType().name()));
        map.put("duration", createPart(String.valueOf(dto.getDuration())));
        map.put("reservationDeadline", createPart(String.valueOf(dto.getReservationDeadline())));
        map.put("cancellationDeadline", createPart(String.valueOf(dto.getCancellationDeadline())));
        map.put("minimumArrangement", createPart(String.valueOf(dto.getMinimumArrangement())));
        map.put("maximumArrangement", createPart(String.valueOf(dto.getMaximumArrangement())));
        map.put("offeringCategoryID", createPart(String.valueOf(dto.getOfferingCategoryID())));
        map.put("offeringCategoryName", createPart(dto.getOfferingCategoryName()));
        map.put("ownerId", createPart(String.valueOf(dto.getOwnerId())));

        // eventTypesIDs[0], eventTypesIDs[1], ...
        List<Long> ids = dto.getEventTypesIDs();
        if (ids != null) {
            for (int i = 0; i < ids.size(); i++) {
                map.put("eventTypesIDs[" + i + "]", createPart(String.valueOf(ids.get(i))));
            }
        }
        List<String> imagesToDelete = dto.getImagesToDelete();
        if (imagesToDelete != null) {
            for (int i = 0; i < imagesToDelete.size(); i++) {
                String fullUrl = imagesToDelete.get(i);
                String fileName = fullUrl.substring(fullUrl.lastIndexOf('/') + 1);
                map.put("imagesToDelete[" + i + "]", createPart(fileName));
            }
        }


        return map;
    }

    private RequestBody createPart(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value == null ? "" : value);
    }

    public void createService() {
        serviceCreateRequestDTO = new ServiceCreateRequestDTO();
        serviceCreateRequestDTO.setName(name.getValue());
        serviceCreateRequestDTO.setDescription(description.getValue());
        serviceCreateRequestDTO.setPrice(price.getValue());
        serviceCreateRequestDTO.setDiscount(discount.getValue());
        serviceCreateRequestDTO.setVisible(isVisible.getValue());
        serviceCreateRequestDTO.setAvailable(isAvailable.getValue());
        serviceCreateRequestDTO.setSpecifics(specifics.getValue());
        serviceCreateRequestDTO.setReservationType(getReservationType());
        serviceCreateRequestDTO.setReservationDeadline(reservationDeadline.getValue());
        serviceCreateRequestDTO.setCancellationDeadline(cancelDeadline.getValue());

        serviceCreateRequestDTO.setDuration(duration.getValue());
        serviceCreateRequestDTO.setMinimumArrangement(minArrangement.getValue());
        serviceCreateRequestDTO.setMaximumArrangement(maxArrangement.getValue());

        serviceCreateRequestDTO.setEventTypesIDs(eventTypeIds.getValue());
        serviceCreateRequestDTO.setOfferingCategoryID(offeringCategoryId.getValue());
        if(offeringCategoryId.getValue() == null){
            serviceCreateRequestDTO.setOfferingCategoryName(categoryInput.getValue());
            serviceCreateRequestDTO.setOfferingCategoryID(-1L);
            offeringCategoryId.setValue(-1L);
        }
        serviceCreateRequestDTO.setOwnerId(ownerId.getValue());

        //serviceCreateRequestDTO.setImages(newImages.getValue());
        serviceCreateRequestDTO.setImagesToDelete(imagesToDelete.getValue());

        Map<String, RequestBody> partMap = convertToRequestBodyMap(serviceCreateRequestDTO);
        List<MultipartBody.Part> imageParts = new ArrayList<>();

        for (File file : newImages.getValue()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "images",
                    file.getName(),
                    requestFile
            );

            imageParts.add(body);
        }


        Call<ResponseBody> call = Boolean.TRUE.equals(isEditMode.getValue()) ?
                                    serviceApi.updateService(serviceId.getValue(),partMap, imageParts)
                                    :serviceApi.createService(partMap, imageParts);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    _addedService.setValue(true);
                } else {
                    errorMessageFromServer.setValue(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessageFromServer.setValue("Networking problem " + t.toString());
            }
        });

    }

    public void fillTheForm(ServiceCreateResponseDTO service) {
        fetchFullService(service);

        name.setValue(service.getName());
        description.setValue(service.getDescription());
        price.setValue( service.getPrice());
        priceString.setValue(String.valueOf(service.getPrice()));
        discount.setValue((int) service.getDiscount());
        isAvailable.setValue(service.isAvailable());
        isAvailable.setValue(service.isVisible());
        specifics.setValue(service.getSpecifics());
        reservationDeadline.setValue(service.getReservationDeadline());
        cancelDeadline.setValue(service.getCancellationDeadline());
        duration.setValue(service.getDuration());
        minArrangement.setValue(service.getMinimumArrangement());
        maxArrangement.setValue(service.getMaximumArrangement());
        confirmationType.setValue(service.getReservationType());
        imagesToDelete.setValue(new ArrayList<>());
        _existingImages.setValue(
                service.getImages().stream().map(imageId ->
                        ConnectionParams.BASE_URL + "api/services/" + serviceId.getValue() + "/images/" + imageId
                ).collect(Collectors.toList())
        );

        syncFront();
    }
    private void fetchFullService(ServiceCreateResponseDTO dto) {
        List<Call<EventType>> eventTypeCalls = new ArrayList<>();
        for (EventTypePreviewDTO e : dto.getEventTypes()) {
            eventTypeCalls.add(eventTypeApi.getEventType(e.getId()));
        }

        Call<OfferingCategory> categoryCall = offeringCategoryApi.getOfferingCategory(dto.getOfferingCategory().getId());

        List<EventType> eventTypes = new ArrayList<>();
        AtomicBoolean categoryFetched = new AtomicBoolean(false);
        AtomicReference<OfferingCategory> category = new AtomicReference<>();

        for (Call<EventType> eventTypeCall : eventTypeCalls) {
            eventTypeCall.enqueue(new Callback<EventType>() {
                @Override
                public void onResponse(Call<EventType> call, Response<EventType> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        eventTypes.add(response.body());
                        eventTypesLive.setValue(eventTypes);
                        eventTypeIds.setValue(eventTypes.stream().map(EventType::getId).collect(Collectors.toList()));
                    }
                }

                @Override
                public void onFailure(Call<EventType> call, Throwable t) {
                    Log.e("API", "Error fetching event type", t);
                }
            });
        }

        categoryCall.enqueue(new Callback<OfferingCategory>() {
            @Override
            public void onResponse(Call<OfferingCategory> call, Response<OfferingCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    category.set(response.body());
                    categoryFetched.set(true);
                    offeringCategoryMutableLiveData.setValue(category.get());
                    categoryInput.setValue(category.get().getName());
                    offeringCategoryId.setValue(category.get().getId());
                }
            }

            @Override
            public void onFailure(Call<OfferingCategory> call, Throwable t) {
                Log.e("API", "Error fetching category", t);
            }
        });
    }


    public void resetForm(){
        name.setValue("");
        description.setValue("");
        price.setValue(0.0);
        priceString.setValue("");
        discount.setValue(0);
        isAvailable.setValue(false);
        isAvailable.setValue(false);
        specifics.setValue("");
        confirmationTypeToggle.setValue(false);
        reservationDeadline.setValue(0);
        cancelDeadline.setValue(0);
        duration.setValue(0);
        minArrangement.setValue(0);
        maxArrangement.setValue(0);
        eventTypeIds.setValue(new ArrayList<>());
        offeringCategoryId.setValue(null);
        _existingImages.setValue(new ArrayList<>());
    }

    public void removeNewImage(File image) {
        List<File> currentImages = newImages.getValue();
        if (currentImages != null && currentImages.remove(image)) {
            newImages.setValue(currentImages);
        }
    }

    public void removeExistingImage(String url) {
        List<String> existingImages = this.existingImages.getValue();
        if (existingImages == null) return;
        if (existingImages.remove(url)) {
            List<String> imagesToDelete = this.imagesToDelete.getValue();
            if (imagesToDelete == null) {
                imagesToDelete = new ArrayList<>();
            }
            imagesToDelete.add(url);
            this.imagesToDelete.setValue(imagesToDelete);
            this._existingImages.setValue(existingImages);
        }
    }
}