package com.commercetools.reviewmanagementsystem.constants;

import com.commercetools.reviewmanagementsystem.dto.CreateReviewDto;
import com.commercetools.reviewmanagementsystem.dto.UpdateDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class AbstractResponse<T> extends ArrayList<T> {
    protected boolean success = true;
    protected String message = "";
    protected int offset = 0;
    protected long size = 0l;


    public AbstractResponse(UpdateDto updateDto, String token) {
    }

    public AbstractResponse(CreateReviewDto dto, String token) {
    }

    public AbstractResponse(String cuId, String pId, String token) {
    }

}
