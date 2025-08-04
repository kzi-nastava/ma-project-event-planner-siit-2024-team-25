package com.team25.event.planner.core;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.team25.event.planner.BuildConfig;
import com.team25.event.planner.communication.api.BlockApi;
import com.team25.event.planner.communication.api.ChatApi;
import com.team25.event.planner.communication.api.ChatRoomApi;
import com.team25.event.planner.communication.api.NotificationApi;
import com.team25.event.planner.core.api.serialization.InstantAdapter;
import com.team25.event.planner.core.api.serialization.LocalDateAdapter;
import com.team25.event.planner.core.api.serialization.LocalDateTimeAdapter;
import com.team25.event.planner.core.api.serialization.LocalTimeAdapter;
import com.team25.event.planner.event.api.BudgetItemApi;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.event.api.EventTypeApi;
import com.team25.event.planner.offering.Api.OfferingApi;
import com.team25.event.planner.offering.Api.OfferingCategoryApi;
import com.team25.event.planner.offering.Api.PriceListApi;
import com.team25.event.planner.product.api.ProductApi;
import com.team25.event.planner.review.api.ReviewApi;
import com.team25.event.planner.service.api.PurchaseApi;
import com.team25.event.planner.service.api.ServiceApi;
import com.team25.event.planner.user.api.AccountApi;
import com.team25.event.planner.user.api.LoginApi;
import com.team25.event.planner.user.api.SuspensionApi;
import com.team25.event.planner.user.api.UserApi;
import com.team25.event.planner.user.api.UserReportApi;
import com.team25.event.planner.user.model.Administrator;
import com.team25.event.planner.user.model.EventOrganizer;
import com.team25.event.planner.user.model.Owner;
import com.team25.event.planner.user.model.RegularUser;
import com.team25.event.planner.user.model.UserRole;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionParams {
    public static final String BASE_URL = BuildConfig.BASE_URL;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .registerTypeAdapterFactory(getUserTypeAdapterFactory())
            .create();

    public static Retrofit retrofit;

    public static EventApi eventApi;

    public static UserApi userApi;

    public static AccountApi accountApi;

    public static LoginApi loginApi;

    public static OfferingApi offeringApi;

    public static EventTypeApi eventTypeApi;

    public static ServiceApi serviceApi;

    public static ProductApi productApi;

    public static OfferingCategoryApi offeringCategoryApi;
    public static BudgetItemApi budgetItemApi;
    public static PurchaseApi purchaseApi;

    public static SuspensionApi suspensionApi;
    public static PriceListApi priceListApi;

    public static UserReportApi userReportApi;

    public static NotificationApi notificationApi;
    public static ChatApi chatApi;
    public static BlockApi blockApi;
    public static ChatRoomApi chatRoomApi;
    public static ReviewApi reviewApi;

    public static void setup(String jwt, AuthInterceptor.LogoutHandler logoutHandler) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(jwt, logoutHandler))
                .build();

        ConnectionParams.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        ConnectionParams.setupServices();
    }

    public static void setupServices() {
        eventApi = retrofit.create(EventApi.class);
        userApi = retrofit.create(UserApi.class);
        accountApi = retrofit.create(AccountApi.class);
        loginApi = retrofit.create(LoginApi.class);
        offeringApi = retrofit.create(OfferingApi.class);
        eventTypeApi = retrofit.create(EventTypeApi.class);
        serviceApi = retrofit.create(ServiceApi.class);
        productApi = retrofit.create(ProductApi.class);
        offeringCategoryApi = retrofit.create(OfferingCategoryApi.class);
        purchaseApi = retrofit.create(PurchaseApi.class);
        budgetItemApi = retrofit.create(BudgetItemApi.class);
        priceListApi = retrofit.create(PriceListApi.class);
        userReportApi = retrofit.create(UserReportApi.class);
        notificationApi = retrofit.create(NotificationApi.class);
        suspensionApi = retrofit.create(SuspensionApi.class);
        chatApi = retrofit.create(ChatApi.class);
        blockApi = retrofit.create(BlockApi.class);
        chatRoomApi = retrofit.create(ChatRoomApi.class);
        reviewApi = retrofit.create(ReviewApi.class);
    }

    private static RuntimeTypeAdapterFactory<RegularUser> getUserTypeAdapterFactory() {
        return RuntimeTypeAdapterFactory
                .of(RegularUser.class, "userRole")
                .registerSubtype(EventOrganizer.class, UserRole.EVENT_ORGANIZER.name())
                .registerSubtype(Owner.class, UserRole.OWNER.name())
                .registerSubtype(Administrator.class, UserRole.ADMINISTRATOR.name())
                .registerSubtype(RegularUser.class, UserRole.REGULAR.name());
    }
}
