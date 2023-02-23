package com.commercetools.reviewmanagementsystem.constants;

import com.commercetools.reviewmanagementsystem.model.request.CreateReviewRequest;
import com.commercetools.reviewmanagementsystem.model.request.UpdateReviewRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class AbstractResponse<T> extends ArrayList<T> {
    protected boolean success = true;
    protected String message = "";

    public AbstractResponse(String cuId, String pId, String token) {
    }

    public AbstractResponse(CreateReviewRequest reviewRequest, String authorization) {
    }

    public AbstractResponse(UpdateReviewRequest updateRequest, String token) {
    }
}
