package com.commercetools.reviewmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private String comment;
    private LocalDateTime dateTime;
    private float rating;
    private String customerId;
    private String productId;
    private float avgRating;

}
