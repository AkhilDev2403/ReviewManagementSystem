package com.commercetools.ReviewManagementSystem.Controller;

import com.commercetools.ReviewManagementSystem.Config.RestClient;
import com.commercetools.ReviewManagementSystem.Dto.CreateReviewDto;
import com.commercetools.ReviewManagementSystem.Dto.TestReviewRequest;
import com.commercetools.ReviewManagementSystem.Dto.UpdateDto;
import com.commercetools.ReviewManagementSystem.Entity.ReviewEntity;
import com.commercetools.ReviewManagementSystem.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("reviews")
public class ReviewController {

    @Value("${secret_auth_key}")
    private String secret_auth_key;

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
        if (Objects.equals(secret, secret_auth_key)) {
            float avg = reviewService.getAvgRating(productId);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put("average rating", avg);
            return ResponseEntity.status(HttpStatus.FOUND).body(returnValue);
        }

        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put("status", "Error: You do not have access to this server !");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }

    //https://7c78-2405-201-f020-b03c-6ca1-4bc7-8f31-ea3b.in.ngrok.io/reviews/add/12/24 - ngrok https (publicly accessible url)
    //https://reviews/add/{customerId}/{productId}
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addReview(@RequestBody CreateReviewDto dto,
                                                         @RequestHeader("secret-key") String secret,
                                                         @RequestHeader("Authorization") String authorization) {
        if (Objects.equals(secret, secret_auth_key)) {
            String response = reviewService.createReview(dto, authorization);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put("status", response);

            if (Objects.equals(response, "Error... Invalid customer.... Please Log in")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnValue);
            }
            if (Objects.equals(response, "You've already added review for this product!")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnValue);
            }
            if (Objects.equals(response, "Invalid Rating.. Please try again")) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(returnValue);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
        }

        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put("status", "Error: You do not have access to this server !");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }


    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> editReview(@RequestBody UpdateDto updateDto,
                                                          @RequestHeader("secret-key") String secret,
                                                          @RequestHeader("Authorization") String authorization) {
        if (Objects.equals(secret, secret_auth_key)) {
            String response = reviewService.updateReview(updateDto, authorization);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put("status", response);

            if (Objects.equals(response, "Error... Invalid customer.... Please Log in")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnValue);
            }
            if (Objects.equals(response, "Error... Invalid ProductId.. Please try again..")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnValue);
            }
            if (Objects.equals(response, "Invalid Rating.. Please try again")) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(returnValue);
            }
            return ResponseEntity.status(HttpStatus.OK).body(returnValue);
        }
        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put("status", "Error you don't have access to this server !");
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
        if (Objects.equals(secret, secret_auth_key)) {
            String deleteResponse = reviewService.deleteReview(customerId, prId, authorization);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put("status", deleteResponse);
            if (Objects.equals(deleteResponse, "Error... Invalid customer.... Please Log in")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(returnValue);
            }
            if (Objects.equals(deleteResponse, "Failed...! No such product Exists.. Please try again.")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnValue);
            }
            return ResponseEntity.status(HttpStatus.OK).body(returnValue);
        }
        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put("status", "Error you don't have access to this server !");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }

    //just for testing
//    @PostMapping("/addReview")
//    public String addRe(@RequestBody TestReviewRequest request) {
//        return reviewService.addReview(request);
//    }

    //just for testing
    @PostMapping("/test")
    public String test(@RequestHeader(value = "Authorization") String Authorization,
                       @RequestHeader(value = "Accept") String Accept) {

        return "success";
    }


}
