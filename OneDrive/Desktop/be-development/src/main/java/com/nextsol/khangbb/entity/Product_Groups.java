package com.nextsol.khangbb.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_product_group")
public class Product_Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;

    private String description;

    private Integer actived;
    private Integer deleted;

    @OneToMany(mappedBy = "productGroup", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Products> productsList;
}
