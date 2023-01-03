package com.nextsol.khangbb.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextsol.khangbb.model.ProductDTO;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SqlResultSetMapping(
        name = "getAllProduct",
        classes = {
                @ConstructorResult(
                        targetClass = ProductDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "importPrice", type = BigDecimal.class),
                                @ColumnResult(name = "whole_price", type = BigDecimal.class),
                                @ColumnResult(name = "retail_price", type = BigDecimal.class),
                                @ColumnResult(name = "quantity", type = Integer.class),
                                @ColumnResult(name = "unit", type = String.class),
                                @ColumnResult(name = "actived", type = Integer.class),
                                @ColumnResult(name = "group_id", type = Long.class),
                                @ColumnResult(name = "createdDate", type = Date.class),
                                @ColumnResult(name = "branch_id", type = Long.class),
                                @ColumnResult(name = "note", type = String.class)
                        }
                )
        }
)


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_product")
public class Products extends BaseEntity {

    private String name;

    private String code;

    private BigDecimal importPrice;

    private BigDecimal whole_price;

    private BigDecimal retail_price;

    private Integer quantity;

    private String unit;

    private String description;

    private String note;

    private Integer deleted;

    private BigDecimal retail_discount_money;
    private BigDecimal wholesale_discount_money;
    private Double retail_discount;
    private Double wholesale_discount;
    private Double discount;
    private BigDecimal discountMoney;
    private Integer actived;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Product_Groups productGroup;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    @JsonIgnore
    @ToString.Exclude
    private Branch branch;


    @OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Code_Detail> code_detailList;

   
}
