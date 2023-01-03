package com.nextsol.khangbb.model;

import com.nextsol.khangbb.entity.Products;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CodeDetailDTO {

    private Long id;
    private Integer quantity;
    private Integer quantityReal;
    private String nameProduct;
    private String codeProduct;
    private Long idCode;
    private Products product;
}
