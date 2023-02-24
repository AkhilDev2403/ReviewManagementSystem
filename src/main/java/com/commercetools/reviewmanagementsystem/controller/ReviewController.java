package com.commercetools.reviewmanagementsystem.controller;

import com.commercetools.reviewmanagementsystem.constants.AbstractResponse;
import com.commercetools.reviewmanagementsystem.core.model.ApiResponse;
import com.commercetools.reviewmanagementsystem.core.model.Constants;
import com.commercetools.reviewmanagementsystem.core.model.CustomHttpStatus;
import com.commercetools.reviewmanagementsystem.dto.ReviewDto;
import com.commercetools.reviewmanagementsystem.model.request.CreateReviewRequest;
import com.commercetools.reviewmanagementsystem.model.request.UpdateReviewRequest;
import com.commercetools.reviewmanagementsystem.model.response.CreateReviewResponse;
import com.commercetools.reviewmanagementsystem.model.response.UpdateReviewResponse;
import com.commercetools.reviewmanagementsystem.model.response.ReviewResponse;
import com.commercetools.reviewmanagementsystem.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("reviews")
public class ReviewController {

    @Value("${Secret_Auth_Key}")
    private String secretAuthKey;

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
    public AbstractResponse<UpdateReviewResponse> editReview(@RequestBody UpdateReviewRequest updateRequest,
                                                             @RequestHeader("Authorization") String authorization) {
        return reviewService.updateReview(updateRequest, authorization);
    }

    @DeleteMapping("/delete/{customerId}/{pId}")
    public AbstractResponse<String> deleteReview(@PathVariable(value = "customerId") String customerId,
                                                 @PathVariable(value = "pId") String prId,
                                                 @RequestHeader("Authorization") String authorization) {
        return reviewService.deleteReview(customerId, prId, authorization);
    }

    @GetMapping("/getAllReviews")
    public List<ReviewResponse> getAllReviews(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "2") int size) {
        List<ReviewResponse> returnValue = new ArrayList<>();
        List<ReviewDto> reviews = reviewService.getAllReview(page, size);
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
