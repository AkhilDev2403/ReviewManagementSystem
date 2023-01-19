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

    //http://localhost:8714/reviews/getAll
    @GetMapping("/getAllReviews")
    public List<ReviewEntity> getAllReviews(){
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
    public Float getAvgRating(@PathVariable(value = "productId") String productId){
        return reviewService.getAvgRating(productId);
    }

    //https://7c78-2405-201-f020-b03c-6ca1-4bc7-8f31-ea3b.in.ngrok.io/reviews/add/12/24 - ngrok https (publicly accessible url's)
    //https://reviews/add/{customerId}/{productId}
    @PostMapping("/add/{customerId}/{productId}")
    public ReviewDto addReview(@PathVariable(value = "customerId") String customerId,
                               @PathVariable(value = "productId") String productId,
                               @RequestBody ReviewDto reviewDto){
      return reviewService.createReview(reviewDto, customerId, productId);
    }


//    @PostMapping
//    public String addRating(){
//        return "rating added";
//    }
//
//    @PutMapping
//    public String editReview(){
//        return "review edited";
//    }

    @DeleteMapping("/delete/{customerId}/{pId}")
    public String  deleteReview(@PathVariable(value = "customerId") String customerId,
                                @PathVariable(value = "pId") String prId)
                                {
        return reviewService.deleteReview(customerId,prId);
    }


}
