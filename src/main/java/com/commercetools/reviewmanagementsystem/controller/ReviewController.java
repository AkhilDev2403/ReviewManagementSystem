package com.commercetools.reviewmanagementsystem.controller;

import com.commercetools.reviewmanagementsystem.constants.AbstractResponse;
import com.commercetools.reviewmanagementsystem.dto.CreateReviewDto;
import com.commercetools.reviewmanagementsystem.dto.UpdateDto;
import com.commercetools.reviewmanagementsystem.model.ReviewResponse;
import com.commercetools.reviewmanagementsystem.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("reviews")
public class ReviewController {

    @Value("${Secret_Auth_Key}")
    private String secretAuthKey;

    @Autowired
    ReviewService reviewService;


    @GetMapping("/getAllReviews")
    public List<ReviewResponse> getAllReviews(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "2") int size) {
        List<ReviewResponse> returnValue = new ArrayList<>();
        List<CreateReviewDto> reviews = reviewService.getAllReview(page, size);
        for (CreateReviewDto reviewDto : reviews) {
            ReviewResponse reviewResponse = new ModelMapper().map(reviewDto, ReviewResponse.class);
            returnValue.add(reviewResponse);
        }
        return returnValue;
    }

    @GetMapping("/get/AvgRating/{productId}")
    public float getAvgRating(@PathVariable(value = "productId") String productId) {
        return reviewService.getAvgRating(productId);
    }

    @PostMapping("/add")
    public AbstractResponse<String> addReview(@RequestBody CreateReviewDto dto,
                                              @RequestHeader("Authorization") String authorization) {
        return reviewService.createReview(dto, authorization);
    }

    @PutMapping("/update")
    public AbstractResponse<String> editReview(@RequestBody UpdateDto updateDto,
                                               @RequestHeader("Authorization") String authorization) {
        return reviewService.updateReview(updateDto, authorization);
    }

    @DeleteMapping("/delete/{customerId}/{pId}")
    public AbstractResponse<String> deleteReview(@PathVariable(value = "customerId") String customerId,
                                                 @PathVariable(value = "pId") String prId,
                                                 @RequestHeader("Authorization") String authorization) {
        return reviewService.deleteReview(customerId, prId, authorization);
    }

}
