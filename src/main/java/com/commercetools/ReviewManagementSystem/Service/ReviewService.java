package com.commercetools.ReviewManagementSystem.Service;

import com.commercetools.ReviewManagementSystem.Dto.CreateReviewDto;
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


    public String createReview(CreateReviewDto dto) {
        Optional<String> check = repository.findCustomerExist(dto.getCustomerId());
        if (check.isEmpty()) {
            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setCustomerId(dto.getCustomerId());
            reviewEntity.setProductId(dto.getProductId());
            reviewEntity.setRating(dto.getRating());
            reviewEntity.setComment(dto.getComment());
            repository.save(reviewEntity);
            return "Review Added Successfully";
        }
        return "You've already reviewed this product!";                              //this comment has to be same in controller also -> if (Objects.equals(response, "You've already added review for this product!")) { ow u can't get the status code
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
        Optional<ReviewEntity> entityList = repository.findByCustomerIdAndProductId(updateDto.getCustomerId(), updateDto.getProductId());
        if (entityList.isEmpty()) {
            return "Error... Invalid customerId or ProductId.. Please try again..";
        }
        ReviewEntity reviewEntity = repository.findByCustomerId(updateDto.getCustomerId());
        reviewEntity.setRating(updateDto.getRating());
        reviewEntity.setComment(updateDto.getComment());
        repository.save(reviewEntity);
        return "Review Updated Successfully.....";
    }

}
