package com.nextsol.khangbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tbl_screen")
@Data
@JsonIgnoreProperties(value= {"screen"})
public class Screen {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
}
