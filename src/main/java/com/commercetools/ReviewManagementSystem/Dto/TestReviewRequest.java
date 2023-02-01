package com.commercetools.ReviewManagementSystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestReviewRequest {
    private String name;
    private String review;
    private float rating;

}
