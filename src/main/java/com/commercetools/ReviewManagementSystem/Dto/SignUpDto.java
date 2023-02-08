package com.commercetools.ReviewManagementSystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer phone;
}
