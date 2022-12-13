package com.commercetools.ReviewManagementSystem.Controller;
import com.commercetools.ReviewManagementSystem.Dto.ReviewDto;
import com.commercetools.ReviewManagementSystem.Entity.ReviewEntity;
import com.commercetools.ReviewManagementSystem.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/getAll")
    public List<ReviewEntity> getReviews(){
       return reviewService.getAllReview();

    }

    @GetMapping("/get/AvgRating/{productId}")
    public Float getAvgRating(@PathVariable(value = "productId") String productId){
        return reviewService.getAvgRating(productId);
    }

    @PostMapping("/add/{customerId}/{productId}")
    public ReviewDto addReview(@PathVariable(value = "customerId") String customerId,
                               @PathVariable(value = "productId") String productId,
                               @RequestBody ReviewDto reviewDto){
      return reviewService.createReview(reviewDto, customerId, productId);
    }


    @PostMapping
    public String addRating(){
        return "rating added";
    }

    @PutMapping
    public String editReview(){
        return "review edited";
    }

    @DeleteMapping("/delete/{pId}/{customerId}")
    public String  deleteReview(@PathVariable(value = "pId") String prId,
                                @PathVariable(value = "customerId") String customerId){
        return reviewService.deleteReview(prId,customerId);
    }


}
