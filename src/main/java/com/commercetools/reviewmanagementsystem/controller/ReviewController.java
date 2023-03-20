package com.commercetools.reviewmanagementsystem.controller;

import com.commercetools.reviewmanagementsystem.core.model.ApiResponse;
import com.commercetools.reviewmanagementsystem.core.model.Constants;
import com.commercetools.reviewmanagementsystem.core.model.CustomHttpStatus;
import com.commercetools.reviewmanagementsystem.dto.ReviewDto;
import com.commercetools.reviewmanagementsystem.model.request.CreateReviewRequest;
import com.commercetools.reviewmanagementsystem.model.request.UpdateReviewRequest;
import com.commercetools.reviewmanagementsystem.model.response.ReviewResponse;
import com.commercetools.reviewmanagementsystem.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addReview(@RequestBody CreateReviewRequest reviewRequest,
                                                 @RequestHeader("Authorization") String authorization) {
        Map data = new HashMap();
        data.put(Constants.DATA, reviewService.createReview(reviewRequest, authorization));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.ordinal()));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> editReview(@RequestBody UpdateReviewRequest updateRequest,
                                                  @RequestHeader("Authorization") String authorization) {
        Map data = new HashMap();
        data.put(Constants.DATA, reviewService.updateReview(updateRequest, authorization));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.ordinal()));
    }

    @DeleteMapping("/delete/{customerId}/{pId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable(value = "customerId") String customerId,
                                                    @PathVariable(value = "pId") String prId,
                                                    @RequestHeader("Authorization") String authorization) {

        Map<String, Object> data = new HashMap<>();
        data.put(Constants.DATA, reviewService.deleteReview(customerId, prId, authorization));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.ordinal()));
    }

    @GetMapping("/getAllReviews/{productId}")
    public List<ReviewResponse> getAllReviews(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "2") int size,
                                              @PathVariable(value = "productId") String productId) {
        List<ReviewResponse> returnValue = new ArrayList<>();
        List<ReviewDto> reviews = reviewService.getAllReview(page, size, productId);
        for (ReviewDto reviewDto : reviews) {
            ReviewResponse reviewResponse = new ModelMapper().map(reviewDto, ReviewResponse.class);
            returnValue.add(reviewResponse);
        }
        return returnValue;
    }

    @GetMapping("/get/AvgRating/{productId}")
    public float getAvgRating(@PathVariable(value = "productId") String productId) {
        return reviewService.getAvgRating(productId);
    }

}
