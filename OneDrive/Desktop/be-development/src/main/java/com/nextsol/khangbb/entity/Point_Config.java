package com.nextsol.khangbb.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_point_config")
public class Point_Config extends BaseEntity {

    private String name;

    private Integer pointConvert ;
    //điểm để quy đổi sang money_convert

    private BigDecimal moneyConvert ;

    private Integer deleted ;

    @Column(name = "start_time")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date endTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    @JsonIgnore
    @ToString.Exclude
    private Branch pointConfig;










}
