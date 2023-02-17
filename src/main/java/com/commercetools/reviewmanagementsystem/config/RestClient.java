package com.commercetools.reviewmanagementsystem.config;

import com.commercetools.reviewmanagementsystem.dto.SignUpDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestClient {

    static String singUpUrl = "http://localhost:8008/user/signup";
    static String url = "http://localhost:8008/user/getAll";

    static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {

        // getUsers();
        // addUserByExchangeMethod();
        getCommercetoolsCustomerDetails();
    }



    private static void addUserByExchangeMethod() {
        SignUpDto user = new SignUpDto("akhi", "dev", "hiaj@mail.com", "123", 123);
        ResponseEntity<SignUpDto> user1 = restTemplate.postForEntity(singUpUrl, user, SignUpDto.class);
        System.out.println("body" + user1.getBody());
    }

    private  void getUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("body" + result);
    }

    public static void getCommercetoolsCustomerDetails(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer ivZt3At7bHH9zXx22KQd_lieXMBshXMp");
        String query = "{\"operationName\":\"queryMyCustomer\",\"variables\":{},\"query\":\"query queryMyCustomer {\\n  me {\\n    customer {\\n      customerId: id\\n      custom {\\n        customFieldsRaw {\\n          name\\n          value\\n          __typename\\n        }\\n        __typename\\n      }\\n      version\\n      email\\n      firstName\\n      lastName\\n      version\\n      customerNumber\\n      customerGroupRef {\\n        customerGroupId: id\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}";
        RestTemplate restTemplate1 = new RestTemplate();
        String baseUrl = "https://api.europe-west1.gcp.commercetools.com/sunrise-spa/graphql";
        ResponseEntity<String> response = restTemplate1.postForEntity(baseUrl, new HttpEntity<>(query,headers), String.class);
        String body = response.getBody();
        System.out.println("body" + body);
    }

}