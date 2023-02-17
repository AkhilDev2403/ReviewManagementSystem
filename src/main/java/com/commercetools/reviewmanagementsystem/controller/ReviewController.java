package com.commercetools.reviewmanagementsystem.controller;

import com.commercetools.reviewmanagementsystem.constants.ResponseMessage;
import com.commercetools.reviewmanagementsystem.dto.CreateReviewDto;
import com.commercetools.reviewmanagementsystem.dto.UpdateDto;
import com.commercetools.reviewmanagementsystem.entity.ReviewEntity;
import com.commercetools.reviewmanagementsystem.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("reviews")
public class ReviewController {

    @Value("${secret_auth_key}")
    private String secretAuthKey;

    @Autowired
    ReviewService reviewService;


    //http://localhost:8714/reviews/getAll
    @GetMapping("/getAllReviews/{pageNumber}/{pageSize}")
    public Page<ReviewEntity> getAllReviews(@PathVariable Integer pageNumber,
                                            @PathVariable Integer pageSize) {
        return reviewService.getAllReview(pageNumber, pageSize);

    }


    @GetMapping("/get/AvgRating/{productId}")
    public ResponseEntity<Map<String, Object>> getAvgRating(@PathVariable(value = "productId") String productId,
                                                            @RequestHeader("secret-key") String secret) {
        if (Objects.equals(secret, secretAuthKey)) {
            float avg = reviewService.getAvgRating(productId);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put(ResponseMessage.AVG_RATING, avg);
            return ResponseEntity.status(HttpStatus.FOUND).body(returnValue);
        }

        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put(ResponseMessage.STATUS, ResponseMessage.UNAUTHORIZED_ACCESS);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }


    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addReview(@RequestBody CreateReviewDto dto,
                                                         @RequestHeader("secret-key") String secret,
                                                         @RequestHeader("Authorization") String authorization) {
        if (Objects.equals(secret, secretAuthKey)) {
            String response = reviewService.createReview(dto, authorization);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put(ResponseMessage.STATUS, response);

            if (Objects.equals(response, ResponseMessage.INVALID_CUSTOMER)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnValue);
            }
            if (Objects.equals(response, ResponseMessage.ALREADY_REVIEWED)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnValue);
            }
            if (Objects.equals(response, ResponseMessage.INVALID_RATING)) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(returnValue);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
        }

        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put(ResponseMessage.STATUS, ResponseMessage.UNAUTHORIZED_ACCESS);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }


    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> editReview(@RequestBody UpdateDto updateDto,
                                                          @RequestHeader("secret-key") String secret,
                                                          @RequestHeader("Authorization") String authorization) {
        if (Objects.equals(secret, secretAuthKey)) {
            String response = reviewService.updateReview(updateDto, authorization);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put(ResponseMessage.STATUS, response);

            if (Objects.equals(response, ResponseMessage.INVALID_CUSTOMER)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnValue);
            }
            if (Objects.equals(response, ResponseMessage.INVALID_PRODUCT)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnValue);
            }
            if (Objects.equals(response, ResponseMessage.INVALID_RATING)) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(returnValue);
            }
            return ResponseEntity.status(HttpStatus.OK).body(returnValue);
        }
        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put(ResponseMessage.STATUS, ResponseMessage.UNAUTHORIZED_ACCESS);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }

    /**
     * Only this api is using params. Just to understand the functionality of params.
     *
     * @param customerId customerId passed in the parameter along the api
     * @param prId       Pid passed in this api as parameter
     * @return
     */
    @DeleteMapping("/delete/{customerId}/{pId}")
    public ResponseEntity<Map<String, Object>> deleteReview(@PathVariable(value = "customerId") String customerId,
                                                            @PathVariable(value = "pId") String prId,
                                                            @RequestHeader("secret-key") String secret,
                                                            @RequestHeader("Authorization") String authorization) {
        if (Objects.equals(secret, secretAuthKey)) {
            String deleteResponse = reviewService.deleteReview(customerId, prId, authorization);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put(ResponseMessage.STATUS, deleteResponse);
            if (Objects.equals(deleteResponse, ResponseMessage.INVALID_CUSTOMER)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnValue);
            }
            if (Objects.equals(deleteResponse, ResponseMessage.NO_SUCH_PRODUCT)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnValue);
            }
            return ResponseEntity.status(HttpStatus.OK).body(returnValue);
        }
        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put(ResponseMessage.STATUS, ResponseMessage.UNAUTHORIZED_ACCESS);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }


}
