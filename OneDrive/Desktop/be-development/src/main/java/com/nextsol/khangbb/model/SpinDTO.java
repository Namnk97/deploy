package com.nextsol.khangbb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpinDTO {
    private Long Id;
    private Long customerId;
    private String phone;
    private String fullname;
    private String address;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date kidBirthdate;
    private Integer kidWeight;
    private Integer diaperSize;
    private Integer awarded;
    private String bonusCode;
 }
