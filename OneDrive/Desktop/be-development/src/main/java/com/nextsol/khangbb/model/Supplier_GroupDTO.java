package com.nextsol.khangbb.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier_GroupDTO {

    private Long id;

    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters long")
    private String name;

    private String description;
}
