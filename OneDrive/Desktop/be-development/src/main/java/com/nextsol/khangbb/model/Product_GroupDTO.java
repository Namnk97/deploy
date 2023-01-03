package com.nextsol.khangbb.model;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class Product_GroupDTO {
    private Long id;

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long")
    private String name;

    private String description;

    @NotEmpty(message = "Code may not be empty")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters long")
    private String code;

    private Integer actived;


}
