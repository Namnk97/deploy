package com.nextsol.khangbb.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextsol.khangbb.model.SupplierDTO;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@SqlResultSetMapping(
        name = "getAllSupplier",
        classes = {
                @ConstructorResult(
                        targetClass = SupplierDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "address", type = String.class),
                                @ColumnResult(name = "contactName", type = String.class),
                                @ColumnResult(name = "phone", type = String.class),
                                @ColumnResult(name = "fax", type = String.class),
                                @ColumnResult(name = "email", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "group_id", type = Long.class)
                        }
                )
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_supplier")
public class Supplier extends BaseEntity {

    private String name;
    private String address;
    private String contactName;
    private String phone;
    private String fax;
    private String email;
    private String description;
    private Integer deleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    @JsonIgnore
    @ToString.Exclude
    private Supplier_Group group;

    @OneToMany(mappedBy = "supplier", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Code> codeList;


}
