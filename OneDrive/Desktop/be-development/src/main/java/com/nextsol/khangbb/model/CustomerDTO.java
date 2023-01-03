package com.nextsol.khangbb.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextsol.khangbb.exception.CheckEmail;
import com.nextsol.khangbb.exception.CheckPhone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO extends BaseModel {

    private String fullname;
    @CheckPhone(message = "Phone Number is invalid")
    private String phone;
    private String address;
    @CheckEmail(message = "Email is invalid")
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private BigDecimal turnover;
    private Integer numberPurchase;
    private Integer point;
    private BigDecimal debit;
    private String description;

    private Integer sex;
    private Long idGroup;

    @JsonProperty("birthdayFrom")
    private Date birthdayFrom;

    @JsonProperty("birthdayTo")
    private Date birthdayTo;

    @JsonProperty("turnoverFrom")
    private BigDecimal turnoverFrom;

    @JsonProperty("turnoverTo")
    private BigDecimal turnoverTo;

    @JsonProperty("debitFrom")
    private BigDecimal debitFrom;

    @JsonProperty("debitTo")
    private BigDecimal debitTo;

    @JsonProperty("pointFrom")
    private Integer pointFrom;

    @JsonProperty("pointTo")
    private Integer pointTo;

    @JsonProperty("numberPurchaseFrom")
    private Integer numberPurchaseFrom;

    @JsonProperty("numberPurchaseTo")
    private Integer numberPurchaseTo;


    public CustomerDTO(Long id, String fullname, String phone, String address, String email, Date birthday, BigDecimal turnover, Integer numberPurchase, Integer point, BigDecimal debit, String description, Long idGroup) {
        this.id = id;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
        this.turnover = turnover;
        this.numberPurchase = numberPurchase;
        this.point = point;
        this.debit = debit;
        this.description = description;
        this.idGroup = idGroup;
    }

    public CustomerDTO(Long id, String fullname, String phone, String address, String email) {
        this.id = id;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public CustomerDTO(String fullname, String phone, String address, String email) {
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }
}
