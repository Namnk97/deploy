package com.nextsol.khangbb.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_supplier_group")
public class Supplier_Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer deleted;

    @OneToMany(mappedBy = "group" , cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Supplier> supplierList;
}
