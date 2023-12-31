package com.nextsol.khangbb.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel {

    protected Long id;
    protected String createdBy;
    protected Date createdDate;
    protected String updatedBy;
    protected Date updatedDate;
}
