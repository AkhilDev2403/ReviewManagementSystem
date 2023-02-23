package com.commercetools.reviewmanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewRequest {
    private String review;
    private float rating;
    private String customerId;
    private String productId;

}
