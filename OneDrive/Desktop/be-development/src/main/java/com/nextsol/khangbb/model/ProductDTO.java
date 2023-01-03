package com.nextsol.khangbb.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductDTO extends BaseModel {
    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long")
    private String name;

    @NotEmpty(message = "Code may not be empty")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters long")
    private String code;

    @DecimalMin(value = "0",message ="importPrice may not negative" )
    private BigDecimal importPrice;
    @DecimalMin(value = "0",message ="whole_price may not negative" )
    private BigDecimal whole_price;
    @NotNull(message = "Retail Price may not be null")
    @DecimalMin(value = "0.0", inclusive = false,message ="Retail Price may not negative" )
    private BigDecimal retail_price;
    @Min(value=0, message="Quantity may not negative")
    private Integer quantity;

    @DecimalMin(value = "0",message ="retail_discount_money may not negative" )
    private BigDecimal retail_discount_money;

    @DecimalMin(value = "0",message ="wholesale_discount_money may not negative" )
    private BigDecimal wholesale_discount_money;

    @DecimalMin(value = "0",message ="retail_discount may not negative" )
    private Double retail_discount;

    @DecimalMin(value = "0",message ="wholesale_discount may not negative" )
    private Double wholesale_discount;
    private String unit;


    private String reason;

    private String description;

    private String note;

    private Integer actived;
    private Long idGroup;

    @JsonProperty("idGroupStore")
    private List<Long> idGroupStore;
    private Long idBranch;
    private Double discount;

    private BigDecimal discountMoney;
    private Date createFrom;

    @JsonProperty("createdTo")
    private Date createTo;

    @JsonProperty("quantityFrom")
    private Integer quantityFrom;

    @JsonProperty("quantityTo")
    private Integer quantityTo;

    public ProductDTO(Long id, String name, String code, BigDecimal importPrice, BigDecimal whole_price, BigDecimal retail_price, Integer quantity, String unit, Integer actived, Long idGroup, Date createdDate, Long idBranch, String note) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.importPrice = importPrice;
        this.whole_price = whole_price;
        this.retail_price = retail_price;
        this.quantity = quantity;
        this.unit = unit;
        this.actived = actived;
        this.idGroup = idGroup;
        this.createdDate = createdDate;
        this.idBranch = idBranch;
        this.note = note;
    }

}
