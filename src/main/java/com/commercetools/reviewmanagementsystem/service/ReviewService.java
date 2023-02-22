package com.commercetools.reviewmanagementsystem.service;

import com.commercetools.reviewmanagementsystem.constants.AbstractResponse;
import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ErrorMessages;
import com.commercetools.reviewmanagementsystem.dto.CreateReviewDto;
import com.commercetools.reviewmanagementsystem.dto.UpdateDto;
import com.commercetools.reviewmanagementsystem.entity.ReviewEntity;
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

    public List<CreateReviewDto> getAllReview(Integer page, Integer size) {
        List<CreateReviewDto> returnValue = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewEntity> reviewList = repository.findAll(pageable);
        List<ReviewEntity> entityList = reviewList.getContent();
        for (ReviewEntity reviewEntity : entityList) {
            CreateReviewDto reviewDto = new ModelMapper().map(reviewEntity, CreateReviewDto.class);
            returnValue.add(reviewDto);
        }
        return returnValue;
    }

    public AbstractResponse<String> createReview(CreateReviewDto dto, String token) {
        AbstractResponse<String> createResponse = new AbstractResponse<>(dto, token);
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(token);
        log.info("commercetools customer ID = " + commercetoolsCustomerId);
        Optional<ReviewEntity> isAlreadyReviewed = repository.findByProductAndUser(dto.getProductId(), dto.getCustomerId());
        String customerId = dto.getCustomerId();

        if (!commercetoolsCustomerId.equals(customerId))
            throw new CustomException(ErrorMessages.INVALID_CUSTOMER.getErrorMessages());

        if (isAlreadyReviewed.isPresent())
            throw new CustomException(ErrorMessages.ALREADY_REVIEWED.getErrorMessages());

        if (dto.getRating() > 10)
            throw new CustomException(ErrorMessages.INVALID_RATING.getErrorMessages());

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setCustomerId(dto.getCustomerId());
        reviewEntity.setProductId(dto.getProductId());
        reviewEntity.setRating(dto.getRating());
        reviewEntity.setComment(dto.getComment());
        repository.save(reviewEntity);
        createResponse.setMessage("Review Added Successfully");
        createResponse.setSuccess(true);
        return createResponse;
    }


    public AbstractResponse<String> deleteReview(String cuId, String pId, String token) {
        AbstractResponse<String> deleteResponse = new AbstractResponse<>(cuId, pId, token);
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(token);
        if (!commercetoolsCustomerId.equals(cuId))
            throw new CustomException(ErrorMessages.INVALID_CUSTOMER.getErrorMessages());
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


    public AbstractResponse<String> updateReview(UpdateDto updateDto, String token) {
        AbstractResponse<String> uploadResponse = new AbstractResponse<>(updateDto, token);
        Optional<ReviewEntity> productExist = repository.findByCustomerIdAndProductId(updateDto.getCustomerId(), updateDto.getProductId());
        String commercetoolsCustomerId = commercetoolsService.getCommercetoolsCustomer(token);
        String customerId = updateDto.getCustomerId();
        if (!commercetoolsCustomerId.equals(customerId))
            throw new CustomException(ErrorMessages.INVALID_CUSTOMER.getErrorMessages());

        if (productExist.isEmpty())
            throw new CustomException(ErrorMessages.INVALID_PRODUCT.getErrorMessages());

        if (updateDto.getRating() > 10)
            throw new CustomException(ErrorMessages.INVALID_RATING.getErrorMessages());

        ReviewEntity entity = repository.findByCustomerDetails(updateDto.getCustomerId(), updateDto.getProductId());
        log.info(String.valueOf(entity));
        entity.setRating(updateDto.getRating());
        entity.setComment(updateDto.getComment());
        repository.save(entity);
        uploadResponse.setSuccess(true);
        uploadResponse.setMessage("Review Updated....");
        return uploadResponse;

    }


}


