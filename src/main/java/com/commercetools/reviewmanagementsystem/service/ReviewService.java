package com.commercetools.reviewmanagementsystem.service;

import com.commercetools.reviewmanagementsystem.dto.CreateReviewDto;
import com.commercetools.reviewmanagementsystem.dto.UpdateDto;
import com.commercetools.reviewmanagementsystem.entity.ReviewEntity;
import com.commercetools.reviewmanagementsystem.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    ReviewRepository repository;


    public String createReview(CreateReviewDto dto, String token) {
        String commercetoolsCustomerId = getCommercetoolsCustomer(token);
        log.info("commercetools customer ID = " + commercetoolsCustomerId);
        Optional<String> checkProductExist = repository.findProductExist(dto.getProductId(), dto.getCustomerId());
        String customerId = dto.getCustomerId();
        if (commercetoolsCustomerId.matches(customerId)) {
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
        } else {
            return "Error...Invalid customer.... Please Log in";
        }

    }


    String getCommercetoolsCustomer(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", token);
        String query = "{\"operationName\":\"queryMyCustomer\",\"variables\":{},\"query\":\"query queryMyCustomer {\\n  me {\\n    customer {\\n      customerId: id\\n      custom {\\n        customFieldsRaw {\\n          name\\n          value\\n          __typename\\n        }\\n        __typename\\n      }\\n      version\\n      email\\n      firstName\\n      lastName\\n      version\\n      customerNumber\\n      customerGroupRef {\\n        customerGroupId: id\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}";
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://api.europe-west1.gcp.commercetools.com/sunrise-spa/graphql";
        ResponseEntity<LinkedHashMap> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(query, headers), LinkedHashMap.class);
        LinkedHashMap result = response.getBody();
        result = (LinkedHashMap) result.get("data");
        result = (LinkedHashMap) result.get("me");
        result = (LinkedHashMap) result.get("customer");
        String cId = (String) result.get("customerId");
        log.info(cId);
        log.info((String) result.get("firstName"));
        return cId;
    }

    public Page<ReviewEntity> getAllReview(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(pageable);
    }


    public String deleteReview(String cuId, String pId, String token) {
        String commercetoolsCustomerId = getCommercetoolsCustomer(token);
        if (commercetoolsCustomerId.matches(cuId)) {
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


    public String updateReview(UpdateDto updateDto, String token) {
        Optional<ReviewEntity> entityList = repository.findByCustomerIdAndProductId(updateDto.getCustomerId(), updateDto.getProductId());
        String commercetoolsCustomerId = getCommercetoolsCustomer(token);
        String customerId = updateDto.getCustomerId();
        if (commercetoolsCustomerId.matches(customerId)) {
            if (entityList.isEmpty()) {
                return "Error... Invalid ProductId.. Please try again..";
            }
            float rating = updateDto.getRating();
            if (rating <= 10) {
                ReviewEntity entity = repository.findByCustomerDetails(updateDto.getCustomerId(), updateDto.getProductId());
                log.info(String.valueOf(entity));
                entity.setRating(updateDto.getRating());
                entity.setComment(updateDto.getComment());
                repository.save(entity);
            } else {

                return "Invalid Rating.. Please try again";
            }
            return "Review Updated Successfully.....";
        }

        return "Error... Invalid customer.... Please Log in";

    }


}


