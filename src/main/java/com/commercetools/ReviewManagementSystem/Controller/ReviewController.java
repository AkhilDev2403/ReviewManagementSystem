package com.commercetools.ReviewManagementSystem.Controller;

import com.commercetools.ReviewManagementSystem.Dto.ReviewDto;
import com.commercetools.ReviewManagementSystem.Dto.UpdateDto;
import com.commercetools.ReviewManagementSystem.Entity.ReviewEntity;
import com.commercetools.ReviewManagementSystem.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("reviews")
public class ReviewController {

    @Value("${secret_auth_key}")
    private String secret_auth_key;

    @Autowired
    ReviewService reviewService;

    //http://localhost:8714/reviews/getAll
    @GetMapping("/getAllReviews")
    public List<ReviewEntity> getAllReviews() {
        return reviewService.getAllReview();

    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    required = true,
//                    name = "authorization",
//                    value = "bearer-token",
//                    dataType = "java.lang.String",
//                    paramType = "header"
//            )
//    })

    @GetMapping("/get/AvgRating/{productId}")
    public ResponseEntity<Map<String, Object>> getAvgRating(@PathVariable(value = "productId") String productId,
                                                            @RequestHeader("secret") String secret) {
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
    @PostMapping("/add/{customerId}/{productId}")
    public ResponseEntity <Map<String, Object>> addReview(@PathVariable(value = "customerId") String customerId,
                                                         @PathVariable(value = "productId") String productId,
                                                         @RequestHeader("secret") String secret,
                                                         @RequestBody ReviewDto reviewDto) {
        if (Objects.equals(secret, secret_auth_key)) {
            String response = reviewService.createReview(reviewDto, customerId, productId);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put("status", response);
            if (Objects.equals(response, "Customer not existed")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(returnValue);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
        }

        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put("status", "Error: You do not have access to this server !");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }


    @PutMapping("/update")
    public ResponseEntity <Map<String, Object>> editReview(@RequestBody UpdateDto updateDto,
                                                          @RequestHeader("secret") String secret) {
        if (Objects.equals(secret, secret_auth_key)) {
            String response = reviewService.updateReview(updateDto);
            HashMap<String, Object> returnValue = new HashMap<>();
            returnValue.put("status", response);
            return  ResponseEntity.status(HttpStatus.OK).body(returnValue);
        }
        HashMap<String, Object> returnValue = new HashMap<>();
        returnValue.put("status", "Error you don't have access to this server !");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(returnValue);
    }

    @DeleteMapping("/delete/{customerId}/{pId}")
    public String deleteReview(@PathVariable(value = "customerId") String customerId,
                               @PathVariable(value = "pId") String prId) {
        return reviewService.deleteReview(customerId, prId);
    }


}
