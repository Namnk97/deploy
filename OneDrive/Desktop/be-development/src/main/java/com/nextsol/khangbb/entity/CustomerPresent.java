package com.nextsol.khangbb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_customer_present")
public class CustomerPresent extends BaseEntity{
    @Column
    private Long customerId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "code_id")
    @JsonIgnore
    @ToString.Exclude
    private Bonus bonus;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "present_id")
    @JsonIgnore
    @ToString.Exclude
    private Present present;
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date kidBirthdate;
    @Column
    private Integer kidWeight;
    @Column
    private Integer diaperSize;
    @Column
    private Integer diaper_quantity;
    @Column
    private Integer awarded;
}
