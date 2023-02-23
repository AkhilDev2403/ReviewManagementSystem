package com.commercetools.reviewmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String customerId;
    private String firstName;
    private String lastName;
    private String productId;
    private float rating;
    private String review;
    private float avgRating;
}
