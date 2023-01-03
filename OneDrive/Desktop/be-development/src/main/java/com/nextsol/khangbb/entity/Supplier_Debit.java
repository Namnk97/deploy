package com.nextsol.khangbb.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_supplier_debit")
public class Supplier_Debit extends BaseEntity{

	private BigDecimal debit ; // Tổng thanh toán
	private BigDecimal pay_debit; //Đã thanh toán
	private Integer deleted;

	@Column(name = "code", unique=true)
	private String code; // Số phiếu

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "supplier_id")
	@JsonIgnore
	@ToString.Exclude
	private Supplier supplier;

}
