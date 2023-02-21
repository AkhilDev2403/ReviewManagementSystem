package com.commercetools.reviewmanagementsystem.constants;

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

    public AbstractResponse(UpdateDto updateDto, String token) {
    }
}
