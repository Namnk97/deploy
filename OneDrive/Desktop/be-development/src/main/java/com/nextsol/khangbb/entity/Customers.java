package com.nextsol.khangbb.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextsol.khangbb.model.CustomerDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SqlResultSetMapping(
        name = "getAllCustomer",
        classes = {
                @ConstructorResult(
                        targetClass = CustomerDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "fullname", type = String.class),
                                @ColumnResult(name = "phone", type = String.class),
                                @ColumnResult(name = "address", type = String.class),
                                @ColumnResult(name = "email", type = String.class),
                                @ColumnResult(name = "birthday", type = Date.class),
                                @ColumnResult(name = "turnover", type = BigDecimal.class),
                                @ColumnResult(name = "number_purchase", type = Integer.class),
                                @ColumnResult(name = "point", type = Integer.class),
                                @ColumnResult(name = "debit", type = BigDecimal.class),
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
@Table(name = "tbl_customer")
public class Customers extends BaseEntity {

    private String fullname;

    private String phone;
    private String address;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private Integer sex;

    private BigDecimal turnover;
    private Integer numberPurchase;
    private Integer point;
    private BigDecimal debit;
    private String description;
    private Integer deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonIgnore
    @ToString.Exclude
    private Customers_Groups customerGroup;

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private List<Purchase_History> historyList;

    public Customers(String fullname, String phone, String address, String email) {
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }
}
