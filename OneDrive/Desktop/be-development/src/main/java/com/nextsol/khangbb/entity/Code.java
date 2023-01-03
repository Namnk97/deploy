package com.nextsol.khangbb.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nextsol.khangbb.model.ReportImportBranch;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SqlResultSetMapping(
        name = "getReport",
        classes = {
                @ConstructorResult(
                        targetClass = ReportImportBranch.class,
                        columns = {
                                @ColumnResult(name = "codeProduct", type = String.class),
                                @ColumnResult(name = "nameProduct", type = String.class),
                                @ColumnResult(name = "quantity", type = Integer.class),
                                @ColumnResult(name = "importPrice", type = BigDecimal.class),
                                @ColumnResult(name = "createDay", type = Date.class),
                                @ColumnResult(name = "codeCode", type = String.class),
                                @ColumnResult(name = "typeCode", type = String.class),
                                @ColumnResult(name = "note", type = String.class)
                        }
                )
        }
)

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_code")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Code extends BaseEntity {

    private String code;
    private String type;
    private Long branchFrom;
    private Long branchTo;
    private String codeBill;
    private String note;
    private Integer status;//1 chấp nhận 2 từ chối 3 chờ xác nhận
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    @ToString.Exclude
    private Supplier supplier;

    @OneToMany(mappedBy = "code", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Code_Detail> detailList;
}
