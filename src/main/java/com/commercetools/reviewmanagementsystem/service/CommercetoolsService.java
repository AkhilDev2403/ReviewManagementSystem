package com.commercetools.reviewmanagementsystem.service;

import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Slf4j
public class CommercetoolsService {

    @Value("${baseUrl}")
    public String url;

    String getCommercetoolsCustomer(String authorization) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("authorization", authorization);
            String query = "{\"operationName\":\"queryMyCustomer\",\"variables\":{},\"query\":\"query queryMyCustomer {\\n  me {\\n    customer {\\n      customerId: id\\n      custom {\\n        customFieldsRaw {\\n          name\\n          value\\n          __typename\\n        }\\n        __typename\\n      }\\n      version\\n      email\\n      firstName\\n      lastName\\n      version\\n      customerNumber\\n      customerGroupRef {\\n        customerGroupId: id\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}";
            RestTemplate restTemplate = new RestTemplate();
            String baseUrl = url;
            ResponseEntity<LinkedHashMap> response = restTemplate.postForEntity(baseUrl, new HttpEntity<>(query, headers), LinkedHashMap.class);
            LinkedHashMap result = response.getBody();
            result = (LinkedHashMap) result.get("data");
            result = (LinkedHashMap) result.get("me");
            result = (LinkedHashMap) result.get("customer");
            String cId = (String) result.get("customerId");
            log.info(cId);
            log.info((String) result.get("firstName"));
            return cId;
        } catch (Exception e) {
            throw new CustomException(ErrorMessages.UNAUTHORIZED_CUSTOMER.getErrorMessages());
        }
    }
}
