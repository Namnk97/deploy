package com.nextsol.khangbb.model;


import com.nextsol.khangbb.exception.CheckEmail;
import com.nextsol.khangbb.exception.CheckPhone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BranchDTO {

    private Long id;


    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters long")
    private String name;

    private String description;

    private String address;

    @CheckPhone(message = "Phone Number is invalid")
    private String phone;

    @CheckEmail(message = "Email is invalid")
    private String email;

    @NotEmpty(message = "district may not be empty")
    @Size(min = 2, max = 20, message = "district must be between 2 and 20 characters long")
    private String district;

    @NotEmpty(message = "city may not be empty")
    @Size(min = 2, max = 20, message = "city must be between 2 and 20 characters long")
    private String city;

    private Integer actived;
}
