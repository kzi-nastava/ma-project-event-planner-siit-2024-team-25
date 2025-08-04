package com.team25.event.planner.review.api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.review.model.ReviewCard;
import com.team25.event.planner.review.model.ReviewRequestDTO;
import com.team25.event.planner.review.model.ReviewResponseDTO;
import com.team25.event.planner.review.model.ReviewStatus;
import com.team25.event.planner.review.model.ReviewUpdateRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewApi {
    @GET("api/reviews")
    Call<Page<ReviewCard>> getReviews(@Query("page") int page, @Query("status") ReviewStatus status);

    @PUT("api/reviews/{reviewId}")
    Call<ReviewCard> updateReview(@Path("reviewId") Long reviewId, @Body ReviewUpdateRequestDTO requestDTO);

    @POST("api/reviews")
    Call<ReviewCard> createReview(@Body ReviewRequestDTO requestDTO);

    @GET("api/reviews/events/{eventId}")
    Call<Page<ReviewResponseDTO>> getReviewsByEvent(@Path("eventId")Long eventId,
                                                    @Query("page") int page);

    @GET("api/reviews/offerings/{offeringId}")
    Call<Page<ReviewResponseDTO>> getReviewsByOffering(@Path("offeringId")Long offeringId,
                                                       @Query("page") int page);
}
