package com.commercetools.reviewmanagementsystem.service;

import com.commercetools.reviewmanagementsystem.constants.AbstractResponse;
import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ErrorMessages;
import com.commercetools.reviewmanagementsystem.dto.CreateReviewDto;
import com.commercetools.reviewmanagementsystem.dto.UpdateDto;
import com.commercetools.reviewmanagementsystem.entity.ReviewEntity;
import com.commercetools.reviewmanagementsystem.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    ReviewRepository repository;

    @Autowired
    CommercetoolsService commercetoolsService;


    public String createReview(CreateReviewDto dto, String token) {
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(token);
        log.info("commercetools customer ID = " + commercetoolsCustomerId);
        Optional<String> checkProductExist = repository.findProductExist(dto.getProductId(), dto.getCustomerId());
        String customerId = dto.getCustomerId();
        if (commercetoolsCustomerId.equals(customerId)) {
            if (checkProductExist.isEmpty()) {
                float rating = dto.getRating();
                if (rating <= 10) {
                    ReviewEntity reviewEntity = new ReviewEntity();
                    reviewEntity.setCustomerId(dto.getCustomerId());
                    reviewEntity.setProductId(dto.getProductId());
                    reviewEntity.setRating(rating);
                    reviewEntity.setComment(dto.getComment());
                    repository.save(reviewEntity);
                } else {
                    return "Invalid Rating.. Please try again";
                }
                return "Review Added Successfully";
            }
            return "You've already reviewed this product!";
        }
        return "Invalid user";

    }


    public Page<ReviewEntity> getAllReview(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(pageable);
    }


    public String deleteReview(String cuId, String pId, String token) {
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(token);
        if (commercetoolsCustomerId.equals(cuId)) {
            String returnValue = "Initial";
            try {
                Integer del = repository.deleteByProductId(cuId, pId);
                log.info("" + del);
                if (del != 0) {
                    returnValue = "Delete Success";
                } else {
                    returnValue = "Failed...! No such product Exists.. Please try again.";
                }
            } catch (Exception e) {
                log.error(e + "error");
            }
            return returnValue;
        }
        return "Error... Invalid customer.... Please Log in";
    }

    public float getAvgRating(String productId) {
        Optional<Float> avgRating = repository.getRatingCount(productId);
        float finalAvgRating;
        finalAvgRating = avgRating.orElse(0.0F);
        log.info(String.valueOf(finalAvgRating));
        return finalAvgRating;
    }


    public AbstractResponse<String> updateReview(UpdateDto updateDto, String token) {
        AbstractResponse<String> uploadResponse = new AbstractResponse<>(updateDto, token);
        Optional<ReviewEntity> entityList = repository.findByCustomerIdAndProductId(updateDto.getCustomerId(), updateDto.getProductId());
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(token);
        String customerId = updateDto.getCustomerId();
        if (commercetoolsCustomerId.equals(customerId)) {
            if (entityList.isEmpty())
                throw new CustomException(ErrorMessages.INVALID_PRODUCT.getErrorMessages());
            float rating = updateDto.getRating();
            if (rating > 10)
                throw new CustomException(ErrorMessages.INVALID_RATING.getErrorMessages());
            ReviewEntity entity = repository.findByCustomerDetails(updateDto.getCustomerId(), updateDto.getProductId());
            log.info(String.valueOf(entity));
            entity.setRating(updateDto.getRating());
            entity.setComment(updateDto.getComment());
            repository.save(entity);
            uploadResponse.setSuccess(true);
            uploadResponse.setMessage("Review Added....");
            return uploadResponse;
        }
        throw new CustomException(ErrorMessages.INVALID_CUSTOMER.getErrorMessages());
    }


}


