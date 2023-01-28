package com.commercetools.ReviewManagementSystem.Service;

import com.commercetools.ReviewManagementSystem.Dto.ReviewDto;
import com.commercetools.ReviewManagementSystem.Dto.UpdateDto;
import com.commercetools.ReviewManagementSystem.Entity.ReviewEntity;
import com.commercetools.ReviewManagementSystem.Repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    ReviewRepository repository;


    public String createReview(ReviewDto reviewDto, String customerId, String productId) {
        Optional<String> check = repository.findCustomerExist(customerId, productId);
        if (check.isEmpty()) {
            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setCustomerId(customerId);
            reviewEntity.setProductId(productId);
            reviewEntity.setComment(reviewDto.getComment());
            reviewEntity.setRating(reviewDto.getRating());
            repository.save(reviewEntity);
            return "Review added";
        }
        return "Customer not existed";

    }


    public List<ReviewEntity> getAllReview() {
        return repository.findAllReviews();
    }


    public String deleteReview(String cuId, String pId) {
        String returnValue = "Initial";
        try {
            Integer del = repository.deleteByProductId(cuId, pId);
            log.info("" + del);
            if (del != 0) {
                returnValue = "Delete Success";
            } else {
                returnValue = "failed... no id";
            }
        } catch (Exception e) {
            log.error(e + "error");
        }
        return returnValue;
    }

    public float getAvgRating(String productId) {
        Optional<Float> avgRating = repository.getRatingCount(productId);
        float finalAvgRating;
        finalAvgRating = avgRating.orElse(0.0F);
        System.out.printf(String.valueOf(finalAvgRating));
        return finalAvgRating;
    }

    public String updateReview(UpdateDto updateDto) {
            ReviewEntity reviewEntity = repository.findByCustomerId(updateDto.getCustomerId());
            ReviewEntity review = repository.findByProductId(updateDto.getProductId());
            reviewEntity.setRating(updateDto.getRating());
            reviewEntity.setComment(updateDto.getComment());
            repository.save(reviewEntity);
            repository.save(review);
            return "updated";
    }
}
