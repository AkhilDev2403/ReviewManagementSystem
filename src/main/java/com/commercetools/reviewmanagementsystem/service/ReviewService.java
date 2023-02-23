package com.commercetools.reviewmanagementsystem.service;

import com.commercetools.reviewmanagementsystem.constants.AbstractResponse;
import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ErrorMessages;
import com.commercetools.reviewmanagementsystem.core.exception.UserNotFoundException;
import com.commercetools.reviewmanagementsystem.dto.ReviewDto;
import com.commercetools.reviewmanagementsystem.entity.ReviewEntity;
import com.commercetools.reviewmanagementsystem.model.request.CreateReviewRequest;
import com.commercetools.reviewmanagementsystem.model.request.UpdateReviewRequest;
import com.commercetools.reviewmanagementsystem.model.response.CreateReviewResponse;
import com.commercetools.reviewmanagementsystem.model.response.UpdateReviewResponse;
import com.commercetools.reviewmanagementsystem.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    ReviewRepository repository;

    @Autowired
    CommercetoolsService commercetoolsService;


    public AbstractResponse<CreateReviewResponse> createReview(CreateReviewRequest reviewRequest, String authorization) {
        AbstractResponse<CreateReviewResponse> createResponse = new AbstractResponse<>(reviewRequest, authorization);
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(authorization);
        log.info("commercetools customer ID = " + commercetoolsCustomerId);
        Optional<ReviewEntity> isAlreadyReviewed = repository.findByProductAndUser(reviewRequest.getProductId(), reviewRequest.getCustomerId());
        String customerId = reviewRequest.getCustomerId();

        if (!commercetoolsCustomerId.equals(customerId))
            throw new UserNotFoundException(ErrorMessages.INVALID_CUSTOMER.getErrorMessages());

        if (isAlreadyReviewed.isPresent())
            throw new CustomException(ErrorMessages.ALREADY_REVIEWED.getErrorMessages());

        if (reviewRequest.getRating() > 5)
            throw new CustomException(ErrorMessages.INVALID_RATING.getErrorMessages());

        ReviewEntity reviewEntity = new ModelMapper().map(reviewRequest, ReviewEntity.class);
        repository.save(reviewEntity);
        createResponse.setMessage("Review Added Successfully");
        createResponse.setSuccess(true);
        return createResponse;
    }


    public AbstractResponse<UpdateReviewResponse> updateReview(UpdateReviewRequest updateRequest, String token) {
        AbstractResponse<UpdateReviewResponse> uploadResponse = new AbstractResponse<>(updateRequest, token);
        Optional<ReviewEntity> productExist = repository.findByCustomerIdAndProductId(updateRequest.getCustomerId(), updateRequest.getProductId());
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(token);
        String customerId = updateRequest.getCustomerId();
        if (!commercetoolsCustomerId.equals(customerId))
            throw new UserNotFoundException(ErrorMessages.INVALID_CUSTOMER.getErrorMessages());

        if (productExist.isEmpty())
            throw new CustomException(ErrorMessages.INVALID_PRODUCT.getErrorMessages());

        if (updateRequest.getRating() > 5)
            throw new CustomException(ErrorMessages.INVALID_RATING.getErrorMessages());

        ReviewEntity entity = repository.findByCustomerDetails(updateRequest.getCustomerId(), updateRequest.getProductId());
        log.info(String.valueOf(entity));
        entity.setRating(updateRequest.getRating());
        entity.setReview(updateRequest.getReview());
        repository.save(entity);
        uploadResponse.setSuccess(true);
        uploadResponse.setMessage("Review Updated....");
        return uploadResponse;
    }

    public AbstractResponse<String> deleteReview(String cuId, String pId, String token) {
        AbstractResponse<String> deleteResponse = new AbstractResponse<>(cuId, pId, token);
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(token);
        if (!commercetoolsCustomerId.equals(cuId))
            throw new UserNotFoundException(ErrorMessages.INVALID_CUSTOMER.getErrorMessages());
        try {
            Integer del = repository.deleteByProductId(cuId, pId);
            log.info("" + del);
            if (del != 0) {
                deleteResponse.setSuccess(true);
                deleteResponse.setMessage("Deleted successfully");
            } else {
                deleteResponse.setSuccess(false);
                deleteResponse.setMessage("Failed...! No such product Exists.. Please try again.");
            }
        } catch (Exception e) {
            throw new CustomException(ErrorMessages.FAILED.getErrorMessages());
        }
        return deleteResponse;
    }

    public float getAvgRating(String productId) {
        Optional<Float> avgRating = repository.getRatingCount(productId);
        float finalAvgRating;
        finalAvgRating = avgRating.orElse(0.0F);
        log.info(String.valueOf(finalAvgRating));
        return finalAvgRating;
    }

    public List<ReviewDto> getAllReview(Integer page, Integer size) {
        List<ReviewDto> returnValue = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewEntity> reviewList = repository.findAll(pageable);
        List<ReviewEntity> entityList = reviewList.getContent();
        for (ReviewEntity reviewEntity : entityList) {
            ReviewDto reviewDto = new ModelMapper().map(reviewEntity, ReviewDto.class);
            returnValue.add(reviewDto);
        }
        return returnValue;
    }

}
