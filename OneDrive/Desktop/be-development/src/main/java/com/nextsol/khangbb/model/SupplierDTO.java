package com.nextsol.khangbb.model;


import com.nextsol.khangbb.exception.CheckEmail;
import com.nextsol.khangbb.exception.CheckPhone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO extends BaseModel {
    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long")
    private String name;
    private String address;
    private String contactName;
    @CheckPhone(message = "Phone Number is invalid")
    private String phone;
    private String fax;
    @CheckEmail(message = "Email is invalid")
    private String email;
    private String description;
    private Long idGroup;

    private Supplier_GroupDTO group;

    public SupplierDTO(Long id, String name, String address, String contactName, String phone, String fax, String email, String description, Long idGroup) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactName = contactName;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.description = description;
        this.idGroup = idGroup;
    }
}
