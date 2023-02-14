package com.commercetools.ReviewManagementSystem.Service;

import com.commercetools.ReviewManagementSystem.Dto.CreateReviewDto;
import com.commercetools.ReviewManagementSystem.Dto.TestReviewRequest;
import com.commercetools.ReviewManagementSystem.Dto.UpdateDto;
import com.commercetools.ReviewManagementSystem.Entity.ReviewEntity;
import com.commercetools.ReviewManagementSystem.Entity.TestEntity;
import com.commercetools.ReviewManagementSystem.Repository.ReviewRepository;
import com.commercetools.ReviewManagementSystem.Repository.TestReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    TestReviewRepository testReviewRepository;


    public String createReview(CreateReviewDto dto, String token) {
        String commercetoolsCustomerId = getCommercetoolsCustomer(token);
        System.out.println("commercetools customer ID = " + commercetoolsCustomerId);
        Optional<String> check = repository.findCustomerExist(dto.getCustomerId());
        String customerId = dto.getCustomerId();
        if (commercetoolsCustomerId.matches(customerId)) {
            if (check.isEmpty()) {
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
            return "You've already reviewed this product!";                              //this comment has to be same in controller also -> if (Objects.equals(response, "You've already added review for this product!")) { ow u can't get the status code
        }
        else {
            return "Error... Invalid customer.... Please Log in";
        }

    }


    String getCommercetoolsCustomer(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", token);
        String query = "{\"operationName\":\"queryMyCustomer\",\"variables\":{},\"query\":\"query queryMyCustomer {\\n  me {\\n    customer {\\n      customerId: id\\n      custom {\\n        customFieldsRaw {\\n          name\\n          value\\n          __typename\\n        }\\n        __typename\\n      }\\n      version\\n      email\\n      firstName\\n      lastName\\n      version\\n      customerNumber\\n      customerGroupRef {\\n        customerGroupId: id\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}";
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "https://api.europe-west1.gcp.commercetools.com/sunrise-spa/graphql";
        ResponseEntity<LinkedHashMap> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(query, headers), LinkedHashMap.class);
        LinkedHashMap result =   response.getBody();
        result = (LinkedHashMap) result.get("data");
        result = (LinkedHashMap) result.get("me");
        result = (LinkedHashMap) result.get("customer");
        String cId = (String) result.get("customerId");
        System.out.println(cId);
        System.out.println(result.get("firstName"));
        //System.out.println("commercetools customerId"  + result.get("customerId"));
        return cId;
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
                returnValue = "Failed...! Wrong Id... Please try again.";
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
        float rating = updateDto.getRating();
        if (rating <= 10) {
            ReviewEntity reviewEntity = repository.findByCustomerId(updateDto.getCustomerId());
            reviewEntity.setRating(updateDto.getRating());
            reviewEntity.setComment(updateDto.getComment());
            repository.save(reviewEntity);
        } else {
            return "Invalid Rating.. Please try again";
        }
        return "Review Updated Successfully.....";
    }

    public String addReview(TestReviewRequest request) {
        TestEntity entity = new TestEntity();
        entity.setName(request.getName());
        entity.setRating(request.getRating());
        entity.setReview(request.getReview());
        testReviewRepository.save(entity);
        return "success";
    }
}


//    String getCommercetoolsCustomer(String token) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("authorization", token);
//        String query = "{\"operationName\":\"queryMyCustomer\",\"variables\":{},\"query\":\"query queryMyCustomer {\\n  me {\\n    customer {\\n      customerId: id\\n      custom {\\n        customFieldsRaw {\\n          name\\n          value\\n          __typename\\n        }\\n        __typename\\n      }\\n      version\\n      email\\n      firstName\\n      lastName\\n      version\\n      customerNumber\\n      customerGroupRef {\\n        customerGroupId: id\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}";
//        RestTemplate restTemplate = new RestTemplate();
//        String baseUrl = "https://api.europe-west1.gcp.commercetools.com/sunrise-spa/graphql";
//        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(query, headers), String.class);
//        String body = response.getBody();
//        System.out.println("body" + body);
//        return "true";
//    }
