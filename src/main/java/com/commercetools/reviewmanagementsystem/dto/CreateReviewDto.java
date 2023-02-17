package com.commercetools.reviewmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewDto {
    private String customerId;
    private String productId;
    private float rating;
    private String comment;

}
