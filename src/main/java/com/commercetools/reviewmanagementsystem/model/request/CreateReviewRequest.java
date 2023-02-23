package com.commercetools.reviewmanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    private String customerId;
    private String productId;
    private float rating;
    private String review;
}
