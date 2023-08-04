package com.dispenser.beertapdipenser.model;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class BaseModel {
    @Column(name = "created_date", insertable = true, updatable = false)
    private Date createdDate;
    @Column(name = "updated_date", insertable = true, updatable = true)
    private Date updatedDate;
}
