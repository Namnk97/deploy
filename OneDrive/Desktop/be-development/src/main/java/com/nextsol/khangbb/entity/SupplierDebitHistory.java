package com.nextsol.khangbb.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_supplier_debit_history")
public class SupplierDebitHistory extends BaseEntity{

	@Column(unique=true)
	private String code; // Số phiếu
	private String method; // Phương thức
	@Column(name="code_foreign")
	private String codeForeign; // Số phiếu tham chiếu
	@Column(name="money_payment")
	private Long moneyPayment; // Số tiền thanh toán

	private Long debit; // Dư nợ còn lại
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "supplier_debit_id")
	@ToString.Exclude
	private Supplier_Debit supplierDebit;
}
